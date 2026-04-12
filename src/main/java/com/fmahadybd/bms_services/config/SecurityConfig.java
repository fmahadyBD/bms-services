package com.fmahadybd.bms_services.config;

import com.fmahadybd.bms_services.auth.security.JwtFilter;
import com.fmahadybd.bms_services.auth.security.LogoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

        private final JwtFilter jwtAuthFilter;
        private final AuthenticationProvider authenticationProvider;
        private final LogoutService logoutService;
        private final CorsConfigurationSource corsConfigurationSource;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                                .csrf(csrf -> csrf.disable())
                                // SecurityConfig.java
                                .authorizeHttpRequests(req -> req
                                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                                .requestMatchers("/uploads/**").permitAll()
                                                .requestMatchers("/images/**", "/css/**", "/js/**", "/webjars/**")
                                                .permitAll()
                                                .requestMatchers("/api/managers/**").hasRole("MANAGER")
                                                .requestMatchers("/api/reports/**").hasRole("MANAGER")
                                                .requestMatchers(
                                                                "/auth/**",
                                                                "/v3/api-docs/**",
                                                                "/swagger-ui/**",
                                                                "/swagger-ui.html",
                                                                "/api/v1/routes/**",
                                                                "/api/v1/bus-requests/**",
                                                                "/api/v1/bus-slots/**",
                                                                "/api/v1/buses/**",
                                                                "/api/v1/surveys/**",
                                                                "/api/courses/**")
                                                .permitAll()
                                                // Student read endpoints — public
                                                .requestMatchers(HttpMethod.GET, "/api/v1/students/**").permitAll()
                                                // Student write endpoints — must be authenticated
                                                .requestMatchers(HttpMethod.PATCH, "/api/v1/students/**")
                                                .authenticated()
                                                .requestMatchers(HttpMethod.DELETE, "/api/v1/students/**")
                                                .authenticated()

                                                // Public: anyone can view surveys
                                                .requestMatchers(HttpMethod.GET, "/api/v1/surveys/**").permitAll()

                                                // Protected: only managers can create/update/delete
                                                .requestMatchers(HttpMethod.POST, "/api/v1/surveys/**")
                                                .hasRole("MANAGER")
                                                .requestMatchers(HttpMethod.PUT, "/api/v1/surveys/**")
                                                .hasRole("MANAGER")
                                                .requestMatchers(HttpMethod.PATCH, "/api/v1/surveys/**")
                                                .hasRole("MANAGER")
                                                .requestMatchers(HttpMethod.DELETE, "/api/v1/surveys/**")
                                                .hasRole("MANAGER")
                                                .anyRequest().authenticated()

                                )
                                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .authenticationProvider(authenticationProvider)
                                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                                .logout(logout -> logout
                                                .logoutUrl("/auth/logout")
                                                .addLogoutHandler(logoutService)
                                                .logoutSuccessHandler((req, res, auth) -> res
                                                                .setStatus(HttpStatus.OK.value())));

                return http.build();
        }
}