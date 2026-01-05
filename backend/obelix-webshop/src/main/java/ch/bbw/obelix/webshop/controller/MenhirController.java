package ch.bbw.obelix.webshop.controller;

import ch.bbw.obelix.quarry.api.dto.MenhirDto;
import ch.bbw.obelix.quarry.api.service.QuarryClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * @author Tim Leo Laurin Leuenberger
 * @version 05.01.2026
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

    @PostMapping("/api/menhir")
    public void addMenhir(@RequestBody MenhirDto menhirDto) {
        quarryClientService.createMenhir(menhirDto);
    }

    @PutMapping("/api/{menhirId}")
    public void updateMenhir(@RequestBody MenhirDto menhirDto, @PathVariable UUID menhirId) {
        quarryClientService.updateMenhir(menhirDto, menhirId);
    }
}
