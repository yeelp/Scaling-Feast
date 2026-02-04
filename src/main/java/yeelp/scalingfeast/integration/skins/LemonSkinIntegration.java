package yeelp.scalingfeast.integration.skins;

import yeelp.scalingfeast.ScalingFeast;

import java.lang.reflect.Field;

public final class LemonSkinIntegration extends AbstractFruitSkinIntegration {
    public static LemonSkinIntegration instance;
    private Object clientCat;
    private Field shouldDrawExhaustion;

    public static LemonSkinIntegration getInstance() {
        return instance == null ? instance = new LemonSkinIntegration() : instance;
    }

    public LemonSkinIntegration() {
        super("ua.myxazaur", "HUDOverlayRenderer");
    }

    @Override
    protected String getName() {
        return "LemonSkin";
    }

    @Override
    protected boolean doSpecificPreInit() throws ClassNotFoundException, NoSuchFieldException {
        try {
            this.clientCat = this.getClass(MODCONFIG).getDeclaredField("CLIENT").get(null);
            this.shouldDrawExhaustion = this.getClass(MODCONFIG + "$ClientCategory").getDeclaredField(SHOW_EXHAUSTION_UNDERLAY);
            return true;
        } catch(IllegalAccessException e) {
            ScalingFeast.fatal("Unable to retrieve LemonSkin config!");
            throw new RuntimeException(e);
        }
    }

    @Override
    protected boolean shouldDrawExhaustion() throws IllegalAccessException {
        return this.shouldDrawExhaustion.getBoolean(this.clientCat);
    }
}
