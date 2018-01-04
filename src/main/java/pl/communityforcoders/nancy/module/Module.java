package pl.communityforcoders.nancy.module;

import java.io.File;
import pl.communityforcoders.nancy.module.annotation.Manifest;

public interface Module {

  void enable();

  void disable();

  void listen();

  void inject();

  Manifest getManifest();

  File getDataFolder();

}
