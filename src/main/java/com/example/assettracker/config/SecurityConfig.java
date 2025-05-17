package com.example.assettracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults()) // CORSを有効化
                .csrf(csrf -> csrf.disable()) // CSRFを無効化（API用途なので）
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/**").permitAll() // 全エンドポイント許可（開発用）
                        .anyRequest().permitAll());

        return http.build();
    }
}
