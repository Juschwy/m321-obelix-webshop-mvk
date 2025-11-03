package ch.bbw.obelix.webshop.config;

import ch.bbw.obelix.quarry.api.QuarryApi;
import ch.bbw.obelix.webshop.exception.BadRequestException;
import ch.bbw.obelix.webshop.service.QuarryClientService;
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
public class QuarryClientConfig {
    @Bean
    public QuarryClientService getQuarryApiService(@Value("${quarry-server-url}") String quarryServerUrl) {
        var webClient = WebClient.builder()
                .baseUrl(quarryServerUrl)
                .defaultStatusHandler(HttpStatusCode::isError, (req) -> Mono.error(BadRequestException::new))
                .build();
        var factory = HttpServiceProxyFactory.builderFor(WebClientAdapter.create(webClient))
                .build();
        return factory.createClient(QuarryClientService.class);
    }
}
