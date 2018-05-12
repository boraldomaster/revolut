package com.revolut.test.container;

import com.revolut.test.model.customer.Customer;
import com.revolut.test.model.customer.CustomerForm;
import com.revolut.test.model.customer.CustomerStore;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Collection;
import java.util.Collections;

@Singleton
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/customers")
public class CustomerContainer {

    private CustomerStore customerStore = StoreFactory.getFactory().getCustomerStore();

    @POST
    public Object create(CustomerForm form)  {
        Integer id = customerStore.create(form).getId();
        return Collections.singletonMap("id", id);
    }

    @GET
    @Path("/size")
    public int size() {
        return customerStore.size();
    }

    private Customer check(Customer customer) {
        if (customer == null)
            throw new IllegalStateException("Customer is not found");
        return customer;
    }

    @GET
    @Path("/get")
    public Customer get(
            @QueryParam("id") Integer id,
            @QueryParam("email") String email,
            @QueryParam("name") String name
    ) {
        if (id != null)
            return check(customerStore.getById(id));
        if (email != null)
            return check(customerStore.getByEmail(email));
        if (name != null)
            return check(customerStore.getByName(name));
        throw new IllegalStateException("You must specify parameter: id, name or email");
    }

    @GET
    @Path("/search")
    public Collection<Customer> search(
            @QueryParam("email") String email,
            @QueryParam("name") String name
    ) {
        if (email != null)
            return customerStore.searchByEmail(email);
        if (name != null)
            return customerStore.searchByName(name);
        throw new IllegalStateException("Customer is not found");
    }

}