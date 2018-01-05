package pl.communityforcoders.nancy.command;

import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import pl.communityforcoders.nancy.command.context.CommandContext;

public interface Command {

  void execute(User user, MessageChannel channel, CommandContext context);

}
