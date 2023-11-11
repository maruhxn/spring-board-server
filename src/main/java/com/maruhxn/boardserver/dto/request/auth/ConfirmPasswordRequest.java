package com.maruhxn.boardserver.dto.request.auth;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
public class ConfirmPasswordRequest {
    @NotEmpty
    @Size(min = 2, max = 20)
    private String currPassword;
}
