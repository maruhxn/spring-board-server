package com.maruhxn.boardserver.interceptor;

import com.maruhxn.boardserver.common.ErrorCode;
import com.maruhxn.boardserver.common.SessionConst;
import com.maruhxn.boardserver.exception.GlobalException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class LogOutCheckInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
            return true;
        }

        throw new GlobalException(ErrorCode.BAD_REQUEST, "이미 로그인 되어있습니다.");
    }
}
