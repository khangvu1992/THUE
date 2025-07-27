package com.example.thuedientu.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        // ✅ Cho phép mọi nguồn (bạn có thể thay bằng localhost:3000 nếu cần hạn chế)
        config.setAllowCredentials(true); // Cho phép gửi cookie/auth header
        config.addAllowedOriginPattern("*"); // Spring Boot >= 2.4 dùng AllowedOriginPattern thay vì addAllowedOrigin("*")
        config.addAllowedHeader("*"); // Cho phép mọi header
        config.addAllowedMethod("*"); // Cho phép mọi phương thức GET, POST, PUT, DELETE...

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // Áp dụng cho mọi endpoint

        return new CorsFilter(source);
    }
}
