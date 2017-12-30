package pl.communityforcoders.nancy.impl.database.mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.commons.lang3.Validate;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import pl.communityforcoders.nancy.Nancy;
import pl.communityforcoders.nancy.NancyConfiguration;
import pl.communityforcoders.nancy.NancyException;
import pl.communityforcoders.nancy.database.mongo.MongoDAO;
import pl.communityforcoders.nancy.database.mongo.MongoDatabase;
import pl.communityforcoders.nancy.impl.database.AbstractDatabase;

public class MongoDatabaseImpl extends AbstractDatabase implements MongoDatabase {

  private final Morphia morphia = new Morphia();
  private final Map<Class, MongoDAO> daoCache = new ConcurrentHashMap<>();

  private final AtomicReference<Datastore> datastoreReference = new AtomicReference<>();

  private String database;
  private MongoClient client;

  public MongoDatabaseImpl(Nancy nancy) {
    super(nancy);
  }

  @Override
  protected void create(NancyConfiguration configuration) {
    if (isRunning()) {
      throw new NancyException("Valid state!");
    }

    database = configuration.getMongo().getDatabase();

    client = new MongoClient(
        new ServerAddress(configuration.getMongo().getHost(), configuration.getMongo().getPort()),
        Collections.singletonList(MongoCredential.createCredential(
            configuration.getMongo().getLogin(),
            configuration.getMongo().getDatabase(),
            configuration.getMongo().getPassword().toCharArray())));
  }

  @Override
  protected void destroy(NancyConfiguration configuration) {
    if (!isRunning()) {
      throw new NancyException("Valid state!");
    }

    daoCache.clear();
    datastoreReference.set(null);

    client.close();
    client = null;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> MongoDAO<T> create(Class<T> entity) {
    Validate.notNull(entity);

    MongoDAO result = daoCache.get(entity);
    if (result == null) {
      if (!morphia.isMapped(entity)) {
        morphia.map(entity);

        datastoreReference.set(morphia.createDatastore(client, database));
      }

      result = new MongoDAO(entity, datastoreReference.get());
      daoCache.put(entity, result);
    }

    return result;
  }

  @Override
  public boolean isRunning() {
    return client != null && !client.isLocked();
  }

}
