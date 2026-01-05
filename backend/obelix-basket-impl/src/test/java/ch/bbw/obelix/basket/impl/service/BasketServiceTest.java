package ch.bbw.obelix.basket.impl.service;

import ch.bbw.obelix.basket.api.dto.BasketDto;
import ch.bbw.obelix.basket.api.dto.BasketItem;
import ch.bbw.obelix.common.exception.BadRequestException;
import ch.bbw.obelix.quarry.api.dto.DecorativenessDto;
import ch.bbw.obelix.quarry.api.dto.MenhirDto;
import ch.bbw.obelix.quarry.api.service.QuarryClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for BasketService
 */
@ExtendWith(MockitoExtension.class)
class BasketServiceTest {

    @Mock
    private QuarryClientService quarryClientService;

    @InjectMocks
    private BasketService basketService;

    @BeforeEach
    void setUp() {
        basketService.leave();
    }

    @Test
    void testOffer_shouldAddItemToBasket() {
        // Given
        BasketItem item = new BasketItem("boar", 2);

        // When
        BasketDto result = basketService.offer(item);

        // Then
        assertNotNull(result);
        assertEquals(1, result.items().size());
        assertEquals("boar", result.items().get(0).name());
        assertEquals(2, result.items().get(0).count());
    }

    @Test
    void testOffer_shouldAddMultipleItems() {
        // Given
        BasketItem item1 = new BasketItem("boar", 2);
        BasketItem item2 = new BasketItem("honey", 3);

        // When
        basketService.offer(item1);
        BasketDto result = basketService.offer(item2);

        // Then
        assertEquals(2, result.items().size());
    }

    @Test
    void testLeave_shouldEmptyBasket() {
        // Given
        basketService.offer(new BasketItem("boar", 2));

        // When
        basketService.leave();

        // Then
        BasketDto result = basketService.offer(new BasketItem("honey", 1));
        assertEquals(1, result.items().size());
    }

    @Test
    void testIsGoodOffer_withBoar_shouldReturnTrue() {
        // Given
        basketService.offer(new BasketItem("boar", 2));
        DecorativenessDto decorativeness = DecorativenessDto.PLAIN;

        // When
        boolean result = basketService.isGoodOffer(decorativeness);

        // Then
        assertTrue(result);
    }

    @Test
    void testIsGoodOffer_withHoney_shouldReturnTrue() {
        // Given
        basketService.offer(new BasketItem("honey", 3));
        DecorativenessDto decorativeness = DecorativenessDto.PLAIN;

        // When
        boolean result = basketService.isGoodOffer(decorativeness);

        // Then
        assertTrue(result);
    }

    @Test
    void testIsGoodOffer_withMagicPotion_shouldReturnFalseForHigherDecor() {
        // Given
        basketService.offer(new BasketItem("magic potion", 5));
        DecorativenessDto decorativeness = DecorativenessDto.SIMPLE;

        // When
        boolean result = basketService.isGoodOffer(decorativeness);

        // Then
        assertFalse(result);
    }

    @Test
    void testIsGoodOffer_withMagicPotionOnly_shouldReturnTrueForPlain() {
        // Given
        basketService.offer(new BasketItem("magic potion", 5));
        DecorativenessDto decorativeness = DecorativenessDto.PLAIN;

        // When
        boolean result = basketService.isGoodOffer(decorativeness);

        // Then
        assertTrue(result); // 0 >= 0 (PLAIN ordinal)
    }

    @Test
    void testIsGoodOffer_withInsufficientValue_shouldReturnFalse() {
        // Given
        basketService.offer(new BasketItem("honey", 1));
        DecorativenessDto decorativeness = DecorativenessDto.MASTERWORK;

        // When
        boolean result = basketService.isGoodOffer(decorativeness);

        // Then
        assertFalse(result);
    }

    @Test
    void testIsGoodOffer_withOtherItem_shouldUseDefaultValue() {
        // Given
        basketService.offer(new BasketItem("fish", 5));
        DecorativenessDto decorativeness = DecorativenessDto.SIMPLE;

        // When
        boolean result = basketService.isGoodOffer(decorativeness);

        // Then
        assertTrue(result);
    }

    @Test
    void testExchange_withGoodOffer_shouldSucceed() {
        // Given
        UUID menhirId = UUID.randomUUID();
        MenhirDto menhir = new MenhirDto(menhirId, 2.5, "Granite", DecorativenessDto.PLAIN, "Test menhir");
        basketService.offer(new BasketItem("boar", 2));

        when(quarryClientService.getMenhirById(menhirId)).thenReturn(menhir);

        // When
        basketService.exchange(menhirId);

        // Then
        verify(quarryClientService).deleteById(menhirId);
    }

    @Test
    void testExchange_withBadOffer_shouldThrowException() {
        // Given
        UUID menhirId = UUID.randomUUID();
        MenhirDto menhir = new MenhirDto(menhirId, 2.5, "Granite", DecorativenessDto.MASTERWORK, "Test menhir");
        basketService.offer(new BasketItem("honey", 1));

        when(quarryClientService.getMenhirById(menhirId)).thenReturn(menhir);

        // When & Then
        assertThrows(BadRequestException.class, () -> basketService.exchange(menhirId));
        verify(quarryClientService, never()).deleteById(menhirId);
    }

    @Test
    void testExchange_shouldEmptyBasketAfterSuccess() {
        // Given
        UUID menhirId = UUID.randomUUID();
        MenhirDto menhir = new MenhirDto(menhirId, 2.5, "Granite", DecorativenessDto.PLAIN, "Test menhir");
        basketService.offer(new BasketItem("boar", 2));

        when(quarryClientService.getMenhirById(menhirId)).thenReturn(menhir);

        // When
        basketService.exchange(menhirId);

        // Then
        BasketDto result = basketService.offer(new BasketItem("honey", 1));
        assertEquals(1, result.items().size());
    }
}
