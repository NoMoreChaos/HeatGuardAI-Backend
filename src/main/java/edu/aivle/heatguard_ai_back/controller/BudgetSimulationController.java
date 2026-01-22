package edu.aivle.heatguard_ai_back.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/budget")
public class BudgetSimulationController {

    @PostMapping("/simul")
    public String postBudgetSimulation() {
        return "budgetSimulation";
    }
}
