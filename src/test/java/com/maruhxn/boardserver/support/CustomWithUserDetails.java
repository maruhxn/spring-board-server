package com.maruhxn.boardserver.support;

import org.springframework.core.annotation.AliasFor;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

//@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
//@Inherited
//@Documented
@WithSecurityContext(factory = CustomUserDetailsSecurityContextFactory.class)
public @interface CustomWithUserDetails {
    String email() default "test@test.com"; // Customize as needed

    String password() default "test"; // Customize as needed

    @AliasFor(annotation = WithSecurityContext.class)
    TestExecutionEvent setupBefore() default TestExecutionEvent.TEST_EXECUTION;
}

