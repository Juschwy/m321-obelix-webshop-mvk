package ch.bbw.obelix.webshop.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for WebshopController
 */
@WebMvcTest(WebshopController.class)
@ActiveProfiles("test")
class WebshopControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testWelcome_shouldReturnWelcomeMessage() throws Exception {
        mockMvc.perform(get("/api"))
                .andExpect(status().isOk())
                .andExpect(content().string("Welcome to Obelix's Menhir Shop! The finest menhirs in all of Gaul! Ces Romains sont fous!"));
    }
}
