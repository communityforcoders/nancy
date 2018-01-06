package pl.communityforcoders.nancy;

import java.util.Optional;
import net.dv8tion.jda.core.JDA;
import pl.communityforcoders.nancy.command.CommandManager;
import pl.communityforcoders.nancy.database.mongo.MongoDatabase;
import pl.communityforcoders.nancy.database.redis.RedisDatabase;
import pl.communityforcoders.nancy.module.ModulesManager;

public interface Nancy {

  void start();

  void stop();

  boolean isRunning();

  NancyConfiguration getConfiguration();

  JDA getJDA();

  Optional<MongoDatabase> getMongo();

  Optional<RedisDatabase> getRedis();

  ModulesManager getModulesManager();

  CommandManager getCommandManager();

}
