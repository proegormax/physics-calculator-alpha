package com.example.physicscalculator.rtd.service;

import com.example.physicscalculator.rtd.RTDType;
import com.example.physicscalculator.rtd.dto.CalculateResistanceRequest;
import com.example.physicscalculator.rtd.dto.CalculateTemperatureRequest;
import com.example.physicscalculator.rtd.dto.ResistanceResponse;
import com.example.physicscalculator.rtd.dto.TemperatureResponse;
import org.springframework.stereotype.Service;

@Service
public class RTDServiceImpl implements RTDService {

    @Override
    public ResistanceResponse calculateResistance(CalculateResistanceRequest request) {
        double t = request.temperatureCelsius();
        double r0 = request.r0();
        RTDType type = request.type();

        double resistance;
        switch (type) {
            case CU -> resistance = calcCuResistance(t, r0);
            case NI -> resistance = calcNiResistance(t, r0);
            case PT385 -> resistance = calcPtResistance(t, r0, 3.9083e-3, -5.775e-7, -4.183e-12);
            case PT391 -> resistance = calcPtResistance(t, r0, 3.969e-3, -5.841e-7, -4.33e-12);
            default -> throw new IllegalArgumentException("Unsupported type");
        }
        // Round to 0.1 like JS
        double rounded = Math.round(resistance * 10.0) / 10.0;
        return new ResistanceResponse(rounded);
    }

    @Override
    public TemperatureResponse calculateTemperature(CalculateTemperatureRequest request) {
        double r = request.resistance();
        double r0 = request.r0();
        RTDType type = request.type();

        double tc;
        switch (type) {
            case CU -> tc = tempFromCu(r, r0);
            case NI -> tc = tempFromNi(r, r0);
            case PT385 -> tc = tempFromPt(r, r0, 3.9083e-3, -5.775e-7);
            case PT391 -> tc = tempFromPt(r, r0, 3.969e-3, -5.841e-7);
            default -> throw new IllegalArgumentException("Unsupported type");
        }
        double k = Math.round((tc + 273.15) * 10.0) / 10.0;
        double f = Math.round((tc * 1.8 + 32) * 10.0) / 10.0;
        double c = Math.round(tc * 10.0) / 10.0;
        return new TemperatureResponse(c, k, f);
    }

    private double calcCuResistance(double t, double r0) {
        double A = 4.28e-3;
        double B = -6.2032e-7;
        double C = 8.5154e-12;
        if (t >= -180 && t < 0) {
            return r0 * (1 + A * t + B * t * (t + 6.7) + C * Math.pow(t, 3));
        } else if (t >= 0 && t <= 200) {
            return r0 * (1 + A * t);
        }
        throw new IllegalArgumentException("Неверное значение температуры");
    }

    private double calcNiResistance(double t, double r0) {
        double A = 5.4963e-3;
        double B = 6.7556e-7;
        double C = -9.2004e-10;
        if (t >= -60 && t < 100) {
            return r0 * (1 + A * t + B * Math.pow(t, 2));
        } else if (t >= 100 && t <= 180) {
            return r0 * (1 + A * t + B * Math.pow(t, 2) + C * (t - 100) * Math.pow(t, 2));
        }
        throw new IllegalArgumentException("Неверное значение температуры");
    }

    private double calcPtResistance(double t, double r0, double A, double B, double C) {
        if (t >= -200 && t < 0) {
            return r0 * (1 + A * t + B * t * t + C * (t - 100) * t * t * t);
        } else if (t >= 0 && t <= 850) {
            return r0 * (1 + A * t + B * t * t);
        }
        throw new IllegalArgumentException("Неверное значение температуры");
    }

    private double tempFromCu(double r, double r0) {
        double A = 4.28e-3;
        double ratio = r / r0;
        double t;
        if (ratio >= 1) {
            t = (ratio - 1) / A;
        } else if (ratio < 1) {
            double D1 = 233.87;
            double D2 = 7.937;
            double D3 = -2.0062;
            double D4 = -0.3953;
            double x = ratio - 1;
            t = D1 * Math.pow(x, 1) + D2 * Math.pow(x, 2) + D3 * Math.pow(x, 3) + D4 * Math.pow(x, 4);
        } else {
            throw new IllegalArgumentException("Вне диапазона");
        }
        if (t <= -180 || t >= 200) throw new IllegalArgumentException("Вне диапазона");
        return t;
    }

    private double tempFromNi(double r, double r0) {
        double A = 5.4963e-3;
        double B = 6.7556e-6; // note: different from resistance formula (intentional per JS)
        double t;
        if (r <= r0) {
            double discriminant = A * A - 4 * B * (1 - r / r0);
            t = (-A + Math.sqrt(discriminant)) / (2 * B);
        } else if (r > r0) {
            double D1 = 144.096;
            double D2 = -25.502;
            double D3 = 4.4876;
            double arg = r / r0 - 1.6172;
            t = 100 + (D1 * Math.pow(arg, 1) + D2 * Math.pow(arg, 2) + D3 * Math.pow(arg, 3));
        } else {
            throw new IllegalArgumentException("Вне диапазона");
        }
        if (t <= -60 || t >= 180) throw new IllegalArgumentException("Вне диапазона");
        return t;
    }

    private double tempFromPt(double r, double r0, double A, double B) {
        double numerator = (-r0 * A + Math.sqrt(r0 * r0 * A * A - 4 * r0 * B * (r0 - r)));
        double t = numerator / (2 * r0 * B);
        if (t <= -200 || t >= 850) throw new IllegalArgumentException("Вне диапазона");
        return t;
    }
}


