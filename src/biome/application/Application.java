package biome.application;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


import biome.controller.core.SimulationEngine;
import biome.view.View;

public class Application 
{
	/**
	 * Main method
	 * 
	 * @param args commain line arguments
	 */
	public static void main(String[] args) 
	{	
		View view = (View) Common.BEAN_CONTEXT.getBean("theView"); 
		SimulationEngine engine = (SimulationEngine)  Common.BEAN_CONTEXT.getBean("theEngine");
		
		engine.initPopulation();
		
		view.start();
	}
}
