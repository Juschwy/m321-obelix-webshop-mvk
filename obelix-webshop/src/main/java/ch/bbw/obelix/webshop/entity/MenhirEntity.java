package ch.bbw.obelix.webshop.entity;

import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import ch.bbw.obelix.webshop.dto.DecorativenessDto;
import ch.bbw.obelix.webshop.dto.MenhirDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A standing stone proudly created in Obelix's quarry.
 */
@Entity
@Table(name = "menhirs")
@Setter
@Getter
@NoArgsConstructor
public class MenhirEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(nullable = false)
	private Double weight; // in tons

	@Column(nullable = false)
	private String stoneType;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Decorativeness decorativeness;

	@Column
	private String description;

	// should be replaced by a mapper like https://mapstruct.org/ but this is simpler for now
	public MenhirDto toDto() {
		return new MenhirDto(getId(), getWeight(), getStoneType(), getDecorativeness().toDto(), getDescription());
	}

	public enum Decorativeness {
		PLAIN, SIMPLE, DECORATED, ORNATE, MASTERWORK;

		public static Decorativeness fromDto(DecorativenessDto decorativeness) {
			return Decorativeness.valueOf(decorativeness.name());
		}

		public DecorativenessDto toDto() {
			return DecorativenessDto.valueOf(name());
		}
	}
}
