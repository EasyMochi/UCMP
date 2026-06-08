package moe.okaeri.ucmp.mixin.client;

import com.cobblemon.mod.common.client.entity.PokemonClientDelegate;
import com.cobblemon.mod.common.client.render.models.blockbench.PosableModel;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = PokemonClientDelegate.class, remap = false)
public class PokemonClientDelegateMixin {

	@Inject(method = "positionRider", at = @At("HEAD"), remap = false)
	private void ucmp$refreshMissingRidingLocator(Entity passenger, Entity.PositionUpdater positionUpdater, CallbackInfo ci) {
		PokemonClientDelegate self = (PokemonClientDelegate) (Object) this;
		PokemonEntity mon = self.getEntity();

		if (mon == null) return;
		if (!mon.hasPassenger(passenger)) return;

		if (passenger == MinecraftClient.getInstance().player) return;

		final String locatorName;
		try {
			locatorName = self.getSeatLocator(passenger);
		} catch (IllegalArgumentException ignored) {
			return;
		}

		if (self.getLocatorStates().containsKey(locatorName)) return;

		PosableModel model = self.getCurrentModel();
		if (model == null) return;

		model.updateLocators(mon, self);
	}
}