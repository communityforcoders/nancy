package pl.communityforcoders.nancy;

import java.io.File;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import pl.communityforcoders.nancy.util.ConfigUtils;

public class NancyConfigurationTest {

  private final static File CONFIG_FILE = new File("test-config.json");

  private static NancyConfiguration configuration;

  @BeforeClass
  public static void generalSetup() {
    configuration = ConfigUtils.loadConfig(CONFIG_FILE, NancyConfiguration.class);
  }

  @Test
  public void discord() {
    NancyConfiguration.Discord discord = configuration.getDiscord();
    Assert.assertNotNull(discord);

    Assert.assertEquals("token", discord.getToken());
    Assert.assertEquals("Nancy ver. 1.0.0.0", discord.getGame());
  }

  @Test
  public void mongo() {
    NancyConfiguration.Mongo mongo = configuration.getMongo();
    Assert.assertNotNull(mongo);

    Assert.assertEquals("localhost", mongo.getHost());
    Assert.assertEquals(27017, mongo.getPort());
    Assert.assertEquals("root", mongo.getLogin());
    Assert.assertEquals("password", mongo.getPassword());
    Assert.assertEquals("database", mongo.getDatabase());
  }

  @Test
  public void redis() {
    NancyConfiguration.Redis redis = configuration.getRedis();
    Assert.assertNotNull(redis);

    Assert.assertEquals("localhost", redis.getHost());
    Assert.assertEquals(6379, redis.getPort());
  }

  @AfterClass
  public static void generalCleanup() {
    if (CONFIG_FILE.exists()) {
      CONFIG_FILE.delete();
    }
  }

}
