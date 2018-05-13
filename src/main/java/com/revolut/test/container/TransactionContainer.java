package com.revolut.test.container;

import com.revolut.test.model.account.Account;
import com.revolut.test.model.account.AccountStore;
import com.revolut.test.model.transaction.Transaction;
import com.revolut.test.model.transaction.TransactionStore;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Collection;
import java.util.Date;

@Singleton
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/transactions")
public class TransactionContainer {

    private AccountStore accountStore = StoreFactory.getFactory().getAccountStore();
    private TransactionStore transactionStore = StoreFactory.getFactory().getTransactionStore();

    @POST
    public Object create(TransactionForm form) {
        Account source = form.getSource() == null ? null : accountStore.getById(form.getSource());
        Account target = form.getTarget() == null ? null :
                form.getSource().equals(form.getTarget()) ? source : accountStore.getById(form.getTarget());
        return transactionStore.create(source, target, form.getAmount());
    }

    @GET
    @Path("/account/{id}")
    public Collection<Transaction> listByAccount(@PathParam("id") Integer accountID)  {
        Account account = accountID == null ? null : accountStore.getById(accountID);
        return transactionStore.listByAccount(account);
    }

    @GET
    @Path("/date")
    public Collection<Transaction> listByDate(@QueryParam("start") long start, @QueryParam("end") long end) {
        return transactionStore.listByDate(new Date(start), new Date(end + 1));
    }
}
