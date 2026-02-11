package yeelp.scalingfeast.integration.skins;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import yeelp.scalingfeast.ModConsts.IntegrationIds;
import yeelp.scalingfeast.config.ModConfig;
import yeelp.scalingfeast.handlers.Handler;
import yeelp.scalingfeast.integration.IIntegratable;

/**
 * Abstract integration for mods that draw exhaustion. Typically called [some fruit]Skin, hence the name
 * @author Yeelp
 */
public abstract class AbstractFruitSkinIntegration implements IIntegratable {

    private static boolean handlerRegistered = false;

    protected AbstractFruitSkinIntegration() {

    }

    /**
     * Get the name of the fruit skin mod
     *
     * @return the name of the mod. Typically, also the name of the mod's main class.
     */
    protected abstract String getName();

    @Override
    public final boolean preIntegrate(FMLPreInitializationEvent evt) {
        return true;
    }

    @Override
    public boolean integrate(FMLInitializationEvent evt) {
        return true;
    }

    @Override
    public final boolean postIntegrate(FMLPostInitializationEvent evt) {
        if(!handlerRegistered && !Loader.isModLoaded(IntegrationIds.FERMIUM_ID)) {
            new Handler() {
                @SubscribeEvent
                public void onPlayerJoin(PlayerLoggedInEvent evt) {
                    if(ModConfig.compat.suppressSkinWarnings) {
                        return;
                    }
                    evt.player.sendMessage(new TextComponentString(String.format("[Scaling Feast] You may notice inconsistencies with %s's hunger tooltips showing incorrect amounts. You can fix this problem by installing FermiumBooter.", AbstractFruitSkinIntegration.this.getName())));
                    evt.player.sendMessage(new TextComponentString("[Scaling Feast] You can turn off this message in Scaling Feast's config under compat -> suppressSkinWarnings"));
                }
            }.register();
            handlerRegistered = true;
        }
        return true;
    }

    /**
     * Draw the exhaustion underlay as this mod would do normally.
     *
     * @param exhaustion the player's current exhaustion
     * @param mc         the Minecraft instance
     * @param left       x coord to draw at
     * @param top        y coord to draw at
     * @param alpha      transparency
     */
    protected abstract void callDrawExhaustion(float exhaustion, Minecraft mc, int left, int top, float alpha);

    /**
     * Check if the config allows the exhaustion to be drawn
     * @return true if exhaustion should be drawn.
     */
    protected abstract boolean shouldDrawExhaustion();

    /**
     * Draw the exhaustion underlay as this mod would do normally if enabled in the config.
     *
     * @param exhaustion the player's current exhaustion
     * @param mc         the Minecraft instance
     * @param left       x coord to draw at
     * @param top        y coord to draw at
     * @param alpha      transparency
     */
    public final void drawExhaustion(float exhaustion, Minecraft mc, int left, int top, float alpha) {
        if(this.shouldDrawExhaustion()) {
            this.callDrawExhaustion(exhaustion, mc, left, top, alpha);
        }
    }
}
