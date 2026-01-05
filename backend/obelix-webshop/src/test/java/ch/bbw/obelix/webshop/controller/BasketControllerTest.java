package ch.bbw.obelix.webshop.controller;

import ch.bbw.obelix.basket.api.dto.BasketDto;
import ch.bbw.obelix.basket.api.dto.BasketItem;
import ch.bbw.obelix.basket.api.service.BasketClientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for BasketController
 */
@WebMvcTest(ch.bbw.obelix.webshop.controller.BasketController.class)
@ActiveProfiles("test")
class BasketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BasketClientService basketClientService;

    @Test
    void testOffer_shouldAddItemToBasket() throws Exception {
        // Given
        BasketItem item = new BasketItem("boar", 2);
        ArrayList<BasketItem> items = new ArrayList<>();
        items.add(item);
        BasketDto basketDto = new BasketDto(items);
        
        when(basketClientService.offer(any(BasketItem.class))).thenReturn(basketDto);

        // When & Then
        mockMvc.perform(put("/api/basket/offer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(item)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items[0].name").value("boar"))
                .andExpect(jsonPath("$.items[0].count").value(2));

        verify(basketClientService).offer(any(BasketItem.class));
    }

    @Test
    void testLeave_shouldClearBasket() throws Exception {
        // When & Then
        mockMvc.perform(delete("/api/basket"))
                .andExpect(status().isOk());

        verify(basketClientService).leave();
    }

    @Test
    void testExchangeFor_shouldCallBasketService() throws Exception {
        // Given
        UUID menhirId = UUID.randomUUID();

        // When & Then
        mockMvc.perform(post("/api/basket/buy/{menhirId}", menhirId))
                .andExpect(status().isOk());

        verify(basketClientService).exchangeFor(menhirId);
    }
}
