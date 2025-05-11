package org.test.sotfgen.api.weather.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Daily(
        List<DailyData> data
) {
}
