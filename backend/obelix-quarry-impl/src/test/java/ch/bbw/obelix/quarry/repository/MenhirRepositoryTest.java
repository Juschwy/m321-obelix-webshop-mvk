package ch.bbw.obelix.quarry.repository;

import ch.bbw.obelix.quarry.entity.MenhirEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for MenhirRepository
 */
@DataJpaTest
@ActiveProfiles("test")
class MenhirRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MenhirRepository menhirRepository;

    private MenhirEntity granite;
    private MenhirEntity marble;
    private MenhirEntity limestone;

    @BeforeEach
    void setUp() {
        menhirRepository.deleteAll();

        granite = new MenhirEntity();
        granite.setStoneType("Granite Gaulois");
        granite.setWeight(2.5);
        granite.setDecorativeness(MenhirEntity.Decorativeness.DECORATED);
        granite.setDescription("Premium granite");
        entityManager.persist(granite);

        marble = new MenhirEntity();
        marble.setStoneType("Roman Marble");
        marble.setWeight(3.0);
        marble.setDecorativeness(MenhirEntity.Decorativeness.MASTERWORK);
        marble.setDescription("Liberated from Romans");
        entityManager.persist(marble);

        limestone = new MenhirEntity();
        limestone.setStoneType("Local Limestone");
        limestone.setWeight(1.5);
        limestone.setDecorativeness(MenhirEntity.Decorativeness.PLAIN);
        limestone.setDescription("Budget option");
        entityManager.persist(limestone);

        entityManager.flush();
    }

    @Test
    void testFindByStoneTypeContainingIgnoreCase_shouldFindMatchingMenhirs() {
        // When
        List<MenhirEntity> results = menhirRepository.findByStoneTypeContainingIgnoreCase("granite");

        // Then
        assertEquals(1, results.size());
        assertEquals("Granite Gaulois", results.get(0).getStoneType());
    }

    @Test
    void testFindByStoneTypeContainingIgnoreCase_withPartialMatch_shouldFindAll() {
        // When
        List<MenhirEntity> results = menhirRepository.findByStoneTypeContainingIgnoreCase("marble");

        // Then
        assertEquals(1, results.size());
        assertEquals("Roman Marble", results.get(0).getStoneType());
    }

    @Test
    void testFindByStoneTypeContainingIgnoreCase_withNoMatch_shouldReturnEmpty() {
        // When
        List<MenhirEntity> results = menhirRepository.findByStoneTypeContainingIgnoreCase("sandstone");

        // Then
        assertTrue(results.isEmpty());
    }

    @Test
    void testFindByStoneTypeContainingIgnoreCase_shouldBeCaseInsensitive() {
        // When
        List<MenhirEntity> results = menhirRepository.findByStoneTypeContainingIgnoreCase("GRANITE");

        // Then
        assertEquals(1, results.size());
    }

    @Test
    void testFindMenhirByDecorativeness_shouldFindByDecorativeness() {
        // When
        List<MenhirEntity> results = menhirRepository.findMenhirByDecorativeness(MenhirEntity.Decorativeness.DECORATED);

        // Then
        assertEquals(1, results.size());
        assertEquals(MenhirEntity.Decorativeness.DECORATED, results.get(0).getDecorativeness());
    }

    @Test
    void testFindMenhirByDecorativeness_withMultipleMatches_shouldFindAll() {
        // Given - add another PLAIN menhir
        MenhirEntity anotherPlain = new MenhirEntity();
        anotherPlain.setStoneType("Simple Stone");
        anotherPlain.setWeight(1.0);
        anotherPlain.setDecorativeness(MenhirEntity.Decorativeness.PLAIN);
        anotherPlain.setDescription("Another plain one");
        entityManager.persist(anotherPlain);
        entityManager.flush();

        // When
        List<MenhirEntity> results = menhirRepository.findMenhirByDecorativeness(MenhirEntity.Decorativeness.PLAIN);

        // Then
        assertEquals(2, results.size());
    }

    @Test
    void testFindMenhirByDecorativeness_withNoMatch_shouldReturnEmpty() {
        // When
        List<MenhirEntity> results = menhirRepository.findMenhirByDecorativeness(MenhirEntity.Decorativeness.ORNATE);

        // Then
        assertTrue(results.isEmpty());
    }

    @Test
    void testFindAll_shouldReturnAllMenhirs() {
        // When
        List<MenhirEntity> results = menhirRepository.findAll();

        // Then
        assertEquals(3, results.size());
    }

    @Test
    void testSave_shouldPersistNewMenhir() {
        // Given
        MenhirEntity newMenhir = new MenhirEntity();
        newMenhir.setStoneType("New Stone");
        newMenhir.setWeight(2.0);
        newMenhir.setDecorativeness(MenhirEntity.Decorativeness.SIMPLE);
        newMenhir.setDescription("Brand new");

        // When
        MenhirEntity saved = menhirRepository.save(newMenhir);

        // Then
        assertNotNull(saved.getId());
        assertEquals("New Stone", saved.getStoneType());
    }

    @Test
    void testDeleteById_shouldRemoveMenhir() {
        // Given
        long countBefore = menhirRepository.count();

        // When
        menhirRepository.deleteById(granite.getId());

        // Then
        assertEquals(countBefore - 1, menhirRepository.count());
        assertFalse(menhirRepository.findById(granite.getId()).isPresent());
    }
}
