package biome.application;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import biome.model.organismattributes.Diet;
import biome.model.organisms.Animal;
import biome.view.Common;
import biome.view.View;

public class SliderChangeListener //implements ChangeListener
{
	/*
	public final static int CARNIVORE_SPEED = 0;
	public final static int CARNIVORE_VISION = 1;
	public final static int HERBIVORE_SPEED = 2;
	public final static int HERBIVORE_VISION = 3;
	
	private int attribute;
	
	private View app;
	
	public SliderChangeListener(int attribute, View app)
	{
		this.attribute = attribute;
		this.app = app;
	}
	
	
	@Override
	public void stateChanged(ChangeEvent e)
	{	
		JSlider source = (JSlider)e.getSource();
        
        if (!source.getValueIsAdjusting()) 
        {
            int value = (int)source.getValue();

			switch(this.attribute)
			{
			case CARNIVORE_SPEED:
				Common.CARNIVORE_SPEED = value;
				break;
			case CARNIVORE_VISION:
				Common.CARNIVORE_VISION = value;
				break;
			case HERBIVORE_VISION:
				Common.HERBIVORE_VISION = value;
				break;
			case HERBIVORE_SPEED:
				Common.HERBIVORE_SPEED = value;
				break;
			}
        }
        
        for (Animal an : app.getAnimals())
        {
        	if (an.getDiet().getValue() == Diet.DIET_HERBIVORE)
        	{
        		an.setVision(Common.HERBIVORE_VISION);
        		an.setSpeed(Common.HERBIVORE_SPEED);
        	}
        	else if (an.getDiet().getValue() == Diet.DIET_CARNIVORE)
        	{
        		an.setVision(Common.CARNIVORE_VISION);
        		an.setSpeed(Common.CARNIVORE_SPEED);
        	}
        }
	}
 */
}
