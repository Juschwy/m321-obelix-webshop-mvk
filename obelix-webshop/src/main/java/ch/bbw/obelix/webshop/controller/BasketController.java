package ch.bbw.obelix.webshop.controller;

import ch.bbw.obelix.quarry.api.dto.BasketDto;
import ch.bbw.obelix.webshop.service.BasketService;
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
    private final BasketService basketService;

    @PutMapping("/api/basket/offer")
    public BasketDto offer(@RequestBody BasketDto.BasketItem basketItem) {
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
