/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package biome.model.board;

import biome.model.organisms.Organism;

/**
 *
 * @author matthewbernstein
 */
public class Square 
{
	public final static int ROCK_TYPE = 0;
	public final static int WATER_TYPE = 1;
	public final static int GRASS_TYPE = 2;
    public final static int NUM_TYPES = 3;
    
    public final static int LAND = 0;
    public final static int WATER = 1;
     
    private Organism occupant = null;

    private int terrain;
    
    private int row;
    private int column;
    
    public Square() { }
    
    public Square(int row, int column, int type) {
        
        this.row = row;
        this.column = column;
        this.terrain = type;
    }
    
    public void removeOccupant()
    {
    	//this.occupant.setPosition(null, null);
    	this.occupant = null;
    }
    
    public void setOccupant(Organism organism) 
    {
        this.occupant = organism;
        this.occupant.setPosition(this.row, this.column);
    }
    
    public Organism getOccupantOrganism() 
    {  
        return this.occupant;
    }
    
    public Boolean isEmpty() 
    {
        return this.occupant == null ? true : false;
    }
    
    public int getRow() 
    {   
        return this.row;
    }
    
    public int getColumn() 
    {    
        return this.column;
    }
    
    public int getTerrainType()
    {
    	return terrainType(terrain);
    }
    
    public int getTerrain() 
    {    
        return this.terrain;
    }
    
    public void setTerrain(int terrain) 
    {    
        this.terrain = terrain;
    }
    
    public static int terrainType(int terrain)
    {
    	if (terrain == GRASS_TYPE || terrain == ROCK_TYPE)
    	{
    		return LAND;
    	}
    	else
    	{
    		return WATER;
    	}
    }
    
}
