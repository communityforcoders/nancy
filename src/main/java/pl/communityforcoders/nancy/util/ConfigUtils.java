package pl.communityforcoders.nancy.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import org.apache.commons.lang3.Validate;

public final class ConfigUtils {

  private final static Gson GSON = new GsonBuilder()
      .disableHtmlEscaping().setPrettyPrinting()
      .create();

  private ConfigUtils() {
  }

  public static <T> T loadConfig(File file, Class<T> implementationFile) {
    Validate.notNull(file);
    Validate.notNull(implementationFile);

    try {
      if (!file.exists()) {
        if (!file.createNewFile()) {
          throw new IllegalStateException("Cannot create config file!");
        }

        Files.write(file.toPath(), GSON.toJson(implementationFile.newInstance())
            .getBytes(StandardCharsets.UTF_8));
      }
    } catch (IOException | InstantiationException | IllegalStateException |
        IllegalAccessException ex) {
      throw new IllegalStateException("Cannot create config file!", ex);
    }

    try {
      return GSON.fromJson(new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8),
          implementationFile);
    } catch (IOException ex) {
      throw new IllegalStateException("Cannot read config file!", ex);
    }
  }

}
