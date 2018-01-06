package pl.communityforcoders.nancy.command.context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import org.apache.commons.lang3.Validate;

public interface CommandContext {

  static CommandContext parse(String prefix, Message message) {
    Validate.notNull(prefix);
    Validate.notNull(message);

    String[] rawArray = message.getContentRaw().split(" ");
    List<String> params = new ArrayList<>();
    List<String> flags = new ArrayList<>();
    Map<String, String> values = new HashMap<>();

    boolean value = false;
    String valueKey = null;

    for (String param : rawArray) {
      if (!prefix.isEmpty() && param.equals(prefix)) {
        continue;
      }

      if (param.startsWith("--")) {
        flags.add(param.substring(2, param.length()));
        continue;
      }

      if (param.startsWith("-")) {
        value = true;
        valueKey = param.substring(1, param.length());
        continue;
      }

      if (value) {
        value = false;
        values.put(valueKey, param);
        continue;
      }

      params.add(param);
    }

    return new CommandContextImpl(params, flags, values,
        message.getMentionedUsers(), message.getMentionedRoles(), message.getMentionedChannels());
  }

  static CommandContext parse(Message message) {
    Validate.notNull(message);

    return parse("", message);
  }

  List<String> getParams();

  default boolean hasParam(String param) {
    Validate.notNull(param);

    return getParams().contains(param);
  }

  default Optional<String> getParam(int index) {
    if (getParams().size() < index) {
      return Optional.empty();
    }

    return Optional.ofNullable(getParams().get(index - 1));
  }

  List<String> getFlags();

  default boolean hasFlag(String flag) {
    Validate.notNull(flag);

    return getFlags().contains(flag);
  }

  Map<String, String> getValues();

  default boolean hasValue(String key) {
    Validate.notNull(key);

    return getValues().get(key) != null;
  }

  default Optional<String> getValue(String key) {
    Validate.notNull(key);

    return Optional.ofNullable(getValues().get(key));
  }

  List<User> getMentionedUsers();

  default boolean isUserMentioned(User user) {
    Validate.notNull(user);

    return getMentionedUsers().contains(user);
  }

  List<Role> getMentionedRoles();

  default boolean isRoleMentioned(Role role) {
    Validate.notNull(role);

    return getMentionedRoles().equals(role);
  }

  List<TextChannel> getMentionedChannels();

  default boolean isChannelMentioned(TextChannel channel) {
    Validate.notNull(channel);

    return getMentionedChannels().equals(channel);
  }

}
