package security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    @Value("${cors.allowed-origins}")
//    private String allowedOrigins;
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        System.err.println("Filter Chain : ");
//        http
//                // Enable CORS (so React can call your API)
//                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
//
//                // Disable CSRF (not needed for APIs)
//                .csrf(csrf -> csrf.disable())
//
//                // 🔒 ALL REQUESTS REQUIRE AUTHENTICATION
//                .authorizeHttpRequests(auth -> auth
//                        .anyRequest().authenticated()  // ← Everything is private!
//                )
//
//                // Use JWT tokens for authentication
//                .oauth2ResourceServer(oauth2 -> oauth2
//                        .jwt(jwt -> {
//                            // Spring Boot will automatically validate:
//                            // ✅ Token signature (using Auth0's public keys)
//                            // ✅ Token expiration
//                            // ✅ Token issuer (Auth0)
//                            // ✅ Token audience (your API)
//                        })
//                );
//
//        return http.build();
//    }
//
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        System.err.println("Filter Dude : ");
//
//        CorsConfiguration configuration = new CorsConfiguration();
//
//        // Allow your React app to call this API
//        configuration.setAllowedOrigins(Arrays.asList(allowedOrigins.split(",")));
//        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//        configuration.setAllowedHeaders(Arrays.asList("*"));
//        configuration.setAllowCredentials(true);
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }
//}

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${cors.allowed-origins}")
    private String allowedOrigins;

    // Add these properties for ID token validation
    @Value("${auth0.domain}")
    private String auth0Domain;

    @Value("${auth0.client-id}")
    private String clientId;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        System.err.println("Filter Chain configured for ID Token validation");

        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.decoder(idTokenDecoder()))
                );

        return http.build();
    }

    @Bean
    public JwtDecoder idTokenDecoder() {
        String issuerUri = "https://" + auth0Domain + "/";

        // Create decoder for Auth0 issuer
        NimbusJwtDecoder jwtDecoder = JwtDecoders.fromIssuerLocation(issuerUri);

        // Set custom validator for ID tokens
        jwtDecoder.setJwtValidator(idTokenValidator());

        return jwtDecoder;
    }

    @Bean
    public OAuth2TokenValidator<Jwt> idTokenValidator() {
        return new OAuth2TokenValidator<Jwt>() {
            @Override
            public OAuth2TokenValidatorResult validate(Jwt jwt) {
                System.out.println("🔍 Validating ID Token...");
                System.out.println("Token Audience: " + jwt.getAudience());
                System.out.println("Expected Client ID: " + clientId);
                System.out.println("Token Subject: " + jwt.getSubject());
                System.out.println("Token Claims: " + jwt.getClaims().keySet());

                // ID tokens should have audience = CLIENT_ID
                if (jwt.getAudience() != null && jwt.getAudience().contains(clientId)) {
                    System.out.println("✅ ID Token audience validation passed");
                    return OAuth2TokenValidatorResult.success();
                }

                // Check if it's a single audience string
                if (jwt.getClaimAsString("aud") != null && jwt.getClaimAsString("aud").equals(clientId)) {
                    System.out.println("✅ ID Token audience validation passed (single aud)");
                    return OAuth2TokenValidatorResult.success();
                }

                System.out.println("❌ ID Token audience validation failed");
                OAuth2Error error = new OAuth2Error(
                        "invalid_audience",
                        "ID Token audience must be the client ID: " + clientId,
                        null
                );
                return OAuth2TokenValidatorResult.failure(error);
            }
        };
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        System.err.println("CORS configured for ID Token requests");

        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(allowedOrigins.split(",")));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}