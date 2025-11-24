package ch.bbw.obelix.basket.api.config;

import ch.bbw.obelix.basket.api.service.BasketClientService;
import ch.bbw.obelix.common.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
@ConditionalOnProperty(name = "obelix.basket.url")
@RequiredArgsConstructor
public class BasketClientConfig {
    private final WebClient.Builder webClientBuilder;

    @Value("${obelix.basket.url}")
    private String basketServerUrl;

    @Bean
    public BasketClientService getBasketClientService() {
        var webClient = webClientBuilder
                .baseUrl(basketServerUrl)
                .defaultStatusHandler(HttpStatusCode::isError, (req) -> Mono.error(BadRequestException::new))
                .build();
        var factory = HttpServiceProxyFactory.builderFor(WebClientAdapter.create(webClient))
                .build();
        return factory.createClient(BasketClientService.class);
    }
}
