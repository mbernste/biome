package biome.utils;

import java.util.Random;

import pair.Pair;

public class MathUtils {
	
	/**
	 * Random number generator
	 */
	public final static Random RAND = new Random();
	
	public static Float zCross(Pair<Float, Float> v1, Pair<Float, Float> v2)
    {
	    float  p = (v1.getFirst() * v2.getSecond()) - (v2.getFirst() * v1.getSecond());
	    return p;
    }
    
    /**
	 * Calculate the Euclidean distance between two coordinates
	 * 
	 * @param coordA the first coordinate
	 * @param coordB the second coordinate
	 * @return the Euclidean distance between the two coordinates
	 */
    public static Float distance(Pair<Float, Float> coordA, 
								  Pair<Float, Float> coordB)
	{
		Double term1 = Math.pow(coordA.getFirst() - coordB.getFirst(), 2);
		Double term2 = Math.pow(coordA.getSecond() - coordB.getSecond(), 2);
		return (float) Math.sqrt(term1 + term2);
	}
    
    /**
     * Linearly interpolate between two values
     * 
     * @param first the first value
     * @param second the second value
     * @param percentage interpolation value
     * @return the interpolated value
     */
    public static Float interpolate(float first, float second, float percentage)
    {
    	float diff = second - first;
    	return first +  diff * percentage;
    }
    
    /**
     * Generate a random integer within the range (-limit, limit)
     * 
     * @param limit the limit
     * @return a random integer within the range (-limit, limit)
     */
    public static int generateRandInt(int limit)
    {
		int result = RAND.nextInt(limit);  
		result = RAND.nextBoolean() ? result : -result;
		return result;        
    }
}
