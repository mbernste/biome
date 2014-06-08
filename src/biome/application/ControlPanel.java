
 
package biome.application;
 
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.*;
 
/*
 * SliderDemo.java requires all the files in the images/doggy
 * directory.
 */
@SuppressWarnings("serial")
public class ControlPanel extends JPanel
                        implements ActionListener,
                                   WindowListener
{
    //Set up slider parameters.
    static final int SPEED_MIN = 1;
    static final int SPEED_MAX = 30;
    static final int VISION_MIN = 1;
    static final int VISION_MAX = 30;
    
    static final int CARNIVORE_SPEED_INIT = 6;
    static final int HERBIVORE_SPEED_INIT = 4;
    static final int CARNIVORE_VISON_INIT = 20;
    static final int HERBIVORE_VISION_INIT = 10;
    
    //This label uses ImageIcon to show the doggy pictures.
    JLabel picture;
    Application app;
 
    public ControlPanel(Application app) 
    {
    	ArrayList<JLabel> labels = new ArrayList<JLabel>();
    	ArrayList<JSlider> sliders = new ArrayList<JSlider>();
    	
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
 
         JLabel cSpeedLabel = new JLabel("Carnivore Speed", JLabel.CENTER);
         cSpeedLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
         labels.add(cSpeedLabel);
         
         JLabel hSpeedLabel = new JLabel("Herbivore Speed", JLabel.CENTER);
         cSpeedLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
         labels.add(hSpeedLabel);
         
         JLabel cVisionLabel = new JLabel("Carnivore Vision", JLabel.CENTER);
         cSpeedLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
         labels.add(cVisionLabel);
         
         JLabel hVisionLabel = new JLabel("Herbivore Vision", JLabel.CENTER);
         cSpeedLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
         labels.add(hVisionLabel);
        
 
        // Create sliders
        JSlider carnivorSpeedSlider = new JSlider(JSlider.HORIZONTAL,
                                              	SPEED_MIN, SPEED_MAX, CARNIVORE_SPEED_INIT);
        sliders.add(carnivorSpeedSlider);
        
        JSlider herbivoreSpeedSlider = new JSlider(JSlider.HORIZONTAL,
                								SPEED_MIN, SPEED_MAX, HERBIVORE_SPEED_INIT);
        sliders.add(herbivoreSpeedSlider);
        
        JSlider carnivoreVisionSlider = new JSlider(JSlider.HORIZONTAL,
												VISION_MIN, VISION_MAX, HERBIVORE_VISION_INIT);
        sliders.add(carnivoreVisionSlider);
        
        JSlider herbivoreVisionSlider = new JSlider(JSlider.HORIZONTAL,
												VISION_MIN, VISION_MAX, HERBIVORE_VISION_INIT);
        sliders.add(herbivoreVisionSlider);
        
        carnivorSpeedSlider.addChangeListener(new SliderChangeListener(SliderChangeListener.CARNIVORE_SPEED, app));
        herbivoreSpeedSlider.addChangeListener(new SliderChangeListener(SliderChangeListener.HERBIVORE_SPEED, app));
        carnivoreVisionSlider.addChangeListener(new SliderChangeListener(SliderChangeListener.CARNIVORE_VISION, app));
        herbivoreVisionSlider.addChangeListener(new SliderChangeListener(SliderChangeListener.HERBIVORE_VISION, app));
 
        // Turn on labels at major tick marks
        Font font = new Font("Serif", Font.ITALIC, 15);
        
        for (JSlider slider : sliders)
        {
        	 slider.setMajorTickSpacing(10);
             slider.setMinorTickSpacing(1);
             slider.setPaintTicks(true);
             slider.setPaintLabels(true);
             slider.setBorder(
                     BorderFactory.createEmptyBorder(0,0,10,0));
             slider.setFont(font);             
        }
        
        for (int i = 0; i < 4; i++)
        {
        	add(labels.get(i));
        	add(sliders.get(i));
        }
       
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
    }
 
    /** Add a listener for window events. */
    void addWindowListener(Window w) 
    {
        w.addWindowListener(this);
    }
 
   
    public void windowOpened(WindowEvent e) {}
    public void windowClosing(WindowEvent e) {}
    public void windowClosed(WindowEvent e) {}
    public void windowActivated(WindowEvent e) {}
    public void windowDeactivated(WindowEvent e) {}
 
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    public static void createAndShowGUI(Application app) 
    {
        //Create and set up the window.
        JFrame frame = new JFrame("Control Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ControlPanel gui = new ControlPanel(app);
                 
        //Add content to the window.
        frame.add(gui, BorderLayout.CENTER);
 
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
 
	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}
