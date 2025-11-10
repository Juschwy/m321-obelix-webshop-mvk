package ch.bbw.obelix.basket.impl.controller;

import ch.bbw.obelix.basket.api.BasketApi;
import ch.bbw.obelix.basket.api.dto.BasketDto;
import ch.bbw.obelix.basket.api.dto.BasketItem;
import ch.bbw.obelix.basket.impl.service.BasketService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * @author schules
 * @version 10.11.2025
 */

@RestController
@RequiredArgsConstructor
public class BasketController implements BasketApi {
    private final BasketService basketService;

    @PutMapping("/api/basket/offer")
    public BasketDto offer(@RequestBody BasketItem basketItem) {
        return basketService.offer(basketItem);
    }

    @DeleteMapping("/api/basket")
    public void leave() {
        basketService.leave();
    }

    @PostMapping("/api/basket/buy/{menhirId}")
    public void exchangeFor(@PathVariable UUID menhirId) {
        basketService.exchange(menhirId);
    }
}
