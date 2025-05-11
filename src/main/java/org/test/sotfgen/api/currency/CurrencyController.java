package org.test.sotfgen.api.currency;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CurrencyController {

    private final CurrencyService currencyService;

    @GetMapping("/currency/{currency}")
    public ResponseEntity<CurrencyResponseDto> getCurrency(@PathVariable String currency) {
        CurrencyResponseDto response = currencyService.getCurrency(currency);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
    }
}
