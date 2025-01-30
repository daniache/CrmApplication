package org.manmet.crmapplication.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestdto {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    // Getters and Setters
}
