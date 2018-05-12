package com.revolut.test.model.currency;

import com.revolut.test.model.Money;
import com.revolut.test.model.index.UniqueIndex;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class CurrencyStore {

    private ConcurrentMap<String, Currency> index = new ConcurrentHashMap<>();

    public Currency create(String code, BigDecimal rate) {
        if (StringUtils.isEmpty(code))
            throw new IllegalStateException("Empty code");
        if (rate == null)
            throw new IllegalStateException("Empty rate");
        if (rate.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalStateException("Rate is not positive");
        Currency currency = new Currency();
        currency.setCode(code);
        currency.setRate(rate);
        Currency currency_ = index.putIfAbsent(code, currency);
        if (currency_ != null)
            throw new IllegalStateException("Duplicate code");
        return currency;
    }

    public Currency get(String code) {
        return index.get(code);
    }

    public Collection<Currency> all() {
        return index.values();
    }

}
