package pl.communityforcoders.nancy.module;

import java.io.File;
import java.util.Optional;

public interface ModulesManager {

  File MODULES_DIRECTORY = new File("modules_directory");

  Optional<Module> get(String name);

  Module find(File file);

}
