package neuralnet2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class TextLabelIterator implements LabelIterator {

	public ConfigSetup setup;
	public BufferedReader reader;
	
	public TextLabelIterator()
	{
		
	}
	
	public TextLabelIterator(ConfigSetup setup) throws IOException
	{
		setSetup(setup);
	}
	
	@Override
	public void setSetup(ConfigSetup setup) throws IOException {
		this.setup = setup;
		this.reader = new BufferedReader(new FileReader(new File(setup.labelFile)));
	}

	@Override
	public double[] next() throws IOException {
		String line = reader.readLine();
		String[] labelsS = line.split(",");
		double[] labels = new double[labelsS.length];
		for(int i = 0; i < labelsS.length; i++)
		{
			labels[i] = Double.parseDouble(labelsS[i]);
		}
		return labels;
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
