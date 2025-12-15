package ch.bbw.obelix.quarry.service;

import ch.bbw.obelix.common.exception.BadRequestException;
import ch.bbw.obelix.quarry.api.dto.DecorativenessDto;
import ch.bbw.obelix.quarry.api.dto.MenhirDto;
import ch.bbw.obelix.quarry.entity.MenhirEntity;
import ch.bbw.obelix.quarry.repository.MenhirRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

/**
 * @author Tim Leo Laurin Leuenberger
 * @version 15.12.2025
 */

@Service
@RequiredArgsConstructor
public class QuarryService {
    private final MenhirRepository menhirRepository;

    public void updateMenhir(MenhirDto menhirDto, UUID menhirId) {
        menhirRepository.updateMenhir(menhirDto, menhirId);
    }

    public void createMenhir(MenhirDto menhirDto) {
        var menhir = new MenhirEntity();
        menhir.setWeight(menhirDto.weight());
        menhir.setStoneType(menhirDto.stoneType());
        menhir.setDecorativeness(MenhirEntity.Decorativeness.fromDto(menhirDto.decorativeness()));
        menhir.setDescription(menhirDto.description());
        menhirRepository.save(menhir);
    }

    public void deleteById(UUID menhirId) {
        menhirRepository.deleteById(menhirId);
    }

    public List<MenhirDto> getAllMenhirs() {
        return menhirRepository.findAll()
                .stream().map(MenhirEntity::toDto).toList();
    }

    public MenhirDto getMenhirById(UUID menhirId) {
        return menhirRepository.findById(menhirId)
                .map(MenhirEntity::toDto)
                .orElseThrow(() -> new BadRequestException("unknwon menhir with id " + menhirId));
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
}
