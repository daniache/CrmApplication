package org.manmet.crmapplication.diagnostic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/authorisation")
public class DiagnosticController {

    private final WebSocketAuthorizationController webSocketAuthorizationController;
    private final DiagnosticScanner diagnosticScanner;

    @Autowired
    public DiagnosticController(WebSocketAuthorizationController webSocketAuthorizationController, DiagnosticScanner diagnosticScanner) {
        this.webSocketAuthorizationController = webSocketAuthorizationController;
        this.diagnosticScanner = diagnosticScanner;
    }



    @GetMapping("/requirements")
    public List<ApiAuthorizationInfo> getAuthorizationRequirements() {
        return diagnosticScanner.scanClasses();
    }

    // New endpoint to get specific authorisation info based on API route
    @PostMapping("/diagnose")
    public ApiAuthorizationInfo getAuthorizationForRoute(@RequestBody Map<String, String> request) {
        String apiRoute = request.get("apiRoute");  // Extract the API route from the request body

        // Scan all authorisation details and filter by the provided API route
        Optional<ApiAuthorizationInfo> result = diagnosticScanner.scanClasses().stream()
                .filter(info -> apiRoute.equalsIgnoreCase(info.getApiRoute()))
                .findFirst();

        // Return the matching authorization info or throw an exception if not found
        return result.orElseThrow(() -> new RuntimeException("No authorisation info found for API route: " + apiRoute));
    }
}
