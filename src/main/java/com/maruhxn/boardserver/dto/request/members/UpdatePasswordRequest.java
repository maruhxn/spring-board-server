package com.maruhxn.boardserver.dto.request.members;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePasswordRequest {
    private String currPassword;
    private String newPassword;
    private String confirmNewPassword;
}
