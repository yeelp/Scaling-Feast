package yeelp.scalingfeast.hud;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import yeelp.scalingfeast.ModConsts;
import yeelp.scalingfeast.api.ScalingFeastAPI;
import yeelp.scalingfeast.config.ModConfig;
import yeelp.scalingfeast.config.ModConfig.HUDCategory.InfoStyle;

public enum BloatedInfoStringDrawables implements IDrawable {
	SIMPLE {
		@Override
		protected String getString(short bloat) {
			return "x" + (bloat / ModConsts.VANILLA_MAX_HUNGER + 1);
		}
	},
	ADVANCED {
		@Override
		protected String getString(short bloat) {
			return String.valueOf(bloat);
		}
	};

	private final String name;
	
	BloatedInfoStringDrawables() {
		this.name = String.format("sf_bloat_info_%s", this.name().toLowerCase());
	}
	
	@Override
	public String getSectionName() {
		return this.name;
	}

	@Override
	public boolean requiresBlend() {
		return false;
	}

	@Override
	public boolean requiresAlpha() {
		return false;
	}

	@Override
	public boolean shouldDraw(EntityPlayer player) {
		return true;
	}

	@Override
	public void draw(Minecraft mc, EntityPlayer player, int left, int top) {
		GL11.glPushMatrix();
		GL11.glTranslatef(ModConfig.hud.infoXOffset, ModConfig.hud.infoYOffset, 0);
		GL11.glScalef(0.5f, 0.5f, 1.0f);
		short bloat = ScalingFeastAPI.accessor.getSFFoodStats(player).getBloatedHungerAmount();
		mc.fontRenderer.drawStringWithShadow(this.getString(bloat), (left + 1) / 0.5f, (top + 4.5f) / 0.5f, 0xffffff);
		GL11.glPopMatrix();
	}
	
	protected abstract String getString(short bloat);
	
	public static IDrawable getInfoDrawable() {
		return ModConfig.hud.infoStyle == InfoStyle.SIMPLE ? SIMPLE : ADVANCED;
	}
	
}
