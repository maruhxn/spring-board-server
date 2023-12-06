package com.maruhxn.boardserver.interceptor;

import com.maruhxn.boardserver.common.exception.ErrorCode;
import com.maruhxn.boardserver.common.exception.GlobalException;
import com.maruhxn.boardserver.domain.Member;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;
import java.util.Objects;

import static com.maruhxn.boardserver.common.SessionConst.LOGIN_MEMBER;

@RequiredArgsConstructor
public class IdentificationInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        Member loginMember = (Member) session.getAttribute(LOGIN_MEMBER);
        Map<String, String> pathVariables = (Map<String, String>) request.getAttribute(
                HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        Long memberId = Long.valueOf(String.valueOf(pathVariables.get("memberId")));
        if (!Objects.equals(loginMember.getId(), memberId))
            throw new GlobalException(ErrorCode.FORBIDDEN);
        return true;
    }
}
