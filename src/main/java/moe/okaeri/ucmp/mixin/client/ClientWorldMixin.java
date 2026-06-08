package moe.okaeri.ucmp.mixin.client;

import moe.okaeri.ucmp.client.PendingPassengerPackets;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientWorld.class)
public class ClientWorldMixin {

	@Inject(method = "addEntity", at = @At("TAIL"))
	private void ucmp$retryPendingPassengerPackets(Entity entity, CallbackInfo ci) {
		PendingPassengerPackets.tryApplyAll();
	}

	@Inject(method = "disconnect", at = @At("HEAD"))
	private void ucmp$clearPendingPassengerPackets(CallbackInfo ci) {
		PendingPassengerPackets.clear();
	}
}