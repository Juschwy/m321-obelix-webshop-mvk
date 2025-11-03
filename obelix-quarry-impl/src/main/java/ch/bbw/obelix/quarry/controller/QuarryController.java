package ch.bbw.obelix.quarry.controller;

import ch.bbw.obelix.quarry.api.QuarryApi;
import ch.bbw.obelix.quarry.api.dto.MenhirDto;
import ch.bbw.obelix.quarry.entity.MenhirEntity;
import ch.bbw.obelix.quarry.repository.MenhirRepository;
import ch.bbw.obelix.quarry.service.QuarryService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.StandardException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * @author schules
 * @version 27.10.2025
 */

@RestController
@RequiredArgsConstructor
public class QuarryController implements QuarryApi {
    private final QuarryService quarryService;

    /**
     * Note that this should only be called by Asterix himself.
     * Hopefully, no customer will ever find this endpoint...
     */
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

    @StandardException
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static class UnknownMenhirException extends RuntimeException {}
}
