package ch.bbw.obelix.basket.api;

import ch.bbw.obelix.basket.api.dto.BasketDto;
import ch.bbw.obelix.basket.api.dto.BasketItem;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.PostExchange;
import org.springframework.web.service.annotation.PutExchange;

import java.util.UUID;

/**
 * @author schules
 * @version 03.11.2025
 */

public interface BasketApi {
    @PutExchange("/api/basket/offer")
    BasketDto offer(@RequestBody BasketItem basketItem);

    @DeleteExchange("/api/basket")
    void leave();

    @PostExchange("/api/basket/buy/{menhirId}")
    void exchangeFor(@PathVariable UUID menhirId);
}
