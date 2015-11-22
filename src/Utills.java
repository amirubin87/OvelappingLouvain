import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class Utills {
	
	 
    public static int IntersectionSize(Set<Integer> set1, Set<Integer> set2) {
		int ans = 0;
		Set<Integer> small = set1;
		Set<Integer> big = set2;
		if(set2.size() < set1.size()){
			small = set2;
			big = set1;
		}
		for(Integer i : small){			
				if(big.contains(i)){
					ans++;
				}
			}		
		return ans;
	}

	public Map<Integer,List<Integer>> RenumberComms(Map<Integer,List<Integer>> map){
	    int count = 0;
		Map<Integer,List<Integer>> ans = new HashMap<Integer, List<Integer>>();
		Map<Integer,Integer> new_values = new HashMap<Integer,Integer>();
	    for (Entry<Integer,List<Integer>>entry : map.entrySet()){
	    	List<Integer> newSet = new ArrayList<Integer>();	        
	        for (int comm : entry.getValue()){
	            Integer new_value = new_values.get((Integer)comm);
	            if (new_value == null){
	                new_values.put((Integer)comm, count);
	                new_value = count;
	                count = count + 1;
	            }
	            newSet.add(new_value);
	        }
	        ans.put(entry.getKey(), newSet);
	    }
	    return ans;
	}

}
