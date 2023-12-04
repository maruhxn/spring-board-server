package com.maruhxn.boardserver.support;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class CommonDocController {
    @PostMapping("/validation-error")
    public void errorSample(@RequestBody @Valid SampleRequest dto) {
    }

    @GetMapping("/global-exception")
    public void globalExceptionSample() {
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SampleRequest {

        @NotEmpty
        private String name;

        @Email
        private String email;
    }
}
