import com.revolut.test.model.customer.CustomerForm;
import com.revolut.test.model.customer.CustomerStore;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CustomerTest {

    @Test
    public void testPutGet()  {
        CustomerStore store = new CustomerStore();
        CustomerForm form = new CustomerForm();
        form.setEmail("john@gmail.com");
        form.setName("John");
        Integer id = store.create(form).getId();
        Assert.assertEquals(id, store.getByEmail(form.getEmail()).getId());
        Assert.assertEquals(id, store.getByName(form.getName()).getId());
        Assert.assertEquals(id, store.getById(id).getId());
    }

    @Test(expected = IllegalStateException.class)
    public void testUnique()  {
        CustomerStore store = new CustomerStore();
        CustomerForm form = new CustomerForm();
        form.setEmail("john@gmail.com");
        form.setName("John");
        store.create(form);
        store.create(form);
    }

    @Test
    public void testSearch()  {
        CustomerStore store = new CustomerStore();
        {
            CustomerForm form = new CustomerForm();
            form.setEmail("john1@gmail.com");
            form.setName("John 1 Johnson");
            store.create(form);
        }
        {
            CustomerForm form = new CustomerForm();
            form.setEmail("john2@gmail.com");
            form.setName("John 2 Johnson");
            store.create(form);
        }
        {
            CustomerForm form = new CustomerForm();
            form.setEmail("sam@gmail.com");
            form.setName("Sam Johnson");
            store.create(form);
        }
        Assert.assertEquals(2, store.searchByEmail("john").size());
        Assert.assertEquals(2, store.searchByName("John").size());
    }

    @Test
    public void testUniqueMultiThreaded() throws InterruptedException {
        CustomerStore store = new CustomerStore();
        CustomerForm form = new CustomerForm();
        form.setEmail("john@gmail.com");
        form.setName("John");
        int threads = 100;
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        for (int i = 0; i < threads; i++) {
            pool.submit(() -> {
                store.create(form);
            });
        }
        pool.shutdown();
        pool.awaitTermination(1, TimeUnit.DAYS);
        Assert.assertEquals(1, store.size());
    }

}
