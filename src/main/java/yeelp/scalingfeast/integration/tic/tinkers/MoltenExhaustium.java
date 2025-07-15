package yeelp.scalingfeast.integration.tic.tinkers;

import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.fluid.FluidMolten;
import slimeknights.tconstruct.smeltery.block.BlockMolten;
import yeelp.scalingfeast.ModConsts;
import yeelp.scalingfeast.integration.tic.TiCConsts;

public final class MoltenExhaustium extends FluidMolten {

	public MoltenExhaustium() {
		super("exhaustion_fluid", TiCConsts.EXHAUSTING_COLOUR, FluidMolten.ICON_MetalStill, FluidMolten.ICON_MetalFlowing);
		this.setUnlocalizedName(Util.prefix(this.fluidName));
		this.setTemperature(500);
		this.setLuminosity(3);
		this.setViscosity(50000);
		this.setDensity(8000);
	}

	BlockMolten getBlockMolten() {
		BlockMolten molten = new BlockMolten(this);
		molten.setTranslationKey("molten_" + this.getName());
		molten.setRegistryName(ModConsts.MOD_ID, "molten_" + this.getName());
		return molten;
	}
}
//setUnlocalizedName\(([^U])
//setTranslationKey\(\1