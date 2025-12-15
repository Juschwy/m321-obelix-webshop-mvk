package ch.bbw.obelix.webshop.controller;

import ch.bbw.obelix.quarry.api.dto.DecorativenessDto;
import ch.bbw.obelix.quarry.api.dto.MenhirDto;
import ch.bbw.obelix.quarry.api.service.QuarryClientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for MenhirController
 */
@WebMvcTest(MenhirController.class)
@ActiveProfiles("test")
class MenhirControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QuarryClientService quarryClientService;

    @Test
    void testGetAllMenhirs_shouldReturnListOfMenhirs() throws Exception {
        // Given
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        MenhirDto menhir1 = new MenhirDto(id1, 2.5, "Granite", DecorativenessDto.PLAIN, "Description 1");
        MenhirDto menhir2 = new MenhirDto(id2, 3.0, "Marble", DecorativenessDto.DECORATED, "Description 2");
        
        when(quarryClientService.getAllMenhirs()).thenReturn(Arrays.asList(menhir1, menhir2));

        // When & Then
        mockMvc.perform(get("/api/menhirs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(id1.toString()))
                .andExpect(jsonPath("$[0].stoneType").value("Granite"))
                .andExpect(jsonPath("$[1].id").value(id2.toString()))
                .andExpect(jsonPath("$[1].stoneType").value("Marble"));
    }

    @Test
    void testGetAllMenhirs_withEmptyList_shouldReturnEmptyArray() throws Exception {
        // Given
        when(quarryClientService.getAllMenhirs()).thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get("/api/menhirs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void testGetMenhirById_shouldReturnMenhir() throws Exception {
        // Given
        UUID id = UUID.randomUUID();
        MenhirDto menhir = new MenhirDto(id, 2.5, "Granite", DecorativenessDto.MASTERWORK, "Premium menhir");
        
        when(quarryClientService.getMenhirById(id)).thenReturn(menhir);

        // When & Then
        mockMvc.perform(get("/api/menhirs/{menhirId}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.weight").value(2.5))
                .andExpect(jsonPath("$.stoneType").value("Granite"))
                .andExpect(jsonPath("$.decorativeness").value("MASTERWORK"))
                .andExpect(jsonPath("$.description").value("Premium menhir"));
    }
}
