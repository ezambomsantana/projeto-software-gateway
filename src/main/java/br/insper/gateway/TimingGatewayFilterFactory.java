package br.insper.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

@Component
public class TimingGatewayFilterFactory extends AbstractGatewayFilterFactory<TimingGatewayFilterFactory.Config> {

    private static final Logger log = LoggerFactory.getLogger(TimingGatewayFilterFactory.class);

    public TimingGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if (!config.isEnabled()) {
                return chain.filter(exchange);
            }

            long start = System.nanoTime();

            return chain.filter(exchange)
                    .doFinally(signalType -> {
                        long elapsedMs = (System.nanoTime() - start) / 1_000_000;
                        log.info("[{}] {} {} took {} ms",
                                config.getLabel(),
                                exchange.getRequest().getMethod(),
                                exchange.getRequest().getURI().getPath(),
                                elapsedMs);
                    });
        };
    }

    public static class Config {
        private boolean enabled = true;
        private String label = "route";

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }
    }
}
