package biome.controller.behaviors;

import biome.application.Common;
import biome.controller.actions.Action;
import biome.controller.actions.Move;
import biome.controller.actions.Reproduce;
import biome.model.board.Board;
import biome.model.organisms.Organism;

public class ReproduceBySeeds extends Behavior
{
	private String name = "REPRODUCE_BY_SEEDS";
	
	private int radius;
	private int turn;
	private int turnsToReproduce;
	private int numProgeny;
	
	public ReproduceBySeeds(int numProgeny, int radius, int turnsToReproduce)
	{		
		turn = 0;
		this.radius = radius;
		this.numProgeny = numProgeny;
		this.turnsToReproduce = turnsToReproduce;
	}
	
	@Override
	public Action run(Board board, Board buffer, Organism organism) 
	{
		return new Reproduce(numProgeny, radius);
	}

	@Override
	public Integer calculatePriority() 
	{
		if (turn >= turnsToReproduce)
		{
			turn = 0;
			return 10;
		}
		else
		{
			return 0;
		}
	}
	
	@Override
	public void iterate()
	{
		turn++;
	}
	
	@Override
	public String toString()
	{
		return this.name;
	}
}
