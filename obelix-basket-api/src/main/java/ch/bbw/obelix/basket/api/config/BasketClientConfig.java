package ch.bbw.obelix.basket.api.config;

import ch.bbw.obelix.basket.api.service.BasketClientService;
import ch.bbw.obelix.common.exception.BadRequestException;
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
public class BasketClientConfig {
    @Bean
    public BasketClientService getBasketClientService(@Value("${basket-server-url:http://localhost:8082}") String basketServerUrl) {
        var webClient = WebClient.builder()
                .baseUrl(basketServerUrl)
                .defaultStatusHandler(HttpStatusCode::isError, (req) -> Mono.error(BadRequestException::new))
                .build();
        var factory = HttpServiceProxyFactory.builderFor(WebClientAdapter.create(webClient))
                .build();
        return factory.createClient(BasketClientService.class);
    }
}
