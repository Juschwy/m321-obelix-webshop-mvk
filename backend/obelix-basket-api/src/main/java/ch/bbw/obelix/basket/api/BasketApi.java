package ch.bbw.obelix.basket.api;

import ch.bbw.obelix.basket.api.dto.BasketDto;
import ch.bbw.obelix.basket.api.dto.BasketItem;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import org.springframework.web.service.annotation.PutExchange;

import java.util.List;
import java.util.UUID;

/**
 * @author Tim Leo Laurin Leuenberger
 * @version 15.12.2025
 */

@HttpExchange("/api/basket")
public interface BasketApi {
    @PutExchange("/offer")
    BasketDto offer(@RequestBody BasketItem basketItem);

    @PutExchange("/offerMultible")
    List<BasketDto> offerMultible(@RequestBody List<BasketItem> basketItems);

    @DeleteExchange("/")
    void leave();

    @PostExchange("/buy/{menhirId}")
    void exchangeFor(@PathVariable UUID menhirId);

    @PostExchange("/buyMultible")
    void exchangeForMultible(@RequestBody List<UUID> menhirIds);
}
