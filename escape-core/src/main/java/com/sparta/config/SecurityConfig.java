package com.sparta.config;

import com.sparta.global.jwt.JwtProvider;
import com.sparta.global.jwt.RefreshTokenService;
import com.sparta.global.security.UserDetailsServiceImpl;
import com.sparta.global.security.filter.CustomAccessDeniedHandler;
import com.sparta.global.security.filter.CustomAuthenticationEntryPoint;
import com.sparta.global.security.filter.JwtAuthenticationFilter;
import com.sparta.global.security.filter.JwtAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
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
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true) // secured 어노테이션 활성화 빌드업
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtProvider jwtProvider;
  private final UserDetailsServiceImpl userDetailsService;
  private final AuthenticationConfiguration authenticationConfiguration;
  private final RefreshTokenService refreshTokenService;
  private final CustomAccessDeniedHandler customAccessDeniedHandler;
  private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
      throws Exception {
    return configuration.getAuthenticationManager();
  }

  @Bean
  public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
    JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtProvider, refreshTokenService);
    filter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
    return filter;
  }

  @Bean
  public JwtAuthorizationFilter jwtAuthorizationFilter() {
    return new JwtAuthorizationFilter(jwtProvider, refreshTokenService, userDetailsService);
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOriginPatterns(List.of("*"));
    configuration.addAllowedHeader("*");
    configuration.addAllowedMethod("*");
    configuration.setAllowCredentials(true);
    configuration.addExposedHeader("Authorization");
    configuration.addExposedHeader("RefreshToken");

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable); // CSFF 설정
    http.cors(corsConfigurer -> corsConfigurer.configurationSource(
        corsConfigurationSource())); // cors 관련 설정

    http.sessionManagement((sessionManagement) ->
        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    http.authorizeHttpRequests((requests) -> requests
        .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
        .permitAll() //resource 접근 허용 설정
        .requestMatchers("/api/core/status").permitAll()
        .requestMatchers("/api/admin/status").permitAll()
        .requestMatchers("/api/manager/status").permitAll()
        .requestMatchers("/api/consumer/status").permitAll()
        .requestMatchers("/api/reservations/status").permitAll()
        .requestMatchers("/api/search/status").permitAll()
        .requestMatchers(HttpMethod.POST, "/api/core/users/signup").permitAll() // non-user 접근허용
        .requestMatchers(HttpMethod.POST, "/api/core/users/mail").permitAll() // non-user 접근허용
        .requestMatchers(HttpMethod.POST, "/api/core/auth/reissue").permitAll()
        .requestMatchers(HttpMethod.GET, "/api/search/**").permitAll() // non-user 접근허용 + 차후 리팩토링
        .requestMatchers("/api/admin/**").hasAuthority("ADMIN")
        .requestMatchers("/api/manager/**").hasAuthority("MANAGER")
        .anyRequest().authenticated()
    );

    http.addFilterBefore(jwtAuthorizationFilter(), JwtAuthenticationFilter.class);
    http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

    http.exceptionHandling(exceptionHandling ->
        exceptionHandling
            .authenticationEntryPoint(customAuthenticationEntryPoint)
            .accessDeniedHandler(customAccessDeniedHandler)
    );

    return http.build();
  }
}
