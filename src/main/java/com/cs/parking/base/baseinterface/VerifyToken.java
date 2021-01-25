package com.cs.parking.base.baseinterface;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName VerifyToken
 * @Description TODO
 * @Author QQ163
 * @Date 2021/1/17 0:35
 **/
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface VerifyToken {
    boolean requited() default true;
}
