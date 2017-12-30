package pl.communityforcoders.nancy;

import java.io.File;
import pl.communityforcoders.nancy.impl.NancyImpl;
import pl.communityforcoders.nancy.util.ConfigUtils;

public final class NancyBootstrap {

  public static void main(String[] args) {
    NancyConfiguration configuration = ConfigUtils.loadConfig(new File("config.json"),
        NancyConfiguration.class);

    Nancy nancy = new NancyImpl(configuration);
    nancy.start();
  }

}
