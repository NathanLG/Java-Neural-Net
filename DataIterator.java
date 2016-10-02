package neuralnet2;

import java.io.IOException;

public interface DataIterator {

	public abstract void setSetup(ConfigSetup setup) throws IOException;
	public abstract double[] next() throws IOException;
	public abstract void rewind() throws IOException;
	public abstract boolean ready() throws IOException;
}
