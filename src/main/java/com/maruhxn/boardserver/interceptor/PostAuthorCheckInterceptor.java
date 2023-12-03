package com.maruhxn.boardserver.interceptor;

import com.maruhxn.boardserver.auth.common.AccountContext;
import com.maruhxn.boardserver.common.exception.ErrorCode;
import com.maruhxn.boardserver.common.exception.GlobalException;
import com.maruhxn.boardserver.domain.Post;
import com.maruhxn.boardserver.domain.Role;
import com.maruhxn.boardserver.repository.PostRepository;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;

@RequiredArgsConstructor
public class PostAuthorCheckInterceptor implements HandlerInterceptor {

    @Resource
    private PostRepository postRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final String method = request.getMethod();

        if (HttpMethod.GET.matches(method)) return true;

        if (HttpMethod.PATCH.matches(method) || HttpMethod.DELETE.matches(method)) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            AccountContext accountContext = (AccountContext) authentication.getPrincipal();

            Map<String, String> pathVariables = (Map<String, String>) request.getAttribute(
                    HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

            Long postId = Long.valueOf(String.valueOf(pathVariables.get("postId")));
            Post findPost = postRepository.findWithMemberFirstById(postId).orElseThrow(
                    () -> new GlobalException(ErrorCode.NOT_FOUND_POST));

            if (!accountContext.getAuthorities().contains(Role.ROLE_ADMIN)
                    && !accountContext.getId().equals(findPost.getMember().getId())) {
                throw new GlobalException(ErrorCode.FORBIDDEN);
            }

            return true;
        }

        throw new GlobalException(ErrorCode.BAD_REQUEST);
    }
}
