package pl.communityforcoders.nancy.impl.module;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang3.Validate;
import pl.communityforcoders.nancy.Nancy;
import pl.communityforcoders.nancy.NancyException;
import pl.communityforcoders.nancy.module.Module;
import pl.communityforcoders.nancy.module.ModulesManager;
import pl.communityforcoders.nancy.module.annotation.Manifest;

public class ModulesManagerImpl implements ModulesManager {

  private final Gson gson = new Gson();
  private final Map<String, ModuleImpl> moduleMap = new ConcurrentHashMap<>();
  private final Nancy nancy;

  public ModulesManagerImpl(Nancy nancy) {
    Validate.notNull(nancy);

    this.nancy = nancy;
  }

  @Override
  public Optional<Module> get(String name) {
    Validate.notNull(name);

    return Optional.ofNullable(moduleMap.get(name));
  }

  @Override
  public Module find(File file) {
    Validate.notNull(file);

    try {
      URLClassLoader loader = new URLClassLoader(new URL[]{ file.toURL() }, ClassLoader.getSystemClassLoader());

      try (InputStream stream =  loader.getResourceAsStream("plugin.json")) {
        if (stream == null) {
          throw new NancyException("Stream cannot be null!");
        }

        JsonObject plugin = gson.fromJson(new InputStreamReader(stream), JsonObject.class);
        JsonElement main = plugin.get("main");
        if (main == null) {
          throw new NancyException("Main cannot be null!");
        }

        Object instance = loader.loadClass(main.getAsString()).newInstance();
        ModuleImpl module = new ModuleImpl(nancy, instance);

        Manifest manifest = module.getManifest();
        if (manifest == null) {
          throw new NancyException("Manifest cannot be null!");
        }

        get(manifest.name()).ifPresent(target -> {
          throw new NancyException("Module with name `" + manifest.name() + "` must be null!");
        });

        if (!module.getDataFolder().exists()) {
          module.getDataFolder().mkdir();
        }

        moduleMap.put(manifest.name(), module);
        module.enable();
        module.listen();

        return module;
      } catch (IOException ex) {
        throw new NancyException(ex);
      }

    } catch (MalformedURLException | ClassNotFoundException | InstantiationException
        | IllegalAccessException ex) {
      throw new NancyException(ex);
    }
  }

  public void cleanup() {
    moduleMap.values().forEach(ModuleImpl::disable);
    moduleMap.clear();
  }

}
