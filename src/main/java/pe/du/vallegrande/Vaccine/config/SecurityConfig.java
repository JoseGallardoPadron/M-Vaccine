package pe.du.vallegrande.Vaccine.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationManager jwtAuthenticationManager;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(jwtAuthenticationManager);
        authenticationWebFilter.setServerAuthenticationConverter(jwtAuthenticationFilter);

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeExchange(exchanges -> exchanges
                        // Rutas públicas (login, registro, etc.) - ajusta según tu aplicación
                        .pathMatchers(HttpMethod.POST, "/auth/login", "/auth/register").permitAll()
                        .pathMatchers(HttpMethod.GET, "/health", "/actuator/**").permitAll()
                        
                        // Rutas de vacunas - Solo lectura para USER y ADMIN
                        .pathMatchers(HttpMethod.GET, "/vaccines", "/vaccines/**").hasAnyRole("USER", "ADMIN")
                        
                        // Rutas de creación y modificación - Solo ADMIN
                        .pathMatchers(HttpMethod.POST, "/vaccines/create").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.PUT, "/vaccines/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.DELETE, "/vaccines/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.PATCH, "/vaccines/activate/**").hasRole("ADMIN")
                        
                        // Todos los demás endpoints requieren autenticación
                        .anyExchange().authenticated()
                )
                .addFilterAt(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}