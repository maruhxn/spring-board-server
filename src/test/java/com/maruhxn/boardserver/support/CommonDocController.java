package com.maruhxn.boardserver.support;

import com.maruhxn.boardserver.common.exception.ErrorCode;
import com.maruhxn.boardserver.common.exception.GlobalException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class CommonDocController {
    @PostMapping("/validation-error")
    public void errorSample(@RequestBody @Valid SampleRequest dto) {
    }

    @GetMapping("/global-exception")
    public void globalExceptionSample() {
        throw new GlobalException(ErrorCode.INTERNAL_ERROR);
    }

    @GetMapping("/error-code")
    public Map<String, ErrorCodeResponse> findEnums() {
        Map<String, ErrorCodeResponse> map = new HashMap<>();
        for (ErrorCode errorCode : ErrorCode.values()) {
            map.put(errorCode.name(), new ErrorCodeResponse(errorCode));
        }
        return map;
    }

    @Getter
    @NoArgsConstructor
    protected static class ErrorCodeResponse {
        private String code;
        private String message;
        private HttpStatus httpStatus;

        public ErrorCodeResponse(ErrorCode errorCode) {
            this.code = errorCode.name();
            this.message = errorCode.getMessage();
            this.httpStatus = errorCode.getHttpStatus();
        }
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
