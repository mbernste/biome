package biome.organisms;

import java.util.ArrayList;

import com.jme3.scene.Node;

import pair.Pair;

import biome.actions.Action;
import biome.behaviors.Behavior;
import biome.environment.Board;
import biome.view.ViewData;

/**
 * Represents a single organism in the simulation.
 * 
 * @author Matthew Bernstein - matthewb@cs.wisc.edu
 */
public class Organism 
{
	protected ViewData view;
    
    protected int age;
    protected int orgId;
    protected int libido;
    
    protected int nutritionalValue;
  
    protected Integer row = 0;
    protected Integer column = 0;    
        
    protected static int idCount = 0;
    
    protected ArrayList<Behavior> behaviors;
    
    /**
     * Constructor
     */
    public Organism() 
    {
    	this.orgId = idCount++;
    	this.behaviors = new ArrayList<Behavior>();
        this.age = 0;
        this.libido = 0;
    }

    /**
     * @return the number of turns passed since the organism was born
     */
    public int getAge() 
    {
        return this.age;
    }
    
    /**
     * @return the organism's unique integer ID
     */
    public int getOrgId()
    {
        return this.orgId;
    }
    
    /**
     * @return the row of the board that the organism is currently located. 
     * Returns null if the organism is not on the board.
     */
    public int getRow() 
    {
        return this.row;
    }
    
    /**
     * @param row the row of the board where the organism is located
     */
    public void setRow(int row) 
    {
        this.row = row;
    }
    
    /**
     * @return the column of the board that the organism is currently located. 
     * Returns null if the organism is not on the board. 
     */
    public int getColumn()
    {
        return this.column;
    }
    
    /**
     * Set the organism's current row and column location
     * 
     * @param row the row at which the organism is currently located
     * @param column the column at which the organism is currently located
     */
    public void setPosition(Integer row, Integer column)
    {
        this.row = row;
        this.column = column;
    }
    
    /**
     * @param column the column of the board where the organism is located
     */
    public void setColumn(int column)
    {
        this.column = column;
    }
    
    public ViewData getView()
    {
    	return this.view;
    }
    
    public void setView(ViewData view)
    {
    	this.view = view;
    }
    
    public Action act(Board board, Board buffer)
    {
    	iterateBehaviors();
    	return null;
    }
    
    protected void iterateBehaviors()
    {
    	for (Behavior b : behaviors)
    	{
    		b.iterate();
    	}
    }
    
    public int getNutritionalValue()
    {
    	return this.nutritionalValue;
    }
    
    public Organism giveBirth()
    {
    	return null;
    }
    
    public void resetView()
    {		    	
        Pair<Float, Float> origin = new Pair<Float, Float>();
        origin.setFirst((float) row);
        origin.setSecond((float) column);
        
        view.setOrigin(origin);
        view.setDestination(origin);
        view.calculateRotationAngle();                
    }
    
    public Node getNode()
    {
    	return view.getNode();
    }
}


