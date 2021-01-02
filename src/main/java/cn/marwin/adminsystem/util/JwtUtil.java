package cn.marwin.adminsystem.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.commons.lang.time.DateUtils;

import java.util.Date;

public class JwtUtil {
    public static final String SECRET = "secret"; // 实际使用时替换成服务器上密钥

    public static final Algorithm ALGORITHM = Algorithm.HMAC256(SECRET);

    public static String createToken(String username) {
        Date currentTime = new Date();
        return JWT.create()
                .withIssuer("auth0") // 发布者
                .withIssuedAt(currentTime) // 生效时间
                .withExpiresAt(DateUtils.addHours(currentTime, 2)) // 过期时间，有效期为2小时
                .withClaim("username", username)
                .sign(ALGORITHM);
    }

    public static String verify(String token) {
        JWTVerifier verifier = JWT.require(ALGORITHM)
                .withIssuer("auth0")
                .build();
        try {
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getClaim("username").asString();
        } catch (InvalidClaimException e) {
            // todo:后续补充业务异常
            return "";
        } catch (TokenExpiredException e) {
            // todo:后续补充业务异常
            return "";
        }
    }
}
