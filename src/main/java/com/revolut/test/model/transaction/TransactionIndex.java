package com.revolut.test.model.transaction;

import com.revolut.test.model.index.UniqueIndex;

public class TransactionIndex extends UniqueIndex<TransactionKey, Transaction> {

    public TransactionIndex() {
        super(transaction -> new TransactionKey(transaction.getDate(), transaction.getId()));
    }
}
