package ch.bbw.obelix.quarry.api;

import ch.bbw.obelix.quarry.api.dto.MenhirDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;
import java.util.UUID;

@HttpExchange("/api/quarry")
public interface QuarryApi {
    @GetExchange("/")
    List<MenhirDto> getAllMenhirs();

    @GetExchange("/{menhirId}")
    MenhirDto getMenhirById(@PathVariable UUID menhirId);

    @DeleteExchange("/{menhirId}")
    void deleteById(@PathVariable UUID menhirId);
}
