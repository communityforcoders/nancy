package pl.communityforcoders.nancy.impl;

import com.lambdaworks.redis.RedisException;
import com.mongodb.MongoException;
import java.io.File;
import java.util.Optional;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Game.GameType;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import org.apache.commons.lang3.Validate;
import pl.communityforcoders.nancy.Nancy;
import pl.communityforcoders.nancy.NancyConfiguration;
import pl.communityforcoders.nancy.NancyException;
import pl.communityforcoders.nancy.command.CommandManager;
import pl.communityforcoders.nancy.database.mongo.MongoDatabase;
import pl.communityforcoders.nancy.database.redis.RedisDatabase;
import pl.communityforcoders.nancy.impl.command.CommandManagerImpl;
import pl.communityforcoders.nancy.impl.database.mongo.MongoDatabaseImpl;
import pl.communityforcoders.nancy.impl.database.redis.RedisDatabaseImpl;
import pl.communityforcoders.nancy.impl.module.ModulesManagerImpl;
import pl.communityforcoders.nancy.module.ModulesManager;

public final class NancyImpl implements Nancy {

  private final NancyConfiguration configuration;
  private final ModulesManagerImpl modulesManager = new ModulesManagerImpl(this);
  private final CommandManagerImpl commandManager = new CommandManagerImpl(this);

  private MongoDatabase mongo;
  private RedisDatabase redis;
  private JDA jda;

  public NancyImpl(NancyConfiguration configuration) {
    Validate.notNull(configuration);

    this.configuration = configuration;
  }

  @Override
  public void start() {
    if (isRunning()) {
      throw new NancyException("Valid state!");
    }

    if (configuration.getMongo().isEnabled()) {
      mongo = new MongoDatabaseImpl(this);
      try {
        mongo.open();
      } catch (MongoException ex) {
        throw new NancyException(ex);
      }
    }

    if (configuration.getRedis().isEnabled()) {
      redis = new RedisDatabaseImpl(this);
      try {
        redis.open();
      } catch (RedisException ex) {
        throw new NancyException(ex);
      }
    }

    JDABuilder builder = new JDABuilder(AccountType.BOT);
    builder.setToken(configuration.getDiscord().getToken());
    builder.setGame(Game.of(GameType.DEFAULT, configuration.getDiscord().getGame()));
    builder.setCorePoolSize(configuration.getDiscord().getPoolSize());

    builder.setAutoReconnect(true);
    builder.setBulkDeleteSplittingEnabled(true);

    builder.setAudioEnabled(false);
    builder.setIdle(false);

    try {
      jda = builder.buildBlocking();
    } catch (LoginException | InterruptedException | RateLimitedException ex) {
      throw new NancyException(ex);
    }

    loadModules();
    commandManager.listen();
  }

  @Override
  public void stop() {
    if (!isRunning()) {
      throw new NancyException("Valid state!");
    }

    modulesManager.cleanup();

    jda.shutdown();
    jda = null;

    redis.close();
    redis = null;

    mongo.close();
    mongo = null;
  }

  @Override
  public boolean isRunning() {
    return jda != null;
  }

  @Override
  public NancyConfiguration getConfiguration() {
    return configuration;
  }

  @Override
  public JDA getJDA() {
    return jda;
  }

  @Override
  public Optional<MongoDatabase> getMongo() {
    return Optional.ofNullable(mongo);
  }

  @Override
  public Optional<RedisDatabase> getRedis() {
    return Optional.ofNullable(redis);
  }

  @Override
  public ModulesManager getModulesManager() {
    return modulesManager;
  }

  @Override
  public CommandManager getCommandManager() {
    return commandManager;
  }

  private void loadModules() {
    if (!ModulesManager.MODULES_DIRECTORY.exists() && !ModulesManager.MODULES_DIRECTORY.mkdir()) {
      throw new NancyException("Modules cannot be loaded!");
    }
    for (File file : ModulesManager.MODULES_DIRECTORY.listFiles()) {
      modulesManager.find(file).enable();
    }
  }

}
