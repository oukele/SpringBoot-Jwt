package com.example.oukele.annotation;

import java.lang.annotation.*;

/***
 *  免 JWT token 认证 注解
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JwtIgnore {
}
