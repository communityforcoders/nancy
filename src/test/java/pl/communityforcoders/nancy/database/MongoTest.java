package pl.communityforcoders.nancy.database;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;
import pl.communityforcoders.nancy.Nancy;
import pl.communityforcoders.nancy.NancyConfiguration;
import pl.communityforcoders.nancy.database.mongo.MongoDAO;
import pl.communityforcoders.nancy.database.mongo.MongoDatabase;
import pl.communityforcoders.nancy.impl.database.mongo.MongoDatabaseImpl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MongoTest {

  @Mock
  private Nancy nancy;

  @Mock
  private NancyConfiguration configuration;

  private MongoDatabase database;

  @Before
  public void setup() {
    NancyConfiguration.Mongo mongoConfiguration = mock(NancyConfiguration.Mongo.class);
    when(mongoConfiguration.isEnabled()).thenReturn(true);
    when(mongoConfiguration.getHost()).thenReturn("localhost");
    when(mongoConfiguration.getPort()).thenReturn(27017);
    when(mongoConfiguration.getLogin()).thenReturn("travis");
    when(mongoConfiguration.getPassword()).thenReturn("travis");
    when(mongoConfiguration.getDatabase()).thenReturn("travis");

    when(configuration.getMongo()).thenReturn(mongoConfiguration);
    when(nancy.getConfiguration()).thenReturn(configuration);

    database = new MongoDatabaseImpl(nancy);
    database.open();
  }

  @Test
  public void saveTest() {
    MongoDAO<MongoElement> dao = database.create(MongoElement.class);
    Assert.assertNotNull(dao);

    MongoElement firstElement = new MongoElement(0, "first", "value");
    dao.save(firstElement);

    MongoElement secondElement = new MongoElement(1, "second", "value");
    dao.save(secondElement);
  }

  @Test
  public void loadTest() {
    MongoDAO<MongoElement> dao = database.create(MongoElement.class);
    Assert.assertNotNull(dao);

    MongoElement firstElement = dao.findOne("_id", 0);
    Assert.assertNotNull(firstElement);
    Assert.assertEquals(0, firstElement.getId());
    Assert.assertEquals("first", firstElement.getFirst());
    Assert.assertEquals("value", firstElement.getSecond());

    MongoElement secondValue = dao.findOne("_id", 1);
    Assert.assertNotNull(secondValue);
    Assert.assertEquals(1, secondValue.getId());
    Assert.assertEquals("second", secondValue.getFirst());
    Assert.assertEquals("value", secondValue.getSecond());
  }

  @After
  public void cleanup() {
    database.close();
    database = null;
  }

  @Entity(value = "MongoElements", noClassnameStored = true)
  public static class MongoElement {

    @Id
    private long id;

    @Indexed(unique = true)
    private String first;

    @Indexed
    private String second;

    public MongoElement() {
      // for Morphia mapper.
    }

    public MongoElement(long id, String first, String second) {
      this.id = id;
      this.first = first;
      this.second = second;
    }

    public long getId() {
      return id;
    }

    public String getFirst() {
      return first;
    }

    public String getSecond() {
      return second;
    }

  }

}
