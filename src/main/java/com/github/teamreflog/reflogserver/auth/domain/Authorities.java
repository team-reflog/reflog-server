package com.github.teamreflog.reflogserver.auth.domain;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.core.annotation.AliasFor;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Authorities {

    @AliasFor("roles")
    MemberRole[] value() default {};

    @AliasFor("value")
    MemberRole[] roles() default {};
}
