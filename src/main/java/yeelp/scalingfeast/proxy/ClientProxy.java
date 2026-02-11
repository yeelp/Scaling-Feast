package yeelp.scalingfeast.proxy;

import com.google.common.base.Functions;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import yeelp.scalingfeast.ModConsts;
import yeelp.scalingfeast.ScalingFeast;
import yeelp.scalingfeast.handlers.HUDOverlayHandler;
import yeelp.scalingfeast.handlers.Handler;
import yeelp.scalingfeast.init.SFItems;
import yeelp.scalingfeast.screen.GuiFingerprintViolationWarning;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

@SuppressWarnings("unused")
public final class ClientProxy extends Proxy {
	public static final String FINGERPRINT_FILE = "sf_ignorefingerprint.txt";

	@Override
	public void preInit() {
		super.preInit();
		SFItems.registerRenders();
	}

	@Override
	public void init() {
		super.init();
		new HUDOverlayHandler().register();
	}

	@SuppressWarnings("EmptyMethod")
    @Override
	public void postInit() {
		super.postInit();
	}

	@Override
	public void handleFingerprintViolation() {
		File f = new File(ScalingFeast.getModConfigDirectory(), FINGERPRINT_FILE);
		if(f.exists()) {
			ScalingFeast.debug("Found indicator file to ignore fingerprint warnings, checking if version matches...");
			try(BufferedReader reader = new BufferedReader(new FileReader(f))) {
				String line = reader.readLine();
				if(line.contains(ModConsts.MOD_VERSION)) {
					ScalingFeast.debug("Version matches, ignoring warnings");
					return;
				}
			}
			catch(IOException e) {
				Arrays.stream(e.getStackTrace()).map(Functions.toStringFunction()).forEach(ScalingFeast::fatal);
			}
		}
		new Handler() {
			private boolean openedOnce = false;

			@SubscribeEvent
			public void onGuiOpen(GuiOpenEvent evt) {
				if(!this.openedOnce && evt.getGui() instanceof GuiMainMenu) {
					this.openedOnce = true;
					evt.setGui(new GuiFingerprintViolationWarning(evt.getGui()));
				}
			}
		}.register();
	}
}
