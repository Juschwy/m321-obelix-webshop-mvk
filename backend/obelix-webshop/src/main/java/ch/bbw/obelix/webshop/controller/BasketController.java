package ch.bbw.obelix.webshop.controller;

import ch.bbw.obelix.basket.api.dto.BasketDto;
import ch.bbw.obelix.basket.api.dto.BasketItem;
import ch.bbw.obelix.basket.api.service.BasketClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * @author schules
 * @version 03.11.2025
 */

@RestController
@RequiredArgsConstructor
public class BasketController {
    private final BasketClientService basketClientService;

    @PutMapping("/api/basket/offer")
    public BasketDto offer(@RequestBody BasketItem basketItem) {
        return basketClientService.offer(basketItem);
    }

    @DeleteMapping("/api/basket")
    public void leave() {
        basketClientService.leave();
    }

    @PostMapping("/api/basket/buy/{menhirId}")
    public void exchangeFor(@PathVariable UUID menhirId) {
        basketClientService.exchangeFor(menhirId);
    }
}
