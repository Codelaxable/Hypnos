package re.cod.hypnos.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import re.cod.hypnos.HypnosManager;

@Mixin(ServerPlayerEntity.class)
abstract class ServerPlayerEntityMixin extends PlayerEntity {
    public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile profile) {
        super(world, pos, yaw, profile);
    }

    @Inject(method = "sleep", at = @At("RETURN"))
    public void onSleep(BlockPos pos, CallbackInfo info) {
        ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) (PlayerEntity) this;
        HypnosManager.getHypnosManager().trySkipNight(serverPlayerEntity);
    }

    @Inject(method = "wakeUp", at = @At("RETURN"))
    public void onWakeUp(boolean bl, boolean updateSleepingPlayers, CallbackInfo info) {
        ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) (PlayerEntity) this;
        HypnosManager.getHypnosManager().onWakeUp(serverPlayerEntity);
    }
}
