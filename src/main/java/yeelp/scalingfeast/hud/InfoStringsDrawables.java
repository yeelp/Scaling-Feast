package yeelp.scalingfeast.hud;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Tuple;
import squeek.applecore.api.AppleCoreAPI;
import yeelp.scalingfeast.ModConsts;
import yeelp.scalingfeast.config.ModConfig;
import yeelp.scalingfeast.config.ModConfig.HUDCategory.InfoStyle;
import yeelp.scalingfeast.util.HUDUtils;

public enum InfoStringsDrawables implements IDrawable {
	SIMPLE {
		@Override
		public void drawString(Minecraft mc, EntityPlayer player, int left, int top, int hunger, int max) {
			mc.fontRenderer.drawStringWithShadow(String.format("x%d/%d", (int) Math.ceil((float) hunger / ModConsts.VANILLA_MAX_HUNGER), (int) Math.ceil((float) max / ModConsts.VANILLA_MAX_HUNGER)), (left + 1) / 0.5f, (top + 4.5f) / 0.5f, getColour(hunger, max));
		}
	},
	ADVANCED {
		@Override
		public void drawString(Minecraft mc, EntityPlayer player, int left, int top, int hunger, int max) {
			float sat = player.getFoodStats().getSaturationLevel();
			float y = (top + 4.5f) / 0.5f;
			boolean drawingSat = false;
			for(Iterable<Tuple<String, Integer>> it : HUDUtils.getAdvancedInfoString(player)) {
				float x = (left + 1) / 0.5f;
				for(Tuple<String, Integer> t : it) {
					int colour;
					if(t.getSecond() == null) {
						if(drawingSat) {
							colour = DrawUtils.getSaturationTextColour(sat);
						}
						else {
							colour = getColour(hunger, max);
						}
					}
					else {
						colour = t.getSecond().intValue();
					}
					mc.fontRenderer.drawStringWithShadow(t.getFirst(), x, y, colour);
					x += mc.fontRenderer.getStringWidth(t.getFirst());
				}
				if(!ModConfig.hud.drawSaturation) {
					break;
				}
				y = top / 0.5f;
				drawingSat = true;
			}
		}
	};
	
	private final String name;
	
	private InfoStringsDrawables() {
		this.name = String.format("sf_info_%s", this.name().toLowerCase());
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
		int hunger = player.getFoodStats().getFoodLevel();
		int max = AppleCoreAPI.accessor.getMaxHunger(player);
		this.drawString(mc, player, left, top, hunger, max);
		GL11.glPopMatrix();
	}
	
	protected abstract void drawString(Minecraft mc, EntityPlayer player, int left, int top, int hunger, int max);
	
	public static IDrawable getInfoDrawable() {
		return ModConfig.hud.infoStyle == InfoStyle.SIMPLE ? SIMPLE : ADVANCED;
	}
	
	protected static int getColour(int hunger, int max) {
		if(hunger == max) {
			return 0x55ff55;
		}
		else if(hunger > 0.5 * max) {
			return 0xffffff;
		}
		else if(hunger > 0.25 * max) {
			return 0xffff55;
		}
		else if(hunger > 0.1 * max) {
			return 0xffaa00;
		}
		else if(hunger > 0) {
			return 0xff5555;
		}
		else {
			return 0xaa0000;
		}
	}
}
