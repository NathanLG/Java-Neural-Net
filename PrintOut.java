package neuralnet2;

public class PrintOut implements OutputMethod {

	@Override
	public void output(String message) {
		System.out.println(message);

	}

	@Override
	public void setSetup(ConfigSetup setup) 
	{
	}

}
