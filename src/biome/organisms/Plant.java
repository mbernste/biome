package biome.organisms;

import biome.actions.Action;
import biome.behaviors.ReproduceBySeeds;
import biome.environment.Board;
import biome.view.Common;
import biome.view.ViewData;

public class Plant extends Organism
{
	private static int NUM_PROGENY = Common.NUM_PLANT_PROGENY;
	private static int RADIUS = 10;
	
	private int numTurnsToReproduce;
		
	public Plant()
	{
		super();
		
    	this.view = new ViewData(this);
		
		nutritionalValue = 3;
		
		numTurnsToReproduce = Common.PLANT_REPRODUCE_FREQUENCY + Common.RAND.nextInt(10);
		behaviors.add(new ReproduceBySeeds(NUM_PROGENY, RADIUS, numTurnsToReproduce));
	}
	
	@Override
	public Action act(Board board, Board buffer)
	{
		iterateBehaviors();
		
		if ( behaviors.get(0).calculatePriority(buffer) > 0)
		{
			return behaviors.get(0).run(board, buffer, this);
		}
		else
		{
			return null;
		}
	}
	
	@Override
	public Organism giveBirth()
	{
		Plant child = new Plant();
		return child;
	}
	
	@Override 
	public String toString()
	{
		return "PLANT_" + orgId;
	}

}
