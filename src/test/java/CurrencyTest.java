import com.revolut.test.model.Money;
import com.revolut.test.model.currency.Currency;
import com.revolut.test.model.currency.CurrencyStore;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

public class CurrencyTest {

    @Test
    public void testPutGet() {
        CurrencyStore store = new CurrencyStore();
        String code = "USD";
        Currency currency = store.create(code, BigDecimal.TEN);
        Assert.assertNotNull(currency);
        currency = store.get(code);
        Assert.assertNotNull(currency);
        Assert.assertEquals(new Money(BigDecimal.TEN).get(), currency.getRate());
    }


}
