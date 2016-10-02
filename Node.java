package neuralnet2;

import java.util.Random;

class Node
{
	private int numInputs;
	private double[] weights;
	private double a;
	private double error;
	private double bias = 1;
	
	public Node(int numInputs){
		this.numInputs = numInputs;
		this.weights = new double[numInputs];
		Random r = new Random(5);
		
		for(int i = 0; i < weights.length; i++){
			if(r.nextBoolean()){
				weights[i] = r.nextDouble();
			}
			else{
				weights[i] = -1 * r.nextDouble();
			}
			
			//weights[i] = 1;
		}
	}
	
	public Node(int numInputs, double bias){
		this.bias = bias;
		this.numInputs = numInputs;
		this.weights = new double[numInputs];
		Random r = new Random();
		
		for(int i = 0; i < weights.length; i++){
			if(r.nextBoolean()){
				weights[i] = r.nextDouble();
			}
			else{
				weights[i] = -1 * r.nextDouble();
			}
			
			//weights[i] = 1;
		}
	}
	
	public double feedForward(double[] inputs){
		double returnVal = 0;
		for(int i=0;i<inputs.length;i++){
			returnVal += inputs[i] * weights[i];
		}
		returnVal += this.bias;
		a = returnVal;
		return returnVal;
	}
	public double getWeight(int i){
		return weights[i];
	}
	public double[] getWeights()
	{
		return weights;
	}
	public void setWeight(int i, double d){
		weights[i] = d;
	}
	public double getA(){
		return a;
	}
	public double getError() {
		return error;
	}
	public void setError(double error) {
		this.error = error;
	}
	
	public double getBias()
	{
		return this.bias;
	}
	
	public void setBias(double newBias)
	{
		this.bias = newBias;
	}
}
