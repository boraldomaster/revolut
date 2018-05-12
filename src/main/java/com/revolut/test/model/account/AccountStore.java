package com.revolut.test.model.account;

import com.revolut.test.model.currency.Currency;
import com.revolut.test.model.customer.Customer;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

public class AccountStore {
    private AtomicInteger id = new AtomicInteger();
    private ConcurrentMap<Integer, Account> indexById = new ConcurrentHashMap<>();
    private ConcurrentMap<Integer, ConcurrentMap<String, Account>> indexByCustomer = new ConcurrentHashMap<>();

    public Account open(Customer customer, Currency currency, BigDecimal amount) {
        ConcurrentMap<String, Account> map = new ConcurrentHashMap<>();
        ConcurrentMap<String, Account> map_ = indexByCustomer.putIfAbsent(customer.getId(), map);
        map = map_ == null ? map : map_;
        customer.lock();
        try {
            String code = currency.getCode();
            if (map.containsKey(code))
                throw new IllegalStateException("Account already exists");
            Account account = new Account();
            account.setCustomer(customer);
            account.setCurrency(currency);
            account.setAmount(amount);
            int id = this.id.incrementAndGet();
            account.setId(id);
            String iban = RandomStringUtils.randomNumeric(22) + StringUtils.leftPad(String.valueOf(id), 10, '0');
            account.setIban(iban);
            map.put(code, account);
            indexById.put(id, account);
            return account;
        } finally {
            customer.unlock();
        }
    }

    public Collection<Account> listByCustomer(Customer customer) {
        return indexByCustomer.get(customer.getId()).values();
    }

    public Account getById(Integer id) {
        return indexById.get(id);
    }
}
