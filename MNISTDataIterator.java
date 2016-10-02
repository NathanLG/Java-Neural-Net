package neuralnet2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MNISTDataIterator implements DataIterator {

	private ConfigSetup setup;
	private FileInputStream inputStream;
	
	public MNISTDataIterator()
	{
		
	}
	
	public MNISTDataIterator(ConfigSetup setup) throws IOException
	{
		setSetup(setup);
	}
	
	public void setSetup(ConfigSetup setup) throws IOException
	{
		this.setup = setup;
		if(inputStream != null)
		{
			inputStream.close();
		}
		inputStream = new FileInputStream(new File(setup.dataFile));
		inputStream.skip(16);
	}
	
	@Override
	public double[] next() throws IOException
	{
		double[] inputs = new double[784];
		for(int i = 0; i < 784; i++)
		{
			inputs[i] = inputStream.read();
		}
		/*for(int x = 0; x < 28; x++)
		{
			for(int y = 0; y < 28; y++)
			{
				System.out.printf("%3f ", inputs[y + 28*x]);
			}
			System.out.println();
		}
		System.out.println();
		System.out.println();
		System.out.println();*/
			
		return inputs;
	}

	@Override
	public void rewind() throws IOException 
	{
		setSetup(this.setup);
	}

	@Override
	public boolean ready() throws IOException 
	{
		return (inputStream.available() >= 784);
	}

}
