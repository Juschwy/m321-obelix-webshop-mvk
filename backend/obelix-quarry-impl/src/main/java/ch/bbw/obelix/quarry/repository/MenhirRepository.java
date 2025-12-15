package ch.bbw.obelix.quarry.repository;

import ch.bbw.obelix.quarry.api.dto.MenhirDto;
import ch.bbw.obelix.quarry.entity.MenhirEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * @author Tim Leo Laurin Leuenberger
 * @version 15.12.2025
 */

@Repository
public interface MenhirRepository extends JpaRepository<MenhirEntity, UUID> {

	List<MenhirEntity> findByStoneTypeContainingIgnoreCase(String stoneType);

	List<MenhirEntity> findMenhirByDecorativeness(MenhirEntity.Decorativeness decorativeness);

    void updateMenhir(MenhirDto menhirDto, UUID menhirId);
}
