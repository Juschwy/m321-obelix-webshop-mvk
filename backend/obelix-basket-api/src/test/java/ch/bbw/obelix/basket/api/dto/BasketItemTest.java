package ch.bbw.obelix.basket.api.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for BasketItem validation
 */
class BasketItemTest {

    @Test
    void testBasketItem_withValidData_shouldCreateSuccessfully() {
        // When
        BasketItem item = new BasketItem("boar", 2);

        // Then
        assertEquals("boar", item.name());
        assertEquals(2, item.count());
    }

    @Test
    void testBasketItem_withNullName_shouldThrowException() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> new BasketItem(null, 2));
    }

    @Test
    void testBasketItem_withEmptyName_shouldThrowException() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> new BasketItem("", 2));
    }

    @Test
    void testBasketItem_withZeroCount_shouldThrowException() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> new BasketItem("boar", 0));
    }

    @Test
    void testBasketItem_withNegativeCount_shouldThrowException() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> new BasketItem("boar", -1));
    }

    @Test
    void testBasketItem_withMinimumValidCount_shouldCreateSuccessfully() {
        // When
        BasketItem item = new BasketItem("honey", 1);

        // Then
        assertEquals("honey", item.name());
        assertEquals(1, item.count());
    }

    @Test
    void testBasketItem_withLargeCount_shouldCreateSuccessfully() {
        // When
        BasketItem item = new BasketItem("fish", 100);

        // Then
        assertEquals("fish", item.name());
        assertEquals(100, item.count());
    }
}
