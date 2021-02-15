package com.cs.parking.base.interceptor;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.cs.parking.base.baseinterface.ParameterVerify;
import com.cs.parking.base.baseinterface.VerifyToken;
import com.cs.parking.base.utils.JWTUtil;
import com.cs.parking.base.utils.ParameterVerifyUtil;
import com.cs.parking.code.ParameterCode;
import org.springframework.core.MethodParameter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @ClassName JWTAuthInterceptor
 * @Description TODO
 * @Author QQ163
 * @Date 2021/1/17 0:21
 **/
public class JWTAuthInterceptor implements HandlerInterceptor {
    private JWTVerifier verifier;
    private Integer uid;

    @Override
    public boolean preHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //如果表示映射到Controller方法直接反行
        if (!(handler instanceof HandlerMethod)){
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        MethodParameter[] methodParameters = handlerMethod.getMethodParameters();
        //检测指定类型的注释存在于此元素上
        if (method.isAnnotationPresent(VerifyToken.class)){
            VerifyToken verifyToken = method.getAnnotation(VerifyToken.class);
            //是否需要检验
            if (verifyToken != null && verifyToken.requited()){
                DecodedJWT decodedJWT = JWTUtil.getInstance().decodedJWT(request);
            }
        }

       /* if (method.isAnnotationPresent(ParameterVerify.class)){
            ParameterVerify parameterVerify = method.getAnnotation(ParameterVerify.class);
            if (parameterVerify != null){
                String[] parameterKeys = parameterVerify.parameterKey();
                String[] parameterNames = parameterVerify.parameterName();
                ParameterCode parameterCode = parameterVerify.parameterCode();
                Map<String, String[]> parameterMap = request.getParameterMap();
                ParameterVerifyUtil.getInstance().verifyParameterVer(parameterKeys,parameterNames,parameterCode,parameterMap);
            }
        }*/
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
