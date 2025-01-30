package org.manmet.crmapplication.diagnostic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiAuthorizationInfo {
    private String classEntity;
    private String method;
    private String apiRoute;
    private String requiredAuthorization;

    // Constructor, Getters, Setters
}
