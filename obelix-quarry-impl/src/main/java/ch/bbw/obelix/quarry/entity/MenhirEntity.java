package ch.bbw.obelix.quarry.entity;

import ch.bbw.obelix.quarry.api.dto.DecorativenessDto;
import ch.bbw.obelix.quarry.api.dto.MenhirDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

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
