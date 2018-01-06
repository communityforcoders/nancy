package pl.communityforcoders.nancy.command.context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import org.apache.commons.lang3.Validate;

class CommandContextImpl implements CommandContext {

  private final List<String> params;
  private final List<String> flags;
  private final Map<String, String> values;

  private final List<User> mentionedUsers;
  private final List<Role> mentionedRoles;
  private final List<TextChannel> mentionedChannels;

  public CommandContextImpl(List<String> params, List<String> flags, Map<String, String> values,
      List<User> mentionedUsers, List<Role> mentionedRoles, List<TextChannel> mentionedChannels) {
    Validate.notNull(params);
    Validate.notNull(flags);
    Validate.notNull(values);

    this.params = params;
    this.flags = flags;
    this.values = values;

    this.mentionedUsers = mentionedUsers;
    this.mentionedRoles = mentionedRoles;
    this.mentionedChannels = mentionedChannels;
  }

  public CommandContextImpl(List<String> params, List<String> flags, Map<String, String> values) {
    this(params, flags, values, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
  }

  public List<String> getParams() {
    return new ArrayList<>(params);
  }

  public List<String> getFlags() {
    return new ArrayList<>(flags);
  }

  public Map<String, String> getValues() {
    return new HashMap<>(values);
  }

  public List<User> getMentionedUsers() {
    return mentionedUsers;
  }

  public List<Role> getMentionedRoles() {
    return mentionedRoles;
  }

  public List<TextChannel> getMentionedChannels() {
    return mentionedChannels;
  }

}
