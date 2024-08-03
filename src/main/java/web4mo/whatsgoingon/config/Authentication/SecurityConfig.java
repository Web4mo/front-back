package web4mo.whatsgoingon.config.Authentication;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.webmvc.core.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import web4mo.whatsgoingon.domain.user.entity.RefreshToken;
import web4mo.whatsgoingon.domain.user.repository.RefreshTokenRepository;

import java.util.List;

import static org.thymeleaf.util.StringUtils.substring;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final RefreshTokenRepository refreshTokenRepository;

    private static final String[] LIST = {
            "/home",
            "/swagger-ui/index.html",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/api-docs/**",
            "/v3/api-docs/**",
            "/auth/signup/**",
            "/auth/login",
            "/reissue",
            "/api/profile/**"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)throws Exception {

        //cors 설정
        http.cors(c-> {
            CorsConfigurationSource source= request -> {
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowedOrigins(List.of("http://whatsgoingon-api"));
                config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
                return config;
            };
            c.configurationSource(source);

        });
        http.httpBasic(AbstractHttpConfigurer::disable);
        http.csrf(AbstractHttpConfigurer::disable);
        http.formLogin(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests((auth) ->auth.requestMatchers(LIST).permitAll()
                .anyRequest().authenticated());
        http.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.logout((logoutConfig)-> logoutConfig
            .logoutUrl("/logout")
                    .logoutSuccessHandler((request, response, authentication)
                            -> {
                                String ath = request.getHeader("Authorization").substring(7);
                                authentication=jwtTokenProvider.getAuthentication(ath);
                                RefreshToken rel=refreshTokenRepository.findByUserId(authentication.getName());
                                rel.updateRefreshToken("logout");
                                refreshTokenRepository.save(rel); // save 호출로 업데이트 보장
                                log.info("Refresh token after update: " + rel.getRefreshToken());

                                SecurityContextHolder.clearContext();
                                response.sendRedirect("/home");
                    })
                    .deleteCookies("remember-me"));

        http.exceptionHandling((exception)-> exception
                .accessDeniedHandler(jwtAccessDeniedHandler)
                .authenticationEntryPoint(jwtAuthenticationEntryPoint));

        return http.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),UsernamePasswordAuthenticationFilter.class).build();
    }
}
