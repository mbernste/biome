package biome.behaviors;

import java.util.Comparator;

import biome.actions.Action;
import biome.environment.Board;
import biome.organisms.Organism;

public class  Behavior 
{
	public Action run(Board board, Board buffer, Organism organism)
	{
		return null;
	}
	
	public Integer calculatePriority(Board boare)
	{
		return null;
	}
	
	public void iterate() {};
	
	public static Comparator<Behavior> generateComparator(final Board b)
	{
		return new Comparator<Behavior>() 
	            {
					private Board board = b;
			
					public int compare(Behavior b1, Behavior b2) 
					{
						return -b1.calculatePriority(board).compareTo(b2.calculatePriority(board));
					}
	            };
	}
}
