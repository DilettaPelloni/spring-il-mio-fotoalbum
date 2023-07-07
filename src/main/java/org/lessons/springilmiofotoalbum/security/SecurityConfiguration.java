package org.lessons.springilmiofotoalbum.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {


    @Bean
    DbUserDetailsService userDetailsService() {
        return new DbUserDetailsService();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
            .requestMatchers("api/**").permitAll()
            .requestMatchers("/admin/**").hasAnyAuthority("ADMIN", "SUPERADMIN")
            .requestMatchers("/**").permitAll()
            .and().formLogin().loginPage("/login")
            .and().logout().logoutSuccessUrl("/");
        http.csrf().disable();
        return http.build();
    }

}
