package pl.communityforcoders.nancy;

import com.google.gson.annotations.SerializedName;

public class NancyConfiguration {

  @SerializedName("discord")
  private final Discord discord = new Discord();

  @SerializedName("mongo")
  private final Mongo mongo = new Mongo();

  @SerializedName("redis")
  private final Redis redis = new Redis();

  public static class Discord {

    @SerializedName("token")
    private final String token = "token";

    @SerializedName("game")
    private final String game = "Nancy ver. 1.0.0.0";

    @SerializedName("pool-size")
    private final int poolSize = 4;

    public String getToken() {
      return token;
    }

    public String getGame() {
      return game;
    }

    public int getPoolSize() {
      return poolSize;
    }

  }

  public static class Mongo {

    @SerializedName("enabled")
    private final boolean enabled = false;

    @SerializedName("host")
    private final String host = "localhost";

    @SerializedName("port")
    private final int port = 27017;

    @SerializedName("login")
    private final String login = "root";

    @SerializedName("password")
    private final String password = "password";

    @SerializedName("database")
    private final String database = "database";

    public boolean isEnabled() {
      return enabled;
    }

    public String getHost() {
      return host;
    }

    public int getPort() {
      return port;
    }

    public String getLogin() {
      return login;
    }

    public String getPassword() {
      return password;
    }

    public String getDatabase() {
      return database;
    }

  }

  public static class Redis {

    @SerializedName("enabled")
    private final boolean enabled = false;

    @SerializedName("host")
    private final String host = "localhost";

    @SerializedName("port")
    private final int port = 6379;

    public boolean isEnabled() {
      return enabled;
    }

    public String getHost() {
      return host;
    }

    public int getPort() {
      return port;
    }

  }

  public Discord getDiscord() {
    return discord;
  }

  public Mongo getMongo() {
    return mongo;
  }

  public Redis getRedis() {
    return redis;
  }

}
