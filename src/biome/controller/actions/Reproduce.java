package biome.controller.actions;

import java.util.HashSet;
import java.util.Set;

import biome.application.Common;
import biome.model.board.Board;
import biome.model.organisms.Organism;
import biome.utils.MathUtils;

public class Reproduce extends Action
{
	String name = "REPRODUCE";
	
	private int numProgeny;
	private int radius;
	
	public Reproduce(int numProgeny, int radius)
	{
		super();
		this.numProgeny = numProgeny;
		this.radius = radius;
	}
	
	@Override
	public ActionResult  performAction(Board board, Board buffer, Organism org)
	{
		super.performAction(board, buffer, org);
		
		System.out.println(org + " executing " + this);

        ActionResult result = new ActionResult();
		Set<Organism> born = new HashSet<Organism>();
		Set<Organism> dead = new HashSet<Organism>();
		
		for (int i = 0; i < numProgeny; i++)
		{
			int rowDisp = MathUtils.generateRandInt(radius) + 1;
	        int columnDisp = MathUtils.generateRandInt(radius) + 1;
	       
	        int toRow = (org.getRow() + rowDisp) % buffer.getNumRows();
	        int toColumn = (org.getColumn() + columnDisp) % buffer.getNumColumns();

	        if (toRow < 0) toRow = buffer.getNumRows() + toRow;
	        if (toColumn < 0) toColumn = buffer.getNumColumns() + toColumn;
	        
	        
	        if (!buffer.isSquareOccupied(toRow, toColumn))
	        {
	        	Organism child = org.giveBirth();
	        	buffer.setOrganism(toRow, toColumn, child);
	        	born.add(child);
	        }	        
		}
		
		result.setBorn(born);
		result.setDead(dead);
				
        return result;
	}
	
	@Override
	public String toString()
	{
		return this.name;
	}
}
