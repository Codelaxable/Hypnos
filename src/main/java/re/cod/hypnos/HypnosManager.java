package re.cod.hypnos;

import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.GameRules;
import re.cod.hypnos.config.Config;

import java.util.stream.Collectors;

public class HypnosManager {
    Config.Data cfg;
    HypnosBroadcaster broadcaster;

    HypnosManager() {
        cfg = Config.get().data;
        this.broadcaster = new HypnosBroadcaster(cfg);
    }

    public void skipNight(ServerWorld serverWorld) {
        if (serverWorld.getGameRules().getBoolean(GameRules.DO_DAYLIGHT_CYCLE)) {
            long l = serverWorld.getLevelProperties().getTimeOfDay() + 24000L;
            serverWorld.setTimeOfDay(l - l % 24000L);
        }

        serverWorld.getPlayers().stream().filter(LivingEntity::isSleeping).collect(Collectors.toList()).forEach((player) -> {
            player.wakeUp(false, false);
        });
        if (serverWorld.getGameRules().getBoolean(GameRules.DO_WEATHER_CYCLE)) {
            serverWorld.setWeather(0, 0, false, false);
        }
    }

    public void trySkipNight(ServerPlayerEntity player) {
        ServerWorld serverWorld = player.getServerWorld();
        if (cfg.ignoreSleepDuringDay && serverWorld.isDay()) {
            return;
        }
        int sleeping = (int) serverWorld.getPlayers().stream().filter(LivingEntity::isSleeping).count();
        int players = serverWorld.getPlayers().size();
        int required = players * cfg.playerPercentage / 100;
        required = required > 0 ? required : 1;
        if (sleeping >= required) {
            broadcaster.sendSkipNightMessage(serverWorld, sleeping, required);
            this.skipNight(serverWorld);
        } else {
            broadcaster.sendNotEnoughPlayersSleepingMessage(player, sleeping, required);
        }
    }

    public void onWakeUp(ServerPlayerEntity player) {
        if (!cfg.enableWakeUp)
            return;
        ServerWorld serverWorld = player.getServerWorld();
        int sleeping = (int) serverWorld.getPlayers().stream().filter(LivingEntity::isSleeping).count();
        int players = serverWorld.getPlayers().size();
        int required = players * cfg.playerPercentage / 100;
        broadcaster.sendWakeUpMessage(player, sleeping, required);
    }

    static private final HypnosManager hypnosManager = new HypnosManager();

    static public HypnosManager getHypnosManager() {
        return hypnosManager;
    }
}
