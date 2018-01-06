package pl.communityforcoders.nancy.impl.command;

import java.util.Optional;
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
    CommandContext context = CommandContext.parse(message.getContentRaw().split(" ")[0],
        message);

    Optional<String> optionalCommandName = context.getParam(1);
    if (!optionalCommandName.isPresent()) {
      return;
    }
    Optional<Command> optionalCommand = manager.get(optionalCommandName.get());
    if (!optionalCommand.isPresent()) {
      return;
    }

    Command command = optionalCommand.get();
    command.execute(messageEvent.getAuthor(), messageEvent.getTextChannel(), context);
  }

}
