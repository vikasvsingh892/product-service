package com.store.productservice.config;

import brave.Tracing;
import brave.baggage.BaggagePropagation;
import brave.baggage.BaggagePropagationConfig;
import brave.context.slf4j.MDCScopeDecorator;
import brave.handler.SpanHandler;
import brave.propagation.CurrentTraceContext;
import brave.propagation.ThreadLocalCurrentTraceContext;
import brave.sampler.Sampler;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.brave.bridge.BraveBaggageManager;
import io.micrometer.tracing.brave.bridge.BraveTracer;
import io.micrometer.tracing.brave.bridge.BraveTracing;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TracingConfig {

    @Bean
    public Tracer tracer() {
        // Create a ThreadLocalCurrentTraceContext for logging traceId
        CurrentTraceContext currentTraceContext = ThreadLocalCurrentTraceContext.newBuilder()
                .addScopeDecorator(MDCScopeDecorator.create()) // Adds traceId and spanId to MDC (for logging)
                .build();

        // Define a Brave Tracing instance
        Tracing tracing = Tracing.newBuilder()
                .currentTraceContext(currentTraceContext)
                .supportsJoin(false) // Prevents joining existing trace context
                .traceId128Bit(true) // Generates longer traceId for better uniqueness
                .sampler(Sampler.ALWAYS_SAMPLE) // Ensures all requests are traced (use `Sampler.NEVER_SAMPLE` in production)
                .propagationFactory(BaggagePropagation.newFactoryBuilder(BaggagePropagation.FactoryBuilder.Default.INSTANCE)
                        .add(BaggagePropagationConfig.SingleBaggageField.remote("traceId"))
                        .add(BaggagePropagationConfig.SingleBaggageField.remote("spanId"))
                        .build())
                .build();

        // Use BraveBaggageManager for handling trace context
        BraveBaggageManager baggageManager = new BraveBaggageManager();

        // Convert Brave Tracer into Micrometer Tracer
        return new BraveTracer(tracing.tracer(), currentTraceContext, baggageManager);
    }
}
