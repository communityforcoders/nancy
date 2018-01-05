package pl.communityforcoders.nancy.command.context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.Validate;

class CommandContextImpl implements CommandContext {

  private final List<String> params;
  private final List<String> flags;
  private final Map<String, String> values;

  public CommandContextImpl(List<String> params, List<String> flags, Map<String, String> values) {
    Validate.notNull(params);
    Validate.notNull(flags);
    Validate.notNull(values);

    this.params = params;
    this.flags = flags;
    this.values = values;
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

}
