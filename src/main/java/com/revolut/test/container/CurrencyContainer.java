package com.revolut.test.container;

import com.revolut.test.model.currency.Currency;
import com.revolut.test.model.currency.CurrencyStore;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Collection;

@Singleton
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/currencies")
public class CurrencyContainer {

    private CurrencyStore currencyStore = StoreFactory.getFactory().getCurrencyStore();

    @POST
    public Currency create(CurrencyForm form) {
        return currencyStore.create(form.getCode(), form.getRate());
    }

    @GET
    @Path("/{code}")
    public Object get(@PathParam("code") String code) {
        return currencyStore.get(code);
    }

    @GET
    public Object get() {
        return currencyStore.all();
    }



}
