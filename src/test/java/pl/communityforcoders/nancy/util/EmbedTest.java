package pl.communityforcoders.nancy.util;

import java.awt.Color;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.MessageEmbed.Field;
import org.junit.Assert;
import org.junit.Test;

public class EmbedTest {

  @Test
  public void infoTest() {
    MessageEmbed embed = EmbedUtils.info("title", new Field("name", "value", true));

    Assert.assertEquals(Color.BLUE, embed.getColor());
    Assert.assertEquals("title", embed.getTitle());
    Assert.assertEquals(1, embed.getFields().size());
    Assert.assertEquals("name", embed.getFields().get(0).getName());
    Assert.assertEquals("value", embed.getFields().get(0).getValue());
    Assert.assertEquals("Copyright \u00A9 2017-present CommunityForCoders", embed.getFooter().getText());
    Assert.assertEquals("https://communityforcoders.pl/assets/image/logo.png", embed.getFooter().getIconUrl());
  }

  @Test
  public void agreeTest() {
    MessageEmbed embed = EmbedUtils.agree("title", new Field("name", "value", true));

    Assert.assertEquals(Color.GREEN, embed.getColor());
    Assert.assertEquals("title", embed.getTitle());
    Assert.assertEquals(1, embed.getFields().size());
    Assert.assertEquals("name", embed.getFields().get(0).getName());
    Assert.assertEquals("value", embed.getFields().get(0).getValue());
    Assert.assertEquals("Copyright \u00A9 2017-present CommunityForCoders", embed.getFooter().getText());
    Assert.assertEquals("https://communityforcoders.pl/assets/image/logo.png", embed.getFooter().getIconUrl());
  }

  @Test
  public void warningTest() {
    MessageEmbed embed = EmbedUtils.warning("title", new Field("name", "value", true));

    Assert.assertEquals(Color.YELLOW, embed.getColor());
    Assert.assertEquals("title", embed.getTitle());
    Assert.assertEquals(1, embed.getFields().size());
    Assert.assertEquals("name", embed.getFields().get(0).getName());
    Assert.assertEquals("value", embed.getFields().get(0).getValue());
    Assert.assertEquals("Copyright \u00A9 2017-present CommunityForCoders", embed.getFooter().getText());
    Assert.assertEquals("https://communityforcoders.pl/assets/image/logo.png", embed.getFooter().getIconUrl());
  }

  @Test
  public void errorTest() {
    MessageEmbed embed = EmbedUtils.error("title", new Field("name", "value", true));

    Assert.assertEquals(Color.RED, embed.getColor());
    Assert.assertEquals("title", embed.getTitle());
    Assert.assertEquals(1, embed.getFields().size());
    Assert.assertEquals("name", embed.getFields().get(0).getName());
    Assert.assertEquals("value", embed.getFields().get(0).getValue());
    Assert.assertEquals("Copyright \u00A9 2017-present CommunityForCoders", embed.getFooter().getText());
    Assert.assertEquals("https://communityforcoders.pl/assets/image/logo.png", embed.getFooter().getIconUrl());
  }

}
