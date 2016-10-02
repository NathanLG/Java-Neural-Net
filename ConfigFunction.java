package neuralnet2;

import java.io.IOException;

public abstract class ConfigFunction {
	public abstract void run(String[] args, ConfigSetup start) throws IOException;
}
