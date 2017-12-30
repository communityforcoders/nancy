package pl.communityforcoders.nancy;

import net.dv8tion.jda.core.JDA;
import pl.communityforcoders.nancy.database.mongo.MongoDatabase;
import pl.communityforcoders.nancy.database.redis.RedisDatabase;
import pl.communityforcoders.nancy.module.ModulesManager;

public interface Nancy {

  void start();

  void stop();

  boolean isRunning();

  NancyConfiguration getConfiguration();

  JDA getJDA();

  MongoDatabase getMongo();

  RedisDatabase getRedis();

  ModulesManager getModulesManager();

}
