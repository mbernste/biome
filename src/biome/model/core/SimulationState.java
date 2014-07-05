package biome.model.core;

import java.util.ArrayList;
import java.util.List;

import biome.application.Common;
import biome.model.board.Board;
import biome.model.board.BoardFactory;
import biome.model.board.Square;
import biome.model.organisms.Animal;
import biome.model.organisms.Organism;
import biome.model.organisms.Plant;

public class SimulationState 
{
	private Board board;
	private Board buffer;
	
	private List<Organism> organisms;
	private List<Animal> animals;

	
	public SimulationState() {
		BoardFactory bFactory = new BoardFactory();
			
		this.organisms = new ArrayList<>();
		this.animals = new ArrayList<>();
		
		this.board = bFactory.buildNewBoard(Common.NUM_ROWS, Common.NUM_COLS);
		
	}
	
	public List<Organism> getOrganisms() {
		return this.organisms;
	}
	
	public List<Animal> getAnimals() {
		return this.animals;
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
	
	public Board getBoard() 
	{
		return this.board;
	}
	
	public Board getBuffer() 
	{
		return this.buffer;
	}
	
	public void setBoard(Board board) {
		this.board = board;
	}
	
	public void setBuffer(Board buffer) {
		this.buffer = buffer;
	}

	public void addOrganisms(List<Organism> newOrgs) 
	{
		organisms.addAll(newOrgs);
		for (Organism bornOrg : newOrgs)
		{
			if (bornOrg instanceof Animal)
			{
				animals.add((Animal) bornOrg);
			}
		}		
	}
	
	public void removeOrganisms(List<Organism> removeOrgs) {
		for (Organism removedOrg : removeOrgs)
		{
			System.out.println("SIMULATION STATE REMOVE " + removedOrg);
		}
		
		animals.removeAll(removeOrgs);
		organisms.removeAll(removeOrgs);
	}
	
	public List<Plant> getPlants()
	{
		List<Plant> plants = new ArrayList<>();
		
		for (Organism org : organisms)
		{
			if (org instanceof Plant)
			{
				plants.add((Plant) org);
			}
		}
		
		return plants;
	}
}
	
