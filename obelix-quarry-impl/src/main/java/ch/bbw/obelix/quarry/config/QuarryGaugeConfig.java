package ch.bbw.obelix.quarry.config;

import ch.bbw.obelix.quarry.repository.MenhirRepository;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Configuration;

/**
 * @author schules
 * @version 17.11.2025
 */

@Configuration
public class QuarryGaugeConfig {

    public QuarryGaugeConfig(MeterRegistry meterRegistry, MenhirRepository menhirRepository) {
        Gauge.builder("menhirs_count", menhirRepository::count)
                .description("Number of menhirs")
                .register(meterRegistry);
    }
}
