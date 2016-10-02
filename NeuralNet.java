package neuralnet2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

class NeuralNet {
	public double learningConstant = .0001;
	private Node[][] nodes;
	private int numInputs;
	private int[] nodesPerLayer;
	private int numOutputsInFinalLayer;
	private int numWeights;
	public int maxTimesThrough = 50000;
	private double trainingThreshold = 0.00000001;
	public int diagnosticPeriod = 1000;
	
	public Node[][] getNodes() {
		return nodes;
	}

	public NeuralNet(int numInputs, int[] nodesPerLayer, int numOutputsInFinalLayer)
	{
		this.numInputs = numInputs;
		this.nodesPerLayer = nodesPerLayer;
		this.numOutputsInFinalLayer = numOutputsInFinalLayer;
		//construct nodes with nodesPerLayer.length + 1 to account for missing final layer
		nodes = new Node[nodesPerLayer.length + 1][]; 
		for(int i = 0; i < nodesPerLayer.length; i++)
		{
			nodes[i] = new Node[nodesPerLayer[i]];//size of each layer determined by nodesPerLayer array
		}
		//final layer of nodes is determined by numOutputInFinalLayer
		nodes[nodes.length-1] = new Node[numOutputsInFinalLayer];
		
		for(int j = 0; j < nodes[0].length; j++)
		{
			nodes[0][j] = new Node(numInputs);
		}
		for(int i = 1; i < nodes.length; i++)
		{
			for(int j = 0; j < nodes[i].length; j++)
			{
				nodes[i][j] = new Node(nodes[i-1].length);//receives information from all of the nodes in th previous layer
			}
		}	
		for(int layer = 0; layer < nodes.length; layer++)
		{
			for(int n = 0; n < nodes[layer].length; n++)
			{
				for(int w = 0; w < nodes[layer][n].getWeights().length; w++)
				{
					this.numWeights++;
				}
				this.numWeights++;
			}
		}
	}
	
	public double activationFunction(double input)
	{
		//return Math.tanh(input);//hyperbolic tangent of input
		
		//return (input > 0)?input:0;
		
		return (input >= 0)?(input):(Math.exp(input)-1);
		
		
//		if(input < 0)
//		{
//			return 0;
//		}
//		else if(input < 10)
//		{
//			return input;
//		}
//		else
//		{
//			return 10;
//		}
		
	}
	
	private double activationPrime(double a) 
	{
		//return 1 - activationFunction(a)*activationFunction(a);
		
		//return (a > 0)?1:0;
		
		return (a >= 0)?(1):(activationFunction(a) + 1); //here alpha is 1
		
//		if(a < 0)
//		{
//			return 0;
//		}
//		else if(a < 10)
//		{
//			return 1;
//		}
//		else
//		{
//			return 0;
//		}
	}
	private double outputActivation(double input)
	{
		return activationFunction(input);
	}
	private double outputActivationPrime(double input)
	{
		return 1;
	}
	
	public double[] feedForward(double[] inputs)
	{
		double[] recentOutputs = inputs;
		
		for(int i = 0; i < nodes.length; i++)
		{
			double[] newOutputs = new double[nodes[i].length]; 
			for(int j = 0; j < nodes[i].length; j++)
			{
				//calculates the outputs for row i, based on recentOuputs as inputs, and stores those in newOutputs
				if(i != nodes.length - 1)
				{
					newOutputs[j] = activationFunction(nodes[i][j].feedForward(recentOutputs));
				}
				else
				{
					newOutputs[j] = outputActivation(nodes[i][j].feedForward(recentOutputs));
				}
			}
			recentOutputs = newOutputs;
		}
		
		return recentOutputs;
	}

