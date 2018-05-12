package com.revolut.test.model.currency;

import com.revolut.test.model.Money;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.math.BigDecimal;

public class Currency {
    private String code;
    private Money rate;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getRate() {
        return rate.get();
    }

    public void setRate(BigDecimal rate) {
        this.rate = new Money(rate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Currency currency = (Currency) o;

        return new EqualsBuilder()
                .append(code, currency.code)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(code)
                .toHashCode();
    }
}
