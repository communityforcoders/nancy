package pl.communityforcoders.nancy.database;

import com.lambdaworks.redis.RedisFuture;
import java.util.concurrent.ExecutionException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.communityforcoders.nancy.Nancy;
import pl.communityforcoders.nancy.NancyConfiguration;
import pl.communityforcoders.nancy.database.redis.RedisDatabase;
import pl.communityforcoders.nancy.impl.database.redis.RedisDatabaseImpl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static com.jayway.awaitility.Awaitility.await;

@RunWith(MockitoJUnitRunner.class)
public class RedisTest {

  @Mock
  private Nancy nancy;

  @Mock
  private NancyConfiguration configuration;

  private RedisDatabase database;

  @Before
  public void setup() {
    NancyConfiguration.Redis redis = mock(NancyConfiguration.Redis.class);
    when(redis.getHost()).thenReturn("localhost");
    when(redis.getPort()).thenReturn(6379);

    when(configuration.getRedis()).thenReturn(redis);
    when(nancy.getConfiguration()).thenReturn(configuration);

    database = new RedisDatabaseImpl(nancy);
    database.open();
  }

  @Test
  public void getAndSetTest() throws InterruptedException, ExecutionException {
    database.set("set-test", "value");

    RedisFuture<String> future = database.get("set-test");
    await().until(future::isDone);

    String value = future.get();

    Assert.assertNotNull(value);
    Assert.assertEquals(value, "value");
  }

  @After
  public void cleanup() {
    database.close();
    database = null;
  }

}
