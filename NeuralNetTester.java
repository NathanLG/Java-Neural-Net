package neuralnet2;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JFrame;


public class NeuralNetTester {
	
	public static final double MIN_ACT = -1;
	public static final double MAX_ACT = 1;
	public static final Random r = new Random();
	public static final double ALPHA = r.nextDouble() + r.nextInt(15);
	public static final double BETA = r.nextDouble() + r.nextInt(15);
	public static final double GAMMA = r.nextDouble() + r.nextInt(50);

	public static final double ALPHA2 = r.nextDouble() + r.nextInt(15);
	public static final double BETA2 = r.nextDouble() + r.nextInt(15);
	public static final double GAMMA2 = r.nextDouble() + r.nextInt(50);
	//public static final double slope = r.nextDouble() + r.nextInt(10);

	public static String testInput(double x, double y, NeuralNet n)
	{
		double result = n.feedForward(new double[]{x, y})[0];
		
		String readout = "The neural net, working on (" + x + ", " + y + ") returned " + result;
		
		if(testParameter(x,y))
		{
			readout += " when it should have returned " + MAX_ACT + " which is";
			if(result > 0)
				readout += " correct.";
			else
				readout += " wrong.";
		}
		else
		{
			readout += " when it should have returned " + MIN_ACT + " which is";
			if(result < 0)
				readout += " correct.";
			else
				readout += " wrong.";
		}
		
		return readout;
	}
	
	
	
