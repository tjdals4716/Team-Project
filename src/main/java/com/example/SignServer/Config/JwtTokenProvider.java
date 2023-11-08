package com.example.SignServer.Config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {


    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);
    private final UserDetailsService userDetailsService;

    @Value("${jwt.token.key}") //properties의 secret key를 가져온다.
    private String secretkey;

    // secretkey를 Base64로 인코딩
    // @PostConstruct는 빈이 생성되면 자동으로 실행되게 하는 어노테이션, 초기화에 사용.
    @PostConstruct
    protected void init(){
        logger.info("JwtTokenProvider : 초기화를 완료했습니다.(init)");
        secretkey = Base64.getEncoder().encodeToString(secretkey.getBytes());
    }

    // Token 발급
    public String createToken(String email, List<String> roles){
        logger.info("JwtTokenProvider : 토큰을 발급하였습니다.(createToken)");
        // JwtToken 값 넣는 claims
        Claims claims = Jwts.claims().setSubject(email);
        // 유저의 권한 목록
        claims.put("roles",roles);
        Date now = new Date();

        //토큰의 유효시간을 1시간으로 설정
        long tokenValidTime = 60 * 60 * 1000L;

        return Jwts.builder()

                .setClaims(claims)
                // Token 발급 시간
                .setIssuedAt(now)
                // Token 만료 시간
                .setExpiration(new Date(now.getTime()+ tokenValidTime))
                // 사용할 암호화 알고리즘, 암호화에 사용될 키 설정
                .signWith(SignatureAlgorithm.HS256, secretkey)
                .compact();
    }

    // Token 인증 정보 조회
    @Transactional
    public Authentication getAuthentication(String token){
        logger.info("JwtTokenProvider : 토큰 인증 정보 조회를 시작합니다.(getAuthentication)");
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserUid(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "",userDetails.getAuthorities());
    }

    // Token에서 회원 정보 추출
    public String getUserUid(String token){
        logger.info("JwtTokenProvider : 토큰에서 회원 정보를 추출합니다.(getUserUid)");
        return Jwts.parser().setSigningKey(secretkey).parseClaimsJws(token).getBody().getSubject();
    }

    // Request의 Header에서 token 값 추출
    public String resolveToken(HttpServletRequest request){
        logger.info("JwtTokenProvider : 헤더에서 토큰 값을 추출합니다.(resolveToken)");
        return request.getHeader("AUTH-TOKEN");
    }

    // Token 유효성과 만료여부 체크
    public boolean validateToken(String token) {
        logger.info("JwtTokenProvider : 토큰 유효성과 만료여부를 체크합니다.(validateToken)");
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretkey).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            logger.info("JwtTokenProvider : 토큰 유효성 검사에 실패했습니다.(validateToken Fail");
            return false;
        }
    }
}
