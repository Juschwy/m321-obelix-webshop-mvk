package ch.bbw.obelix.basket.api.dto;

/**
 * @author schules
 * @version 03.11.2025
 */

public record BasketItem(String name, int count) {

    public BasketItem {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("name is empty");
        }
        if (count < 1) {
            throw new IllegalArgumentException("count must be greater than zero");
        }
    }
}
