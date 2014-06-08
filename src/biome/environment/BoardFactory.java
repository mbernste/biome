/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package biome.environment;

import biome.core.Square;
import biome.organisms.Animal;
import biome.organisms.Organism;
import biome.organisms.Plant;
import biome.organisms.Tree;
import biome.organisms.attributes.Diet;
import biome.organisms.attributes.TerrainType;
import biome.view.Common;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author Matthew Bernstein - matthewb@cs.wisc.edu
 */
public class BoardFactory 
{
	/**
	 * Number of iterations for cyclic cell automata
	 */
	private final static int CYCLIC_CELL_ITERATIONS = 100; 

	/**
	 * The board under construction
	 */
	private Board board;

	/**
	 * Creates a brand new Board object with a specified number of 
	 * rows and columns.
	 * This class uses Cyclic Cellular Automata for generating a board with
	 * contiguous, yet randomized, blocks of similar terrain.
	 *
	 * @param numRows number of rows
	 * @param numColumns number of columns
	 * @return a new Board instance
	 */
	public Board buildNewBoard(int numRows, int numColumns, int numAnimals)
	{   
		board = new Board();
		board.rows = numRows;
		board.columns = numColumns;
		board.squares = new Square[numRows][numColumns];

		buildSquares();
		cyclicCellTerrain();
						
		return board;
	}

	/**
	 * Creates square objects for the board and assigns them initial random
	 * terrain types
	 */
	private void buildSquares() 
	{    
		Random rand = new Random();
		Square currSquare;

		int currType;
		for (int r = 0; r < board.rows; r++) 
		{ 
			for (int c = 0; c < board.columns; c++) 
			{
				currType = rand.nextInt(Square.NUM_TYPES);
				currSquare = new Square(r, c, currType);
				board.squares[r][c] = currSquare;
			}
		}
	}
	
	public Set<Organism> populateBoard(Board board, 
									   int numHerbivores, 
									   int numCarnivores, 
									   int numPlants)
    {		
		Set<Organism> organisms = new HashSet<Organism>();
		
		Organism o = null;
		for (int i = 0; i < numHerbivores; i++)
		{			
			Square square = Common.randomUnoccupiedSquare(board);
			if (square != null)
			{
				o = new Animal(new TerrainType(square.getTerrainType()), new Diet(Diet.DIET_HERBIVORE));
				square.setOccupant(o);
				organisms.add(o);
			}
		}
		
		System.out.println("Placed all herbivores");
		
		for (int i = 0; i < numCarnivores; i++)
		{						
			Square square = Common.randomUnoccupiedSquare(board);
			if (square != null)
			{
				o = new Animal(new TerrainType(square.getTerrainType()), new Diet(Diet.DIET_CARNIVORE));
				square.setOccupant(o);
				organisms.add(o);
			}
		}
		
		System.out.println("Placed all carnivores");
				
		for (int i = 0 ; i < numPlants; i++)
		{					
			o = new Plant();
			
			System.out.println("Generating...");
			Square square = Common.randomUnoccupiedSquare(board);
			System.out.println("Generated.");
			if (square != null)
			{
				square.setOccupant(o);
				organisms.add(o);
				System.out.println("Placed plant: " + i + " at " + o.getRow() + ", " + o.getColumn());
			}
		} 
		
		
		for (int i = 0; i < Common.NUM_TREES; i++)
		{
			o = new Tree();
			
			System.out.println("Generating...");
			Square square = Common.randomUnoccupiedSquare(board);
			System.out.println("Generated.");
			if (square != null && square.getTerrain() == Square.GRASS_TYPE)
			{
				square.setOccupant(o);
				organisms.add(o);
				System.out.println("Placed tree: " + i + " at " + o.getRow() + ", " + o.getColumn());
			}
		}
		
		
		System.out.println("Placed all trees");
		
		return organisms;
    }

	/**
	 * Generates randomized yet contiguous clusters of similar terrain using
	 * cyclic cellular automata.
	 */
	private void cyclicCellTerrain()
	{

		int val;
		int val1;
		int neighbor = 1;
		int threshold = 4;
		int count;
		int types[][] = new int[board.rows][board.columns];

		for (int iter = 0; iter<=CYCLIC_CELL_ITERATIONS; iter++) 
		{
			for (int r = 0; r < board.rows; r++) 
			{
				for (int c = 0; c < board.columns; c++) 
				{ 
					count = 0;
					val = board.squares[r][c].getTerrain();
					int r1, c1;
					for(r1 = r-neighbor; r1<= r+neighbor; r1++) 
					{
						for(c1 = c-neighbor; c1 <= c+neighbor; c1++) 
						{

							if (r1==r && c1==c) continue;

							r1 = (r1<0) ? r1+board.rows : r1;
							c1 = (c1<0) ? c1+board.columns : c1;

							val1 = board.squares[r1%board.rows][c1%board.columns].getTerrain();
							if (val1 == val + 1 || (val==2 && val1==0)) 
							{
								count++;
							}

						}
					}

					if (count >= threshold) 
					{
						types[r][c] = (val+1)%3;
					}
					else 
					{
						types[r][c]= val;
					}
				}
			}
			for (int r=0; r < board.rows; r++) 
			{
				for (int c = 0; c < board.columns; c++) 
				{

					board.squares[r][c].setTerrain(types[r][c]);
				}
			}
		} 
	}
	
	
}
