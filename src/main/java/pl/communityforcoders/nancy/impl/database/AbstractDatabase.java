package pl.communityforcoders.nancy.impl.database;

import org.apache.commons.lang3.Validate;
import pl.communityforcoders.nancy.Nancy;
import pl.communityforcoders.nancy.NancyConfiguration;
import pl.communityforcoders.nancy.database.Database;

public abstract class AbstractDatabase implements Database {

  private final Nancy nancy;

  public AbstractDatabase(Nancy nancy) {
    Validate.notNull(nancy);

    this.nancy = nancy;
  }

  @Override
  public void open() {
    this.create(nancy.getConfiguration());
  }

  @Override
  public void close() {
    this.destroy(nancy.getConfiguration());
  }

  protected abstract void create(NancyConfiguration configuration);

  protected abstract void destroy(NancyConfiguration configuration);

}
