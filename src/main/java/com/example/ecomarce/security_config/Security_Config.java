package com.example.ecomarce.security_config;


import com.example.ecomarce.service_pkg.UserDetailsIML;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@Configuration

@EnableWebSecurity

public class Security_Config {

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsIML();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(bCryptPasswordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/orders/**").hasAuthority("ROLE_ORDER")
                        .requestMatchers("/products/**").hasAuthority("ROLE_PRODUCT")
                        .requestMatchers("/user/**").hasAuthority("ROLE_USER")
                        .requestMatchers("/signup_successful","/delete-c-p/**","/signup","/error","/product","/","/static/**","/forget-vfy/**","/details/**","/add/**","/cart/**","/verify","/verification/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(
                        httpSecurityFormLoginConfigurer -> httpSecurityFormLoginConfigurer
                                .loginPage("/login")
                               // .loginProcessingUrl("/login")
                                .defaultSuccessUrl("/verify"  , true)
                                .permitAll()
                )
                .rememberMe(rememberMe -> rememberMe
                       .key("uniqueAndSecret") // A unique key for remember-me functionality
                        .userDetailsService(userDetailsService())
                        .tokenValiditySeconds(14 * 24 * 60 * 60) // 14 days
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(org.springframework.security.config.http.SessionCreationPolicy.ALWAYS) // Always create a session
                        .maximumSessions(1) // Only one session per user allowed
                        .expiredUrl("/loginP?expired") // Redirect on session expiry
                ).build();
    }


}


