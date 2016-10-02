package neuralnet2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class TextDataIterator implements DataIterator {

	public ConfigSetup setup;
	public BufferedReader reader;
	
	public TextDataIterator()
	{
		
	}
	
	public TextDataIterator(ConfigSetup setup) throws IOException
	{
		setSetup(setup);
	}
	
	@Override
	public void setSetup(ConfigSetup setup) throws IOException {
		this.setup = setup;
		this.reader = new BufferedReader(new FileReader(new File(setup.dataFile)));
	}

	@Override
	public double[] next() throws IOException {
		String line = reader.readLine();
		String[] inputsS = line.split(",");
		double[] inputs = new double[inputsS.length];
		for(int i = 0; i < inputsS.length; i++)
		{
			inputs[i] = Double.parseDouble(inputsS[i]);
		}
		return inputs;
	}

	@Override
	public void rewind() throws IOException {
		reader.close();
		setSetup(this.setup);
	}

	@Override
	public boolean ready() throws IOException {
		return reader.ready();
	}

}
