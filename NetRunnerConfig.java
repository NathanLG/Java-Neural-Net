package neuralnet2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/*
 * Config file structure:
 * 
 * Inputs. Hidden layers (comma seperated). Outputs
 * how to get weights(Train/Read). Filename(if train, file contains training data. Otherwise, it contains weights).
 * 
 * 
 * A note on data files. They are formatted as the first line with the number of inputs. 
 * Then, each line is a comma seperated list of inputs
 * then a period, then a comma seperated list of outputs.
 */

public class NetRunnerConfig {
	public static void main(String[] args) throws IOException
	{
		String configFile = args[0];
		ConfigSetup c = new ConfigSetup(configFile);
	}
}
