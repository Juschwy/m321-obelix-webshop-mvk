package ch.bbw.obelix.quarry.service;

import ch.bbw.obelix.common.exception.BadRequestException;
import ch.bbw.obelix.quarry.api.dto.MenhirDto;
import ch.bbw.obelix.quarry.entity.MenhirEntity;
import ch.bbw.obelix.quarry.repository.MenhirRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Test class for QuarryService
 */
@ExtendWith(MockitoExtension.class)
class QuarryServiceTest {

    @Mock
    private MenhirRepository menhirRepository;

    @InjectMocks
    private QuarryService quarryService;

    @Test
    void testGetAllMenhirs_shouldReturnAllMenhirs() {
        // Given
        MenhirEntity menhir1 = createMenhirEntity("Granite", MenhirEntity.Decorativeness.PLAIN, 2.5);
        MenhirEntity menhir2 = createMenhirEntity("Marble", MenhirEntity.Decorativeness.DECORATED, 3.0);
        
        when(menhirRepository.findAll()).thenReturn(Arrays.asList(menhir1, menhir2));

        // When
        List<MenhirDto> result = quarryService.getAllMenhirs();

        // Then
        assertEquals(2, result.size());
        assertEquals("Granite", result.get(0).stoneType());
        assertEquals("Marble", result.get(1).stoneType());
    }

    @Test
    void testGetAllMenhirs_withEmptyRepository_shouldReturnEmptyList() {
        // Given
        when(menhirRepository.findAll()).thenReturn(Arrays.asList());

        // When
        List<MenhirDto> result = quarryService.getAllMenhirs();

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetMenhirById_shouldReturnMenhir() {
        // Given
        UUID id = UUID.randomUUID();
        MenhirEntity menhir = createMenhirEntity("Granite", MenhirEntity.Decorativeness.PLAIN, 2.5);
        menhir.setId(id);
        
        when(menhirRepository.findById(id)).thenReturn(Optional.of(menhir));

        // When
        MenhirDto result = quarryService.getMenhirById(id);

        // Then
        assertNotNull(result);
        assertEquals(id, result.id());
        assertEquals("Granite", result.stoneType());
    }

    @Test
    void testGetMenhirById_withNonExistentId_shouldThrowException() {
        // Given
        UUID id = UUID.randomUUID();
        when(menhirRepository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(BadRequestException.class, () -> quarryService.getMenhirById(id));
    }

    @Test
    void testDeleteById_shouldCallRepository() {
        // Given
        UUID id = UUID.randomUUID();

        // When
        quarryService.deleteById(id);

        // Then
        verify(menhirRepository).deleteById(id);
    }

    @Test
    void testCreateDefaultMenhirs_shouldCreateThreeMenhirs() {
        // Given
        when(menhirRepository.save(any(MenhirEntity.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        quarryService.createDefaultMenhirs();

        // Then
        verify(menhirRepository).deleteAll();
        verify(menhirRepository, times(3)).save(any(MenhirEntity.class));
    }

    @Test
    void testInitializeMenhirs_withEmptyRepository_shouldCreateDefaults() {
        // Given
        when(menhirRepository.count()).thenReturn(0L);
        when(menhirRepository.save(any(MenhirEntity.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        quarryService.initializeMenhirs();

        // Then
        verify(menhirRepository).deleteAll();
        verify(menhirRepository, times(3)).save(any(MenhirEntity.class));
    }

    @Test
    void testInitializeMenhirs_withExistingData_shouldNotCreateDefaults() {
        // Given
        when(menhirRepository.count()).thenReturn(5L);

        // When
        quarryService.initializeMenhirs();

        // Then
        verify(menhirRepository, never()).deleteAll();
        verify(menhirRepository, never()).save(any(MenhirEntity.class));
    }

    private MenhirEntity createMenhirEntity(String stoneType, MenhirEntity.Decorativeness decorativeness, double weight) {
        MenhirEntity menhir = new MenhirEntity();
        menhir.setId(UUID.randomUUID());
        menhir.setStoneType(stoneType);
        menhir.setDecorativeness(decorativeness);
        menhir.setWeight(weight);
        menhir.setDescription("Test description");
        return menhir;
    }
}
