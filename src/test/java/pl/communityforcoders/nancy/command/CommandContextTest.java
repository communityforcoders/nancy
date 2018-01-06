package pl.communityforcoders.nancy.command;

import net.dv8tion.jda.core.entities.Message;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.communityforcoders.nancy.command.context.CommandContext;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CommandContextTest {

  @Mock
  private Message message;

  @Test
  public void paramsTest() {
    when(message.getContentRaw()).thenReturn("first second");
    CommandContext context = CommandContext.parse(message);

    Assert.assertNotNull(context);

    Assert.assertTrue(context.hasParam("first"));
    Assert.assertEquals("first", context.getParam(1).get());

    Assert.assertTrue(context.hasParam("second"));
    Assert.assertEquals("second", context.getParam(2).get());
  }

  @Test
  public void valuesTest() {
    when(message.getContentRaw()).thenReturn("-firstkey firstvalue -secondkey secondvalue");
    CommandContext context = CommandContext.parse(message);

    Assert.assertNotNull(context);

    Assert.assertTrue(context.hasValue("firstkey"));
    Assert.assertFalse(context.hasValue("-firstkey"));
    Assert.assertFalse(context.hasValue("firstvalue"));
    Assert.assertEquals("firstvalue", context.getValue("firstkey").get());

    Assert.assertTrue(context.hasValue("secondkey"));
    Assert.assertFalse(context.hasValue("-secondkey"));
    Assert.assertFalse(context.hasValue("secondvalue"));
    Assert.assertEquals("secondvalue", context.getValue("secondkey").get());
  }

  @Test
  public void flagsTest() {
    when(message.getContentRaw()).thenReturn("--firstflag --secondflag");
    CommandContext context = CommandContext.parse(message);

    Assert.assertNotNull(context);

    Assert.assertTrue(context.hasFlag("firstflag"));
    Assert.assertTrue(context.hasFlag("secondflag"));
  }

  @Test
  public void mixedTest() {
    when(message.getContentRaw()).thenReturn("prefix --firstflag firstparam -firstkey firstvalue "
        + "-secondkey secondvalue --secondflag secondparam");
    CommandContext context = CommandContext.parse("prefix", message);

    Assert.assertNotNull(context);

    Assert.assertFalse(context.hasParam("prefix"));

    Assert.assertTrue(context.hasParam("firstparam"));
    Assert.assertEquals("firstparam", context.getParam(1).get());

    Assert.assertTrue(context.hasParam("secondparam"));
    Assert.assertEquals("secondparam", context.getParam(2).get());

    Assert.assertTrue(context.hasValue("firstkey"));
    Assert.assertFalse(context.hasValue("-firstkey"));
    Assert.assertFalse(context.hasValue("firstvalue"));
    Assert.assertEquals("firstvalue", context.getValue("firstkey").get());

    Assert.assertTrue(context.hasValue("secondkey"));
    Assert.assertFalse(context.hasValue("-secondkey"));
    Assert.assertFalse(context.hasValue("secondvalue"));
    Assert.assertEquals("secondvalue", context.getValue("secondkey").get());

    Assert.assertTrue(context.hasFlag("firstflag"));
    Assert.assertTrue(context.hasFlag("secondflag"));
  }

  @After
  public void cleanup() {
    message = null;
  }

}
