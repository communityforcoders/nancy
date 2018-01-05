package pl.communityforcoders.nancy.impl.command;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang3.Validate;
import pl.communityforcoders.nancy.Nancy;
import pl.communityforcoders.nancy.command.Command;
import pl.communityforcoders.nancy.command.CommandManager;
import pl.communityforcoders.nancy.command.annotation.Manifest;

public class CommandManagerImpl implements CommandManager {

  private final Nancy nancy;
  private final CommandCaller caller;
  private final Map<String, Command> commandMap = new ConcurrentHashMap<>();

  public CommandManagerImpl(Nancy nancy) {
    Validate.notNull(nancy);

    this.nancy = nancy;
    this.caller = new CommandCaller(this);
  }

  @Override
  public Optional<Command> get(String name) {
    Validate.notNull(name);

    return Optional.ofNullable(commandMap.get(name));
  }

  @Override
  public void register(Object... instances) {
    Validate.notNull(instances);

    for (Object instance : instances) {
      for (Method method : instance.getClass().getMethods()) {
        perform(instance, method);
      }
    }
  }

  public void listen() {
    nancy.getJDA().addEventListener(caller);
  }

  private void perform(Object instance, Method method) {
    Validate.notNull(method);

    if (!method.isAccessible()) {
      method.setAccessible(true);
    }

    if (!method.isAnnotationPresent(Manifest.class)) {
      return;
    }

    Command command = new CommandImpl(instance, method);
    Manifest manifest = method.getDeclaredAnnotation(Manifest.class);
    for (String name : manifest.name()) {
      if (get(name).isPresent()) {
        continue;
      }

      commandMap.put(name, command);
    }
  }

}
