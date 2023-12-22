package com.maruhxn.boardserver.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.maruhxn.boardserver.auth.common.AjaxAuthenticationToken;
import com.maruhxn.boardserver.auth.common.AjaxAuthenticationTokenMixin;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.security.jackson2.SecurityJackson2Modules;

@Configuration
@Profile("prod")
public class SessionConfig implements BeanClassLoaderAware {
    private ClassLoader loader;

    @Bean
    @Profile("prod")
    public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
        // GenericJackson2JsonRedisSerializer: 객체의 클래스 지정 없이 모든 Class Type을 JSON 형태로 저장할 수 있는 Serializer이다.
        // Object의 class 및 package까지 전부 함께 저장하게 되어 다른 프로젝트에서 redis에 저장되어 있는 값을 사용하려면 package까지 일치시켜줘야한다
        return new GenericJackson2JsonRedisSerializer(objectMapper());
    }

    /**
     * Customized {@link ObjectMapper} to add mix-in for class that doesn't have default
     * constructors
     *
     * @return the {@link ObjectMapper} to use
     */
    private ObjectMapper objectMapper() {
        // 다양한 유형의 객체에 대한 유효성을 검사하는 타입 검사기를 생성
        PolymorphicTypeValidator typeValidator = BasicPolymorphicTypeValidator
                .builder()
                .allowIfBaseType(Object.class)
                .build();

        ObjectMapper mapper = new ObjectMapper();

        // Java 8의 날짜 및 시간 API를 지원하기 위한 모듈 등록
        mapper.registerModules(new JavaTimeModule());
        // Spring Security의 클래스를 활용할 수 있도록 모듈 등록
        mapper.registerModules(SecurityJackson2Modules.getModules(this.loader));
        // objectmapper가 직렬화 시 type 정보를 저장할 scope를 지정한다.
        // 여기서는 non-final 클래스들에 대해 타입 정보를 저장할 수 있도록 했다.(GenericJackson2JsonRedisSerializer의 기본 동작 방식)
        // 이를 추가해주지 않으면 class type 정보를 포함하지 않기 때문에, 직렬화된 데이터에 어떠한 type 정보도 없으며 역직렬화시에는 기본 타입인 LinkedHashMap으로 역직렬화 되어 에러가 발생한다.
        mapper.activateDefaultTyping(typeValidator, ObjectMapper.DefaultTyping.NON_FINAL);
        // 특정 클래스에 대한 MixIn을 추가하여 직렬화/역직렬화 시 특정 클래스에 대한 정보를 제어
        // 이를 추가해주지 않으면 SecurityJackson2Modules에 등록되어 있지 않고 우리가 사용자 정의한 필드에 대한 정보는 누락된다.
        mapper.addMixIn(AjaxAuthenticationToken.class,
                AjaxAuthenticationTokenMixin.class);
        return mapper;
    }

    /*
     * @see
     * org.springframework.beans.factory.BeanClassLoaderAware#setBeanClassLoader(java.lang.ClassLoader)
     */
    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.loader = classLoader;
    }

}
