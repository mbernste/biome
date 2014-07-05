package biome.controller.behaviors;

import java.util.Comparator;

import biome.controller.actions.Action;
import biome.model.board.Board;
import biome.model.core.SimulationState;
import biome.model.organisms.Organism;
import biome.application.Common;

public class  Behavior 
{
	protected SimulationState simState;
	
	public Behavior() {
		this.simState = (SimulationState) Common.BEAN_CONTEXT.getBean("theState");
	}
	
	public Action run(Board board, Board buffer, Organism organism)
	{
		return null;
	}
	
	public Integer calculatePriority()
	{
		return null;
	}
	
	public void iterate() {};
	
	public static Comparator<Behavior> generateComparator()
	{
		return new Comparator<Behavior>() 
	            {			
					public int compare(Behavior b1, Behavior b2) 
					{
						return -b1.calculatePriority().compareTo(b2.calculatePriority());
					}
	            };
	}
}
