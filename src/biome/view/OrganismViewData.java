package biome.view;

import java.util.HashMap;
import java.util.Map;

import pair.Pair;

import biome.model.organisms.Organism;

public class OrganismViewData {
	
	/**
	 * Maps every organism to a OrganismViewData object for rendering
	 */
	private Map<Organism, OrganismView> organismViewDataStore;
	
	public OrganismViewData()
	{
		organismViewDataStore = new HashMap<>();
	}
	
	public void addOrganism(Organism org)
	{
		OrganismView vData = new OrganismView(org);
		this.organismViewDataStore.put(org, vData);
		this.resetOrganismView(org);
	}
	
	public void removeOrganism(Organism org)
	{
		this.organismViewDataStore.remove(org);
	}
	
	public OrganismView getOrganismViewData(Organism org)
	{
		return this.organismViewDataStore.get(org);
	}
	
	public float getYOffset(Organism org)
	{
		return this.organismViewDataStore.get(org).getYOffSet();
	}
	
	public float getZOffset(Organism org)
	{
		return this.organismViewDataStore.get(org).getZOffSet();
	}
	
	public float getXOffset(Organism org)
	{
		return this.organismViewDataStore.get(org).getXOffSet();
	}
	
	public float getRotationAngle(Organism org)
	{
		return this.organismViewDataStore.get(org).getRotationAngle();
	}
	
	public Pair<Float, Float> getOrigin(Organism org)
	{
		return this.organismViewDataStore.get(org).getOrigin();
	}
	
	public Pair<Float, Float> getDestination(Organism org)
	{
		return this.organismViewDataStore.get(org).getDestination();
	}
	
	public void resetOrganismView(Organism org) {
				
		Pair<Float, Float> origin = new Pair<Float, Float>();
        origin.setFirst((float) org.getRow());
        origin.setSecond((float) org.getColumn());
        
        OrganismView vData = organismViewDataStore.get(org);
        vData.setOrigin(origin);
        vData.setDestination(origin);
        vData.calculateRotationAngle();   
     }
	
	
	/*
	 * TODO REFACTOR
	 */
	public void setViewData(Organism org, OrganismView vData)
	{		
		this.organismViewDataStore.put(org, vData);
	}

	@Override
	public String toString() {
		return this.organismViewDataStore.toString();
	}
	
}
