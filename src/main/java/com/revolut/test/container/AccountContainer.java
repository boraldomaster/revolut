package com.revolut.test.container;

import com.revolut.test.model.account.Account;
import com.revolut.test.model.account.AccountStore;
import com.revolut.test.model.currency.Currency;
import com.revolut.test.model.currency.CurrencyStore;
import com.revolut.test.model.customer.Customer;
import com.revolut.test.model.customer.CustomerStore;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Singleton
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/accounts")
public class AccountContainer {

    private AccountStore accountStore = StoreFactory.getFactory().getAccountStore();
    private CustomerStore customerStore = StoreFactory.getFactory().getCustomerStore();
    private CurrencyStore currencyStore = StoreFactory.getFactory().getCurrencyStore();

    @POST
    public Account open(AccountForm form) {
        Customer customer = customerStore.getById(form.getCustomer());
        if (customer == null)
            throw new IllegalStateException("Customer is not found");
        Currency currency = currencyStore.get(form.getCurrency());
        if (currency == null)
            throw new IllegalStateException("Currency is not found");
        return accountStore.open(customer, currency, form.getAmount());
    }

    @GET
    public Object get(@QueryParam("customer") Integer customer_) {
        Customer customer = customerStore.getById(customer_);
        if (customer == null)
            throw new IllegalStateException("Customer is not found");
        return accountStore.listByCustomer(customer);
    }
}