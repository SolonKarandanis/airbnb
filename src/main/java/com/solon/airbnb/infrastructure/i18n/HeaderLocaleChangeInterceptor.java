package com.solon.airbnb.infrastructure.i18n;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import jakarta.annotation.Nullable;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HeaderLocaleChangeInterceptor implements HandlerInterceptor{
	
	 /** Logger for the class. */
    protected static final Logger LOG = LoggerFactory.getLogger(HeaderLocaleChangeInterceptor.class);

    /** Default name of the locale specification header: "lang". */
    public static final String DEFAULT_HEADER_NAME = "lang";

    /** The name of custom HTTP header. */
    private String headerName = DEFAULT_HEADER_NAME;

    /** Ignore locale when invalid? */
    private boolean ignoreInvalidLocale = false;
    
    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws ServletException {
        boolean output = true;

        String newLocale = request.getHeader(getHeaderName());
        if (newLocale != null) {
            LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
            throwIllegalStateExceptionIfNoLocaleResolver(localeResolver);
            try {
                localeResolver.setLocale(request, response, parseLocaleValue(newLocale));
            } catch (IllegalArgumentException ex) {
                if (isIgnoreInvalidLocale()) {
                    LOG.debug("Ignoring invalid locale value [{}]: {}, Response Content-Type: {}, Handler.class: {} ", newLocale, ex.getMessage(),
                            response.getContentType(), handler.getClass());
                } else {
                    throw ex;
                }
            }
        }

        return output;
    }
    
    /**
     * Locale resolver must be present in HTTP request.
     *
     * @param localeResolver
     *            <code>LocaleResolver</code>
     */
    private void throwIllegalStateExceptionIfNoLocaleResolver(final LocaleResolver localeResolver) {
        if (localeResolver == null) {
            throw new IllegalStateException("No LocaleResolver found: not in a DispatcherServlet request?");
        }
    }
    
    /**
     * Parse the given locale value as coming from a request header.
     * <p>
     * The default implementation calls {@link StringUtils#parseLocale(String)},
     * accepting the {@link Locale#toString} format as well as BCP 47 language tags.
     *
     * @param localeValue
     *            the locale value to parse
     * @return the corresponding {@code Locale} instance
     */
    @Nullable
    protected Locale parseLocaleValue(String localeValue) {
        return StringUtils.parseLocale(localeValue);
    }
    
    /**
     * Set the name of the parameter that contains a locale specification
     * in a locale change request. Default is "lang".
     */
    public void setHeaderName(String paramName) {
        this.headerName = paramName;
    }

    /**
     * Return the name of the parameter that contains a locale specification
     * in a locale change request.
     */
    public String getHeaderName() {
        return this.headerName;
    }
    
    /**
     * Set whether to ignore an invalid value for the locale parameter.
     *
     * @since 4.2.2
     */
    public void setIgnoreInvalidLocale(boolean ignoreInvalidLocale) {
        this.ignoreInvalidLocale = ignoreInvalidLocale;
    }

    /**
     * Return whether to ignore an invalid value for the locale parameter.
     *
     * @since 4.2.2
     */
    public boolean isIgnoreInvalidLocale() {
        return this.ignoreInvalidLocale;
    }

}
