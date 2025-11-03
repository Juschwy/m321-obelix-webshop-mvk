package ch.bbw.obelix.quarry.api.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.With;

/**
 * A basket is an offer of various items in exchange for a standing stone.
 */
@With
public record BasketDto(ArrayList<BasketItem> items) {

	public static BasketDto empty() {
		return new BasketDto(new ArrayList<>());
	}

	public record BasketItem(String name, int count) {

		public BasketItem {
			if (name == null || name.isEmpty()) {
				throw new IllegalArgumentException("name is empty");
			}
			if (count < 1) {
				throw new IllegalArgumentException("count must be greater than zero");
			}
		}
	}
}
