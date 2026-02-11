package yeelp.scalingfeast.integration.skins;

import net.minecraft.client.Minecraft;
import ua.myxazaur.lemonskin.ModConfig;
import ua.myxazaur.lemonskin.client.HUDOverlayRenderer;

public final class LemonSkinIntegration extends AbstractFruitSkinIntegration {
    public static LemonSkinIntegration instance;

    public static LemonSkinIntegration getInstance() {
        return instance == null ? instance = new LemonSkinIntegration() : instance;
    }

    public LemonSkinIntegration() {

    }

    @Override
    protected String getName() {
        return "LemonSkin";
    }

    @Override
    protected boolean shouldDrawExhaustion() {
        return ModConfig.CLIENT.SHOW_FOOD_EXHAUSTION_UNDERLAY;
    }

    @Override
    protected void callDrawExhaustion(float exhaustion, Minecraft mc, int left, int top, float alpha) {
        HUDOverlayRenderer.drawExhaustionOverlay(exhaustion, mc, left, top, alpha);
    }
}
