package re.cod.hypnos.cmd;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import re.cod.hypnos.cmd.config.ConfigField;
import re.cod.hypnos.config.Config;
import java.util.StringJoiner;

import static net.minecraft.server.command.CommandManager.literal;
import static net.minecraft.server.command.CommandManager.argument;


public class ConfigCommand implements Command<ServerCommandSource> {
  Config cfg;
  ConfigField[] configFields;

  ConfigCommand(ConfigField[] configFields) {
    cfg = Config.get();
    this.configFields = configFields;
  }

  @Override
  public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
    StringJoiner joiner = new StringJoiner("\n");
    for (ConfigField field : configFields) {
      try {
        joiner.add(String.format("%s = %s", field.name, field.get().toString()));
      } catch (ConfigField.InvalidConfigFieldException e) {
        e.printStackTrace();
        context.getSource().sendError(new LiteralText(e.getMessage()));
        return 0;
      }
    }
    context.getSource().sendFeedback(new LiteralText(joiner.toString()), false);
    return SINGLE_SUCCESS;
  }

  public static LiteralArgumentBuilder<ServerCommandSource> build(LiteralArgumentBuilder<ServerCommandSource> builder) {
    ConfigField[] configFields = ConfigField.listConfigFields();
    LiteralArgumentBuilder<ServerCommandSource> configCmd = literal("config")
        .requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2))
        .executes(new ConfigCommand(configFields));
    for (ConfigField configField : configFields) {
      configCmd = buildField(configCmd, configField);
    }
    return builder.then(configCmd);
  }

  public static LiteralArgumentBuilder<ServerCommandSource> buildField(LiteralArgumentBuilder<ServerCommandSource> builder, ConfigField field) {
    return builder
        .then(literal(field.name).executes(context -> {
          try {
            Object cfgValue = field.get();
            context.getSource().sendFeedback(new LiteralText(String.format("%s = %s", field.name, cfgValue.toString())), false);
          } catch (ConfigField.InvalidConfigFieldException e) {
            e.printStackTrace();
            context.getSource().sendError(new LiteralText(e.getMessage()));
            return 0;
          }
          return SINGLE_SUCCESS;
        }).then(argument(field.name, field.argumentType).executes(context -> {
          try {
            Object arg = context.getArgument(field.name, field.type);
            field.set(arg);
          } catch (ConfigField.InvalidConfigFieldException e) {
            e.printStackTrace();
            context.getSource().sendError(new LiteralText(e.getMessage()));
            return 0;
          }
          return SINGLE_SUCCESS;
        })));
  }
}
