package ch.bbw.obelix.basket.api.dto;

import lombok.With;

import java.util.ArrayList;

/**
 * @author schules
 * @version 03.11.2025
 */

@With
public record BasketDto(ArrayList<BasketItem> items) {

    public static BasketDto empty() {
        return new BasketDto(new ArrayList<>());
    }


}