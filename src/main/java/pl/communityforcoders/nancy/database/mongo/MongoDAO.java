package pl.communityforcoders.nancy.database.mongo;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;

public class MongoDAO<T> extends BasicDAO<T, String> {

  public MongoDAO(Class<T> entity, Datastore datastore) {
    super(entity, datastore);
  }

}
