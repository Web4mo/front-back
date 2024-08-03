package web4mo.whatsgoingon.config.Authentication;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import web4mo.whatsgoingon.domain.user.dto.TokenDto;
import web4mo.whatsgoingon.domain.user.service.UserService;


import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;


@Slf4j
@Component
public class JwtTokenProvider {
    private final Key key;
    private final CustomerUserDetailsService customerUserDetailsService;
    private static final long EXPIRE_TIME=1000*60*60; //1시간
    private static final long REFRESH_EXPIRE_TIME=1000*60*60*24; //1일

    public JwtTokenProvider(@Value("${jwt.secrete}") String secretKey, CustomerUserDetailsService customUserDetailsService, CustomerUserDetailsService customerUserDetailsService) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.customerUserDetailsService = customerUserDetailsService;
    }

    //유저 정보로 accessToken, refreshtoken 생성
    public TokenDto generateTokenDto(Authentication authentication) {

        String accessToken = createAccessToken(authentication);
        String refreshToken = createRefreshToken(authentication);
        log.info("authority: "+authentication.getAuthorities().toString());

        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(authentication.getName())
                .build();
    }

    public String createAccessToken(Authentication authentication){
        return generateToken(authentication, EXPIRE_TIME);
    }

    public String createRefreshToken(Authentication authentication){
        return generateToken(authentication, REFRESH_EXPIRE_TIME);
    }

    public String generateToken(Authentication authentication, long expireTime){
        //권한 가져오기
        long now = (new Date()).getTime();
        Date ExpiresIn = new Date(now + expireTime);

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        // token 생성
        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim("Auth", authorities)
                .setIssuedAt(new Date(now))
                .setExpiration(ExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }



    //jwt 토큰 복호화해서 토큰에 들어있는 정보 꺼냄
    public Authentication getAuthentication(String accessToken){
        Claims claims = parseClaims(accessToken);

        if (claims.get("Auth") == null){
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        //클레입에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("Auth").toString().split("."))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        // UserDetails 객체를 만들어서 Authentication return
        // UserDetails: interface, Member: UserDetails를 구현한 class
        UserDetails userDetails = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(userDetails , "", authorities);

    }

public Boolean validateToken(String token) {
    try {
        Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
        return true;
    } catch (SecurityException | MalformedJwtException e) {
        log.info("Invalid JWT Token", e);
    } catch (ExpiredJwtException e) {
        log.info("Expired JWT Token", e);
    } catch (UnsupportedJwtException e) {
        log.info("Unsupported JWT Token", e);
    } catch (IllegalArgumentException e) {
        log.info("JWT claims string is empty.", e);
    }
    return false;
}

    // accessToken
    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parser()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
