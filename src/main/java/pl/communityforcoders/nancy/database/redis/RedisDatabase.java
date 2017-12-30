package pl.communityforcoders.nancy.database.redis;

import com.lambdaworks.redis.RedisFuture;
import pl.communityforcoders.nancy.database.Database;

public interface RedisDatabase extends Database {

  RedisFuture<String> get(String key);

  RedisFuture<String> set(String key, String value);

}
