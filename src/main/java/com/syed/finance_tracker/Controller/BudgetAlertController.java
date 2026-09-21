package com.syed.finance_tracker.Controller;

import com.syed.finance_tracker.Dto.BudgetAlertResponse;
import com.syed.finance_tracker.Repository.UserRepository;
import com.syed.finance_tracker.Service.BudgetAlertService;
import com.syed.finance_tracker.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BudgetAlertController {

    private final BudgetAlertService budgetAlertService;
    private final UserRepository userRepository;

    public BudgetAlertController(
            BudgetAlertService budgetAlertService,
            UserRepository userRepository) {

        this.budgetAlertService = budgetAlertService;
        this.userRepository = userRepository;
    }

    @GetMapping("/budget-alerts")
    public List<BudgetAlertResponse> getBudgetAlerts() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));

        return budgetAlertService.getBudgetAlerts(user);
    }
}