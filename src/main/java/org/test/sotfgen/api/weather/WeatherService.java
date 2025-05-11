package org.test.sotfgen.api.weather;

import org.springframework.stereotype.Service;
import org.test.sotfgen.api.weather.dto.WeatherResponseDto;

@Service
public interface WeatherService {
    WeatherResponseDto getWeather(Float longitude, Float latitude, String parts, String units);
}
