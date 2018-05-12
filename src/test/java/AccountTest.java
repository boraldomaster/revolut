import com.revolut.test.model.account.Account;
import com.revolut.test.model.account.AccountStore;
import com.revolut.test.model.currency.Currency;
import com.revolut.test.model.customer.Customer;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

public class AccountTest {

    @Test
    public void testPutGet() {
        AccountStore store = new AccountStore();
        Customer customer = new Customer();
        customer.setId(1);
        Currency currency = new Currency();
        currency.setCode("USD");
        Account account = store.open(customer, currency, BigDecimal.TEN);
        Assert.assertEquals(account, store.getById(account.getId()));
        Assert.assertEquals(1, store.listByCustomer(customer).size());
    }
}
