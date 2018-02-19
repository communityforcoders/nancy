package pl.communityforcoders.nancy.module;

import java.io.File;
import net.dv8tion.jda.core.JDA;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.communityforcoders.nancy.Nancy;
import pl.communityforcoders.nancy.command.CommandManager;
import pl.communityforcoders.nancy.impl.module.ModulesManagerImpl;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ModuleTest {

  @Mock
  private Nancy nancy;

  @Mock
  private JDA jda;

  @Mock
  private CommandManager manager;

  private ModulesManagerImpl modulesManager;

  @Before
  public void setup() {
    doAnswer((mock) -> null).when(jda).addEventListener();
    doAnswer((mock) -> null).when(manager).register(Matchers.any());
    when(nancy.getJDA()).thenReturn(jda);
    when(nancy.getCommandManager()).thenReturn(manager);

    modulesManager = new ModulesManagerImpl(nancy);
  }

  @Test
  public void moduleTest() {
    File file = new File(getClass().getClassLoader()
        .getResource("nancy-test-1.0.0.2.jar")
        .getFile());

    Module module = modulesManager.find(file);
    Assert.assertEquals("TestModule", module.getManifest().name());
    Assert.assertEquals("kacperduras", module.getManifest().author());
    Assert.assertEquals("1.0.0.2", module.getManifest().version());
  }

  @After
  public void cleanup() {
    modulesManager.cleanup();
    modulesManager = null;
  }

}
