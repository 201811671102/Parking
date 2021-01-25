package com.cs.parking.base.baseinterface;

import com.cs.parking.code.ParameterCode;
import org.apache.ibatis.jdbc.Null;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName ParameterVerify
 * @Description TODO
 * @Author QQ163
 * @Date 2021/1/19 0:10
 **/
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ParameterVerify {
    String[] parameterKey() default " ";
    String[] parameterName() default "";
    ParameterCode parameterCode();
}
