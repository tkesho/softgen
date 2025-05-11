package org.test.sotfgen.api.currency;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {

    private final WebClient webClient;

    @Value("${currency.api.key}")
    private String currencyApiKey;
    @Value("${currency.api.url.scheme}")
    private String currencyUrlScheme;
    @Value("${currency.api.url.host}")
    private String currencyUrlHost;
    @Value("${currency.api.url.path}")
    private String currencyUrlPath;
    @Value("${currency.api.token.header}")
    private String currencyTokenHeader;

    @Override
    public CurrencyResponseDto getCurrency(String currency) {
        try {
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .scheme(currencyUrlScheme)
                            .host(currencyUrlHost)
                            .path(currencyUrlPath + currency)
                            .build())
                    .header(currencyTokenHeader, "Bearer " + currencyApiKey)
                    .retrieve()
                    .bodyToMono(CurrencyResponseDto.class)
                    .block();
        } catch (Exception e) {
            throw new RuntimeException("Unknown error during currency API call", e);
        }
    }
}