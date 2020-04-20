package yeelp.scalingfeast.helpers;

/**
 * Exception to indicate that a module was not loaded, but was expected. 
 * @author Yeelp
 *
 */
public class ModuleNotLoadedException extends Exception 
{
	/**
	 * Build a new ModuleNotLoadedException
	 * @param msg the error message associated with this ModuleNotLoadedException.
	 */
	public ModuleNotLoadedException(String msg)
	{
		super(msg);
	}
}
