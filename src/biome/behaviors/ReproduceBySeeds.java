package biome.behaviors;

import biome.actions.Action;
import biome.actions.Move;
import biome.actions.Reproduce;
import biome.environment.Board;
import biome.organisms.Organism;
import biome.view.Common;

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
	public Integer calculatePriority(Board board) 
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
