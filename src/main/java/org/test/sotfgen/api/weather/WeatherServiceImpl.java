package org.test.sotfgen.api.weather;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.test.sotfgen.api.weather.dto.WeatherResponseDto;


@Service
@RequiredArgsConstructor
public class WeatherServiceImpl implements WeatherService {

    @Value("${weather.api.key}")
    private String weatherApiKey;
    @Value("${weather.api.token.header}")
    private String weatherTokenHeader;
    @Value("${weather.api.url.scheme}")
    private String weatherUrlScheme;
    @Value("${weather.api.url.host}")
    private String weatherUrlHost;
    @Value("${weather.api.url.path}")
    private String weatherUrlPath;


    private final WebClient webClient;

    @Override
    public WeatherResponseDto getWeather(Float longitude, Float latitude, String parts, String units) {
        try {
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .scheme(weatherUrlScheme)
                            .host(weatherUrlHost)
                            .path(weatherUrlPath)
                            .queryParam("lon", longitude)
                            .queryParam("lat", latitude)
                            .queryParam("parts", parts)
                            .queryParam("units", units)
                            .build())
                    .header(weatherTokenHeader, weatherApiKey)
                    .retrieve()
                    .bodyToMono(WeatherResponseDto.class)
                    .block();
        } catch (WebClientResponseException e) {
            throw new RuntimeException("Error from weather API: " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            throw new RuntimeException("Unknown error during weather API call", e);
        }
    }
}