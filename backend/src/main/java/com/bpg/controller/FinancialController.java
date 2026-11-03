package com.bpg.controller;

import com.bpg.dto.FinancialDTOs.*;
import com.bpg.financial.FinancialEngine;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/financials")
@RequiredArgsConstructor
public class FinancialController {

    private final FinancialEngine financialEngine;

    @PostMapping("/calculate")
    public ResponseEntity<FullFinancialModelDTO> calculate(@RequestBody FinancialInputsDTO inputs) {
        return ResponseEntity.ok(financialEngine.calculateFullModel(inputs));
    }
}
