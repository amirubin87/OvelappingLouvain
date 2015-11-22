import java.io.IOException;

public class RunOL {
	

	public static void main(String[] args) throws IOException {	
		
		
		String outputPath = "C:/Temp/res/Java";
		String pathToGraph = "C:/Temp/network.dat";
		 double[]betas = new double[] {1.01};
		 double alpha = 0.5;
		 
		OL ol= new OL(pathToGraph,betas,alpha,outputPath);
		ol.FindCommunities();
	}

}
