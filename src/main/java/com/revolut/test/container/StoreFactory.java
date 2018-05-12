package com.revolut.test.container;

import com.revolut.test.model.account.AccountStore;
import com.revolut.test.model.currency.CurrencyStore;
import com.revolut.test.model.customer.CustomerStore;
import com.revolut.test.model.transaction.TransactionStore;

public class StoreFactory {

    private static StoreFactory factory = new StoreFactory();

    private StoreFactory() {
    }

    public static StoreFactory getFactory() {
        return factory;
    }

    private AccountStore accountStore = new AccountStore();

    public AccountStore getAccountStore() {
        return accountStore;
    }

    private TransactionStore transactionStore = new TransactionStore();

    public TransactionStore getTransactionStore() {
        return transactionStore;
    }

    private CustomerStore customerStore = new CustomerStore();

    public CustomerStore getCustomerStore() {
        return customerStore;
    }

    private CurrencyStore currencyStore = new CurrencyStore();

    public CurrencyStore getCurrencyStore() {
        return currencyStore;
    }
}
