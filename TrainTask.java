package neuralnet2;

import java.io.IOException;
import java.util.ArrayList;

public class TrainTask implements Task {

	private ConfigSetup setup;
	
	public TrainTask(ConfigSetup setup)
	{
		this.setup = setup;
	}
	
	@Override
	public void run() throws IOException 
	{
		ArrayList<double[]> inputsAL = new ArrayList<double[]>();
		ArrayList<double[]> desiredAL = new ArrayList<double[]>();
		while(setup.dataIterator.ready())
		{
			inputsAL.add(setup.dataIterator.next());
			//System.out.println(inputsAL.get(inputsAL.size() - 1));
			desiredAL.add(setup.labelIterator.next());
		}
		double[][] inputs = new double[inputsAL.size()][inputsAL.get(0).length];
		for(int i = 0; i < inputsAL.size(); i++)
		{
			for(int j = 0; j < inputsAL.get(i).length; j++)
			{
				inputs[i][j] = inputsAL.get(i)[j];
			}
		}
		double[][] desired = new double[desiredAL.size()][desiredAL.get(0).length];
		for(int i = 0; i < desiredAL.size(); i++)
		{
			for(int j = 0; j < desiredAL.get(i).length; j++)
			{
				desired[i][j] = desiredAL.get(i)[j];
			}
		}

		System.out.println("Inputs: " + inputs.length + ". Desired: " + desired.length);
		if(setup.logOutput != null && setup.trainingThreshold != null)
			setup.getN().train(inputs, desired, setup.trainingThreshold, setup.logOutput);
		else if(setup.logOutput != null)
			setup.getN().train(inputs, desired, setup.logOutput);
		else if(setup.trainingThreshold != null)
			setup.getN().train(inputs, desired, setup.trainingThreshold);
		else
		{
			setup.getN().train(inputs, desired);
		}
		if(setup.weightOutFile != null)
		{
			setup.getN().writeWeights(setup.weightOutFile);
		}
	}

}
