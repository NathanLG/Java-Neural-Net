package neuralnet2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;


public class ConfigSetup {
	
	private static HashMap<String, DataIterator> dataIterators = new HashMap<String, DataIterator>();
	static
	{
		dataIterators.put("MNIST", new MNISTDataIterator());
		dataIterators.put("text", new TextDataIterator());
	}
	
	private static HashMap<String, LabelIterator> labelIterators = new HashMap<String, LabelIterator>();
	static
	{
		labelIterators.put("MNIST", new MNISTLabelIterator());
		/*labelIterators.put("MNIST0", new MNISTLabelIterator(0));
		labelIterators.put("MNIST1", new MNISTLabelIterator(1));
		labelIterators.put("MNIST2", new MNISTLabelIterator(2));
		labelIterators.put("MNIST3", new MNISTLabelIterator(3));
		labelIterators.put("MNIST4", new MNISTLabelIterator(4));
		labelIterators.put("MNIST5", new MNISTLabelIterator(5));
		labelIterators.put("MNIST6", new MNISTLabelIterator(6));
		labelIterators.put("MNIST7", new MNISTLabelIterator(7));
		labelIterators.put("MNIST8", new MNISTLabelIterator(8));
		labelIterators.put("MNIST9", new MNISTLabelIterator(9));*/
		labelIterators.put("text", new TextLabelIterator());
	}
	
	private static HashMap<String, OutputMethod> outputMethods = new HashMap<String, OutputMethod>();
	static
	{
		outputMethods.put("print", new PrintOut());
	}
	
	private static HashMap<String, ConfigFunction> configFunctions = new HashMap<String, ConfigFunction>();
	static
	{
        configFunctions.put("task", new ConfigFunction ()
        {
            public void run(String[] args, ConfigSetup start)
            {
                if(args[0].toLowerCase().equals("test"))
                {
                	start.task = new ProcessTask(start);
                }
                else if(args[0].toLowerCase().equals("train"))
                {
                	start.task = new TrainTask(start);
                }
            }
            
        });
        
        configFunctions.put("weights", new ConfigFunction () 
        {
        	public void run(String[] args, ConfigSetup start)
        	{
        		if(args[0].equals("random"))
        		{
        			start.weights = WeightType.RANDOM;
        		}
        		else if(args[0].equals("file"))
        		{
        			start.weights = WeightType.FILE;
        			start.getN().readInWeights(args[1]);
        		}
        	}
        });
            
        configFunctions.put("inputs", new ConfigFunction ()
        {
        	public void run(String[] args, ConfigSetup start) throws IOException
        	{
        		start.dataIterator = ConfigSetup.dataIterators.get(args[0]);
        		start.dataFile = args[1];
        		start.dataIterator.setSetup(start);
        	}
        });
        
        configFunctions.put("labels", new ConfigFunction ()
        {
        	public void run(String[] args, ConfigSetup start) throws IOException
        	{
        		start.labelIterator = ConfigSetup.labelIterators.get(args[0]);
        		start.labelFile = args[1];
        		start.labelIterator.setSetup(start);
        	}
        });
        
        configFunctions.put("output", new ConfigFunction ()
        {
        	public void run(String[] args, ConfigSetup start)
        	{
        		start.logOutput = ConfigSetup.outputMethods.get(args[0]);
        		start.logOutput.setSetup(start);
        		if(args.length >= 2)
        		{
        			start.outputFile = args[1];
        		}
        		
        	}
        });
        
        configFunctions.put("threshold", new ConfigFunction ()
        {
        	public void run(String[] args, ConfigSetup start)
        	{
        		start.trainingThreshold = Double.parseDouble(args[0]);
        	}
        });
        
        configFunctions.put("outputWeights", new ConfigFunction()
        {
        	public void run(String[] args, ConfigSetup start)
        	{
        		start.weightOutFile = args[0];
        	}
        });
        
        configFunctions.put("diagnosticPeriod", new ConfigFunction()
        {
        	public void run(String[] args, ConfigSetup start)
        	{
        		start.getN().diagnosticPeriod = Integer.parseInt(args[0]);
        	}
        });

        configFunctions.put("maxTimesThrough", new ConfigFunction()
        {
        	public void run(String[] args, ConfigSetup start)
        	{
        		start.getN().maxTimesThrough = Integer.parseInt(args[0]);
        	}
        });
        
        configFunctions.put("learningConstant", new ConfigFunction()
        {
        	public void run(String[] args, ConfigSetup start)
        	{
        		start.getN().learningConstant = Double.parseDouble(args[0]);
        	}
        });
	}
	
	private Task task;

    public enum WeightType { RANDOM, FILE };
	private WeightType weights;
	private String weightFile = null;
	
	public DataIterator dataIterator;
	public String dataFile;
	
	public LabelIterator labelIterator;
	public String labelFile;
	
	public OutputMethod logOutput;
	public String outputFile;
	
	public String weightOutFile;
	
	public Double trainingThreshold;
	
	private int inputs;
	private int[] nodesPerLayer;
	private int outputs;
	private NeuralNet n;
	
	public ConfigSetup(String configFile) throws IOException
	{
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(configFile)));
			String header = br.readLine();
			String[] headerParts = header.split("\\.");
			this.inputs = Integer.parseInt(headerParts[0]);
			if(!headerParts[1].equals(""))
			{
				String[] nodesPerLayerS = headerParts[1].split(",");
				this.nodesPerLayer = new int[nodesPerLayerS.length];
				for(int i = 0; i < nodesPerLayerS.length; i++)
				{
					nodesPerLayer[i] = Integer.parseInt(nodesPerLayerS[i]);
				}
			}
			this.outputs = Integer.parseInt(headerParts[2]);
			
			this.n = new NeuralNet(this.inputs, this.nodesPerLayer, this.outputs);
			
			while(br.ready())
			{
				//System.out.println("foo");
				// Each line is <command>.<inputs to the function (CSV format w/ no spaces)
				String nextLine = br.readLine();
				String[] cmdInputs = nextLine.split(":");
				String[] args = cmdInputs[1].split(",");
				configFunctions.get(cmdInputs[0]).run(args, this);
			}
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.task.run();
	}
	
	public NeuralNet getN()
	{
		return this.n;
	}
}
