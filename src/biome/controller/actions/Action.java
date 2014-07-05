/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package biome.controller.actions;


import biome.application.Common;
import biome.model.board.Board;
import biome.model.organisms.Organism;
import biome.view.OrganismViewData;

public class Action 
{    
    // Action Types
	public enum Type {MOVE, EAT, DEATH, REPRODUCE};
   
    protected Type actionType;
    protected int actionRow;
    protected int actionColumn;
    
    protected String name;
    
    protected OrganismViewData organismViewData;
    
    
    public Action() 
    {
    	System.out.println("IS CONTEXT NULL?: " + (Common.getApplicationContext() == null));
    	this.organismViewData = (OrganismViewData) Common.getApplicationContext()
    								.getBean("theOrganismViewData");
    }
    
    public Action(Type type, int row, int column)
    {
    	this();
        this.actionType = type;
        this.actionRow = row;
        this.actionColumn = column;
    }
    
    @Override
    public String toString()
    {
    	return this.name;
    }
    
    public int getActionRow()
    {
        return this.actionRow;
    }
    
    public int getActionColumn()
    {
        return this.actionColumn;
    }
    
    public Type getActionType() 
    {
        return this.actionType;
    }
    
    public ActionResult performAction(Board board, 
    								  Board buffer,
    								  Organism organism)
    {
    	return null;
    }
    
}
