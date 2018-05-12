import com.revolut.test.model.Money;
import com.revolut.test.model.account.Account;
import com.revolut.test.model.currency.Currency;
import com.revolut.test.model.customer.Customer;
import com.revolut.test.model.transaction.TransactionStore;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TransactionTest {


    private void assertAmount(BigDecimal amount, Account account) {
        Assert.assertEquals(new Money(amount).get(), account.getAmount());
    }

    @Test
    public void testPut() {
        TransactionStore store = new TransactionStore();

        Currency currency = new Currency();
        currency.setCode("USD");
        Customer customer = new Customer();
        customer.setId(1);

        Account source = new Account();
        source.setId(1);
        source.setAmount(BigDecimal.TEN);
        source.setCurrency(currency);
        source.setCustomer(customer);

        Account target = new Account();
        target.setId(2);
        target.setAmount(BigDecimal.ZERO);
        target.setCurrency(currency);
        target.setCustomer(customer);

        store.create(source, target, BigDecimal.TEN);
        assertAmount(BigDecimal.ZERO, source);
        assertAmount(BigDecimal.TEN, target);
    }

    @Test
    public void testPutCurrencyConverter() {
        TransactionStore store = new TransactionStore();

        Customer customer = new Customer();
        customer.setId(1);

        Account source = new Account();
        source.setId(1);
        source.setAmount(BigDecimal.TEN);
        source.setCustomer(customer);
        {
            Currency currency = new Currency();
            currency.setCode("USD");
            currency.setRate(BigDecimal.valueOf(60));
            source.setCurrency(currency);
        }

        Account target = new Account();
        target.setId(2);
        target.setAmount(BigDecimal.ZERO);
        target.setCustomer(customer);
        {
            Currency currency = new Currency();
            currency.setCode("RUB");
            currency.setRate(BigDecimal.ONE);
            target.setCurrency(currency);
        }

        store.create(source, target, BigDecimal.TEN);
        assertAmount(BigDecimal.ZERO, source);
        assertAmount(BigDecimal.valueOf(600), target);
    }

    @Test(expected = IllegalStateException.class)
    public void testInsufficientFunds() {
        TransactionStore store = new TransactionStore();
        Account source = new Account();
        source.setId(1);
        source.setAmount(BigDecimal.ZERO);
        Account target = new Account();
        target.setId(2);
        target.setAmount(BigDecimal.ZERO);
        store.create(source, target, BigDecimal.TEN);
    }

    @Test
    public void testPutMultiThreaded() throws InterruptedException {
        TransactionStore store = new TransactionStore();
        int threads = 100;
        BigDecimal initial = BigDecimal.valueOf(threads);

        Currency currency = new Currency();
        currency.setCode("USD");

        Account source = new Account();
        source.setId(1);
        source.setAmount(initial);
        source.setCurrency(currency);

        Account target = new Account();
        target.setId(2);
        target.setAmount(BigDecimal.ZERO);
        target.setCurrency(currency);

        ExecutorService pool = Executors.newFixedThreadPool(threads);
        for (int i = 0; i < threads; i++) {
            pool.submit(() -> {
                store.create(source, target, BigDecimal.ONE);
            });
        }

        pool.shutdown();
        pool.awaitTermination(1, TimeUnit.DAYS);

        assertAmount(BigDecimal.ZERO, source);
        assertAmount(initial, target);
        Assert.assertEquals(100, store.listByParty(source).size());
        Assert.assertEquals(100, store.listByParty(target).size());

    }


}
