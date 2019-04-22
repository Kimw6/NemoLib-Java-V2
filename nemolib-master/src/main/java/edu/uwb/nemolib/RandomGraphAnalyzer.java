package edu.uwb.nemolib;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * RandomGraphAnalyzer is a facade class that generates and enumerates using
 * RAND-ESU a set of random network graphs based on the degree sequence vector
 * of a specified graph. The output of the analyze() method can be used by a
 * RelativeFrequencyAnalyzer object to determine whether a target graph contains any
 * network motifs.
 */
public final class RandomGraphAnalyzer {

	private SubgraphEnumerator enumerator;
	private int randomGraphCount;

	public RandomGraphAnalyzer(SubgraphEnumerator enumerator,
	                           int randomGraphCount) {
		this.enumerator = enumerator;
		this.randomGraphCount = randomGraphCount;

	}

	/**
	 * Generate and enumerate a set of random graphs.
	 * @param targetGraph the network graph from which to derive a degree
	 *                    sequence vector for generating random graphs
	 * @param subgraphSize the getSize of subgraph to enumerate
	 * @return mapping of labels to relative frequencies as found in the
	 * random graph pool
	 */
        
        public Map<String, List<Double>> analyze (Graph targetGraph, int subgraphSize) {

		// create the return map and fill it with the labels we found in the
		// target graph, as those are the only labels about which we care
		Map<String, List<Double>> labelToRelativeFrequencies = new HashMap<>();
                int numTrials = randomGraphCount;
                
                
                // Get the vertexList first from the targetGraph, since they are the same.
                //List<Integer> degreeSequenceVector = RandomGraphGenerator.getDegreeSequenceVector(targetGraph);

		
		// generate randomized list of vertices
		// the vertexList is a set where each node is represented by a number
		// of elements equal to that vertex's degree
		/*List<Integer> vertexList = new ArrayList<>();
		for (int vertex = 1; vertex < targetGraph.getSize()+1; ++vertex) {
			for (int degree = 0; degree < degreeSequenceVector.get(vertex-1);
					++degree) {
				vertexList.add(vertex);
			}
		}
                	*/
                               
		for(int i = 0; i < randomGraphCount; i++) {
			
                        /*Graph randomGraph; 
                        if (targetGraph.getDir()) randomGraph = RandomGraphGenerator.generateDir(targetGraph);
                        else randomGraph = RandomGraphGenerator.generate(targetGraph);
                        */

                        Graph randomGraph = RandomGraphGenerator.generate(targetGraph);
			// enumerate random graphs
			SubgraphCount subgraphCount = new SubgraphCount();
			enumerator.enumerate(randomGraph, subgraphSize, subgraphCount);
			subgraphCount.label();

			Map<String, Double> curLabelRelFreqMap =
					subgraphCount.getRelativeFrequencies();
                        
                        
                        // To correctly calculate the mean value, if none of patterns are detected, then decrease the number of trials.
                        if (curLabelRelFreqMap.size()==0) {
                            numTrials--;
                            continue;
                        }

			// populate labelToRelativeFrequencies with result
			for (Map.Entry<String, Double> curLabelRelFreqPair :
					curLabelRelFreqMap.entrySet()) {
				String curLabel = curLabelRelFreqPair.getKey();
				Double curFreq = curLabelRelFreqPair.getValue();

				if (!labelToRelativeFrequencies.containsKey(curLabel)) {
					labelToRelativeFrequencies.put(curLabel, new LinkedList<>());
				}
				labelToRelativeFrequencies.get(curLabel).add(curFreq);
			}
                        
  		}

                 
		// fill in with zeros any List that is less than subgraph count to
		// ensure non-detection is accounted for.
		
                for (List<Double> freqs :
				labelToRelativeFrequencies.values()) {
			while (freqs.size() < numTrials) {
				freqs.add(0.0);
			}
		}
                
  
               ////////////////////////////////////////////////////////////
		return labelToRelativeFrequencies;
	}
        
	/*public Map<String, List<Double>> analyze (Graph targetGraph, int subgraphSize) {

		// create the return map and fill it with the labels we found in the
		// target graph, as those are the only labels about which we care
		Map<String, List<Double>> labelToRelativeFrequencies = new HashMap<>();
                int numTrials = randomGraphCount;
                
                
                

		for(int i = 0; i < randomGraphCount; i++) {
			Graph randomGraph = RandomGraphGenerator.generate(targetGraph);

			// enumerate random graphs
			SubgraphCount subgraphCount = new SubgraphCount();
			enumerator.enumerate(randomGraph, subgraphSize, subgraphCount);
			subgraphCount.label();

			Map<String, Double> curLabelRelFreqMap =
					subgraphCount.getRelativeFrequencies();
                        
                        
                        // To correctly calculate the mean value, if none of patterns are detected, then decrease the number of trials.
                        if (curLabelRelFreqMap.size()==0) {
                            numTrials--;
                            continue;
                        }

			// populate labelToRelativeFrequencies with result
			for (Map.Entry<String, Double> curLabelRelFreqPair :
					curLabelRelFreqMap.entrySet()) {
				String curLabel = curLabelRelFreqPair.getKey();
				Double curFreq = curLabelRelFreqPair.getValue();

				if (!labelToRelativeFrequencies.containsKey(curLabel)) {
					labelToRelativeFrequencies.put(curLabel, new LinkedList<>());
				}
				labelToRelativeFrequencies.get(curLabel).add(curFreq);
			}
                        
  		}

                 
		// fill in with zeros any List that is less than subgraph count to
		// ensure non-detection is accounted for.
		
                for (List<Double> freqs :
				labelToRelativeFrequencies.values()) {
			while (freqs.size() < numTrials) {
				freqs.add(0.0);
			}
		}
               ////////////////////////////////////////////////////////////
		return labelToRelativeFrequencies;
	}*/
}