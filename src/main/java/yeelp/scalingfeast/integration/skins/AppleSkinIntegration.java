package yeelp.scalingfeast.integration.skins;

import java.lang.reflect.Field;

public final class AppleSkinIntegration extends AbstractFruitSkinIntegration {
    public static AppleSkinIntegration instance;
    private Field shouldDrawExhaustion;

    public static AppleSkinIntegration getInstance() {
        return instance == null ? instance = new AppleSkinIntegration() : instance;
    }

    public AppleSkinIntegration() {
        super("squeek", "HUDOverlayHandler");
    }

    @Override
    protected String getName() {
        return "AppleSkin";
    }

    @Override
    protected boolean doSpecificPreInit() throws ClassNotFoundException, NoSuchFieldException {
        this.shouldDrawExhaustion = this.getClass(MODCONFIG).getDeclaredField(SHOW_EXHAUSTION_UNDERLAY);
        return true;
    }

    @Override
    protected boolean shouldDrawExhaustion() throws IllegalAccessException {
        return this.shouldDrawExhaustion.getBoolean(null);
    }
}
