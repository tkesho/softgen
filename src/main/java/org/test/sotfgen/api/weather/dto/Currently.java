package org.test.sotfgen.api.weather.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Currently(
        String icon,
        double precipIntensity,
        int uvIndex
) {
}