	//updates the weights
	public void trainOne(double[] inputs, double[] targets)
	{
		//Propagate Forward
		double[] results = feedForward(inputs);
		//Calculate Error for Output Nodes
		for(int i = 0; i < results.length; i++)
		{
			//System.out.println(results[i] - targets[i]);
			nodes[nodes.length-1][i].setError( (results[i]-targets[i]));//*activationPrime(nodes[nodes.length-1][i].getA()) );
			//System.out.println((results[i]-targets[i])*activationPrime(nodes[nodes.length-1][i].getA()));
			//System.out.println(nodes[nodes.length-1][i].getError());
		}
		//System.out.println(results[0] - targets[0]);
		//Back-propagate Error (note: nodes includes the results "layer" so we must subtract two to get to true second to last layer
		for(int layer = nodes.length-2; layer >= 0; layer--)
		{
			//loop through each node in the layer 
			for(int j = 0; j < nodes[layer].length; j++)
			{
				double accumulatedError = 0;
				//Summing up the accumulated errors
				for(int k = 0; k < nodes[layer+1].length; k++)
				{
					accumulatedError += activationPrime(nodes[layer][j].getA()) * nodes[layer+1][k].getWeight(j) * nodes[layer+1][k].getError();
					//System.out.println("Accumulated Error: " + nodes[layer][j].getA());
				}
				nodes[layer][j].setError(accumulatedError);
			}
		}
		
		//Update Weights
		for(int j = 0; j < nodes[0].length; j++)
		{
			for(int w = 0; w < nodes[0][j].getWeights().length; w++)
			{
				//System.out.println(nodes[0][j].getError());
				nodes[0][j].setBias(nodes[0][j].getBias() - nodes[0][j].getError() * this.learningConstant);
				nodes[0][j].setWeight(w, nodes[0][j].getWeight(w) - nodes[0][j].getError() * inputs[w] * this.learningConstant);
			}
		}
		//System.out.println(nodes[0][0].getError());// * inputs[0]);
		for(int layer = 1; layer < nodes.length; layer++)
		{
			for(int j = 0; j < nodes[layer].length; j++)
			{
				for(int w = 0; w < nodes[layer][j].getWeights().length; w++)
				{
					//System.out.println(nodes[layer][j].getError());
					nodes[layer][j].setBias(nodes[layer][j].getBias() - nodes[layer][j].getError() * this.learningConstant);
					nodes[layer][j].setWeight(w, nodes[layer][j].getWeight(w) - nodes[layer][j].getError() * activationFunction(nodes[layer - 1][w].getA()) * this.learningConstant);
				}
			}
		}
		//System.out.println(nodes[0][0].getWeight(0));
		//System.out.println("........................................");
		
	}
	
	
	public void train(double[][] inputs, double[][] desired)
	{
		train(inputs, desired, this.trainingThreshold, new PrintOut());
	}
	
	public void train(double[][] inputs, double[][] desired, double threshold)
	{
		train(inputs, desired, threshold, new PrintOut());
	}
	
	public void train(double[][] inputs, double[][] desired, OutputMethod output)
	{
		train(inputs, desired, this.trainingThreshold, output);
	}
	
