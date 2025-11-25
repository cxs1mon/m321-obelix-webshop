package ch.bbw.obelix.webshop.service;

import ch.bbw.obelix.quarry.api.MenhirDto;
import ch.bbw.obelix.quarry.api.QuarryApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.List;
import java.util.UUID;


@Service
public class QuarryWebclientService implements QuarryApi {

    private final QuarryApi client;

    public QuarryWebclientService(
            WebClient.Builder webClientBuilder,
            @Value("${quarry.base-url:http://localhost:8081}") String quarryBaseUrl) {

        WebClient webClient = webClientBuilder
                .baseUrl(quarryBaseUrl)
                .defaultStatusHandler(
                        status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> response.bodyToMono(String.class)
                                .defaultIfEmpty("")
                                .map(body -> {
                                    int code = response.statusCode().value();
                                    if (code == 400) {
                                        return new MenhirNotFoundException("unknown menhir");
                                    }
                                    return new QuarryUpstreamException("Upstream error " + response.statusCode() + (body.isBlank() ? "" : ": " + body));
                                })
                )
                .build();

        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builder()
                .exchangeAdapter(WebClientAdapter.create(webClient))
                .build();

        this.client = factory.createClient(QuarryApi.class);
    }

    @Override
    public List<MenhirDto> getAllMenhirs() {
        return client.getAllMenhirs();
    }

    @Override
    public MenhirDto getMenhirById(UUID menhirId) {
        return client.getMenhirById(menhirId);
    }

    @Override
    public void deleteById(UUID menhirId) {
        client.deleteById(menhirId);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    static class MenhirNotFoundException extends RuntimeException {
        MenhirNotFoundException(String message) { super(message); }
    }

    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    static class QuarryUpstreamException extends RuntimeException {
        QuarryUpstreamException(String message) { super(message); }
    }
}