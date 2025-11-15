package ch.bbw.obelix.quarry.api.config;

import ch.bbw.obelix.common.exception.BadRequestException;
import ch.bbw.obelix.quarry.api.service.QuarryClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import reactor.core.publisher.Mono;

/**
 * @author schules
 * @version 02.11.2025
 */

@Configuration
@RequiredArgsConstructor
public class QuarryClientConfig {
    private final WebClient.Builder webClientBuilder;

    @Value("${quarry-server-url:http://localhost:8081}")
    private String quarryServerUrl;

    @Bean
    public QuarryClientService getQuarryApiService() {
        var webClient = webClientBuilder
                .baseUrl(quarryServerUrl)
                .defaultStatusHandler(HttpStatusCode::isError, (req) -> Mono.error(BadRequestException::new))
                .build();
        var factory = HttpServiceProxyFactory.builderFor(WebClientAdapter.create(webClient))
                .build();
        return factory.createClient(QuarryClientService.class);
    }
}
