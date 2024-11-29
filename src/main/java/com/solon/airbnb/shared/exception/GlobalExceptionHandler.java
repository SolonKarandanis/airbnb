/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solon.airbnb.shared.exception;

import java.net.URI;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import org.json.JSONArray;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.solon.airbnb.shared.common.AirbnbConstants;
import com.solon.airbnb.shared.utils.StringUtil;

import jakarta.validation.ConstraintViolationException;


/**
 *
 * @author solon
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /** Logger for the class. */
    protected static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final String EXCEPTION_LINE_GAP = " ---------- ";

    @Autowired
    private MessageSource messageSource;

	@ExceptionHandler(ResourceNotFoundException.class)
    ProblemDetail handleResourceNotFoundException(ResourceNotFoundException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        problemDetail.setTitle("Resource Not Found");
        problemDetail.setType(URI.create("https://api.bookmarks.com/errors/not-found"));
        problemDetail.setProperty("errorCategory", "Generic");
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }

    @ExceptionHandler(value = { RuntimeException.class })
    public ResponseEntity<Object> handleRuntimeException(final RuntimeException e, final WebRequest request) {
        LOG.debug(" HANDLER: handleRuntimeException [message: {}, class: {}] ", e.getMessage(), e.getClass().getName());
        if (e instanceof AccessDeniedException) {
            /* Raised when the implementation of the permission evaluator returns false (unauthorized). */
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("");
        } else if (e instanceof ConstraintViolationException) {
            String message = e.getMessage();
            int index = message.indexOf(':');
            if (index >= 0) {
                message = message.substring(index + 1);
            }
            return ResponseEntity.badRequest().body(message.trim());
        } else {
            return getInternalServerErrorResponse(e, request);
        }
    }

    /**
     * NOTE: Later on, message arguments have to be translated.
     *
     * @param e
     *            <code>Exception</code>
     * @return <code>ResponseEntity</code>
     */
    @ExceptionHandler(value = { AirbnbException.class })
    public ResponseEntity<Object> handleEDException(final AirbnbException e, final WebRequest request) {
        LOG.debug(" HANDLER: handleEDException [message: {}, class: {}] ", e.getMessage(), e.getClass().getName());
        if (e instanceof AirbnbException) {
            /* Validation error, handle as HTTP 400 and translate the error message. */
            return ResponseEntity.badRequest().body(serializeErrorMessageToJson(getTranslatedErrorMessage(e, request)));
        } else {
            return getInternalServerErrorResponse(e, request);
        }
    }

    /**
     * NOTE: Later on, message arguments have to be translated.
     *
     * @param e
     *            <code>Exception</code>
     * @return <code>ResponseEntity</code>
     */
    @ExceptionHandler(value = { AggregateAirbnbException.class })
    public ResponseEntity<Object> handleAggregateEDException(final AggregateAirbnbException e, final WebRequest request) {
        if (e instanceof AggregateAirbnbException) {
            /* Validation error, handle as HTTP 400 and translate the error message. */
            StringBuilder serializededMessage = new StringBuilder();
            for (AirbnbException ed : e.getBasket()) {
                LOG.debug(" HANDLER: handleEDException [message: {}, class: {}] ", ed.getMessage(), ed.getClass().getName());
                serializededMessage.append(serializeErrorMessageToJson(getTranslatedErrorMessage(ed, request)));
            }
            return ResponseEntity.badRequest().body(serializededMessage.toString());
        } else {
            return getInternalServerErrorResponse(e, request);
        }
    }
    
    @ExceptionHandler(value = { BusinessException.class })
    public ResponseEntity<Object> handleBusinessException(final BusinessException e, final WebRequest request) {
        LOG.debug(" HANDLER: handleBusinessException [message: {}, class: {}] ", e.getMessage(), e.getClass().getName());
        if (e instanceof BusinessException) {
            /* Validation error, handle as HTTP 400 and translate the error message. */
            return ResponseEntity.badRequest().body(serializeErrorMessageToJson(getTranslatedErrorMessage(e, request)));

        } else {
            return getInternalServerErrorResponse(e, request);
        }

    }

    /**
     * Handler for NoResultFoundException
     *
     * @param e
     *            <code>Exception</code>
     * @return <code>ResponseEntity</code>
     */
    @ExceptionHandler(value = { NotFoundException.class })
    public ResponseEntity<Object> handleNoResultFoundException(final NotFoundException e, final WebRequest request) {
        LOG.debug(" HANDLER: handleNoResultFoundException");
        if (e instanceof NotFoundException) {
            /* Empty result set, handle as HTTP 404. */
            return ResponseEntity.notFound().build();
        } else {
            return getInternalServerErrorResponse(e, request);
        }
    }

    /**
     * Handler for ResourceAccessException
     *
     * @param e
     *            <code>Exception</code>
     * @return <code>ResponseEntity</code>
     */
    @ExceptionHandler(value = { ResourceAccessException.class })
    public ResponseEntity<Object> handleResourceAccessException(final ResourceAccessException e, final WebRequest request) {
        LOG.debug(" HANDLER: handleResourceAccessException [message: {}, class: {}] ", e.getMessage(), e.getClass().getName());
        return getBadGatewayResponse(e, request);
    }

