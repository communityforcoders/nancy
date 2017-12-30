package pl.communityforcoders.nancy.impl.database.redis;

import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.RedisFuture;
import com.lambdaworks.redis.RedisURI;
import com.lambdaworks.redis.api.StatefulRedisConnection;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.Validate;
import pl.communityforcoders.nancy.Nancy;
import pl.communityforcoders.nancy.NancyConfiguration;
import pl.communityforcoders.nancy.NancyException;
import pl.communityforcoders.nancy.database.redis.RedisDatabase;
import pl.communityforcoders.nancy.impl.database.AbstractDatabase;

public class RedisDatabaseImpl extends AbstractDatabase implements RedisDatabase {

  private RedisClient client;
  private StatefulRedisConnection<String, String> connection;

  public RedisDatabaseImpl(Nancy nancy) {
    super(nancy);
  }

  @Override
  protected void create(NancyConfiguration configuration) {
    if (isRunning()) {
      throw new NancyException("Valid state!");
    }

    client = RedisClient.create(
        new RedisURI(configuration.getRedis().getHost(), configuration.getRedis().getPort(),
            60, TimeUnit.SECONDS));
    connection = client.connect();
  }

  @Override
  public RedisFuture<String> get(String key) {
    Validate.notNull(key);

    return connection.async().get(key);
  }

  @Override
  public RedisFuture<String> set(String key, String value) {
    Validate.notNull(key);
    Validate.notNull(value);

    return connection.async().set(key, value);
  }

  @Override
  protected void destroy(NancyConfiguration configuration) {
    if (!isRunning()) {
      throw new NancyException("Valid state!");
    }

    connection.close();
    client.shutdown();
  }

  @Override
  public boolean isRunning() {
    return client != null && connection != null && connection.isOpen();
  }
}
