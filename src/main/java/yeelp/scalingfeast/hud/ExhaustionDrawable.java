package yeelp.scalingfeast.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.FoodStats;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import squeek.applecore.api.AppleCoreAPI;
import yeelp.scalingfeast.ScalingFeast;
import yeelp.scalingfeast.integration.ModIntegrationKernel;
import yeelp.scalingfeast.integration.skins.AbstractFruitSkinIntegration;

@SideOnly(Side.CLIENT)
public final class ExhaustionDrawable extends AbstractDrawable {
	private boolean err = false;
	private AbstractFruitSkinIntegration currentIntegration;
	
	public ExhaustionDrawable() {
		super("sf_exhaustion_override", false, false);
		ModIntegrationKernel.getCurrentSkinIntegration().ifPresent(abstractFruitSkinIntegration -> this.currentIntegration = abstractFruitSkinIntegration);
		if(this.currentIntegration == null) {
			ScalingFeast.debug("No skin present, will not draw exhaustion");
			this.err = true;
		}
	}
	
	@Override
	public boolean shouldDraw(EntityPlayer player) {
		return !this.err;
	}

	@Override
	protected void draw(Minecraft mc, EntityPlayer player, FoodStats stats, int left, int top) {
		this.err = !this.currentIntegration.drawExhaustion(AppleCoreAPI.accessor.getExhaustion(player), mc, left, top, 0);
	}

}
