package org.test.sotfgen.api.weather;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.test.sotfgen.api.weather.dto.WeatherResponseDto;

@RestController
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping("/weather")
    public ResponseEntity<WeatherResponseDto> getWeather(
            @RequestParam Float lon,
            @RequestParam Float lat,
            @RequestParam(required = false) String parts,
            @RequestParam(required = false) String units
    ) {
        WeatherResponseDto response = weatherService.getWeather(lon, lat, parts, units);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
}