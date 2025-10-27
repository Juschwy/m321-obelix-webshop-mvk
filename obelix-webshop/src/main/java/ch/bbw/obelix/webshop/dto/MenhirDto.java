package ch.bbw.obelix.webshop.dto;

import java.util.UUID;

public record MenhirDto(UUID id, Double weight, String stoneType, DecorativenessDto decorativeness, String description) {}
