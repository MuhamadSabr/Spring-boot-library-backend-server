package com.mmd.library.configuration;

import com.mmd.library.constant.RoleConstants;
import com.mmd.library.filter.JWTTokenGeneratorFilter;
import com.mmd.library.filter.JWTTokenRefresher;
import com.mmd.library.filter.JWTTokenValidatorFilter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Configuration
public class SecurityConfiguration {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("api/messages/admin/**", "api/messages/search/findByClosed").hasRole(RoleConstants.ADMIN.name())
                        .requestMatchers(
                                "/api/books/checkout/**", "/api/books/currentCheckedOutCountByUser", "/api/books/currentLoans",
                                "/api/books/isCheckedOutByUser/**", "/api/reviews/add", "/api/reviews/hasUserLeftReview/**",
                                "/api/books/returnCheckedOutBook/**", "/api/books/renewCheckedOutBook/**", "api/messages/**"
                        ).authenticated()
                        .requestMatchers("/login", "/logout", "/api/books/**", "/api/reviews/**").permitAll()
                        .anyRequest().authenticated())
                .cors(cors-> cors.configurationSource(corsConfigurationSource()));

        http.httpBasic(ht -> ht.authenticationEntryPoint(authenticationEntryPoint()));
        http.formLogin(fr -> fr.successHandler(authenticationSuccessHandler()).failureHandler(authenticationFailureHandler()).disable());

        http.addFilterAfter(new JWTTokenGeneratorFilter(), BasicAuthenticationFilter.class);
        http.addFilterAfter(new JWTTokenRefresher(), BasicAuthenticationFilter.class);
        http.addFilterBefore(new JWTTokenValidatorFilter(), BasicAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.applyPermitDefaultValues();
        configuration.setAllowedOrigins(List.of("http://localhost:3000")); // Allow all origins
        configuration.setAllowedMethods(List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS")); // Allow all methods
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("Authorization", "Success", "Failure"));//Exposing header from backend to frontend
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    @Bean
    AuthenticationEntryPoint authenticationEntryPoint() {
        return new LibraryAuthenticationEntryPoint();
    }

    @Bean
    AuthenticationSuccessHandler authenticationSuccessHandler(){
        return new LibraryAuthenticationSuccessHandler();
    }

    @Bean
    AuthenticationFailureHandler authenticationFailureHandler() {
        return new LibraryAuthenticationFailureHandler();
    }

    static class LibraryAuthenticationEntryPoint implements AuthenticationEntryPoint{// For handling failures of basic auth
        @Override
        public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
            response.addHeader("Failure", "Failed to login basic auth, " + authException.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    static class LibraryAuthenticationSuccessHandler implements AuthenticationSuccessHandler{
        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
            response.addHeader("Success", "Success authentication");
        }
    }

    static class LibraryAuthenticationFailureHandler implements AuthenticationFailureHandler{
        @Override
        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
            response.addHeader("Failure", "Failed login form-based");
        }
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
