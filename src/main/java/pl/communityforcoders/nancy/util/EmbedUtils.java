package pl.communityforcoders.nancy.util;

import java.awt.Color;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.MessageEmbed.Field;
import org.apache.commons.lang3.Validate;

public class EmbedUtils {

  public static MessageEmbed info(String title, Field... fields) {
    Validate.notNull(fields);
    EmbedBuilder builder = new EmbedBuilder();

    if (title != null && !title.isEmpty()) {
      builder.setTitle(title);
    }

    for (Field field : fields) {
      builder.addField(field);
    }

    builder.setColor(Color.BLUE);

    return footer(builder).build();
  }

  public static MessageEmbed info(Field... fields) {
    return info(null, fields);
  }

  public static MessageEmbed agree(String title, Field... fields) {
    Validate.notNull(fields);
    EmbedBuilder builder = new EmbedBuilder();

    if (title != null && !title.isEmpty()) {
      builder.setTitle(title);
    }

    for (Field field : fields) {
      builder.addField(field);
    }

    builder.setColor(Color.GREEN);

    return footer(builder).build();
  }

  public static MessageEmbed agree(Field... fields) {
    return agree(null, fields);
  }

  public static MessageEmbed warning(String title, Field... fields) {
    Validate.notNull(fields);
    EmbedBuilder builder = new EmbedBuilder();

    if (title != null && !title.isEmpty()) {
      builder.setTitle(title);
    }

    for (Field field : fields) {
      builder.addField(field);
    }

    builder.setColor(Color.YELLOW);

    return footer(builder).build();
  }

  public static MessageEmbed warning(Field... fields) {
    return warning(null, fields);
  }

  public static MessageEmbed error(String title, Field... fields) {
    Validate.notNull(fields);
    EmbedBuilder builder = new EmbedBuilder();

    if (title != null && !title.isEmpty()) {
      builder.setTitle(title);
    }

    for (Field field : fields) {
      builder.addField(field);
    }

    builder.setColor(Color.RED);

    return footer(builder).build();
  }

  public static MessageEmbed error(Field... fields) {
    return error(null, fields);
  }

  private static EmbedBuilder footer(EmbedBuilder builder) {
    Validate.notNull(builder);

    return builder.setFooter(
        "Copyright \u00A9 2017-present CommunityForCoders",
        "https://communityforcoders.pl/assets/image/logo.png"
    );
  }

}
