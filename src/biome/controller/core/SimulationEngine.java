
package biome.controller.core;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import biome.model.board.Board;
import biome.model.board.BoardFactory;
import biome.model.core.SimulationState;
import biome.model.organisms.Organism;
import biome.application.Common;
import biome.controller.actions.Action;
import biome.controller.actions.ActionResult;

/**
 * 
 * @author matthewbernstein
 */
public class SimulationEngine 
{	
	private SimulationState simState;
	
	private List<Organism> lastDead = new ArrayList<>();	
	
	
	@Autowired
	public SimulationEngine(@Qualifier("theState") SimulationState simState) 
	{
		this.simState = simState;
	}

	public void initPopulation() 
	{
		BoardFactory bFactory = new BoardFactory();
		simState.addOrganisms(bFactory.populateBoard(simState.getBoard(), 
				Common.NUM_HERBIVORES, 
				Common.NUM_CARNIVORES, 
				Common.NUM_PLANTS));
		
		simState.setBuffer(new Board(simState.getBoard())); // TODO REFACTOR
	}
	
	/**
	 * Perform an iteration of the simulation
	 */
	public IterationResult iterate() 
	{       	
		IterationResult result = new IterationResult();
		
		List<Organism> born = new ArrayList<>();
		List<Organism> dead = new ArrayList<>();
				
		/*
		 * Organisms where they used to be
		 */ 
		for (Organism org : simState.getOrganisms()) 
		{	
			if (!lastDead.contains(org))
			{
				simState.getBuffer().setOrganism(org.getRow(), org.getColumn(), org);
			}
		} 

		/*
		 * Now all of the organisms act
		 */ 
		for (Organism org : simState.getOrganisms())
		{			
			if (!lastDead.contains(org) && !dead.contains(org))
			{
				ActionResult actionResult;
				
				Action action = org.act();
				if (action != null)
				{
					actionResult = action.performAction(simState.getBoard(), simState.getBuffer(), org);
			
					born.addAll(actionResult.getBorn());
					dead.addAll(actionResult.getDead());
				}
			}
		} 
		addBorn(born);		
		removeDead(lastDead);
		
		result.setDead(dead);
		result.setLastDead(lastDead);
		result.setBorn(born);
		
		/*
		 *  Swap the current board with the buffer board and build a new buffer
		 */
		simState.setBoard(simState.getBuffer()); 
		simState.setBuffer(new Board(simState.getBoard()));
		
		lastDead = dead;
		
		return result;
	}
	
	private void addBorn(List<Organism> born)
	{
		simState.addOrganisms(born);
	}
	
	private void removeDead(List<Organism> dead)
	{
		simState.removeOrganisms(dead);	
	}
}
