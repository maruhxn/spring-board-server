package com.maruhxn.boardserver.dto.request.members;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePasswordRequest {
    @Size(min = 2, max = 20)
    private String currPassword;
    @Size(min = 2, max = 20)
    private String newPassword;
    @Size(min = 2, max = 20)
    private String confirmNewPassword;
}
