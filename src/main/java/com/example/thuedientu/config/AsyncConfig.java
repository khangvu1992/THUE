package com.example.thuedientu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);  // Số luồng tối thiểu
        executor.setMaxPoolSize(4);   // Số luồng tối đa
        executor.setQueueCapacity(100); // Dung lượng hàng đợi
        executor.setThreadNamePrefix("async-thread-");
        executor.initialize();
        return executor;
    }
}
