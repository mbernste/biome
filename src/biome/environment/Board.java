/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package biome.environment;

import biome.core.Square;
import biome.organisms.Organism;

import pair.Pair;

/**
 *
 * @author matthewbernstein
 */
public class Board 
{
    protected Square[][] squares;
    
    protected int rows;
    protected int columns;
 
    /**
     * Copy constructor.  This creates a new board based on the input board.
     * This method does not copy occupants.  It only copies squars.
     * 
     * @param board the board used to build this board
     */
    public Board(Board board)
    {
		this.squares = new Square[board.rows][board.columns];
		this.rows = board.rows;
		this.columns = board.columns;
		
		for (int r=0; r < rows; r++) 
		{
			for (int c=0; c < columns; c++) 
			{        
				Square tmpSquare = new Square(r, c, board.getSquareType(r,c));
				squares[r][c] = tmpSquare;
			}
		}		    
    }

    /**
     * Default Constructor
     */
    public Board() {}
    
    public Organism getOrganism(int row, int column) throws RuntimeException 
    {    
        if (row > rows || column > columns) {
            throw new RuntimeException("Requesting organism from invalid board "
                    + " coordinate.");
        }
        
        return squares[row][column].getOccupantOrganism();
    }
    
    public void setOrganism(int targetRow, int targetColumn, Organism organism) 
    throws RuntimeException
    {    	
        // Check board coordinates are valid
        if (targetRow > rows || targetColumn > columns)
        {
            throw new RuntimeException("Trying to set " + organism + " to "
                    + "an invalid board coordinate at " + targetRow + ", " 
                    + targetColumn + ".");
        }
        
        // Check square is unoccupied
        if (!squares[targetRow][targetColumn].isEmpty())
        {
            throw new RuntimeException("Trying to set " + organism + " to "
                    + "an occupied square at " + targetRow + ", " 
                    + targetColumn + ". This square is occupied by " + 
                    squares[targetRow][targetColumn].getOccupantOrganism());
        }
        
        squares[targetRow][targetColumn].setOccupant(organism);
    }
    
    public void removeOrganism(int targetRow, int targetColumn)
    {
    	// Check board coordinates are valid
        if (targetRow > rows || targetColumn > columns)
        {
            throw new RuntimeException("Trying to remove an organism from "
                    + "an invalid board coordinate at " + targetRow + ", " 
                    + targetColumn + ".");
        }
    	
    	squares[targetRow][targetColumn].removeOccupant();
    }
    
    public Square getSquare(int row, int column) 
    {    
        return squares[row][column];
    }
    
    /**
     * Determine whether a specific square is occupied
     * 
     * @param row the row of the square
     * @param column the column of the square
     * @return true if the square at the given row and column is occupied, 
     * false otherwise
     */
    public Boolean isSquareOccupied(int row, int column)
    {
    	return !squares[row][column].isEmpty();
    }
      
    public int getSquareType(int row, int column) 
    { 
        return getSquare(row, column).getTerrain();
    }
    
    public int getNumColumns() 
    {    
        return columns;
    }
    
    public int getNumRows() 
    {    
        return rows;
    }
    
    /**
	 * Calculate the Euclidean distance between two board coordinates
	 * 
	 * @param coordA the first coordinate
	 * @param coordB the second coordinate
	 * @return the Euclidean distance between the two coordinates
	 */
    public static double distance(Pair<Integer, Integer> coordA, 
								  Pair<Integer, Integer> coordB)
	{    	
		Double term1 = Math.pow(coordA.getFirst() - coordB.getFirst(), 2);
		Double term2 = Math.pow(coordA.getSecond() - coordB.getSecond(), 2);
		return Math.sqrt(term1 + term2);
	}
    
    
}
