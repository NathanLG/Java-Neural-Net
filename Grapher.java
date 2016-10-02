package neuralnet2;

import javax.swing.JFrame;
import java.awt.Graphics;
import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.BorderLayout; 

public class Grapher extends JPanel {
	
	private double xMin;
	private double xMax;
	private double yMin;
	private double yMax;
	private ColorPoint[] points;
	private double graphSquareSize;
	private int circleRadius;
	
	private int width;
	private int height;
	
	public Grapher(double xMin, double xMax, double yMin, double yMax, ColorPoint[] points, double graphSquareSize, int circleRadius) // set up graphics window
	{
		//setup
		super();
		setBackground(Color.WHITE);
		
		//initialize global variables
		this.xMin = xMin;
		this.xMax = xMax;
		this.yMin = yMin;
		this.yMax = yMax;
		this.points = points;
		this.graphSquareSize = graphSquareSize;
		this.circleRadius = circleRadius;
	}
	
	public void paintComponent(Graphics g)
	{
		width = getWidth();             // width of window in pixels
	    height = getHeight();           // height of window in pixels
		
		super.paintComponent(g); // call superclass to make panel display correctly
		
		drawAxes(g);
		drawGrid(g);
		drawPoints(g);
	}
	
	private void drawAxes(Graphics g)
	{
		g.setColor(Color.black);
		
		//draw x axis	
		g.fillRect(0, getOriginYInPixels(), width, 3);
		//System.out.println("Y orign: " + getOriginYInPixels());
		
		//draw y axis
		g.fillRect(getOriginXInPixels(), 0, 3, height);
		//System.out.println("X origin: " + getOriginXInPixels());
	}
	
	private void drawGrid(Graphics g)
	{
		g.setColor(Color.black);
		
		//draw vertical lines
		int xUnitInPixels = (int) (width / (xMax - xMin));
		int initialLeftOffsetInUnits;
		
		if(xMax > 0)
		{
			initialLeftOffsetInUnits =  (int) (graphSquareSize - (xMin % graphSquareSize));
		}
		else
		{
			initialLeftOffsetInUnits = (int) (Math.abs(xMin) % graphSquareSize);
		}
		
		for(int x = initialLeftOffsetInUnits * xUnitInPixels; x < width; x += xUnitInPixels * graphSquareSize)
		{
			g.drawLine(x, 0, x, height);
		}
		
		//draw horizontal lines
		int yUnitInPixels = (int) (height / (yMax - yMin));
		int initialTopOffsetInUnits;
		
		if(yMax > 0)
		{
			initialTopOffsetInUnits = (int) (graphSquareSize - (yMin % graphSquareSize));
		}
		else
		{
			initialTopOffsetInUnits = (int) (Math.abs(yMin) % graphSquareSize);
		}
		
		for(int y = initialTopOffsetInUnits * yUnitInPixels; y < height; y += yUnitInPixels * graphSquareSize)
		{
			g.drawLine(0, y, width, y);
		}
		
		//System.out.println("Lines drawn.");
	}
	
	private void drawPoints(Graphics g)
	{
		int originXInPixels = getOriginXInPixels();
		int originYInPixels = getOriginYInPixels();
		int xUnitInPixels = (int) (width / ((double) xMax - xMin));
		int yUnitInPixels = (int) (height / ((double) yMax - yMin));
		
		for(ColorPoint p : points)
		{
			g.setColor(p.getC());
			g.fillOval((int) ((originXInPixels + (p.getX() * xUnitInPixels)) - (circleRadius/2.0)), (int) ((originYInPixels - (p.getY() * yUnitInPixels)) - circleRadius/2.0), circleRadius, circleRadius);
		}
	}
	
	private int getOriginXInPixels()
	{
		if(xMin > 0)
		{
			//System.out.println("Case 1 (x)");
			return (int) (-((xMin - 0) * (width/((double) xMax - xMin))));
		}
		else if(xMin < 0 && 0 < xMax)
		{
			//System.out.println("Case 2 (x)");
			return (int) ((0 - xMin) * (width/((double) xMax - xMin)));
		}
		else if(xMax < 0)
		{
			//System.out.println("Case 3 (x)");
			return (int) (width + ((0 - xMin) * (width/((double) xMax - xMin))));
		}
		else if(xMin == 0)
		{
			//System.out.println("Case 4 (x)");
			return 0;
		}
		else if(xMax == 0)
		{
			//System.out.println("Case 5 (x)");
			return width;
		}
		else
		{
			//System.out.println("Case 6 (x)");
			return Integer.MIN_VALUE;
		}
	}
	private int getOriginYInPixels()
	{
		if(yMax < 0)
		{
			//System.out.println("Case 1 (y)");
			return (int) (-((0 - yMax) * (height/((double) yMax - yMin))));
		}
		else if(yMin < 0 && 0 < yMax)
		{
			//System.out.println("Case 2 (y)");
			return (int) ((yMax - 0) * (height/((double) yMax - yMin)));
		}
		else if(yMin > 0)
		{
			//System.out.println("Case 3 (y)");
			return (int) (height + (yMin - 0) * (height/((double) yMax - yMin)));
		}
		else if(yMax == 0)
		{
			//System.out.println("Case 4 (y)");
			return 0;
		}
		else if(yMin == 0)
		{
			//System.out.println("Case 5 (y)");
			return height;
		}
		else
		{
			//System.out.println("Case 6 (y)");
			return Integer.MIN_VALUE;
		}
	}
	
	public static void main(String[] args)
    {
        Grapher panel = new Grapher(-3, 3, -3, 3, new ColorPoint[]{new ColorPoint(1, 1, Color.CYAN), new ColorPoint(-2, -3, Color.red)}, 1, 20);                            // window for drawing
        JFrame application = new JFrame();                            // the program itself
        
        application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   // set frame to exit
                                                                      // when it is closed
        application.add(panel);           


        application.setSize(500, 400);         // window is 500 pixels wide, 400 high
        application.setVisible(true);          
    }

}