	public static boolean testParameter(double x, double y)
	{
		//if(ALPHA * x + BETA * y > GAMMA || ALPHA2*x + BETA2*y > GAMMA2)
		//if((x-20)*(x-20) + (y-20)*(y-20) < 625)
		if(x*x < y)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public static double[][][] generateTestSet(int xLowBound, int xHighBound, int yLowBound, int yHighBound, int howMany)
	{
		double[][][] result = new double[2][howMany][];
		Random r = new Random(5);
		for(int i = 0; i < howMany; i++)
		{
			//generate the locations of the random points and set them to x and y
			int leftShift = 0 - xLowBound;
			int downShift = 0 - yLowBound;
			
			double x = r.nextInt(xHighBound - xLowBound) + r.nextDouble() - 1;
			double y = r.nextInt(yHighBound - yLowBound) + r.nextDouble() - 1;
			
			x -= leftShift;
			y -= downShift;
			
			result[0][i] = new double[2];
			result[1][i] = new double[1];
			
			result[0][i][0] = x;
			result[0][i][1] = y;
			
			if(testParameter(x, y))
			{
				result[1][i][0] = MAX_ACT;
			}
			else
			{
				result[1][i][0] = MIN_ACT;
			}
		}
		return result;
	}
	
	public static void main(String[] args) {

		//(o) -> (c 
		
		int[] nodesPerLayer = {8};
		NeuralNet n = new NeuralNet(2, nodesPerLayer, 1);
		
		double[][] inputs = new double[64][2];
		double[][] desired = new double[64][1];
		
		double[][][] result = generateTestSet(-100, 100, -100, 100, 2048);
		inputs = result[0];
		desired = result[1];
		
		for(Node[] nodes : n.getNodes())
		{
			for(Node node : nodes)
			{
				for(double weight : node.getWeights())
				{
					System.out.println("The weight is: " + weight + ".");
				}
			}
		}	
		
		/*n.getNodes()[0][0].setWeight(0, ALPHA*1000);
		n.getNodes()[0][0].setWeight(1, BETA*1000);
		n.getNodes()[0][0].setBias(-GAMMA*1000);
		
		n.getNodes()[0][1].setWeight(0, ALPHA2*1000);
		n.getNodes()[0][1].setWeight(1, BETA2*1000);
		n.getNodes()[0][1].setBias(-GAMMA2*1000);
		
		n.getNodes()[1][0].setWeight(0, 1000);
		n.getNodes()[1][0].setWeight(1, 1000);*/
		
		//n.getNodes()[0
		
		n.train(inputs, desired, 0.0000000000000000001);
		n.writeWeights("foo.txt");
		//n.readInWeights("foo.txt");
		System.out.println("--------------------------------");
		System.out.println("Alpha: " + ALPHA + ", Beta: " + BETA + ", Gamma: " + GAMMA);
		System.out.println("Alpha2: " + ALPHA2 + ", Beta2: " + BETA2 + ", Gamma2: " + GAMMA2);
		
		for(Node[] nodes : n.getNodes())
		{
			for(Node node : nodes)
			{
				for(double weight : node.getWeights())
				{
					System.out.println("The weight is: " + weight + ".");
				}
				System.out.println("Bias: " + node.getBias());
				System.out.println("n________________");
			}
			System.out.println("l______________");
			
		}
		
		//test a grid of values and observe the results
		double xMin = -50;
		double xMax = 50;
		double yMin = -50;
		double yMax = 50;
		
		generateGraph(xMin, xMax, yMin, yMax, n);
		
		Scanner in = new Scanner(System.in);
		
		while(true)
		{
			System.out.println("Please enter xMin for zoom.");
			xMin = in.nextDouble();
			System.out.println("Please enter xMax for zoom.");
			xMax = in.nextDouble();
			System.out.println("Please enter yMin for zoom.");
			yMin = in.nextDouble();
			System.out.println("Please enter yMax for zoom.");
			yMax = in.nextDouble();
			
			generateGraph(xMin, xMax, yMin, yMax, n);
		}
		

//		System.out.println(testInput(2.0, 1.0, n));
//		System.out.println(testInput(5.0, 1.0, n));
//		System.out.println(testInput(1.0, 5.0, n));
//		System.out.println(testInput(4.0, 5.0, n));
//		System.out.println(testInput(1.0, 10.0, n));
//		System.out.println(testInput(36.0, 17.0, n));
//		System.out.println(testInput(4.0, 4.1, n));
//		System.out.println(testInput(4.0, 4.0, n));
//		System.out.println(testInput(10.0, 0, n));
//		System.out.println(testInput(10.0, -8, n));
//		System.out.println(testInput(1000.0, -3000, n));
//		System.out.println(testInput(2000.0, -1990, n));
//		System.out.println(testInput(-2.0, -20, n));
//		System.out.println(testInput(-3000, -3, n));
//		System.out.println(testInput(-2000.0, 5.0, n));
//		System.out.println(testInput(-2.0, 1.0, n));
		
		
//		System.out.println(n.feedForward(new double[]{2.0, 1.0})[0]);
//		System.out.println(n.feedForward(new double[]{-2.0, 1.0})[0]);
//		System.out.println(n.feedForward(new double[]{5.0, 2.0})[0]);
//		System.out.println(n.feedForward(new double[]{1.0, 5.0})[0]);
//		System.out.println(n.feedForward(new double[]{4.0, 5.0})[0]);
//		System.out.println(n.feedForward(new double[]{1.0, 10.0})[0]);
//		System.out.println(n.feedForward(new double[]{20000.0, 1.0})[0]);
//		System.out.println(n.feedForward(new double[]{1.0, 1.0})[0]);
		/*double[] inputs = {100,200};
		System.out.println("The result is: " + n.feedForward(inputs)[0] + ".");
		
		for(Node[] nodes : n.getNodes())
		{
			for(Node node : nodes)
			{
				for(double weight : node.getWeights())
				{
					System.out.println("The weight is: " + weight + ".");
				}
			}
		}
		n.trainOne(inputs, new double[] {1});
		System.out.println("--------------------------------");
		System.out.println("The result is: " + n.feedForward(inputs)[0] + ".");
		for(Node[] nodes : n.getNodes())
		{
			for(Node node : nodes)
			{
				for(double weight : node.getWeights())
				{
					System.out.println("The weight is: " + weight + ".");
				}
			}
		}
	}*/

	}
	
	private static void generateGraph(double xMin, double xMax, double yMin, double yMax, NeuralNet n)
	{
		//ALERT: DOES NOT FUNTION PROPERLY WITH NON-INTEGER MAX AND MINS, OR 0 MAX AND MINS
		
		final int pixelWidth = 500;
		final int pixelHeight = 500;
		
		final int pixelsPerPoint = 10;
		
		//System.out.println("There should be " + pixelWidth / pixelsPerPoint + " nodes.");
		
		final double xRes = (xMax - xMin) / ((double) pixelWidth / pixelsPerPoint);
		final double yRes = (yMax - yMin) / ((double) pixelHeight / pixelsPerPoint);
		
		//System.out.println("So each node shall be " + (xMax - xMin) / ((double) pixelWidth / pixelsPerPoint) + " units apart.");
		
		ArrayList<ColorPoint> results = new ArrayList<ColorPoint>();
		
		for(double x = xMin; x <= xMax; x += xRes)
		{
			for(double y = yMin; y <= yMax; y += yRes)
			{
				if(testParameter(x, y))
				{
					if(n.feedForward(new double[]{(double) x, (double) y})[0] > 0)
					{
						results.add(new ColorPoint(x, y, Color.green));
					}
					else
					{
						results.add(new ColorPoint(x, y, Color.red));
					}
				}
				else
				{
					if(n.feedForward(new double[]{(double) x, (double) y})[0] < 0)
					{
						results.add(new ColorPoint(x, y, Color.blue));
					}
					else
					{
						results.add(new ColorPoint(x, y, Color.orange));
					}
				}
			}
		}
		
		Grapher graph = new Grapher(xMin, xMax, yMin, yMax, results.toArray(new ColorPoint[0]), 1, (int) (pixelsPerPoint * 0.8));
		JFrame application = new JFrame();
                                                                      
        application.add(graph);           


        application.setSize(pixelWidth, pixelHeight);        
        application.setVisible(true);  
	}
}
