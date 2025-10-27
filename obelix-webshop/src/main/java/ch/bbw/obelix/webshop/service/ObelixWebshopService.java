package ch.bbw.obelix.webshop.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

import ch.bbw.obelix.webshop.controller.ObelixWebshopController;
import ch.bbw.obelix.webshop.dto.BasketDto;
import ch.bbw.obelix.webshop.dto.DecorativenessDto;
import ch.bbw.obelix.webshop.entity.MenhirEntity;
import ch.bbw.obelix.webshop.repository.MenhirRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.StandardException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Note that Obelix is definitely not multitasking-capable.
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ObelixWebshopService {

	@Lazy
	private final ObelixWebshopController quarryWebclient;

	private final MenhirRepository menhirRepository;

	private BasketDto basket;

	static <T> List<T> append(List<T> immutableList, T element) {
		var tmpList = new ArrayList<>(immutableList);
		tmpList.add(element);
		return Collections.unmodifiableList(tmpList);
	}

	public BasketDto offer(@NonNull BasketDto.BasketItem basketItem) {
		basket = basket.withItems(append(basket.items(), basketItem));
		return basket;
	}

	@PostConstruct
	public void leave() {
		basket = BasketDto.empty();
	}

	public boolean isGoodOffer(DecorativenessDto decorativeness) {
		var stoneWorth = decorativeness.ordinal();
		var basketWorth = basket.items()
				.stream().map(x -> switch (x.name().toLowerCase(Locale.ROOT)) {
					case "boar" -> 5; // oh boy, oh boy!
					case "honey" -> 2;
					case "magic potion" -> 0; // not allowed to drink this!
					default -> 1; // everything is worth something
				} * x.count()).reduce(0, Integer::sum);
		log.info("basket worth {} vs menhir worth {} ({})", basketWorth, decorativeness, stoneWorth);
		return basketWorth >= stoneWorth;
	}

	public void exchange(UUID menhirId) {
		var menhir = quarryWebclient.getMenhirById(menhirId);
		var decorativeness = menhir.decorativeness();
		if (!isGoodOffer(decorativeness)) {
			throw new BadOfferException("Bad Offer: That won't even feed Idefix!");
		}
		quarryWebclient.deleteById(menhirId);
		leave();
	}

	@PostConstruct
	public void initializeMenhirs() {
		// Only initialize if the database is empty
		if (menhirRepository.count() == 0) {
			createDefaultMenhirs();
		}
	}

	public void createDefaultMenhirs() {
		menhirRepository.deleteAll();

		var obelixSpecial = new MenhirEntity();
		obelixSpecial.setWeight(2.5);
		obelixSpecial.setStoneType("Granite Gaulois");
		obelixSpecial.setDecorativeness(MenhirEntity.Decorativeness.DECORATED);
		obelixSpecial.setDescription("Obelix's personal favorite! Perfect for throwing at Romans. ");
		menhirRepository.save(obelixSpecial);

		var getafixMasterpiece = new MenhirEntity();
		getafixMasterpiece.setWeight(4.2);
		getafixMasterpiece.setStoneType("Mystical Dolmen Stone");
		getafixMasterpiece.setDecorativeness(MenhirEntity.Decorativeness.MASTERWORK);
		getafixMasterpiece.setDescription("Blessed by Getafix himself! This menhir is rumored to " +
				"enhance magic potion brewing. Side effects may include: sudden urge to fight Romans.");
		menhirRepository.save(getafixMasterpiece);

		var touristTrap = new MenhirEntity();
		touristTrap.setWeight(1.0);
		touristTrap.setStoneType("Imported Roman Marble");
		touristTrap.setDecorativeness(MenhirEntity.Decorativeness.PLAIN);
		touristTrap.setDescription("Budget-friendly option! Made from 'liberated' Roman materials. " +
				"Perfect for beginners or those who just want to annoy Caesar. Asterix approved!");
		menhirRepository.save(touristTrap);
	}

	@StandardException
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public static class BadOfferException extends RuntimeException {}
}
