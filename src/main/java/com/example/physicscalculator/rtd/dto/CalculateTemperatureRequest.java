package com.example.physicscalculator.rtd.dto;

import com.example.physicscalculator.rtd.RTDType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CalculateTemperatureRequest(
        @NotNull RTDType type,
        @NotNull @Min(1) Double resistance,
        @NotNull @Min(1) Double r0
) {
    public CalculateTemperatureRequest {
        if (r0 <= 0) throw new IllegalArgumentException("R0 must be positive");
        if (resistance <= 0) throw new IllegalArgumentException("Resistance must be positive");
    }
}


