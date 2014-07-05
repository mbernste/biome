package biome.controller.behaviors;

import pair.Pair;
import biome.application.Common;
import biome.controller.actions.Action;
import biome.controller.actions.Eat;
import biome.controller.actions.Move;
import biome.model.board.Board;
import biome.model.organismattributes.Diet;
import biome.model.organisms.Animal;
import biome.model.organisms.Organism;
import biome.utils.MathUtils;

public class CarnivoreFeed extends Behavior
{
	private Animal animal;
	private String name = "CARNIVORE_FEED";
	
	public CarnivoreFeed(Organism organism)
	{
		if (!(organism instanceof Animal))
		{
			throw new RuntimeException("Only animals can exhibit the behavior" +
					this);
		}
		
		this.animal = (Animal) organism;
		animal.setDiet(Diet.DIET_CARNIVORE);
	}
	
	@Override
	public String toString()
	{
		return this.name;
	}
	
	@Override
	public Action run(Board board, Board buffer, Organism organism) 
	{
		if (!(organism instanceof Animal))
		{
			throw new RuntimeException("Only animals may exhibit " + this);
		}
		
		
		Animal an = (Animal) organism;
		
		System.out.println(an +  " is performing " + this);
		
		int vision = an.getVision().getValue();
		
		float closestDistance = Float.MAX_VALUE;
		Pair<Integer, Integer> toPrey = new Pair<Integer, Integer>();
		boolean found = false;
		
		Pair<Float, Float> orgLoc = new Pair<Float, Float>((float) an.getRow(), (float) an.getColumn());
		
		/*
		 * Look at all squares within this organism's vision.  Find closest
		 * prey in these squares.  If this prey is beyond the speed of the
		 * creature, move towards the plant.  Otherwise, eat the plant.
		 */
		for (int r = an.getRow() - vision; r < an.getRow() + vision; r++)
		{
			for (int c = an.getColumn() - vision; c < an.getColumn() + vision; c++)
			{	
				Pair<Float, Float> preyLoc = new Pair<Float, Float>((float) r, (float) c);
		        	
				Pair<Integer, Integer> bCoord = Common.bringWithinBoard(r, c);
		        Integer bRow = bCoord.getFirst();
		        Integer bColumn = bCoord.getSecond();
		        
		        /*
		         * If square is occupied by an herbivore
		         */
		        if (buffer.isSquareOccupied(bRow, bColumn) &&
		        	buffer.getSquareType(bRow, bColumn) == an.getTerrainType() &&
		        	buffer.getOrganism(bRow, bColumn) instanceof Animal &&
		        	((Animal) buffer.getOrganism(bRow, bColumn)).getDiet().getValue() == Diet.DIET_HERBIVORE)
		        {
		        	float distance = MathUtils.distance(orgLoc, preyLoc);
		        	if (distance < closestDistance)
		        	{		        		
		        		toPrey = new Pair<Integer, Integer>(r - an.getRow(), c - an.getColumn());
		        		closestDistance = distance;
		        		found = true;
		        	}
		        }
			}
		}
		
		if (found)
		{
			if (closestDistance < an.getSpeed())
			{
				return new Eat(toPrey.getFirst(), toPrey.getSecond());
			}
			else
			{
				int moveRow = (int) (an.getSpeed() * toPrey.getFirst() / closestDistance);
				int moveCol = (int) (an.getSpeed() * toPrey.getSecond() / closestDistance);
				
				Pair<Integer, Integer> bCoord = Common.bringWithinBoard(an.getRow() + moveRow, an.getColumn() + moveCol);	
				int bRow = bCoord.getFirst();
				int bCol = bCoord.getSecond();
				
				if (!buffer.isSquareOccupied(bRow, bCol) &&
					buffer.getSquare(bRow, bCol).getTerrainType() == an.getTerrainType())
				{
					System.out.println(an + " is moving towards prey by going to square " + bRow + ", " + bCol);
					return new Move(moveRow, moveCol);
				}
				else
				{
					System.out.println(an +  " couldn't move towards prey because of incompatible terrain.");
				}
			}
		}
		
		System.out.println(this + " produced null by " + an);
		
		return null;
		
	}

	@Override
	public Integer calculatePriority() 
	{ 
		return animal.getHunger();
	}
	
	@Override
	public void iterate() {};

}
