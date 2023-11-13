package com.maruhxn.boardserver.config;

import com.maruhxn.boardserver.interceptor.LogInCheckInterceptor;
import com.maruhxn.boardserver.interceptor.LogOutCheckInterceptor;
import com.maruhxn.boardserver.interceptor.PostAuthorCheckInterceptor;
import com.maruhxn.boardserver.resolver.LoginMemberArgumentResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("*")
                .allowCredentials(true);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInCheckInterceptor())
                .order(1)
                .addPathPatterns("/members/**");
        registry.addInterceptor(new LogOutCheckInterceptor())
                .order(2)
                .addPathPatterns("/auth/register", "/auth/login");
        registry.addInterceptor(postAuthorCheckInterceptor())
                .order(3)
                .addPathPatterns("/posts/{postId}", "/posts/{postId}/images/{imageId}");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver());
    }

    @Bean
    public PostAuthorCheckInterceptor postAuthorCheckInterceptor() {
        return new PostAuthorCheckInterceptor();
    }
}
