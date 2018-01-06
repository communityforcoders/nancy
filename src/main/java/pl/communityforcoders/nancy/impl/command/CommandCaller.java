package pl.communityforcoders.nancy.impl.command;

import java.util.Optional;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.EventListener;
import org.apache.commons.lang3.Validate;
import pl.communityforcoders.nancy.command.Command;
import pl.communityforcoders.nancy.command.CommandManager;
import pl.communityforcoders.nancy.command.context.CommandContext;

final class CommandCaller implements EventListener {

  private final CommandManager manager;

  public CommandCaller(CommandManager manager) {
    Validate.notNull(manager);

    this.manager = manager;
  }

  @Override
  public void onEvent(Event event) {
    if (!(event instanceof MessageReceivedEvent)) {
      return;
    }

    MessageReceivedEvent messageEvent = (MessageReceivedEvent) event;
    Message message = messageEvent.getMessage();

    String prefix = message.getContentRaw().split(" ")[0];
    CommandContext context = CommandContext.parse(prefix, message);

    Optional<Command> optionalCommand = manager.get(prefix);
    if (!optionalCommand.isPresent()) {
      return;
    }

    Command command = optionalCommand.get();
    if (command.getManifest().type().length == 0) {
      command.execute(messageEvent.getAuthor(), messageEvent.getChannel(), context);
      return;
    }

    for (ChannelType type : command.getManifest().type()) {
      if (message.isFromType(type)) {
        command.execute(messageEvent.getAuthor(), messageEvent.getChannel(), context);
        break;
      }
    }
  }

}
