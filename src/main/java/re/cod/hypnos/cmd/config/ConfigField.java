package re.cod.hypnos.cmd.config;

import com.mojang.brigadier.arguments.ArgumentType;
import re.cod.hypnos.config.Config;

import static com.mojang.brigadier.arguments.BoolArgumentType.bool;
import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;
import static com.mojang.brigadier.arguments.StringArgumentType.greedyString;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ConfigField {
  public static class InvalidConfigFieldException extends Exception {
    public InvalidConfigFieldException(String message) {
      super(message);
    }
  }

  private static final Map<Class<?>, ArgumentType<?>> typeToArgumentMap = new HashMap<>();
  static {
    typeToArgumentMap.put(int.class, integer());
    typeToArgumentMap.put(String.class, greedyString());
    typeToArgumentMap.put(boolean.class, bool());
  }

  public final String name;
  public final ArgumentType<?> argumentType;
  public final Class<?> type;

  ConfigField(String name, Class<?> type) {
    this.name = name;
    this.argumentType = typeToArgumentMap.get(type);
    this.type = type;
  }

  public Object get() throws InvalidConfigFieldException {
    try {
      Config cfg = Config.get();
      Field field = cfg.data.getClass().getDeclaredField(this.name);
      return field.get(cfg.data);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new InvalidConfigFieldException(e.getMessage());
    }
  }

  public void set(Object value) throws InvalidConfigFieldException {
    try {
      Config cfg = Config.get();
      Field field = cfg.data.getClass().getDeclaredField(this.name);
      field.set(cfg.data, value);
    } catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException e) {
      throw new InvalidConfigFieldException(e.getMessage());
    }
  }

  public static ConfigField[] listConfigFields() {
    Field[] fields = Config.get().data.getClass().getDeclaredFields();
    ConfigField[] configFields = new ConfigField[fields.length];

    for (int i = 0; i < fields.length; i++) {
      Field field = fields[i];
      configFields[i] = new ConfigField(field.getName(), field.getType());
    }
    return configFields;
  }
}
