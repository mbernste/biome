package biome.model.organisms;

import biome.application.Common;
import biome.controller.actions.Action;
import biome.controller.behaviors.ReproduceBySeeds;
import biome.model.board.Board;
import biome.view.OrganismView;

public class Plant extends Organism
{
	private static int NUM_PROGENY = Common.NUM_PLANT_PROGENY;
	private static int RADIUS = 10;
	
	private int numTurnsToReproduce;
		
	public Plant()
	{
		super();
				
		nutritionalValue = 3;
		
		numTurnsToReproduce = Common.PLANT_REPRODUCE_FREQUENCY + Common.RAND.nextInt(10);
		behaviors.add(new ReproduceBySeeds(NUM_PROGENY, RADIUS, numTurnsToReproduce));
	}
	
	@Override
	public Action act()
	{
		iterateBehaviors();
		
		if ( behaviors.get(0).calculatePriority() > 0)
		{
			return behaviors.get(0).run(this.simState.getBoard(), this.simState.getBoard(), this);
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
