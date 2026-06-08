package moe.okaeri.ucmp.mixin;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(StatusEffectInstance.class)
public abstract class StatusEffectInstanceMixin {

	@Unique
	private static final String UCMP_COBBLEMON_SHOULDER_EFFECT_INSTANCE = "com.cobblemon.mod.common.pokemon.effects.PotionBaseEffect$ShoulderStatusEffectInstance";

	@Inject(method = "writeNbt()Lnet/minecraft/nbt/NbtElement;", at = @At("HEAD"), cancellable = true)
	private void ucmp$doNotPersistCobblemonShoulderEffects(CallbackInfoReturnable<NbtElement> cir) {
		if (UCMP_COBBLEMON_SHOULDER_EFFECT_INSTANCE.equals(((Object) this).getClass().getName())) {
			NbtCompound nbt = new NbtCompound();
			nbt.putString("id", "cobblemon:invalid_shoulder_effect");
			cir.setReturnValue(nbt);
		}
	}
}