//  This exception is thrown when a method parameter has the wrong type!
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex,
            WebRequest request) {

        List<String> details = new ArrayList<>();
        details.add(ex.getMessage());

        ApiError err = new ApiError(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST,
                "Type Mismatch",
                details);

        return ResponseEntityBuilder.build(err);
    }

//  This exception will be triggered if the request body is invalid!
//  @ExceptionHandler(HttpMessageNotReadableException.class)
  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(
			HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request){
  	ProblemDetail body = createProblemDetail(ex, status, "Malformed JSON request", ex.getMessage(), null, request);
      return handleExceptionInternal(ex, body, headers, status, request);
  }



// This exception will be raised when a handler method argument annotated with @Valid failed validation!
//  @ExceptionHandler(MethodArgumentNotValidException.class)
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

  	ProblemDetail body = createProblemDetail(ex, status, "Validation Errors", ex.getMessage(), null, request);
		return handleExceptionInternal(ex, body, headers, status, request);
  }
  
  
//This exception occurs when a controller method does not receive a required parameter.
//  @ExceptionHandler(MissingServletRequestParameterException.class)
  @Override
  protected ResponseEntity<Object> handleMissingServletRequestParameter(
  		MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

  	ProblemDetail body = createProblemDetail(ex, status, "Missing Parameters", ex.getParameterName(), null, request);
		return handleExceptionInternal(ex, body, headers, status, request);
  }
  

//  @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
  @Override
  protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
			HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
  	StringBuilder builder = new StringBuilder();
		builder.append(ex.getContentType());
		builder.append(" media type is not supported. Supported media types are ");
		ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));
  	ProblemDetail body = createProblemDetail(ex, status, "Missing Parameters", builder.toString(), null, request);
		return handleExceptionInternal(ex, body, headers, status, request);
  }
  

//  @ExceptionHandler(NoHandlerFoundException.class)
  @Override
  protected ResponseEntity<Object> handleNoHandlerFoundException(
			NoHandlerFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
  	String s = String.format("Could not find the %s method for URL %s", ex.getHttpMethod(), ex.getRequestURL());
  	ProblemDetail body = createProblemDetail(ex, status, "Method Not Found",s, null, request);
		return handleExceptionInternal(ex, body, headers, status, request);
  }
    

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAll(
            Exception ex,
            WebRequest request) {

        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());

        ApiError err = new ApiError(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST,
                "Error occurred",
                details);

        return ResponseEntityBuilder.build(err);
    }

    private ResponseEntity<Object> getInternalServerErrorResponse(final Exception e, final WebRequest request) {
        LOG.error(EXCEPTION_LINE_GAP);
        LOG.error(" EXCEPTION: ", e);
        LOG.error(EXCEPTION_LINE_GAP);
        /* Internal server error, handle as HTTP 500. */
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(serializeErrorMessageToJson(getTranslatedErrorMessage("prompt.500", new Object[] {}, request)));
    }

    private ResponseEntity<Object> getBadGatewayResponse(final Exception e, final WebRequest request) {
        LOG.error(EXCEPTION_LINE_GAP);
        LOG.error(" EXCEPTION: ", e);
        LOG.error(EXCEPTION_LINE_GAP);
        /* Internal server error, handle as HTTP 500. */
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body(serializeErrorMessageToJson(getTranslatedErrorMessage("prompt.502", new Object[] {}, request)));
    }

    /**
     * @param
     * @return
     */
    private String getTranslatedErrorMessage(final RepException exception, final WebRequest request) {
        return getTranslatedErrorMessage(exception.getLocalizedMessage(), exception.getKeyArgs(), request);
    }

    /**
     * @param errorMessage
     * @param messageArguments
     * @param request
     * @return
     */
    private String getTranslatedErrorMessage(final String errorMessage, final Object[] messageArguments, final WebRequest request) {
        String translatedMessage = "";

        try {
            translatedMessage = messageSource.getMessage(errorMessage, messageArguments, getRequestLocale(request));
        } catch (NoSuchMessageException e) {
            /* If message is not resolved, then assume that it is a plain-text message. */
            LOG.debug(" CASE: Translation not found for: {} ", errorMessage);
            translatedMessage = messageArguments != null && messageArguments.length > 0 ? StringUtil.replaceParametersInString(errorMessage, messageArguments)
                    : errorMessage;
        }
        return translatedMessage;
    }

    /**
     * @param request
     *            <code>WebRequest</code>
     * @return
     */
    private Locale getRequestLocale(final WebRequest request) {
        String langIsoCode = request.getHeader(AirbnbConstants.HEADER_NAME_LANGUAGE_ISO);
        LOG.debug(" Header Language IsoCode: {} ", langIsoCode);
        return (StringUtil.hasLength(langIsoCode)) ? new Locale(langIsoCode) : Locale.ENGLISH;
    }

    /**
     * @param errorMessages
     * @return
     */
    private String serializeErrorMessagesToJson(final List<String> errorMessages) {
        String output = "";

        try {
            JSONArray json = new JSONArray();
            errorMessages.forEach(json::put);
            output = json.toString();
        } catch (JSONException e) {
            LOG.error(" ERROR - Cannot serialize to JSON: ", e);
            output = "[{Application error occurred. Please report this issue to Helpdesk.}]";
        }
        return output;
    }

    /**
     * @param errorMessage
     * @return
     */
    private String serializeErrorMessageToJson(final String errorMessage) {
        return serializeErrorMessagesToJson(Stream.of(errorMessage).toList());
    }

}
