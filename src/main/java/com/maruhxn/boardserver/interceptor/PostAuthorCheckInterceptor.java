package com.maruhxn.boardserver.interceptor;

import com.maruhxn.boardserver.common.exception.ErrorCode;
import com.maruhxn.boardserver.common.exception.GlobalException;
import com.maruhxn.boardserver.domain.Member;
import com.maruhxn.boardserver.domain.Post;
import com.maruhxn.boardserver.repository.PostRepository;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;

import static com.maruhxn.boardserver.common.SessionConst.LOGIN_MEMBER;

@RequiredArgsConstructor
public class PostAuthorCheckInterceptor implements HandlerInterceptor {

    @Resource
    private PostRepository postRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        final String method = request.getMethod();

        if (HttpMethod.GET.matches(method)) return true;

        if (HttpMethod.PATCH.matches(method) || HttpMethod.DELETE.matches(method)) {
            if (session == null || session.getAttribute(LOGIN_MEMBER) == null) {
                throw new GlobalException(ErrorCode.UNAUTHORIZED);
            }

            Member loginMember = (Member) session.getAttribute(LOGIN_MEMBER);
            Map<String, String> pathVariables = (Map<String, String>) request.getAttribute(
                    HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
            Long postId = Long.valueOf(String.valueOf(pathVariables.get("postId")));
            Post findPost = postRepository.findOneWithAuthor(postId).orElseThrow(
                    () -> new GlobalException(ErrorCode.NOT_FOUND_POST));
            if (!loginMember.getId().equals(findPost.getMember().getId())) {
                throw new GlobalException(ErrorCode.FORBIDDEN);
            }
            return true;
        }

        throw new GlobalException(ErrorCode.BAD_REQUEST);
    }
}
