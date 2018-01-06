package pl.communityforcoders.nancy.command;

import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import pl.communityforcoders.nancy.command.annotation.CommandManifest;
import pl.communityforcoders.nancy.command.context.CommandContext;

public interface Command {

  void execute(User user, TextChannel channel, CommandContext context);

  CommandManifest getManifest();

}
