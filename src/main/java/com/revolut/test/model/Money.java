package com.revolut.test.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.math.BigDecimal;

public class Money {

    public static final Money ZERO = new Money(BigDecimal.ZERO);

    private BigDecimal value;

    public Money(BigDecimal value) {
        this.value = value.setScale(2, BigDecimal.ROUND_DOWN);
    }

    public BigDecimal get() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Money money = (Money) o;

        return new EqualsBuilder()
                .append(value, money.value)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(value)
                .toHashCode();
    }
}
