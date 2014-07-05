package biome.controller.behaviors;

import pair.Pair;
import biome.application.Common;
import biome.controller.actions.Action;
import biome.controller.actions.Eat;
import biome.controller.actions.Move;
import biome.model.board.Board;
import biome.model.organisms.Animal;
import biome.model.organisms.Organism;
import biome.model.organisms.Plant;
import biome.utils.MathUtils;

public class HerbivoreFeed extends Behavior 
{		
	private Animal animal;
	private String name = "HEBIVORE_FEED";
	
	public HerbivoreFeed(Organism organism)
	{
		if (!(organism instanceof Animal))
		{
			throw new RuntimeException("Only animals can exhibit the behavior" +
					this);
		}
		
		this.animal = (Animal) organism;
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
		
		float closestDistance = Integer.MAX_VALUE;
		Pair<Integer, Integer> toPlant = new Pair<Integer, Integer>();
		boolean found = false;
		
		Pair<Float, Float> orgLoc = new Pair<Float, Float>((float) an.getRow(), (float) an.getColumn());
		
		/*
		 * Look at all squares within this organism's vision.  Find closest
		 * plant in these squares.  If this plant is beyond the speed of the
		 * creature, move towards the plant.  Otherwise, eat the plant.
		 */
		for (int r = an.getRow() - vision; r < an.getRow() + vision; r++)
		{
			for (int c = an.getColumn() - vision; c < an.getColumn() + vision; c++)
			{	
				Pair<Float, Float> plantLoc = new Pair<Float, Float>((float) r, (float) c);
		        	
				Pair<Integer, Integer> bCoord = Common.bringWithinBoard(r, c);
		        Integer bRow = bCoord.getFirst();
		        Integer bColumn = bCoord.getSecond();
		        
		        /*
		         * If square is occupied by a plant
		         */
		        if (buffer.isSquareOccupied(bRow, bColumn) &&
		        	buffer.getOrganism(bRow, bColumn) instanceof Plant &&
		        	buffer.getSquare(bRow, bColumn).getTerrainType() == an.getTerrainType())
		        {
		        	float distance = MathUtils.distance(orgLoc, plantLoc);
		        	if (distance < closestDistance)
		        	{		        		
		        		toPlant = new Pair<Integer, Integer>(r - an.getRow(), c - an.getColumn());
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
				return new Eat(toPlant.getFirst(), toPlant.getSecond());
			}
			else
			{
				int moveRow = (int) (an.getSpeed() * toPlant.getFirst() / closestDistance);
				int moveCol = (int) (an.getSpeed() * toPlant.getSecond() / closestDistance);
				
				Pair<Integer, Integer> bCoord = Common.bringWithinBoard(an.getRow() + moveRow, an.getColumn() + moveCol);	
				int bRow = bCoord.getFirst();
				int bCol = bCoord.getSecond();
				
				if (!buffer.isSquareOccupied(bRow, bCol) &&
					buffer.getSquare(bRow, bCol).getTerrainType() == an.getTerrainType())
				{
					System.out.println(an + " is moving towards plant by going to square " + bRow + ", " + bCol);
					return new Move(moveRow, moveCol);
				}
				else
				{
					System.out.println(an +  " couldn't move towards plant because of incompatible terrain.");
				}
			}
		}
		else
		{
		
			System.out.println(this + " produced null by " + an + " because no plant was found");
		
			return null;
		}
		
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
