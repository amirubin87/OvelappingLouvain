import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class OL {
	public GraphMetaData metaData;
	public UndirectedUnweightedGraph g;
	public double[] betas;
	public double alpha;
	public String outputPath;
	
	public OL(String pathToGraph, double[]betas, double alpha, String outputPath) throws IOException{
		this.betas= betas;
		this.alpha = alpha;
		this.outputPath =outputPath;
		this.g = new UndirectedUnweightedGraph(Paths.get(pathToGraph));
		this.metaData = new GraphMetaData(g);		
	}
	
	public void FindCommunities() throws FileNotFoundException, UnsupportedEncodingException{
		for (double betta : betas){
			Map<Integer,Set<Integer>> comms = FindCommunities(betta);
			WriteToFile(comms, betta);
		}
	}
	
	private void WriteToFile(Map<Integer, Set<Integer>> comms, double betta) throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter(outputPath + betta + ".txt", "UTF-8");
		for ( Set<Integer> listOfNodes : comms.values()){
			if(listOfNodes.size()>0){
				for(int node : listOfNodes){
					writer.print(node + " ");
				}
				writer.println("");
			}
		}		
		writer.close();	
	}

	private Map<Integer,Set<Integer>> FindCommunities(double betta){
	    int isDone = 0;
	    int amountOfScans = 0;
	    int n = g.number_of_nodes();
	    while (isDone < n && amountOfScans < 100){
	    	System.out.println("num of iter: " + amountOfScans);
	        amountOfScans++;
	        for (Integer node : g.nodes()){
	            Set<Integer> c_v_original = metaData.node2coms.get(node);
	            //metaData.Update_Weights_Remove(c_v_original, node);
	            metaData.ClearCommsOfNode(node);
	            Map<Integer, Double> comms_inc = new HashMap<Integer, Double>();
	            Set<Integer> neighborComms = Find_Neighbor_Comms(node);
	            for (Integer neighborComm : neighborComms){
	                double inc= Calc_Modularity_Improvement(neighborComm, node);
	                comms_inc.put(neighborComm, inc);
	            }
	            Set<Integer> c_v_new =Keep_Best_Communities(comms_inc, betta);
	            //metaData.Update_Weights_Add(c_v_new,node);
	            Map<Integer[],Double> commsCouplesIntersectionRatio = metaData.SetCommsForNode(node, c_v_new);
	            boolean haveMergedComms = FindAndMergeComms(commsCouplesIntersectionRatio);
	            if (!haveMergedComms && c_v_new.equals(c_v_original)){
	            	isDone++;
	            }
	            else{
	                isDone = 0;
	            }
	        }
        }    
	    if (amountOfScans == 100){
	        System.out.println("NOTICE - THE ALGORITHM HASNT STABLED. IT STOPPED AFTER SCANNING ALL NODES FOR N TIMES.");
	    }
	    return metaData.com2nodes;
	}	            

	private Set<Integer> Find_Neighbor_Comms(Integer node){
	    Set<Integer>neighborComms = new HashSet<Integer>();
	    for (Integer neighbor : g.neighbors(node)){
	        neighborComms.addAll(metaData.node2coms.get(neighbor));
	    }
    return neighborComms;
    }
	
	private double Calc_Modularity_Improvement(Integer comm, Integer node){
		    return metaData.K_v_c.get(node).get(comm)-metaData.Sigma_c.get(comm)*metaData.K_v.get(node)/(2*metaData.m);
	}
	
	private Set<Integer> Keep_Best_Communities(Map<Integer, Double>comms_imps,double betta){
	    double bestImp = 0;
	    for( double imp : comms_imps.values()){
	    	bestImp = Math.max(bestImp, imp);
	    }
	    Set<Integer> bestComs = new HashSet<Integer>();
	    for(Entry<Integer, Double> entry: comms_imps.entrySet()){
	    		 if (entry.getValue()*betta >= bestImp){
	    				 bestComs.add(entry.getKey());
	    		 }
	    }
	    return bestComs;
	}	
	
	private boolean FindAndMergeComms (Map<Integer[],Double> commsCouplesIntersectionRatio){
	    boolean haveMergedComms = false;
	    Set<Integer> commsToClean = new HashSet<Integer>();
	    for (Entry<Integer[],Double > c1c2intersectionRate : commsCouplesIntersectionRatio.entrySet()){	    	
	        if(c1c2intersectionRate.getValue()>alpha){
	        	Integer[] c1c2 = c1c2intersectionRate.getKey();
	        	commsToClean.add(c1c2[0]);
	        	MergeComms(c1c2);
	        	haveMergedComms = true;
	        }
	    }
	    ClearNodesFromComms(commsToClean);
	    return haveMergedComms;
	}



	private void MergeComms(Integer[] commsToMerge){
		Integer c1 = commsToMerge[0];
		Integer c2 = commsToMerge[1];
		List<Integer> copyOfC1= new ArrayList<>(metaData.com2nodes.get(c1));
		List<Integer> copyOfC2= new ArrayList<>(metaData.com2nodes.get(c2));
	    for (Integer node : copyOfC1){
	        metaData.Update_Weights_Remove(c1, node);
	        if(!copyOfC2.contains(node)){
	        	metaData.Update_Weights_Add(c2, node);
	        	metaData.SymbolicAddNodeToComm(node,c2);
	        }	        
	    }
	}
	
	private void ClearNodesFromComms(Set<Integer> commsToClean) {
		for (Integer c : commsToClean){
			metaData.SymbolicClearComm(c);
		}		
	}

}

