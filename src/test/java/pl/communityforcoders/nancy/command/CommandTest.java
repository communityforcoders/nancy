package pl.communityforcoders.nancy.command;

import java.util.Optional;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.communityforcoders.nancy.Nancy;
import pl.communityforcoders.nancy.command.annotation.CommandManifest;
import pl.communityforcoders.nancy.command.context.CommandContext;
import pl.communityforcoders.nancy.impl.command.CommandManagerImpl;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CommandTest {

  @Mock
  private Nancy nancy;

  private CommandManagerImpl commandManager;

  @Before
  public void setup() {
    JDA jda = mock(JDA.class);

    doAnswer((mock) -> null).when(jda).addEventListener();
    when(nancy.getJDA()).thenReturn(jda);

    commandManager = new CommandManagerImpl(nancy);
    commandManager.listen();
  }

  @Test
  public void commandTest() {
    User user = mock(User.class);
    TextChannel channel = mock(TextChannel.class);
    CommandContext context = mock(CommandContext.class);

    commandManager.register(new TestCommand());

    Optional<Command> first = commandManager.get("test");
    Assert.assertTrue(first.isPresent());
    Assert.assertNotNull(first.get().getManifest());
    first.get().execute(user, channel, context);

    Optional<Command> second = commandManager.get("testcommand");
    Assert.assertTrue(second.isPresent());
    Assert.assertNotNull(second.get().getManifest());
    second.get().execute(user, channel, context);
  }

  @After
  public void cleanup() {
    commandManager = null;
    nancy = null;
  }

  private class TestCommand {

    @CommandManifest(name = {"test", "testcommand"}, type = ChannelType.PRIVATE)
    public void testCommand(User user, TextChannel channel, CommandContext context) {
      Assert.assertNotNull(user);
      Assert.assertNotNull(channel);
      Assert.assertNotNull(context);
    }

  }

}
