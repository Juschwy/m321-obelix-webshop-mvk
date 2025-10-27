package ch.bbw.obelix.webshop.repository;

import java.util.List;
import java.util.UUID;

import ch.bbw.obelix.webshop.entity.MenhirEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenhirRepository extends JpaRepository<MenhirEntity, UUID> {

	List<MenhirEntity> findByStoneTypeContainingIgnoreCase(String stoneType);

	List<MenhirEntity> findMenhirByDecorativeness(MenhirEntity.Decorativeness decorativeness);

}
