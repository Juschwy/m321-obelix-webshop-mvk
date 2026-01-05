package ch.bbw.obelix.basket.api.dto;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for BasketDto
 */
class BasketDtoTest {

    @Test
    void testEmpty_shouldCreateEmptyBasket() {
        // When
        BasketDto basket = BasketDto.empty();

        // Then
        assertNotNull(basket);
        assertNotNull(basket.items());
        assertTrue(basket.items().isEmpty());
    }

    @Test
    void testBasketDto_withItems_shouldStoreItems() {
        // Given
        ArrayList<BasketItem> items = new ArrayList<>();
        items.add(new BasketItem("boar", 2));
        items.add(new BasketItem("honey", 3));

        // When
        BasketDto basket = new BasketDto(items);

        // Then
        assertEquals(2, basket.items().size());
        assertEquals("boar", basket.items().get(0).name());
        assertEquals("honey", basket.items().get(1).name());
    }

    @Test
    void testWith_shouldCreateNewInstanceWithUpdatedItems() {
        // Given
        BasketDto originalBasket = BasketDto.empty();
        ArrayList<BasketItem> newItems = new ArrayList<>();
        newItems.add(new BasketItem("fish", 1));

        // When
        BasketDto updatedBasket = originalBasket.withItems(newItems);

        // Then
        assertEquals(0, originalBasket.items().size());
        assertEquals(1, updatedBasket.items().size());
        assertEquals("fish", updatedBasket.items().get(0).name());
    }
}
