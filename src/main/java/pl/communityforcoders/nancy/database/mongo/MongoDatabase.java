package pl.communityforcoders.nancy.database.mongo;

import pl.communityforcoders.nancy.database.Database;

public interface MongoDatabase extends Database {

  <T> MongoDAO<T> create(Class<T> entity);

}
