package ch.bbw.obelix.quarry.controller;

import ch.bbw.obelix.quarry.api.QuarryApi;
import ch.bbw.obelix.quarry.api.dto.MenhirDto;
import ch.bbw.obelix.quarry.service.QuarryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * @author Tim Leo Laurin Leuenberger
 * @version 15.12.2025
 */

@RestController
@RequiredArgsConstructor
public class QuarryController implements QuarryApi {
    private final QuarryService quarryService;

    @Override
    public void deleteById(@PathVariable UUID menhirId) {
        quarryService.deleteById(menhirId);
    }

    @Override
    public List<MenhirDto> getAllMenhirs() {
        return quarryService.getAllMenhirs();
    }

    @Override
    public MenhirDto getMenhirById(@PathVariable UUID menhirId) {
        return quarryService.getMenhirById(menhirId);
    }

    @Override
    public void createMenhir(@RequestBody MenhirDto menhirDto) {
        quarryService.createMenhir(menhirDto);
    }
}
