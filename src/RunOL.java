import java.io.IOException;

public class RunOL {
	

	public static void main(String[] args) throws IOException {	
		
		if (args.length <3){
			System.out.println("pathToGraph  outputPath  betas  alpha  iteratioNumToStartMerge  maxIterationsToRun");
		}
		else{
			String pathToGraph = "C:/Temp/b-10-om8-2/network.dat";
			String outputPath = "C:/Temp/res/Java";
			double[] betas = new double[] {};
			double alpha = 0.8;
			int iteratioNumToStartMerge = 6;
			int maxIterationsToRun = 100;
			
			if (args.length > 0)
				pathToGraph = args[0];		
			
			if (args.length > 1)
				outputPath = args[1];		
			
			if (args.length > 2)
				betas = ParseDoubleArray(args[2]);		
				
			if (args.length > 3)
				 alpha = Double.parseDouble(args[3]);
			
			if ( args.length > 4)
				 iteratioNumToStartMerge = Integer.parseInt(args[4]);
			
			if ( args.length > 5)
				maxIterationsToRun = Integer.parseInt(args[5]);
			
			String betasString = "";
			for (double d: betas){
				betasString = betasString + d + " ";
			}
			
			System.out.println("pathToGraph:             "+pathToGraph);      
			System.out.println("outputPath:              "+outputPath);
			System.out.println("betas:                   "+betasString);
			System.out.println("alpha:                   "+alpha);
			System.out.println("iteratioNumToStartMerge: "+iteratioNumToStartMerge);
			System.out.println("maxIterationsToRun:      "+maxIterationsToRun);
			System.out.println("");
			OL ol= new OL(pathToGraph,betas,alpha,outputPath, iteratioNumToStartMerge, maxIterationsToRun);
			ol.FindCommunities();
		}
	}

	private static double[] ParseDoubleArray(String string) {
		String[] parts = string.split(",");
		double[] ans= new double[parts.length];
	    int i=0;
	    for(String str:parts){
	    	ans[i]=Double.parseDouble(str);
	        i++;
	    }
		return ans;
	}

}
