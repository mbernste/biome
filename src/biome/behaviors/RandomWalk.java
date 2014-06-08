package biome.behaviors;

import java.util.Random;

import biome.actions.Action;
import biome.actions.Move;
import biome.environment.Board;
import biome.organisms.Animal;
import biome.organisms.Organism;
import biome.view.Common;
import biome.view.ViewData;

public class RandomWalk extends Behavior
{	
	private String name = "RANDOM_WALK";
	
	@Override
	public Action run(Board board, Board buffer, Organism org) 
	{			
		if (!(org instanceof Animal))
		{
			throw new RuntimeException(this + " must be exhibited " +
					"by an animal");
		}
		
		Animal an = (Animal) org;
	
		System.out.println(an + " performing " + this);
		
		Random rand = new Random();
		
		int toRow = 0;
		int toColumn = 0;
		int moveRow = 0;
		int moveColumn = 0;
		int squareType = 0;
		int tries = 0;
		do
		{
			// Random movement 
			int speed = an.getSpeed();
			moveRow = Common.generateRandInt(speed);
			moveColumn = Common.generateRandInt(speed);
	        
	        // Calculate board coordinates of destination
			toRow = (an.getRow() + moveRow) % board.getNumRows();
	        toColumn = (an.getColumn() + moveColumn) % board.getNumColumns();
	        
	        if (toRow < 0) toRow = board.getNumRows() + toRow;
	        if (toColumn < 0) toColumn = board.getNumColumns() + toColumn;
	        
	        squareType = buffer.getSquare(toRow, toColumn).getTerrainType();
	        
	        tries++;
	        if (tries >= 10) break;
		} 
		while (buffer.isSquareOccupied(toRow, toColumn)  ||
			   an.getTerrainType() != squareType);
				
		Move move = null;
		if (tries < 10)
		{	
			move = new Move(moveRow, moveColumn);
		}
		else
		{
			move = new Move(0, 0);
		}
			
        return move;
	}
	
	/**
	 * Priority is given a zero.  Walking randomly should not be a priority.
	 */
	@Override
	public Integer calculatePriority(Board board) 
	{
		return 1;
	}
	
	@Override 
	public void iterate() {};
	
	@Override
	public String toString()
	{
		return this.name;
	}
}
