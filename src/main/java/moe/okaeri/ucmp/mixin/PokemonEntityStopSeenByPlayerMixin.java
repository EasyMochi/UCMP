package moe.okaeri.ucmp.mixin;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;

import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = PokemonEntity.class, remap = false)
public class PokemonEntityStopSeenByPlayerMixin {

	@Redirect(method = "onStoppedTrackingBy", at = @At(value = "INVOKE", target = "Lcom/cobblemon/mod/common/entity/pokemon/PokemonEntity;remove(Lnet/minecraft/entity/Entity$RemovalReason;)V"), require = 1, remap = false)
	private void ucmp$deferStopTrackingDiscard(PokemonEntity self, Entity.RemovalReason reason) {
		if (reason != Entity.RemovalReason.DISCARDED) {
			self.remove(reason);
			return;
		}

		if (self.isRemoved()) return;

		MinecraftServer server = self.getServer();
		if (server == null) {
			self.remove(reason);
			return;
		}

		server.execute(() -> {
			if (self.isRemoved()) return;
			self.remove(reason);
		});
	}
}