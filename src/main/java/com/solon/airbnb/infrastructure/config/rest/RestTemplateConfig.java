package com.solon.airbnb.infrastructure.config.rest;

import com.solon.airbnb.infrastructure.config.rest.interceptor.LoggingInterceptor;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class RestTemplateConfig {

//    @Bean
//    public CsvParser<AirplaneDTO> csvParser() {
//        return new DefaultCsvParser<>(AirplaneDTO.class);
//    }

    @Bean
    RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory());
        restTemplate.setErrorHandler(new RestTemplateResponseErrorHandler());
        List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();
        if(CollectionUtils.isEmpty(interceptors)) {
            interceptors = new ArrayList<>();
        }
        interceptors.add(new LoggingInterceptor());
        restTemplate.setInterceptors(interceptors);
//        CsvHttpMessageConverter<AirplaneDTO> messageConverter = new CsvHttpMessageConverter<>(
//                this.csvParser(),
//                MediaType.ALL
//        );
//        restTemplate.getMessageConverters().add(messageConverter);
        return restTemplate;
    }

    @Bean
    HttpComponentsClientHttpRequestFactory clientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory= new HttpComponentsClientHttpRequestFactory();
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        clientHttpRequestFactory.setHttpClient(httpClient);
        return clientHttpRequestFactory;
    }
}
