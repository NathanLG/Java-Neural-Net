package neuralnet2;

import java.io.IOException;

public class ProcessTask implements Task
{
	private ConfigSetup config;
	
	public ProcessTask(ConfigSetup config)
	{
		this.config = config;
	}
	
	@Override
	public void run() throws IOException
	{
		OutputMethod om = new PrintOut();
		if(config.logOutput != null)
		{
			om = config.logOutput;
		}
		while(config.dataIterator.ready())
		{
			double[] result = config.getN().feedForward(config.dataIterator.next());
			String message = "";
			if(config.labelIterator != null)
			{
				double[] desired = config.labelIterator.next();
				for(int i = 0; i < result.length; i++)
				{
					message += "Output was " + result[i] + ", and it should have been " + desired[i] + ". ";
				}
				System.out.println("___________________________");
			}
			else
			{
				double[] desired = config.labelIterator.next();
				for(int i = 0; i < result.length; i++)
				{
					message += "Output was " + result[i] + ".";
				}
			}
			om.output(message);
		}
	}
}
