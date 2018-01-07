package pl.communityforcoders.nancy.module;

import java.io.File;
import java.util.Optional;

public interface ModulesManager {

  File MODULES_DIRECTORY = new File("modules");

  Optional<Module> get(String name);

  Module find(File file);

}
