package pl.communityforcoders.nancy.command.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import net.dv8tion.jda.core.entities.ChannelType;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandManifest {

  String[] name();

  ChannelType[] type() default {};

}
