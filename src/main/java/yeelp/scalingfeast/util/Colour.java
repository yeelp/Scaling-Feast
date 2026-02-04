package yeelp.scalingfeast.util;

/**
 * A Colour container to be used when drawing hunger shanks. We create our own
 * class to get the behaviour we want
 * 
 * @author Yeelp
 *
 */
public class Colour {
	private final byte r;
	private final byte g;
	private final byte b;

	/**
	 * Build a Colour using a hex string. This constructor assumes a String of just
	 * hex characters. i.e., a String like "0xffffff", while a valid hex string for
	 * Java, will NOT work for this. Instead, pass the String "ffffff" (that is,
	 * omit the "0x" part of the String)
	 * 
	 * @param hex Hex string. Must be of length 6.
	 * @throws IllegalArgumentException if the String doesn't match the format.
	 */
	public Colour(String hex) throws IllegalArgumentException {
		if(hex.length() != 6) {
			throw new IllegalArgumentException(hex + " is not the right size! Should be length 6 exactly!");
		}
		try {
			this.r = Integer.valueOf(Integer.parseInt(hex.substring(0, 2), 16)).byteValue();
			this.g = Integer.valueOf(Integer.parseInt(hex.substring(2, 4), 16)).byteValue();
			this.b = Integer.valueOf(Integer.parseInt(hex.substring(4, 6), 16)).byteValue();
		}
		catch(NumberFormatException e) {
			throw new IllegalArgumentException(hex + " isn't a valid hex String for Colour!");
		}
	}

	/**
	 * Get the red component of this Colour
	 * 
	 * @return the red component of this Colour
	 */
	public byte getR() {
		return (byte) (this.r < 0 ? this.r + 256 : this.r);
	}

	/**
	 * Get the green component of this Colour
	 * 
	 * @return the green component of this Colour
	 */
	public byte getG() {
		return (byte) (this.g < 0 ? this.g + 256 : this.g);
	}

	/**
	 * Get the blue component of this Colour
	 * 
	 * @return the blue component of this Colour
	 */
	public byte getB() {
		return (byte) (this.b < 0 ? this.b + 256 : this.b);
	}
}
