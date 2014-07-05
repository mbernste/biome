package biome.application;

import java.util.Random;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Spatial;

import biome.model.board.Board;
import biome.model.board.Square;

import pair.Pair;

public class Common implements ApplicationContextAware
{	
	/**
	 * Spring application context
	 */
	private static ApplicationContext beanContext;
	
	/**
     * Time/space settings
     */
    public final static float SQUARE_WIDTH = 3.0f;
    public final static float ITERATION_TIME = 1f;
    public final static float SCALE = 0.25f;
    
    /**
	 * Board dimensions
	 */
	public final static int NUM_ROWS = 30;
	public final static int NUM_COLS = 30; 
	
	public final static int NUM_TREES = 5;
	public final static int NUM_PLANTS = 30;
	public final static int NUM_CARNIVORES = 1;
	public final static int NUM_HERBIVORES = 5;
	
	public static int CARNIVORE_SPEED = 6;
	public static int CARNIVORE_VISION = 20;
	
	public static int HERBIVORE_SPEED = 3;
	public static int HERBIVORE_VISION = 10;
	
	public static int NUM_PLANT_PROGENY = 2;
	public static int PLANT_REPRODUCE_FREQUENCY = 10;
	
			
	/**
	 * Random number generator
	 */
	public final static Random RAND = new Random();
	
	/**
	 * Asset manager
	 */
	public static AssetManager ASSET_MANAGER;
	
	public final static String UNSHADED = "Common/MatDefs/Misc/Unshaded.j3md";
	
	public final static ClassPathXmlApplicationContext BEAN_CONTEXT 
							= new ClassPathXmlApplicationContext("Beans.xml"); 	
    
    /**
     * Converts board coordinates to world coordinates.
     * 
     * @param row row on the board
     * @param column column on the board
     * @return the world coordinates of this board position
     */
    public static Pair<Float, Float> convertToWorldCoords(float row, float column)
    {
    	Float x = (row * Common.SQUARE_WIDTH) + (Common.SQUARE_WIDTH / 2);
    	Float y = (column * Common.SQUARE_WIDTH) - (Common.SQUARE_WIDTH / 2);
    	
    	Pair<Float, Float> worldCoord = new Pair<Float, Float>();
    	worldCoord.setFirst(x);
    	worldCoord.setSecond(y);
    	
    	return worldCoord;
    }
    
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
    
    public static Pair<Integer, Integer> bringWithinBoard(int row, int column)
    {
    	Integer validRow = row % NUM_ROWS;
        Integer validColumn = column % NUM_COLS;
        
        if (validRow < 0) validRow = NUM_ROWS + validRow;
        if (validColumn < 0) validColumn = NUM_COLS + validColumn;
        
        return new Pair<Integer, Integer>(validRow, validColumn);
    }

    public static Square randomUnoccupiedSquare(Board board)
    {
    	int tries = 0;
    	int row;
		int col;
		
		do
		{
			row = RAND.nextInt(NUM_ROWS);
			col = RAND.nextInt(NUM_COLS);
			System.out.println(tries);
			tries++;
		}
		while(board.isSquareOccupied(row, col) && tries < 10);
		
		if (tries < 10)
		{
			return board.getSquare(row, col);
		}
		
		return null;
    }

    public static ApplicationContext getApplicationContext() {         
        return beanContext;    
    }
    
	@Override
	public void setApplicationContext(ApplicationContext beanContext)
			throws BeansException {
		Common.beanContext = beanContext;
	}
}
