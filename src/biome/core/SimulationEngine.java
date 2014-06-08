
package biome.core;

import java.lang.RuntimeException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.jme3.scene.Node;

import pair.Pair;

import biome.organisms.Organism;
import biome.organisms.Animal;
import biome.organisms.Plant;
import biome.actions.Action;
import biome.actions.ActionResult;
import biome.actions.Move;
import biome.application.IterationResult;
import biome.environment.Board;
import biome.environment.BoardFactory;
import biome.view.Common;

/**
 * 
 * @author matthewbernstein
 */
public class SimulationEngine 
{
	private Node root;
	
	private Board board;
	private Board buffer;
	
	private Set<Organism> organisms;
	private Set<Animal> animals;
	
	private Set<Organism> lastDead = new HashSet<Organism>();
	
	private final static int GENERATION_ITERATIONS = 30; 

	public SimulationEngine(int rows, int columns) 
	{
		BoardFactory bFactory = new BoardFactory();
		
		this.board = bFactory.buildNewBoard(rows, columns, 1);
		
		this.organisms = bFactory.populateBoard(board, 
												Common.NUM_HERBIVORES, 
												Common.NUM_CARNIVORES, 
												Common.NUM_PLANTS);
		animals = new HashSet<Animal>();
		for (Organism org : organisms)
		{
			if (org instanceof Animal)
			{
				animals.add((Animal) org);
			}
		}
		
		buffer = new Board(board);
	}
	
	public Organism getOrganism(int row, int column)
	{   
		return board.getOrganism(row, column);
	}

	public Square getSquare(int row, int column) 
	{    
		return board.getSquare(row, column);
	}

	public int getSquareType(int row, int column) 
	{ 
		return board.getSquareType(row, column);
	}

	public int getNumColumns() 
	{    
		return board.getNumColumns();
	}

	public int getNumRows() 
	{    
		return board.getNumRows();
	}
	
	public Set<Organism> getOrganisms()
	{
		return this.organisms;
	}
	
	/**
	 * Perform an iteration of the simulation
	 */
	public IterationResult iterate() 
	{       	
		IterationResult result = new IterationResult();
		
		Set<Organism> born = new HashSet<Organism>();
		Set<Organism> dead = new HashSet<Organism>();
		
		/*
		 * Organisms where they used to be
		 */ 
		for (Organism org : organisms) 
		{	
			if (!lastDead.contains(org))
			{
				buffer.setOrganism(org.getRow(), org.getColumn(), org);
			}
			org.resetView();
		} 

		/*
		 * Now all of the organisms act
		 */ 
		for (Organism org : organisms)
		{			
			if (!lastDead.contains(org) && !dead.contains(org))
			{
				ActionResult actionResult;
				
				Action action = org.act(board, buffer);
				if (action != null)
				{
					actionResult = action.performAction(board, buffer, org);
			
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
		board = buffer; 
		buffer = new Board(board);
		
		lastDead = dead;
		
		return result;
	}
	
	public Set<Plant> getPlants()
	{
		Set<Plant> plants = new HashSet<Plant>();
		
		for (Organism org : organisms)
		{
			if (org instanceof Plant)
			{
				plants.add((Plant) org);
			}
		}
		
		return plants;
	}
	
	public Set<Animal> getAnimals()
	{
		/*
		Set<Animal> animals = new HashSet<Animal>();
		
		for (Organism org : organisms)
		{
			if (org instanceof Animal)
			{
				animals.add((Animal) org);
			}
		}
		
		return animals;*/
		
		return animals;
	}
	
	public void addBorn(Set<Organism> born)
	{
		for (Organism bornOrg : born)
		{
			if (bornOrg instanceof Animal)
			{
				animals.add((Animal) bornOrg);
			}
		}
	
		organisms.addAll(born);
		
	}
	
	public void removeDead(Set<Organism> dead)
	{
		for (Organism deadOrg : dead)
		{
			System.out.println("ENGINE REMOVE " + deadOrg);
		}
		
		animals.removeAll(dead);
		organisms.removeAll(dead);
	}
}
