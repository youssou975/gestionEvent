package com.example.gestion_evenement.Controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping("/dashboardadmin")
    @PreAuthorize("hasAuthority('ORGANISATEUR')")
    public String afficherDashboardAdmin() {
        return "dashboardadmin";
    }
}

