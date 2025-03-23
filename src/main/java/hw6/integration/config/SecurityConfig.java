package hw6.integration.config;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter; // ✅ 주입

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(config -> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            System.out.println("🔥 [AUTH ENTRY POINT] 인증 실패!");
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "UNAUTHORIZED: 로그인 필요");
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            System.out.println("⛔ [ACCESS DENIED] 권한 없음!");
                            response.sendError(HttpServletResponse.SC_FORBIDDEN, "FORBIDDEN: 권한 없음");
                        })
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/login", "/signup", "/favicon.ico",
                                "/css/**", "/js/**", "/images/**",
                                "/navbar/**", "/validator/**"
                        ).permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/users").permitAll()
                        .requestMatchers("/posts", "/posts/**").permitAll() // HTML만 띄움
                        .requestMatchers("/api/**").authenticated() // API는 모두 인증 필요
                        .anyRequest().authenticated()

                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();

    }


    // PasswordEncoder 빈 등록
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // AuthenticationManager 등록 (Spring Security 5 이상에서는 필요함)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public FilterRegistrationBean<RequestLoggerFilter> logFilter() {
        FilterRegistrationBean<RequestLoggerFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RequestLoggerFilter());
        registrationBean.addUrlPatterns("/*"); // 모든 요청 경로에 필터 적용
        registrationBean.setOrder(1); // 가장 먼저 실행
        return registrationBean;
    }
}
