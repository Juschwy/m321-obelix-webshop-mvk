package ch.bbw.obelix.quarry.api;

import ch.bbw.obelix.quarry.api.dto.MenhirDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;

import java.util.List;
import java.util.UUID;

public interface QuarryApi {
    @GetExchange("/api/quarry")
    List<MenhirDto> getAllMenhirs();

    @GetExchange("/api/quarry/{menhirId}")
    MenhirDto getMenhirById(@PathVariable UUID menhirId);

    @DeleteExchange("/api/quarry/{menhirId}")
    void deleteById(@PathVariable UUID menhirId);
}
