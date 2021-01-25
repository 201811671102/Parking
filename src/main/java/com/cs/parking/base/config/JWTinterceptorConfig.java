package com.cs.parking.base.config;

import com.cs.parking.base.interceptor.JWTAuthInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @ClassName JWTinterceptorConfig
 * @Description TODO
 * @Author QQ163
 * @Date 2021/1/17 0:57
 **/
@Configuration
public class JWTinterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtAuthInterceptor())
                .addPathPatterns("/**");
    }
    @Bean
    public JWTAuthInterceptor jwtAuthInterceptor(){
        return new JWTAuthInterceptor();
    }
}
