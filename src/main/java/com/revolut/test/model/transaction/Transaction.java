package com.revolut.test.model.transaction;

import com.revolut.test.model.Money;
import com.revolut.test.model.account.Account;

import java.math.BigDecimal;
import java.util.Date;

public class Transaction {
    private Integer id;
    private Account source;
    private Account target;
    private Money amount;
    private Date date;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Account getSource() {
        return source;
    }

    public void setSource(Account source) {
        this.source = source;
    }

    public Account getTarget() {
        return target;
    }

    public void setTarget(Account target) {
        this.target = target;
    }

    public BigDecimal getAmount() {
        return amount.get();
    }

    public void setAmount(BigDecimal amount) {
        this.amount = new Money(amount);
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