	public void train(double[][] inputs, double[][] desired, double threshold, OutputMethod output)
	{
		Random R = new Random();
		int cases = desired.length;
		double[] lastWeights = null;
		double weightChange = 1;
		int timesThrough = 0;
		do
		{
			//System.out.println("FOO1" + diagnosticPeriod);
			if(lastWeights == null)
			{
				lastWeights = new double[this.numWeights];
				int index = 0;
				for(int layer = 0; layer < nodes.length; layer++)
				{
					for(int n = 0; n < nodes[layer].length; n++)
					{
						for(int w = 0; w < nodes[layer][n].getWeights().length; w++)
						{
							lastWeights[index] = nodes[layer][n].getWeight(w);
							index++;
						}
						lastWeights[index] = nodes[layer][n].getBias();
						index++;
					}
				}
			}
			ArrayList<Integer> training = new ArrayList<Integer>();
			for(int i = 0; i < cases/2; i++)
			{
				int r = R.nextInt(cases);
				if(training.contains(r))
				{
					i--;
					continue;
				}
				training.add(r);
			}
			double error = 0.0;
			for(int i = 0; i < inputs.length; i++)
			{
				double err = 0;
				double[] out = this.feedForward(inputs[i]);
				for(int j = 0; j < out.length; j++)
				{
					err += (desired[i][j] - out[j]) * (desired[i][j] - out[j]);
				}
				error += err;
				trainOne(inputs[i], desired[i]);
			}

			double[] curWeights = new double[this.numWeights];
			int index = 0;
			for(int layer = 0; layer < nodes.length; layer++)
			{
				for(int n = 0; n < nodes[layer].length; n++)
				{
					for(int w = 0; w < nodes[layer][n].getWeights().length; w++)
					{
						curWeights[index] = nodes[layer][n].getWeight(w);
						index++;
					}
					curWeights[index] = nodes[layer][n].getBias();
					index++;
				}
			}
			
			double numerator = 0.0;
			double denominator = 0.0;
			for(int i = 0; i < curWeights.length; i++)
			{
				numerator += Math.abs(curWeights[i] - lastWeights[i]);
				denominator += Math.abs(lastWeights[i]);
			}
			weightChange = numerator/denominator;
			lastWeights = curWeights;
			timesThrough++;
			//System.out.println((timesThrough % diagnosticPeriod == 0));
			if(timesThrough % diagnosticPeriod == 0)
			{
				String message = timesThrough + ": " + weightChange + ", " + (error)/inputs.length;
				//System.out.println("FOO");
				output.output(message);
				
			}
		} while (weightChange > threshold && timesThrough < this.maxTimesThrough);
		System.out.println(timesThrough);
	}
	
	/*
	 * System for writing weights. The first line has all of the information needed to create a net.
	 * From then on, each line is a weight.	
	 */
	public void writeWeights(String fileName)
	{
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(fileName)));
			String header = "" + this.numInputs + ".";
			for(int l : this.nodesPerLayer)
			{
				header += l + ",";
			}
			header = header.substring(0, header.length() - 1) + "." + this.numOutputsInFinalLayer;
			System.out.println(header);
			bw.write(header + "\n");
			
			for(int layer = 0; layer < this.nodes.length; layer++)
			{
				for(Node node : this.nodes[layer])
				{
					for(double weight : node.getWeights())
					{
						//System.out.println(weight);
						bw.write("" + weight + "\n");
					}
					bw.write("" + node.getBias() + "\n");
				}
			}
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/*
	 * Note: This assumes that this neural net is built with the same specifications as 
	 * the one in the file. Otherwise, it throws an error. 
	 *
	 */
	public void readInWeights(String fileName)
	{
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(fileName)));
			
			String header = br.readLine();
			String[] headerParts = header.split("\\.");
			int inputs = Integer.parseInt(headerParts[0]);
			if(inputs != this.numInputs)
			{ // ATTN
				System.out.println("PROBLEM. Input numbers not the same!");
				System.out.println("Input: " + inputs + ". Desired: " + this.numInputs);
				System.exit(0);
			}
			String[] nodesPerLayerS = headerParts[1].split(",");
			for(int i = 0; i < nodesPerLayerS.length; i++)
			{
				if(this.nodesPerLayer[i] != Integer.parseInt(nodesPerLayerS[i]))
				{
					System.out.println("PROBLEM. Net structure not the same!");
					System.exit(0);
				}
			}
			if(this.numOutputsInFinalLayer != Integer.parseInt(headerParts[2]))
			{
				System.out.println("PROBLEM. Nets have different numbers of outputs.");
				System.exit(0);
			}
			for(int layer = 0; layer < this.nodes.length; layer++)
			{
				for(int n = 0; n < this.nodes[layer].length; n++)
				{
					Node node = this.nodes[layer][n];
					for(int w = 0; w < node.getWeights().length; w++)
					{
						double myWeight = Double.parseDouble(br.readLine());
						node.setWeight(w, myWeight);
					}
					double bias = Double.parseDouble(br.readLine());
					node.setBias(bias);
				}
			}
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public double getThreshold()
	{
		return this.trainingThreshold;
	}
	
	public void setThreshold(double newThreshold)
	{
		this.trainingThreshold = newThreshold;
	}
}
