package biome.actions;

import java.util.HashSet;
import java.util.Set;

import pair.Pair;

import biome.environment.Board;
import biome.organisms.Organism;
import biome.view.Common;
import biome.view.ViewData;

public class Move extends Action
{	
	private int moveRow;
	private int moveColumn;
	
	public Move(int moveRow, int moveColumn)
	{
		this.name = "MOVE";
		this.moveRow = moveRow;
		this.moveColumn = moveColumn;
	}
	
	@Override
	public ActionResult  performAction(Board board, Board buffer, Organism org)
	{
    	System.out.println(org + " executing " + this);
		
		ActionResult result = new ActionResult();
		
		Pair<Integer, Integer> bCoord = Common.bringWithinBoard(org.getRow() + moveRow, 
																org.getColumn() + moveColumn);
		
		int toRow = bCoord.getFirst();
		int toColumn = bCoord.getSecond();
		
        setView(org);
        buffer.removeOrganism(org.getRow(), org.getColumn());
		buffer.setOrganism(toRow, toColumn, org);
		
		Set<Organism> born = new HashSet<Organism>();
		Set<Organism> dead = new HashSet<Organism>();
		result.setBorn(born);
		result.setDead(dead);
		
        return result;
	}
	
	private void setView(Organism org)
	{
		ViewData view = new ViewData(org);
		
        Pair<Float, Float> origin = new Pair<Float, Float>();
        origin.setFirst((float) org.getRow());
        origin.setSecond((float) org.getColumn());
        
        Pair<Float, Float> destination = new Pair<Float, Float>();
        destination.setFirst((float) (org.getRow() + moveRow));
        destination.setSecond((float) (org.getColumn() + moveColumn));
        		
        view.setOrigin(origin);
        view.setDestination(destination);
        view.calculateRotationAngle();
                
        org.setView(view);
	}

}
