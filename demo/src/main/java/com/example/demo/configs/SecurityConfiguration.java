package com.example.demo.configs;

import static com.example.demo.util.Constants.WHITE_LIST_URL;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import com.example.demo.enums.UserRoles;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

  private final AuthenticationProvider authenticationProvider;
  private final AuthFilter jwtAuthenticationFilter;
  private final LogoutHandler logoutHandler;

  /**
   * Configures the security filter chain for HTTP requests.
   *
   * @param http The HttpSecurity object to configure security settings.
   * @return SecurityFilterChain containing configured security filters.
   * @throws Exception If an error occurs while configuring security.
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    http.csrf(AbstractHttpConfigurer::disable) // Disables CSRF protection
        .authorizeRequests(
            authorize ->
                authorize
                    // Permit access to specified white-listed URLs without authentication
                    .requestMatchers(WHITE_LIST_URL)
                    .permitAll() // Permit access to /auth/** without authentication
                    .requestMatchers(HttpMethod.PUT, "/api/v1/loan/approve/**")
                    .hasRole(
                        UserRoles.ADMIN
                            .name()) // Allow PUT requests to /api/v1/loan/approve/** only for users
                                     // with ADMIN role
                    .requestMatchers("/api/v1/loan/**")
                    .hasAnyRole(UserRoles.ADMIN.name(), UserRoles.CUSTOMER.name())
                    .anyRequest()
                    .authenticated())
        .logout(
            logout ->
                logout
                    .logoutUrl("/v1/auth/signout")
                    .addLogoutHandler(logoutHandler) // Specify the URL for logout
                    .logoutSuccessHandler(
                        (request, response, authentication) ->
                            SecurityContextHolder.clearContext()))
        .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
        .authenticationProvider(authenticationProvider)
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
