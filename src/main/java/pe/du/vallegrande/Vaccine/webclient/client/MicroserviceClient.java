package pe.edu.vallegrande.webclient.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class MicroserviceClient {

    private final WebClient.Builder webClientBuilder;

    public Mono<String> callProtectedEndpoint(String baseUrl, String endpoint, String token) {
        return webClientBuilder.build()
                .get()
                .uri(baseUrl + endpoint)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToMono(String.class)
                .doOnError(error -> log.error("Error calling microservice: {}", error.getMessage()));
    }

    public <T> Mono<T> callProtectedEndpoint(String baseUrl, String endpoint, String token, Class<T> responseType) {
        return webClientBuilder.build()
                .get()
                .uri(baseUrl + endpoint)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToMono(responseType)
                .doOnError(error -> log.error("Error calling microservice: {}", error.getMessage()));
    }
}