package yeelp.scalingfeast.integration.crafttweaker;

import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenSetter;
import yeelp.scalingfeast.util.FoodCapModifier;
import yeelp.scalingfeast.util.FoodCapModifier.Modifier;

@ZenClass("mods.scalingfeast.SFModifier")
@ZenRegister
public class SFModifier
{
	private Modifier mod;
	private float value;
	
	public SFModifier(Modifier m)
	{
		this.mod = m;
		this.value = m.getValue();
	}
	
	@ZenGetter("op")
	public byte getOperation()
	{
		return (byte) mod.getOp().ordinal();
	}
	
	@ZenGetter("value")
	public float getValue()
	{
		return this.value;
	}
	
	@ZenSetter("value")
	public void setValue(float value)
	{
		this.value = value;
	}
}
