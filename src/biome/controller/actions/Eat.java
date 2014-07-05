package biome.controller.actions;

import java.util.HashSet;
import java.util.Set;

import pair.Pair;
import biome.application.Common;
import biome.model.board.Board;
import biome.model.organisms.Animal;
import biome.model.organisms.Organism;
import biome.model.organisms.Plant;
import biome.view.OrganismView;

public class Eat extends Action
{
	private int moveRow;
	private int moveColumn;
	
	public Eat(int moveRow, int moveColumn)
	{
		super();
		this.name = "EAT";
		this.moveRow = moveRow;
		this.moveColumn = moveColumn;
	}
	
	@Override
	public ActionResult performAction(Board board, Board buffer, Organism org)
	{
		if (!(org instanceof Animal))
		{
			throw new RuntimeException("Only animals can exhibit the behavior" +
					this);
		}
		
		Animal an = (Animal) org;
		System.out.println(an + " executing " + this);
		
		ActionResult result = new ActionResult();
		Set<Organism> dead = new HashSet<Organism>();
		Set<Organism> born = new HashSet<Organism>();
		
		Pair<Integer, Integer> bCoord = Common.bringWithinBoard(an.getRow() + moveRow, 
																an.getColumn() + moveColumn);
		
		int toRow = bCoord.getFirst();
		int toColumn = bCoord.getSecond();
		
		if (!buffer.isSquareOccupied(toRow, toColumn))
		{
			throw new RuntimeException(an + " cannot eat an organism at " +
					"square " + toRow + ", " + toColumn + ".  No organism at " +
					"this square");
		}
		
		System.out.println("Eat move is removing " + buffer.getOrganism(toRow, toColumn) + " from " + toRow + ", " + toColumn);
		
		Organism eatenOrg = buffer.getOrganism(toRow, toColumn);
		an.feed(eatenOrg.getNutritionalValue());
		dead.add(eatenOrg);
		buffer.removeOrganism(toRow,  toColumn);
		
		updateViewData(an);
	    buffer.removeOrganism(an.getRow(), an.getColumn());
	    buffer.setOrganism(toRow, toColumn, an);
		
		result.setBorn(born);
		result.setDead(dead);
		return result;
	}
	
	private void updateViewData(Organism org)
	{
		OrganismView vData = new OrganismView(org);
		
        Pair<Float, Float> origin = new Pair<Float, Float>();
        origin.setFirst((float) org.getRow());
        origin.setSecond((float) org.getColumn());
        
        Pair<Float, Float> destination = new Pair<Float, Float>();
        destination.setFirst((float) (org.getRow() + moveRow));
        destination.setSecond((float) (org.getColumn() + moveColumn));
        		
        vData.setOrigin(origin);
        vData.setDestination(destination);
        vData.calculateRotationAngle();
                
       this.organismViewData.setViewData(org, vData);
	} 
}
