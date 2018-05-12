package com.revolut.test.model.customer;

import com.revolut.test.model.index.StringUniqueIndex;
import com.revolut.test.model.index.UniqueIndex;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

public class CustomerStore {
    private AtomicInteger id = new AtomicInteger();
    private UniqueIndex<Integer, Customer> indexById = new UniqueIndex<>(Customer::getId);
    private StringUniqueIndex<Customer> indexByEmail = new StringUniqueIndex<>(Customer::getEmail);
    private StringUniqueIndex<Customer> indexByName = new StringUniqueIndex<>(Customer::getName);

    public Customer create(CustomerForm customerForm)  {
        if (StringUtils.isEmpty(customerForm.getName()))
            throw new IllegalStateException("Empty name");
        if (StringUtils.isEmpty(customerForm.getEmail()))
            throw new IllegalStateException("Empty email");

        Customer account = new Customer();
        account.setName(customerForm.getName());
        account.setEmail(customerForm.getEmail());
        indexByName.lock(account);
        indexByEmail.lock(account);

        try {
            if (indexByName.exists(account))
                throw new IllegalStateException("Duplicate name");
            if (indexByEmail.exists(account))
                throw new IllegalStateException("Duplicate email");
            account.setId(id.incrementAndGet());
            indexByEmail.put(account);
            indexByName.put(account);
            indexById.put(account);
            return account;
        } finally {
            indexByEmail.unlock(account);
            indexByName.unlock(account);
        }
    }

    public int size() {
        return id.get();
    }

    public Customer getById(int id) {
        return indexById.get(id);
    }

    public Customer getByEmail(String email) {
        return indexByEmail.get(email);
    }

    public Customer getByName(String name) {
        return indexByName.get(name);
    }

    public Collection<Customer> searchByEmail(String email) {
        return indexByEmail.search(email);
    }

    public Collection<Customer> searchByName(String name) {
        return indexByName.search(name);
    }

}
