package com.example.physicscalculator.rtd.web;

import com.example.physicscalculator.common.ApiResponse;
import com.example.physicscalculator.rtd.dto.CalculateResistanceRequest;
import com.example.physicscalculator.rtd.dto.CalculateTemperatureRequest;
import com.example.physicscalculator.rtd.dto.ResistanceResponse;
import com.example.physicscalculator.rtd.dto.TemperatureResponse;
import com.example.physicscalculator.rtd.service.RTDService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rtd")
public class RTDController {

    private final RTDService rtdService;

    public RTDController(RTDService rtdService) {
        this.rtdService = rtdService;
    }

    @PostMapping("/resistance")
    public ResponseEntity<ApiResponse<ResistanceResponse>> resistance(@Valid @RequestBody CalculateResistanceRequest request) {
        try {
            ResistanceResponse result = rtdService.calculateResistance(request);
            return ResponseEntity.ok(ApiResponse.success("OK", result));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }

    @PostMapping("/temperature")
    public ResponseEntity<ApiResponse<TemperatureResponse>> temperature(@Valid @RequestBody CalculateTemperatureRequest request) {
        try {
            TemperatureResponse result = rtdService.calculateTemperature(request);
            return ResponseEntity.ok(ApiResponse.success("OK", result));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }
}


