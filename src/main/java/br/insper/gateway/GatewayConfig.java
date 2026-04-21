package br.insper.gateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder, TimingGatewayFilterFactory timing) {
        return builder.routes()
                .route("user-route", r -> r.path("/user/**")
                        .filters(f -> f.stripPrefix(1)
                                .filter(timing.apply(config(true, "user-route"))))
                        .uri("http://localhost:5000"))
                .route("connections-route", r -> r.path("/connections/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("http://localhost:8080"))
                .build();
    }

    private TimingGatewayFilterFactory.Config config(boolean enabled, String label) {
        TimingGatewayFilterFactory.Config config = new TimingGatewayFilterFactory.Config();
        config.setEnabled(enabled);
        config.setLabel(label);
        return config;
    }
}
