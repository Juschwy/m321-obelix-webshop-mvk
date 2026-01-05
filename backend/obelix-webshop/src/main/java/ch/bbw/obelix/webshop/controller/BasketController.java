package ch.bbw.obelix.webshop.controller;

import ch.bbw.obelix.basket.api.dto.BasketDto;
import ch.bbw.obelix.basket.api.dto.BasketItem;
import ch.bbw.obelix.basket.api.service.BasketClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * @author Tim Leo Laurin Leuenberger
 * @version 15.12.2025
 */

@RestController
@RequiredArgsConstructor
public class BasketController {
    private final BasketClientService basketClientService;

    @PutMapping("/api/basket/offer")
    public BasketDto offer(@RequestBody BasketItem basketItem) {
        return basketClientService.offer(basketItem);
    }

    @PutMapping("/api/basket/offerMultible")
    public List<BasketDto> offerMultible(@RequestBody List<BasketItem> basketItems) {
        return basketClientService.offerMultible(basketItems);
    }

    @DeleteMapping("/api/basket")
    public void leave() {
        basketClientService.leave();
    }

    @PostMapping("/api/basket/buy/{menhirId}")
    public void exchangeFor(@PathVariable UUID menhirId) {
        basketClientService.exchangeFor(menhirId);
    }

    @PostMapping("/api/basket/buyMultible")
    public void exchangeForMutible(@RequestBody List<UUID> menhirIds) {
        basketClientService.exchangeForMultible(menhirIds);
    }
}
