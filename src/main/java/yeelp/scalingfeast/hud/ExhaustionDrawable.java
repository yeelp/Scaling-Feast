package yeelp.scalingfeast.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.FoodStats;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import squeek.applecore.api.AppleCoreAPI;
import yeelp.scalingfeast.helpers.AppleSkinHelper;

@SideOnly(Side.CLIENT)
public final class ExhaustionDrawable extends AbstractDrawable {
	private boolean appleskinErr = false;
	
	public ExhaustionDrawable() {
		super("sf_appleskin_exhaustion_override", false, false);
	}
	
	@Override
	public boolean shouldDraw(EntityPlayer player) {
		return AppleSkinHelper.isLoaded() && !this.appleskinErr;
	}

	@Override
	protected void draw(Minecraft mc, EntityPlayer player, FoodStats stats, int left, int top) {
		this.appleskinErr = !AppleSkinHelper.drawExhaustion(AppleCoreAPI.accessor.getExhaustion(player), mc, left, top, 0);
	}

}
