package ch.bbw.obelix.basket.impl.service;

import ch.bbw.obelix.basket.api.dto.BasketDto;
import ch.bbw.obelix.basket.api.dto.BasketItem;
import ch.bbw.obelix.common.exception.BadRequestException;
import ch.bbw.obelix.quarry.api.dto.DecorativenessDto;
import ch.bbw.obelix.quarry.api.service.QuarryClientService;
import jakarta.annotation.PostConstruct;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author Tim Leo Laurin Leuenberger
 * @version 19.01.2026
 */

@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
public class BasketService {
    private final QuarryClientService quarryClientService;

    private BasketDto basket;

    public BasketDto offer(@NonNull BasketItem basketItem) {
        basket.items()
                .add(basketItem);
        return basket;
    }

    public List<BasketDto> offerMultible(@NonNull List<BasketItem> basketItems) {
        var list = new ArrayList<BasketDto>();
        for (BasketItem basketItem : basketItems) {
            list.add(offer(basketItem));
        }
        return list;
    }

    @PostConstruct
    public void leave() {
        basket = BasketDto.empty();
    }

    public boolean isGoodOffer(DecorativenessDto decorativeness) {
        var stoneWorth = decorativeness.ordinal() * 50;
        var basketWorth = basket.items()
                .stream().map(x -> switch (x.name().toLowerCase(Locale.ROOT)) {
                    case "silber" -> 1;
                    case "schaf" -> 40;
                    case "wildschwein" -> 67;
                    case "esel" -> 100;
                    case "gold" -> 150;
                    case "kamel" -> 160;
                    default -> 1; // everything is worth something
                } * x.count()).reduce(0, Integer::sum);
        log.info("basket worth {} vs menhir worth {} ({})", basketWorth, decorativeness, stoneWorth);
        return basketWorth >= stoneWorth;
    }

    public void exchange(UUID menhirId) {
        var menhir = quarryClientService.getMenhirById(menhirId);
        var decorativeness = menhir.decorativeness();
        if (!isGoodOffer(decorativeness)) {
            throw new BadRequestException("Bad Offer: That won't even feed Idefix!");
        }
        quarryClientService.deleteById(menhirId);
        leave();
    }

    @Transactional
    public void exchangeMultible(List<UUID> uuids) {
        uuids.forEach(this::exchange);
    }
}
