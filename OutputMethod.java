package neuralnet2;

public interface OutputMethod {
	public abstract void setSetup(ConfigSetup setup);
	public abstract void output(String message);
}
