//package com.example.thuedientu.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//
//@Configuration
//public class ThreadPoolConfig {
//
//    @Bean
//    public ThreadPoolTaskExecutor taskExecutor() {
//        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        executor.setCorePoolSize(8);   // Số thread cơ bản
//        executor.setMaxPoolSize(12);    // Số thread tối đa
//        executor.setQueueCapacity(25);  // Số lượng công việc có thể chờ trong hàng đợi
//        executor.setThreadNamePrefix("my-thread-");
//        executor.initialize();
//        return executor;
//    }
//}
