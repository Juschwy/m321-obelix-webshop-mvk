package ch.bbw.obelix.basket.impl.controller;

import ch.bbw.obelix.basket.api.BasketApi;
import ch.bbw.obelix.basket.api.dto.BasketDto;
import ch.bbw.obelix.basket.api.dto.BasketItem;
import ch.bbw.obelix.basket.impl.service.BasketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * @author Tim Leo Laurin Leuenberger
 * @version 15.12.2025
 */

@RestController
@RequiredArgsConstructor
public class BasketController implements BasketApi {
    private final BasketService basketService;

    @Override
    public BasketDto offer(@RequestBody BasketItem basketItem) {
        return basketService.offer(basketItem);
    }

    @Override
    public List<BasketDto> offerMultible(@RequestBody List<BasketItem> basketItems) {
        return basketService.offerMultible(basketItems);
    }

    @Override
    public void leave() {
        basketService.leave();
    }

    @Override
    public void exchangeFor(@PathVariable UUID menhirId) {
        basketService.exchange(menhirId);
    }

    @Override
    public void exchangeForMultible(@RequestBody List<UUID> uuids) {

    }
}
