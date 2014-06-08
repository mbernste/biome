/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package board.common;

/**
 *
 * @author matthewbernstein
 */
public class BoardCoordinate {
    
    
    private int row;
    private int column;
    
    
    public BoardCoordinate() {}
    
    public BoardCoordinate(int row, int column) {
        
        this.row = row;
        this.column = column;
    }
    
    public void setRow(int row) {
        this.row = row;
    }
    
    public int getRow() {
        return row;
    }
    
    public void setColumn(int column) {
        this.column = column;
    }
    
    public int getColumn() {
        return column;
    }
    
}
