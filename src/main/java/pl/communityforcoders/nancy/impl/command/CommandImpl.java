package pl.communityforcoders.nancy.impl.command;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import org.apache.commons.lang3.Validate;
import pl.communityforcoders.nancy.command.Command;
import pl.communityforcoders.nancy.command.context.CommandContext;

class CommandImpl implements Command {

  private final Object instance;
  private final Method method;

  public CommandImpl(Object instance, Method method) {
    Validate.notNull(instance);
    Validate.notNull(method);

    this.instance = instance;
    this.method = method;
  }

  @Override
  public void execute(User user, TextChannel channel, CommandContext context) {
    Validate.notNull(user);
    Validate.notNull(channel);
    Validate.notNull(context);

    try {
      method.invoke(instance, user, channel, context);
    } catch (IllegalAccessException | InvocationTargetException ex) {
      throw new RuntimeException(ex);
    }
  }

}
