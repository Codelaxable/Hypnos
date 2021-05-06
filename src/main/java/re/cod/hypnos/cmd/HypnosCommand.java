package re.cod.hypnos.cmd;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class HypnosCommand {
  public static LiteralArgumentBuilder<ServerCommandSource> hypnosCommand() {
    LiteralArgumentBuilder<ServerCommandSource> builder = CommandManager.literal("hypnos");
    builder = ConfigCommand.build(builder);
    builder = ConfigSaveCommand.build(builder);
    return builder;
  }

  public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
    dispatcher.register(hypnosCommand());
  }
}
