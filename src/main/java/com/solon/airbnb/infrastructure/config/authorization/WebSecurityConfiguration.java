package com.solon.airbnb.infrastructure.config.authorization;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.solon.airbnb.user.domain.Authority;
import com.solon.airbnb.user.repository.AuthorityRepository;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {
	
	@Value("${security.jwt.key}")
    private String signKey;
	private final JwtAuthenticationFilter jwtAuthFilter;
	private final AuthorityRepository authorityRepository;
	
	public WebSecurityConfiguration(JwtAuthenticationFilter jwtAuthFilter,AuthorityRepository authorityRepository) {
        this.jwtAuthFilter=jwtAuthFilter;
        this.authorityRepository=authorityRepository;
    }
	
	@Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList(SecurityConstants.ALLOWED_ORIGIN_PATTERNS));
        configuration.setAllowedMethods(Arrays.asList(SecurityConstants.ALLOWED_METHODS));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Arrays.asList(SecurityConstants.ALLOWED_HEADERS ));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
	
	private String[] getAuthorityNames()  {
        List<String> authorityNames = authorityRepository.findAll().stream()
        		.map(Authority::getName)
        		.toList();
        int len = authorityNames.size();
        String[] authorityNamesArr = new String[len];

        for (int i = 0; i < len; i++) {
        	authorityNamesArr[i] = authorityNames.get(i);
        }

        return authorityNamesArr;
    }

}
