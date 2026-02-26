package com.elite.resort.Security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

        private final JwtFilter jwtFilter;

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

                http
                                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                                .csrf(csrf -> csrf.disable())
                                .sessionManagement(session -> session.sessionCreationPolicy(
                                                SessionCreationPolicy.STATELESS))
                                .authorizeHttpRequests(auth -> auth

                                                // 🔓 PUBLIC APIs
                                                .requestMatchers(
                                                                "/api/auth/**",
                                                                "/api/rooms/**",
                                                                "/api/contact/**",
                                                                "/api/profile/**",
                                                                "/api/payments/**",
                                                                "/api/images/**",
                                                        "/bookings/**")

                                                .permitAll()
                                                .requestMatchers("/api/admin/rooms/**").authenticated()
                                                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                                                .       requestMatchers("/api/profile/**").hasRole("ADMIN")
                                                .requestMatchers("/admin/**").hasRole("ADMIN")

                                )

                                .addFilterBefore(
                                                jwtFilter,
                                                UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }
        
        // ✅ CORS CONFIGURATION
        @Bean
        public CorsConfigurationSource corsConfigurationSource() {

                CorsConfiguration config = new CorsConfiguration();

                config.setAllowedOrigins(List.of(
                                "https://elite-resort-website.vercel.app",
                                "https://admin-elite-resort.vercel.app",
                                "http://localhost:5173",
                                "http://localhost:5174"

                ));

                config.setAllowedMethods(List.of(
                                "GET", "POST", "PUT", "DELETE"));

                config.setAllowedHeaders(List.of("*"));

                config.setAllowCredentials(true);

                config.setExposedHeaders(List.of("Authorization"));

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

                source.registerCorsConfiguration("/**", config);

                return source;
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }
}
