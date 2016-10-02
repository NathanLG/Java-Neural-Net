package neuralnet2;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class MNISTLabelIterator implements LabelIterator {

	private int digit;
	private ConfigSetup setup;
	private FileInputStream inputStream;
	
	public MNISTLabelIterator()
	{
		
	}
	
	public MNISTLabelIterator(int digit)
	{
		this.digit = digit;
	}
	
	public MNISTLabelIterator(int digit, ConfigSetup setup) throws IOException
	{
		this.digit = digit;
		setSetup(setup);
	}
	
	public void setSetup(ConfigSetup setup) throws IOException
	{
		this.setup = setup;if(inputStream != null)
		{
			inputStream.close();
		}
		inputStream = new FileInputStream(new File(setup.labelFile));
		inputStream.skip(8);
	}
	
	@Override
	public double[] next() throws IOException 
	{
		double[] result = new double[] {inputStream.read()};
		return result;
	}

	@Override
	public void rewind() throws IOException 
	{
		setSetup(this.setup);
	}

	@Override
	public boolean ready() throws IOException 
	{
		return (inputStream.available() >= 1);
	}

}
