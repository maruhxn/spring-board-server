package com.maruhxn.boardserver.interceptor;

import com.maruhxn.boardserver.common.SessionConst;
import com.maruhxn.boardserver.common.ErrorCode;
import com.maruhxn.boardserver.exception.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class LogInCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED);
        }

        return true;
    }
}
