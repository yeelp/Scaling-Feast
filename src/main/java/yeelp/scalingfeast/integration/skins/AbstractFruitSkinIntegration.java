package yeelp.scalingfeast.integration.skins;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import yeelp.scalingfeast.ScalingFeast;
import yeelp.scalingfeast.integration.IIntegratable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Abstract integration for mods that draw exhaustion. Typically called [some fruit]Skin, hence the name
 * @author Yeelp
 */
public abstract class AbstractFruitSkinIntegration implements IIntegratable {

    protected static final String MODCONFIG = "ModConfig";
    protected static final String SHOW_EXHAUSTION_UNDERLAY = "SHOW_FOOD_EXHAUSTION_UNDERLAY";
    private final String packageRoot, overlayClassName;
    protected Field hasAppleCore;
    protected Method drawExhaustion;
    private boolean loaded;

    protected AbstractFruitSkinIntegration(String packageRoot, String overlayClassName) {
        this.packageRoot = packageRoot + "." + this.getName().toLowerCase();
        this.overlayClassName = overlayClassName;
    }

    /**
     * Get the name of the fruit skin mod
     *
     * @return the name of the mod. Typically, also the name of the mod's main class.
     */
    protected abstract String getName();

    @Override
    public final boolean preIntegrate(FMLPreInitializationEvent evt) {
        try {
            this.drawExhaustion = this.getClass(this.overlayClassName, "client").getDeclaredMethod("drawExhaustionOverlay", float.class, Minecraft.class, int.class, int.class, float.class);
            this.hasAppleCore = this.getClass(this.getName()).getDeclaredField("hasAppleCore");
            return loaded = this.doSpecificPreInit();
        } catch(ClassNotFoundException | NoSuchFieldException | NoSuchMethodException e) {
            ScalingFeast.fatal("Could not load integration for " + this.getName());
            throw new RuntimeException(e);
        }
    }

    /**
     * Do specific preinit tasks unique to each integration.
     * @return true if the specific tasks completed successfully.
     */
    protected abstract boolean doSpecificPreInit() throws ClassNotFoundException, NoSuchFieldException;

    /**
     * Should the integration draw exhaustion?
     * @return true if it should.
     */
    protected abstract boolean shouldDrawExhaustion() throws IllegalAccessException;

    @Override
    public boolean integrate(FMLInitializationEvent evt) {
        return true;
    }

    @Override
    public boolean postIntegrate(FMLPostInitializationEvent evt) {
        return true;
    }

    /**
     * Has this integration loaded correctly? Some functions should make sure the integration has loaded correctly before continuing operations.
     *
     * @return true if loaded, false if something went wrong.
     */
    public final boolean isLoaded() {
        return this.loaded;
    }

    /**
     * Ensure this integration recognizes that AppleCore is present. Likely not needed, but a holdover from older AppleSkin integration.
     */
    private void enableAppleCoreRecognition() {
        try {
            this.hasAppleCore.setBoolean(null, true);
        } catch(IllegalAccessException e) {
            ScalingFeast.err("Unable to trigger AppleCore recognition for "+this.getName());
            throw new RuntimeException(e);
        }
    }

    /**
     * Draw the exhaustion underlay as this mod would do normally.
     *
     * @param exhaustion the player's current exhaustion
     * @param mc         the Minecraft instance
     * @param left       x coord to draw at
     * @param top        y coord to draw at
     * @param alpha      transparency
     * @return true if drawn successfully, false if not. If something went wrong and the exhaustion wasn't drawn successfully, the problem will continue to persist, so it is better to avoid calling this method again to save resources of invoking a method through reflection.
     */
    public final boolean drawExhaustion(float exhaustion, Minecraft mc, int left, int top, float alpha) {
        try {
            this.enableAppleCoreRecognition();
            if(this.shouldDrawExhaustion()) {
                this.drawExhaustion.invoke(null, exhaustion, mc, left, top, alpha);
            }
            return true;
        } catch(IllegalAccessException | InvocationTargetException e) {
            ScalingFeast.err("Scaling Feast couldn't use " + this.getName() + " to draw exhaustion! Something went wrong:");
            ScalingFeast.err(Arrays.toString(e.getStackTrace()));
            return false;
        }
    }

    /**
     * Get a class from the integrated mod.
     * @param name the name of the class
     * @param subpackages any packages the class is contained within outside the package root passed into the constructor. Will navigate the packages in the order listed.
     * @return the Class object specified.
     * @throws ClassNotFoundException If the class wasn't found.
     */
    protected Class<?> getClass(String name, String... subpackages) throws ClassNotFoundException {
        StringBuilder sb = new StringBuilder(this.packageRoot);
        for(String s : subpackages) {
            sb.append(".").append(s);
        }
        sb.append(".").append(name);
        return Class.forName(sb.toString());
    }
}
