package biome.controller.behaviors;

import pair.Pair;
import biome.application.Common;
import biome.controller.actions.Action;
import biome.controller.actions.Eat;
import biome.controller.actions.Move;
import biome.controller.actions.Trample;
import biome.model.board.Board;
import biome.model.organismattributes.Diet;
import biome.model.organisms.Animal;
import biome.model.organisms.Organism;
import biome.model.organisms.Plant;

public class Fear extends Behavior
{
	private Animal animal;
	
	private String name = "FEAR";
	
	private float fearfulness = 1f;
	
	public Fear(Organism organism)
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
		
		float closestDistance = Float.MAX_VALUE;
		Pair<Integer, Integer> toPrey = new Pair<Integer, Integer>();
		boolean found = false;
		
		Pair<Float, Float> orgLoc = new Pair<Float, Float>((float) an.getRow(), (float) an.getColumn());
		
		/*
		 * Look at all squares within this organism's vision.  Find closest
		 * predator in these squares. 
		 */
		for (int r = an.getRow() - vision; r < an.getRow() + vision; r++)
		{
			for (int c = an.getColumn() - vision; c < an.getColumn() + vision; c++)
			{	
				Pair<Float, Float> predLoc = new Pair<Float, Float>((float) r, (float) c);
		        	
				Pair<Integer, Integer> bCoord = Common.bringWithinBoard(r, c);
		        Integer bRow = bCoord.getFirst();
		        Integer bColumn = bCoord.getSecond();
		        
		        /*
		         * If square is occupied by an herbivore
		         */
		        if (buffer.isSquareOccupied(bRow, bColumn) &&
		        	buffer.getSquareType(bRow, bColumn) == an.getTerrainType() &&
		        	buffer.getOrganism(bRow, bColumn) instanceof Animal &&
		        	((Animal) buffer.getOrganism(bRow, bColumn)).getDiet().getValue() == Diet.DIET_CARNIVORE)
		        {
		        	float distance = Common.distance(orgLoc, predLoc);
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
			if (closestDistance < this.fearfulness * an.getVision().getValue())
			{
				int moveRow = (int) -(an.getSpeed() * toPrey.getFirst() / closestDistance);
				int moveCol = (int) -(an.getSpeed() * toPrey.getSecond() / closestDistance);
				
				Pair<Integer, Integer> bCoord = Common.bringWithinBoard(an.getRow() + moveRow, an.getColumn() + moveCol);	
				int bRow = bCoord.getFirst();
				int bCol = bCoord.getSecond();
				
				if (buffer.getSquare(bRow, bCol).getTerrainType() == an.getTerrainType())
				{
					if (buffer.isSquareOccupied(bRow, bCol) &&
						buffer.getOrganism(bRow, bCol) instanceof Plant)
					{
						return new Trample(moveRow, moveCol);
					}
					
					if (!buffer.isSquareOccupied(bRow, bCol))
					{
						System.out.println(an + " is moving away from predator by going to square " + bRow + ", " + bCol);
						return new Move(moveRow, moveCol);
					}
				}
				else
				{
					System.out.println(an +  " couldn't move away from predator because of incompatible terrain.");
				}
			}
		}
		
		System.out.println(this + " produced null by " + an);
		
		return null;
	}

	@Override
	public Integer calculatePriority() 
	{ 
		
		float closestDistance = Float.MAX_VALUE;
		Pair<Integer, Integer> toPrey = new Pair<Integer, Integer>();
		boolean found = false;
		
		Pair<Float, Float> orgLoc = new Pair<Float, Float>((float) animal.getRow(), (float) animal.getColumn());


		int vision = animal.getVision().getValue();
		for (int r = animal.getRow() - vision; r < animal.getRow() + vision; r++)
		{
			for (int c = animal.getColumn() - vision; c < animal.getColumn() + vision; c++)
			{	
				Pair<Float, Float> predLoc = new Pair<Float, Float>((float) r, (float) c);
		        	
				Pair<Integer, Integer> bCoord = Common.bringWithinBoard(r, c);
		        Integer bRow = bCoord.getFirst();
		        Integer bColumn = bCoord.getSecond();
		        
		        /*
		         * If square is occupied by an herbivore
		         */
		        if (simState.getBuffer().isSquareOccupied(bRow, bColumn) &&
		        	simState.getBuffer().getSquareType(bRow, bColumn) == animal.getTerrainType() &&
		        	simState.getBuffer().getOrganism(bRow, bColumn) instanceof Animal &&
		        	((Animal) simState.getBuffer().getOrganism(bRow, bColumn)).getDiet().getValue() == Diet.DIET_CARNIVORE)
		        {
		        	float distance = Common.distance(orgLoc, predLoc);
		        	if (distance < closestDistance)
		        	{		        		
		        		toPrey = new Pair<Integer, Integer>(r - animal.getRow(), c - animal.getColumn());
		        		closestDistance = distance;
		        		found = true;
		        	}
		        }
			}
		}
		
		if (closestDistance < fearfulness * vision)
		{
			return 50;
		}
		else
		{
			return 0;
		}
	}
	
	@Override
	public void iterate() {};

}
