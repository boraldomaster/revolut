package com.revolut.test.model.transaction;

import org.apache.commons.lang3.builder.CompareToBuilder;

import java.util.Date;

public class TransactionKey implements Comparable<TransactionKey>{

    private Date date;
    private Integer id;

    public TransactionKey(Date date) {
        this.date = date;
    }

    public TransactionKey(Date date, Integer id) {
        this.date = date;
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public Integer getId() {
        return id;
    }

    @Override
    public int compareTo(TransactionKey key) {
        return new CompareToBuilder().append(date, key.date).append(id, key.id).build();
    }
}
