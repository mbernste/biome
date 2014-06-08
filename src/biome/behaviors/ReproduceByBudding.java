package biome.behaviors;

import biome.actions.Action;
import biome.actions.Reproduce;
import biome.environment.Board;
import biome.organisms.Animal;
import biome.organisms.Organism;

public class ReproduceByBudding extends Behavior
{
private String name = "REPRODUCE_BY_BUDDING";
	
	private static int RADIUS = 1;
	private static int NUM_PROGENY = 1;
	
	private static int HUNGER_THRESH = 5;
	private static int libidoThresh = 5;

	
	private Animal animal;
	
	public ReproduceByBudding(Animal animal, int libidoThresh)
	{		
		this.animal = animal;
		this.libidoThresh = libidoThresh;
	}
	
	@Override
	public Action run(Board board, Board buffer, Organism organism) 
	{
		System.out.println(organism +  " is performing " + this);
		return new Reproduce(NUM_PROGENY, RADIUS);
	}

	@Override
	public Integer calculatePriority(Board board) 
	{
		if (animal.getHunger() < animal.getLibido() &&
			animal.getLibido() > libidoThresh &&
		    animal.getHunger() < HUNGER_THRESH)
		{
			return animal.getLibido();
		}
		else
		{
			return 0;
		}
	}
	
	@Override
	public String toString()
	{
		return this.name;
	}
}
