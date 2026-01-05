package ch.bbw.obelix.quarry.entity;

import ch.bbw.obelix.quarry.api.dto.DecorativenessDto;
import ch.bbw.obelix.quarry.api.dto.MenhirDto;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for MenhirEntity
 */
class MenhirEntityTest {

    @Test
    void testToDto_shouldConvertEntityToDto() {
        // Given
        MenhirEntity entity = new MenhirEntity();
        UUID id = UUID.randomUUID();
        entity.setId(id);
        entity.setWeight(2.5);
        entity.setStoneType("Granite");
        entity.setDecorativeness(MenhirEntity.Decorativeness.DECORATED);
        entity.setDescription("A beautiful menhir");

        // When
        MenhirDto dto = entity.toDto();

        // Then
        assertEquals(id, dto.id());
        assertEquals(2.5, dto.weight());
        assertEquals("Granite", dto.stoneType());
        assertEquals(DecorativenessDto.DECORATED, dto.decorativeness());
        assertEquals("A beautiful menhir", dto.description());
    }

    @Test
    void testDecorativenessToDto_shouldConvertAllValues() {
        // Test all enum conversions
        assertEquals(DecorativenessDto.PLAIN, MenhirEntity.Decorativeness.PLAIN.toDto());
        assertEquals(DecorativenessDto.SIMPLE, MenhirEntity.Decorativeness.SIMPLE.toDto());
        assertEquals(DecorativenessDto.DECORATED, MenhirEntity.Decorativeness.DECORATED.toDto());
        assertEquals(DecorativenessDto.ORNATE, MenhirEntity.Decorativeness.ORNATE.toDto());
        assertEquals(DecorativenessDto.MASTERWORK, MenhirEntity.Decorativeness.MASTERWORK.toDto());
    }

    @Test
    void testDecorativenessFromDto_shouldConvertAllValues() {
        // Test all enum conversions
        assertEquals(MenhirEntity.Decorativeness.PLAIN, MenhirEntity.Decorativeness.fromDto(DecorativenessDto.PLAIN));
        assertEquals(MenhirEntity.Decorativeness.SIMPLE, MenhirEntity.Decorativeness.fromDto(DecorativenessDto.SIMPLE));
        assertEquals(MenhirEntity.Decorativeness.DECORATED, MenhirEntity.Decorativeness.fromDto(DecorativenessDto.DECORATED));
        assertEquals(MenhirEntity.Decorativeness.ORNATE, MenhirEntity.Decorativeness.fromDto(DecorativenessDto.ORNATE));
        assertEquals(MenhirEntity.Decorativeness.MASTERWORK, MenhirEntity.Decorativeness.fromDto(DecorativenessDto.MASTERWORK));
    }

    @Test
    void testEntityGettersAndSetters() {
        // Given
        MenhirEntity entity = new MenhirEntity();
        UUID id = UUID.randomUUID();

        // When
        entity.setId(id);
        entity.setWeight(3.5);
        entity.setStoneType("Marble");
        entity.setDecorativeness(MenhirEntity.Decorativeness.MASTERWORK);
        entity.setDescription("Premium quality");

        // Then
        assertEquals(id, entity.getId());
        assertEquals(3.5, entity.getWeight());
        assertEquals("Marble", entity.getStoneType());
        assertEquals(MenhirEntity.Decorativeness.MASTERWORK, entity.getDecorativeness());
        assertEquals("Premium quality", entity.getDescription());
    }
}
