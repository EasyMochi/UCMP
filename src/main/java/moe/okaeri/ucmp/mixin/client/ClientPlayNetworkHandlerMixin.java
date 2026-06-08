package moe.okaeri.ucmp.mixin.client;

import moe.okaeri.ucmp.client.PendingPassengerPackets;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.EntityPassengersSetS2CPacket;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {

	@Inject(method = "onEntityPassengersSet", at = @At("HEAD"), cancellable = true)
	private void ucmp$delayUnknownPassengerPacket(EntityPassengersSetS2CPacket packet, CallbackInfo ci) {
		if (PendingPassengerPackets.canApply(packet)) return;

		PendingPassengerPackets.store(packet);
		ci.cancel();
	}
}