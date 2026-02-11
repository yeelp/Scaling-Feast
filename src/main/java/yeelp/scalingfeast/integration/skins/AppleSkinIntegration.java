package yeelp.scalingfeast.integration.skins;

import net.minecraft.client.Minecraft;
import squeek.appleskin.ModConfig;
import squeek.appleskin.client.HUDOverlayHandler;

public final class AppleSkinIntegration extends AbstractFruitSkinIntegration {
    public static AppleSkinIntegration instance;

    public static AppleSkinIntegration getInstance() {
        return instance == null ? instance = new AppleSkinIntegration() : instance;
    }

    public AppleSkinIntegration() {

    }

    @Override
    protected String getName() {
        return "AppleSkin";
    }

    @Override
    protected void callDrawExhaustion(float exhaustion, Minecraft mc, int left, int top, float alpha) {
        HUDOverlayHandler.drawExhaustionOverlay(exhaustion, mc, left, top, alpha);
    }

    @Override
    protected boolean shouldDrawExhaustion() {
        return ModConfig.SHOW_FOOD_EXHAUSTION_UNDERLAY;
    }
}
