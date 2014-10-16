package biome.controller.actions;

import java.util.HashSet;
import java.util.Set;

import pair.Pair;
import biome.application.Common;
import biome.model.board.Board;
import biome.model.organisms.Organism;
import biome.view.OrganismView;

public class Trample extends Action
{
	private int moveRow;
	private int moveColumn;
	
	public Trample(int moveRow, int moveColumn)
	{
		super();
		this.name = "TRAMPLE";
		this.moveRow = moveRow;
		this.moveColumn = moveColumn;
	}
	
	@Override
	public ActionResult  performAction(Board board, Board buffer, Organism org)
	{
		Set<Organism> born = new HashSet<Organism>();
		Set<Organism> dead = new HashSet<Organism>();
		
    	System.out.println(org + " executing " + this);
		
		ActionResult result = new ActionResult();
		
		Pair<Integer, Integer> bCoord = Common.bringWithinBoard(org.getRow() + moveRow, 
																org.getColumn() + moveColumn);
		
		int toRow = bCoord.getFirst();
		int toColumn = bCoord.getSecond();
		
		if (buffer.isSquareOccupied(toRow, toColumn))
		{
			dead.add(buffer.getOrganism(toRow, toColumn));
			buffer.removeOrganism(toRow, toColumn);
		}
		
		
       // setView(org);
        buffer.removeOrganism(org.getRow(), org.getColumn());
		buffer.setOrganism(toRow, toColumn, org);
		
		result.setBorn(born);
		result.setDead(dead);
		
        return result;
	}
	
	private void setView(Organism org)
	{
		OrganismView view = new OrganismView(org);
		
        Pair<Float, Float> origin = new Pair<Float, Float>();
        origin.setFirst((float) org.getRow());
        origin.setSecond((float) org.getColumn());
        
        Pair<Float, Float> destination = new Pair<Float, Float>();
        destination.setFirst((float) (org.getRow() + moveRow));
        destination.setSecond((float) (org.getColumn() + moveColumn));
        		
        view.setOrigin(origin);
        view.setDestination(destination);
        view.calculateRotationAngle();
                
       // org.setView(view);
	}

}
