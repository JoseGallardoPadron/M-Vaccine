package pe.du.vallegrande.Vaccine.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements ServerAuthenticationConverter {

    private final JwtUtil jwtUtil;

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        return Mono.fromCallable(() -> {
            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);

                if (jwtUtil.validateToken(token)) {
                    String email = jwtUtil.extractEmail(token);
                    String role = jwtUtil.extractRole(token);
                    String status = jwtUtil.extractStatus(token);

                    // Check if user is active
                    if ("I".equals(status)) {
                        log.warn("Inactive user attempted to access: {}", email);
                        return null;
                    }

                    List<SimpleGrantedAuthority> authorities = List.of(
                            new SimpleGrantedAuthority("ROLE_" + role)
                    );

                    return new UsernamePasswordAuthenticationToken(email, null, authorities);
                }
            }
            return null;
        });
    }
}