package br.insper.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {


    @Value("${services.user}")
    private String userServiceUrl;

    @Value("${services.connections}")
    private String connectionsServiceUrl;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder, TimingGatewayFilterFactory timing) {
        return builder.routes()
                .route("user-route", r -> r.path("/user/**")
                        .filters(f -> f
                                .filter(timing.apply(config(true, "user-route"))))
                        .uri(userServiceUrl))
                .route("connections-route", r -> r.path("/connections/**")
                        .filters(f -> f
                                .filter(timing.apply(config(true, "connection-route"))))
                        .uri(connectionsServiceUrl))
                .build();
    }

    private TimingGatewayFilterFactory.Config config(boolean enabled, String label) {
        TimingGatewayFilterFactory.Config config = new TimingGatewayFilterFactory.Config();
        config.setEnabled(enabled);
        config.setLabel(label);
        return config;
    }
}
