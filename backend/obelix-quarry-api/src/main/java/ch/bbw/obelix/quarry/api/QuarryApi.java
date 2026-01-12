package ch.bbw.obelix.quarry.api;

import ch.bbw.obelix.quarry.api.dto.MenhirDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * @author Tim Leo Laurin Leuenberger
 * @version 15.12.2025
 */

@HttpExchange("/api/quarry")
public interface QuarryApi {
    @GetExchange("/")
    List<MenhirDto> getAllMenhirs();

    @GetExchange("/{menhirId}")
    MenhirDto getMenhirById(@PathVariable UUID menhirId);

    @DeleteExchange("/{menhirId}")
    void deleteById(@PathVariable UUID menhirId);

    @PostExchange("/")
    void createMenhir(MenhirDto menhirDto);

    @PutExchange("/{menhirId}")
    void updateMenhir(@RequestBody MenhirDto menhirDto, @PathVariable UUID menhirId);
}
