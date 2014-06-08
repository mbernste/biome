package biome.actions;

import java.util.HashSet;
import java.util.Set;

import biome.environment.Board;
import biome.organisms.Organism;

public class Death extends Action
{
	@Override
	public ActionResult  performAction(Board board, Board buffer, Organism org)
	{
		ActionResult result = new ActionResult();
		
		Set<Organism> born = new HashSet<Organism>();
		Set<Organism> dead = new HashSet<Organism>();	
		dead.add(org);
		
		result.setBorn(born);
		result.setDead(dead);
		
        return result;
	}
}
