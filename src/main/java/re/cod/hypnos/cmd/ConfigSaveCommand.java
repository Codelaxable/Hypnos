package re.cod.hypnos.cmd;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;
import re.cod.hypnos.config.Config;

import static net.minecraft.server.command.CommandManager.literal;


public class ConfigSaveCommand implements Command<ServerCommandSource> {
  @Override
  public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
    Config.get().saveConfig();
    return SINGLE_SUCCESS;
  }

  public static LiteralArgumentBuilder<ServerCommandSource> build(LiteralArgumentBuilder<ServerCommandSource> builder) {
    return builder
        .then(
            literal("config-save")
                .requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2))
                .executes(new ConfigSaveCommand()));
  }
}
