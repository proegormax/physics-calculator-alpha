package com.example.physicscalculator.rtd.service;

import com.example.physicscalculator.rtd.dto.CalculateResistanceRequest;
import com.example.physicscalculator.rtd.dto.CalculateTemperatureRequest;
import com.example.physicscalculator.rtd.dto.ResistanceResponse;
import com.example.physicscalculator.rtd.dto.TemperatureResponse;

public interface RTDService {
    ResistanceResponse calculateResistance(CalculateResistanceRequest request);
    TemperatureResponse calculateTemperature(CalculateTemperatureRequest request);
}


