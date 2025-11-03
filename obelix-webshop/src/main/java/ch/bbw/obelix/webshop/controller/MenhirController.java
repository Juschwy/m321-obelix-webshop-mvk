package ch.bbw.obelix.webshop.controller;

import ch.bbw.obelix.quarry.api.dto.MenhirDto;
import ch.bbw.obelix.webshop.service.QuarryClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * @author schules
 * @version 03.11.2025
 */

@RestController
@RequiredArgsConstructor
public class MenhirController {
    private final QuarryClientService quarryClientService;

    @GetMapping("/api/menhirs")
    public List<MenhirDto> getAllMenhirs() {
        return quarryClientService.getAllMenhirs();
    }

    @GetMapping("/api/menhirs/{menhirId}")
    public MenhirDto getMenhirById(@PathVariable UUID menhirId) {
        return quarryClientService.getMenhirById(menhirId);
    }
}
