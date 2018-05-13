package com.revolut.test.model.transaction;

import com.revolut.test.model.account.Account;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class TransactionStore {

    private AtomicInteger id = new AtomicInteger();
    private TransactionIndex indexByDate = new TransactionIndex();
    private ConcurrentMap<Integer, TransactionIndex> indexByParty = new ConcurrentHashMap<>();

    // amount in currency of source
    public Transaction create(Account source, Account target, BigDecimal amount) {
        if (source == null)
            throw new IllegalStateException("Source account is not found");
        if (target == null)
            throw new IllegalStateException("Target account is not found");
        if (source == target)
            throw new IllegalStateException("Source and target account are the same");
        if (amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalStateException("Amount is not positive");
        Stream.of(source, target).sorted(Comparator.comparingInt(Account::getId)).forEach(Account::lock);
        try {
            BigDecimal subtracted = source.getAmount().subtract(amount);
            if (subtracted.compareTo(BigDecimal.ZERO) < 0)
                throw new IllegalStateException("Insufficient funds for source account");
            source.setAmount(subtracted);
            BigDecimal add = source.getCurrency().equals(target.getCurrency()) ? amount : amount
                    .multiply(source.getCurrency().getRate())
                    .divide(target.getCurrency().getRate(), BigDecimal.ROUND_DOWN);
            target.setAmount(target.getAmount().add(add));
            Transaction transaction = new Transaction();
            transaction.setSource(source);
            transaction.setTarget(target);
            transaction.setAmount(amount);
            transaction.setDate(new Date());
            transaction.setId(id.incrementAndGet());
            indexByDate.put(transaction);
            for (Integer id : new int[]{source.getId(), target.getId()}) {
                indexByParty.putIfAbsent(id, new TransactionIndex());
                indexByParty.get(id).put(transaction);
            }
            return transaction;
        } finally {
            Stream.of(source, target).sorted(Comparator.comparingInt(value -> -value.getId())).forEach(Account::unlock);

        }
    }

    public Collection<Transaction> listByAccount(Account party) {
        if (party == null)
            throw new IllegalStateException("Party account is not found");
        TransactionIndex index = indexByParty.get(party.getId());
        return index == null ? Collections.emptyList() : index.all();
    }

    public Collection<Transaction> listByDate(Date start, Date end) {
        return indexByDate.range(new TransactionKey(start), new TransactionKey(end));
    }

}
