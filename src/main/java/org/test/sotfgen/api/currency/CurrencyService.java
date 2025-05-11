package org.test.sotfgen.api.currency;

import org.springframework.stereotype.Service;

@Service
public interface CurrencyService {
    CurrencyResponseDto getCurrency(String currency);
}
