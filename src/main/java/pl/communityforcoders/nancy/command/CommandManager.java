package pl.communityforcoders.nancy.command;

import java.util.Optional;

public interface CommandManager {

  Optional<Command> get(String name);

  void register(Object... instances);

}
