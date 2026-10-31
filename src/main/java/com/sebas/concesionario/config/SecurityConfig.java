package com.sebas.concesionario.config;

import jakarta.servlet.DispatcherType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // las JSP se sirven por FORWARD desde las Actions, que ya pasaron el control de acceso
                .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR).permitAll()
                .requestMatchers("/css/**", "/js/**", "/img/**", "/fotos/**", "/favicon.ico", "/error").permitAll()
                .requestMatchers("/", "/catalogo", "/coche", "/login", "/registro", "/registrar").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/coches/**", "/api/marcas/**").permitAll()
                .requestMatchers("/admin/**", "/api/**").hasRole("ADMIN")
                .anyRequest().authenticated())
            .formLogin(form -> form
                .loginPage("/login")               // GET: Action de Struts que pinta login.jsp
                .loginProcessingUrl("/login")      // POST: lo procesa Spring Security
                .defaultSuccessUrl("/catalogo")
                .failureUrl("/login?error")
                .permitAll())
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/catalogo?salir"))
            .exceptionHandling(ex -> ex.accessDeniedPage("/WEB-INF/jsp/acceso-denegado.jsp"));
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }}
