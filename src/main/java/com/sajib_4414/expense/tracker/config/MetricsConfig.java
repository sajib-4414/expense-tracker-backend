package com.sajib_4414.expense.tracker.config;

import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfig {
    @Bean
    public JvmGcMetrics jvmGcMetrics() {
        return new JvmGcMetrics();
    }
}