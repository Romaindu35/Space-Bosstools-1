package net.mrscauthd.boss_tools.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.PointOfView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PointOfView.class)
public class PointOfViewMixin {

    @Inject(at = @At("HEAD"), method = "Lnet/minecraft/client/settings/PointOfView;func_243194_c()Lnet/minecraft/client/settings/PointOfView;")
    public void func_243194_c(CallbackInfoReturnable<PointOfView> cir) {

        if (Minecraft.getInstance().player.getPersistentData().contains("rocketsit")) {
            boolean rocketsit = Minecraft.getInstance().player.getPersistentData().getBoolean("rocketsit");
            if (rocketsit) {

            }
        }

    }

}
