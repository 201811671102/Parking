package com.cs.parking.base.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.cs.parking.code.BaseCode;
import com.cs.parking.code.UserExceptionCode;
import com.cs.parking.base.config.WSConfig;
import com.cs.parking.exception.ErrorException;
import com.cs.parking.exception.SystemException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @ClassName JWTUtil
 * @Description TODO
 * @Author QQ163
 * @Date 2021/1/16 17:54
 **/
public class JWTUtil {
    //token有效时间  86400000 1000*60*60 1小时，单位毫秒
    final int expiresTime = 3600000;

    private static class JWTUtilHolder{
        private static final JWTUtil instance = new JWTUtil();
    }

    public JWTUtil(){}
    public static JWTUtil getInstance(){
        return JWTUtilHolder.instance;
    }

    public  String createJWT(Integer uid){
        //Signature
        Algorithm algorithm = Algorithm.HMAC256(WSConfig.secretKey);
        //Header
        Map<String, Object> headerClaims = new HashMap();
        headerClaims.put("alg", "HS256");
        headerClaims.put("typ", "JWT");
        String token = com.auth0.jwt.JWT.create()
                .withHeader(headerClaims)
                //Payload
                .withIssuer("CG")//发布者
                .withSubject("CGParkingProject")//主题
                .withAudience("user")//观众 接受者
                .withIssuedAt(new Date())//生成签名时间
                .withExpiresAt(new Date(System.currentTimeMillis()+expiresTime))//有效时间
                .withNotBefore(new Date())//生效时间
                .withJWTId(UUID.randomUUID().toString())//编号
                .withClaim("uid",uid)//自定义参数
                .sign(algorithm);
        return token;
    }
    
    public DecodedJWT decodedJWT(HttpServletRequest request){
        try {
            String jwt = request.getHeader("token");
            if (!StringUtils.isNotBlank(jwt)){
                throw new SystemException(UserExceptionCode.NoToken);
            }
            Algorithm algorithm = Algorithm.HMAC256(WSConfig.secretKey);
            JWTVerifier verifier = JWT.require(algorithm).build();
            return verifier.verify(jwt);
        }catch (TokenExpiredException e){
            throw new SystemException(UserExceptionCode.NoToken);
        }catch (Exception e){
            throw new ErrorException(BaseCode.System_Error,e.getMessage());
        }
    }

    public String refreshJWT(HttpServletRequest request){
        try {
            String jwt = request.getHeader("token");
            if (!StringUtils.isNotBlank(jwt)){
                throw new SystemException(UserExceptionCode.NoToken);
            }
            Algorithm algorithm = Algorithm.HMAC256(WSConfig.secretKey);
            JWTVerifier verifier = JWT.require(algorithm).build();
            Integer uid = verifier.verify(jwt).getClaim("uid").asInt();
            return createJWT(uid);
        }catch (TokenExpiredException e){
            throw new SystemException(UserExceptionCode.NoToken);
        }catch (Exception e){
            throw new ErrorException(BaseCode.System_Error,e.getMessage());
        }
    }
}
