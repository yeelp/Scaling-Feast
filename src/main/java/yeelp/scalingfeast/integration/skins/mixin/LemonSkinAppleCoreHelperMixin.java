package yeelp.scalingfeast.integration.skins.mixin;

import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ua.myxazaur.lemonskin.helpers.AppleCoreHelper;
import ua.myxazaur.lemonskin.helpers.FoodHelper.BasicFoodValues;
import yeelp.scalingfeast.config.ModConfig;
import yeelp.scalingfeast.config.ModConfig.HUDCategory.DisplayStyle;

@Mixin(value = AppleCoreHelper.class, remap = false)
public abstract class LemonSkinAppleCoreHelperMixin {

    @Inject(method = "getFoodValuesForDisplay(Lua/myxazaur/lemonskin/helpers/FoodHelper$BasicFoodValues;Lnet/minecraft/entity/player/EntityPlayer;)Lua/myxazaur/lemonskin/helpers/FoodHelper$BasicFoodValues;", at = @At("HEAD"), cancellable = true)
    private static void hijackFoodValueDisplay(BasicFoodValues fVals, EntityPlayer player, CallbackInfoReturnable<BasicFoodValues> info) {
        if(ModConfig.hud.style == DisplayStyle.OVERLAY) {
            info.setReturnValue(fVals);
        }
    }
}
