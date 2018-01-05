package pl.communityforcoders.nancy.module;

import java.io.File;
import pl.communityforcoders.nancy.module.annotation.ModuleManifest;

public interface Module {

  void enable();

  void disable();

  void listen();

  void inject();

  ModuleManifest getManifest();

  File getDataFolder();

}
