package com.example.physicscalculator.rtd.dto;

import com.example.physicscalculator.rtd.RTDType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CalculateResistanceRequest(
        @NotNull RTDType type,
        @NotNull @Min(-273) @Max(1000) Double temperatureCelsius,
        @NotNull @Min(1) Double r0
) {
    public CalculateResistanceRequest {
        if (r0 <= 0) throw new IllegalArgumentException("R0 must be positive");
    }
}


