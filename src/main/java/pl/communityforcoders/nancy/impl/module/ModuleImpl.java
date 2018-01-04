package pl.communityforcoders.nancy.impl.module;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.hooks.EventListener;
import org.apache.commons.lang3.Validate;
import pl.communityforcoders.nancy.Nancy;
import pl.communityforcoders.nancy.NancyException;
import pl.communityforcoders.nancy.module.Module;
import pl.communityforcoders.nancy.module.ModulesManager;
import pl.communityforcoders.nancy.module.annotation.Inject;
import pl.communityforcoders.nancy.module.annotation.Listener;
import pl.communityforcoders.nancy.module.annotation.Manifest;
import pl.communityforcoders.nancy.module.annotation.OnDisable;
import pl.communityforcoders.nancy.module.annotation.OnEnable;

public class ModuleImpl implements Module {

  private final Nancy nancy;
  private final Object instance;

  private Manifest manifest;
  private Method enable;
  private Method disable;

  public ModuleImpl(Nancy nancy, Object instance) {
    Validate.notNull(nancy);
    Validate.notNull(instance);

    this.nancy = nancy;
    this.instance = instance;
  }

  @Override
  public void enable() {
    if (enable == null) {
      Optional<Method> target = findMethod(OnEnable.class);
      if (!target.isPresent()) {
        throw new NancyException("Method with annotation `@OnEnable` must be exists!");
      }

      enable = target.get();
      if (!enable.isAccessible()) {
        enable.setAccessible(true);
      }
    }

    try {
      if (enable.getParameterTypes().length == 1
          && enable.getParameterTypes()[0] == Nancy.class) {
        enable.invoke(instance, nancy);
      } else {
        enable.invoke(instance);
      }
    } catch (IllegalAccessException | InvocationTargetException ex) {
      throw new NancyException(ex);
    }
  }

  @Override
  public void disable() {
    if (disable == null) {
      Optional<Method> target = findMethod(OnDisable.class);
      if (!target.isPresent()) {
        throw new NancyException("Method with annotation `@OnDisable` must be exists!");
      }

      disable = target.get();
      if (!disable.isAccessible()) {
        disable.setAccessible(true);
      }
    }

    try {
      if (disable.getParameterCount() == 1
          && disable.getParameterTypes()[0] == Nancy.class) {
        disable.invoke(instance, nancy);
      } else {
        disable.invoke(instance);
      }
    } catch (IllegalAccessException | InvocationTargetException ex) {
      throw new NancyException(ex);
    }
  }

  @Override
  public void listen() {
    List<Method> methods = findMethods(Listener.class);
    if (methods.size() == 0) {
      return;
    }

    nancy.getJDA().addEventListener(new ModuleEventListener(instance, methods));
  }

  @Override
  public void inject() {
    List<Field> fields = findFields(Inject.class);
    if (fields.size() == 0) {
      return;
    }

    try {
      for (Field field : fields) {
        if (!field.isAccessible()) {
          field.setAccessible(true);
        }

        if (field.getType() == File.class) {
          field.set(instance, getDataFolder());
        }

        if (field.getType() == Manifest.class) {
          field.set(instance, getManifest());
        }

        if (field.getType() == Nancy.class) {
          field.set(instance, nancy);
        }
      }
    } catch (IllegalAccessException ex) {
      throw new NancyException(ex);
    }
  }

  @Override
  public Manifest getManifest() {
    if (manifest == null) {
      manifest = instance.getClass().getAnnotation(Manifest.class);
    }

    return manifest;
  }

  @Override
  public File getDataFolder() {
    return new File(ModulesManager.MODULES_DIRECTORY, getManifest().name());
  }

  private Optional<Method> findMethod(Class<? extends Annotation> annotation) {
    Validate.notNull(annotation);

    for (Method targetMethod : instance.getClass().getMethods()) {
      for (Annotation targetAnnotation : targetMethod.getAnnotations()) {
        if (targetAnnotation.annotationType() == annotation) {
          return Optional.of(targetMethod);
        }
      }
    }

    return Optional.empty();
  }

  private List<Method> findMethods(Class<? extends Annotation> annotation) {
    Validate.notNull(annotation);

    List<Method> result = new ArrayList<>();

    for (Method targetMethod : instance.getClass().getMethods()) {
      for (Annotation targetAnnotation : targetMethod.getAnnotations()) {
        if (targetAnnotation.annotationType() == annotation) {

          if (!targetMethod.isAccessible()) {
            targetMethod.setAccessible(true);
          }

          if (targetMethod.getParameterCount() == 0 || targetMethod.getParameterCount() == 1) {
            result.add(targetMethod);
          }
          break;
        }
      }
    }

    return result;
  }

  private List<Field> findFields(Class<? extends Annotation> annotation) {
    Validate.notNull(annotation);

    List<Field> result = new ArrayList<>();

    for (Field field : instance.getClass().getFields()) {
      if (!field.isAccessible()) {
        field.setAccessible(true);
      }

      if (field.isAnnotationPresent(annotation)) {
        result.add(field);
      }
    }

    return result;
  }

  private class ModuleEventListener implements EventListener {

    private final Object instance;
    private final List<Method> methods;

    public ModuleEventListener(Object instance, List<Method> methods) {
      Validate.notNull(instance);
      Validate.notNull(methods);

      this.instance = instance;
      this.methods = new ArrayList<>(methods);
    }

    @Override
    public void onEvent(Event event) {
      try {
        for (Method method : methods) {
          if (method.getParameterTypes()[0] == event.getClass()) {
            method.invoke(instance, event);
          }
        }
      } catch (IllegalAccessException | InvocationTargetException ex) {
        throw new NancyException(ex);
      }
    }

  }

}
