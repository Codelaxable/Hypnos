package re.cod.hypnos;

import net.minecraft.network.MessageType;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import org.apache.commons.lang3.text.StrSubstitutor;
import re.cod.hypnos.config.Config;

import java.util.HashMap;
import java.util.Map;

public class HypnosBroadcaster {
    Config.Data cfg;

    HypnosBroadcaster(Config.Data config) {
        this.cfg = config;
    }

    public void sendNotEnoughPlayersSleepingMessage(ServerPlayerEntity player, int sleeping, int required) {
        ServerWorld serverWorld = player.getServerWorld();
        Map<String, String> stringValues = new HashMap<>();
        stringValues.put("player", player.getDisplayName().asString());
        stringValues.put("sleeping", Integer.toString(sleeping));
        stringValues.put("required", Integer.toString(required));

        String msg = StrSubstitutor.replace(cfg.notEnoughPlayerMessage, stringValues, "{", "}");
        Text text = new LiteralText(msg);
        PlayerManager playerManager = serverWorld.getServer().getPlayerManager();
        playerManager.broadcastChatMessage(text, MessageType.CHAT, Util.NIL_UUID);
    }

    public void sendSkipNightMessage(ServerWorld serverWorld, int sleeping, int required) {
        Map<String, String> stringValues = new HashMap<>();
        stringValues.put("sleeping", Integer.toString(sleeping));
        stringValues.put("required", Integer.toString(required));

        String msg = StrSubstitutor.replace(cfg.nightSkipMessage, stringValues, "{", "}");
        Text text = new LiteralText(msg);
        PlayerManager playerManager = serverWorld.getServer().getPlayerManager();
        playerManager.broadcastChatMessage(text, MessageType.CHAT, Util.NIL_UUID);
    }

    public void sendWakeUpMessage(ServerPlayerEntity player, int sleeping, int required) {
        ServerWorld serverWorld = player.getServerWorld();
        Map<String, String> stringValues = new HashMap<>();
        stringValues.put("player", player.getDisplayName().asString());
        stringValues.put("sleeping", Integer.toString(sleeping));
        stringValues.put("required", Integer.toString(required));

        String msg = StrSubstitutor.replace(cfg.wakeUpMessage, stringValues, "{", "}");
        Text text = new LiteralText(msg);
        PlayerManager playerManager = serverWorld.getServer().getPlayerManager();
        playerManager.broadcastChatMessage(text, MessageType.CHAT, Util.NIL_UUID);
    }
}
