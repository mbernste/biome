package biome.application;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import biome.organisms.Animal;
import biome.organisms.attributes.Diet;
import biome.view.Common;

public class SliderChangeListener implements ChangeListener
{
	public final static int CARNIVORE_SPEED = 0;
	public final static int CARNIVORE_VISION = 1;
	public final static int HERBIVORE_SPEED = 2;
	public final static int HERBIVORE_VISION = 3;
	
	private int attribute;
	
	private Application app;
	
	public SliderChangeListener(int attribute, Application app)
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
        	if (an.getDiet() == Diet.DIET_HERBIVORE)
        	{
        		an.setVision(Common.HERBIVORE_VISION);
        		an.setSpeed(Common.HERBIVORE_SPEED);
        	}
        	else if (an.getDiet() == Diet.DIET_CARNIVORE)
        	{
        		an.setVision(Common.CARNIVORE_VISION);
        		an.setSpeed(Common.CARNIVORE_SPEED);
        	}
        }
	}

}
