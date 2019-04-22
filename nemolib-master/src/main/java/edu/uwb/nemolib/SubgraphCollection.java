package edu.uwb.nemolib;

import java.util.HashSet;
import java.util.Set;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Representation of a Subgraph Collection structure, which stores the instances of
 * subgraphs detected in a network separated by subgraph type. A SubgraphCount
 * object exists in one of two states: labeled and unlabeled. Certain 
 * operations can only be performed based on the Subgraph's labeled state.
 */
public class SubgraphCollection implements SubgraphEnumerationResult
{
	//Set<Set<Integer>> subgraphs;
    

	// prevent default constructor from being called
	/*private SubgraphCollection()
	{
		throw new AssertionError();
	}

	SubgraphCollection(int subgraphSize)
	{
		subgraphs = new HashSet<Set<Integer>>();
	}

	void add(Set<Integer> subgraph)
	{
		subgraphs.add(subgraph);
	}*/
        
        // Implement 4/18/2019
        private String Filename;
        private BufferedWriter WriteFileBuffer;
        
        private Map<String, Integer> labelFreqMap; // collect subgraph count
        
        // get the map of g6 to cannonical label. Key is canonical labe, value is the g6 label corresponding to the canocnical label
        private Map<String, String> g6CanLabelMap; 
        
        public SubgraphCollection(String filename){
             Filename=filename;
             init();
            
        }
        
        public SubgraphCollection(){
            
            String filename="SubgraphCollectionG6.txt";
            Filename = filename;
            init();
            
        }
        private void init ()
        {
            // Initialize the hashmap that maps g6 to cannon
            labelFreqMap = new HashMap<String, Integer>();
            try{
            WriteFileBuffer = new BufferedWriter(new FileWriter(Filename));
            }
            catch(IOException Ex){
                System.out.println(Ex.getMessage());
            }
            
        }
                
        
        public String getFilename(){
            return Filename;
        }
        
        public Map<String, Integer> getlabelFreqMap(){
            return labelFreqMap;
        }
        
         public Map<String, String> getg6CanLabelMap(){
            return g6CanLabelMap;
        }
         
         /* This is necessary to 
         public boolean addFrequencies(String label,
	                           Map<Integer, Integer> frequencies) {
		if (labelToVertexToFrequency.containsKey(label)) {
			return false;
		}
		labelToVertexToFrequency.put(label, frequencies);
		return true;
	}

	public Map<Integer, Integer> getFrequencies(String label) {
		return labelToVertexToFrequency.getOrDefault(label, null);
	}

        
         /* Each instances are saved to a file, and the frequency is saved */
        @Override
	public void addSubgraph(Subgraph currentSubgraph)
	{
		String label = currentSubgraph.getByteString();
		int total = 0;
		if (labelFreqMap.containsKey(label)) {
			total = labelFreqMap.get(label);
		}
		total++;
		labelFreqMap.put(label, total);
                try{
                    WriteFileBuffer.write("g6:"+label+":");
                    WriteFileBuffer.newLine();
                    WriteFileBuffer.write(currentSubgraph.toString());
                    WriteFileBuffer.newLine();
                
                }
                catch (IOException Ex){
                    System.out.println(Ex.getMessage());
                }
                
	}
        
        @Override
	public void label()
	{
		// get the canonical labels, which should be ordered.
		Labeler labeler = new Labeler();
		g6CanLabelMap = labeler.getCanonicalLabels(labelFreqMap.keySet());

		Map<String, Integer> canLabelFreqMap = new HashMap<>();
                
		
		// merge labelFreqMap into canLabelFreqMap
		for (Map.Entry<String, Integer> g6LabelFreq: labelFreqMap.entrySet() )
		{
			String g6Label = g6LabelFreq.getKey();
			int freq = g6LabelFreq.getValue();
			String canLabel = g6CanLabelMap.get(g6Label);

			if (canLabelFreqMap.containsKey(canLabel)) {
				freq += canLabelFreqMap.get(canLabel);
			}

			canLabelFreqMap.put(canLabel, freq);
                                                
		}
		
		labelFreqMap = canLabelFreqMap;
                
                // close the file buffer if not closed
                try{
                WriteFileBuffer.close();
                }
                catch (IOException Ex){
                    System.out.println(Ex.getMessage());
                }
                
                /*System.out.println("Replacing g6 label to canonical label for each subgraph");
                 
                try{
                WriteFileBuffer.close();
                
              
                for (String g6label: g6toCan.keySet()){
                    String canLabel = g6toCan.get(g6label);
                   Path path=Paths.get(Filename);
                Stream <String> lines = Files.lines(path);
                List <String> replaced = lines.map(line -> line.replace("g6:"+g6label+";",canLabel)).collect(Collectors.toList());
                Files.write(path, replaced);
                lines.close();               
                
                }
                }
                catch (IOException Ex){
                    System.out.println(Ex.getMessage());
                }
                
                
              */
		
	}
        
        @Override
	public Map<String, Double> getRelativeFrequencies() {
		double total = 0;
		for (Integer freq : labelFreqMap.values()) {
			total += (double) freq;
		}
		Map<String, Double> labelRelFreqMap = new HashMap<>();
		for (Map.Entry<String, Integer> labelFreq : labelFreqMap.entrySet()) {
			String label = labelFreq.getKey();
			int freq = labelFreq.getValue();
			double relFreq = (double) freq / total;
			labelRelFreqMap.put(label, relFreq);
		}
		return labelRelFreqMap;
	}
        
        @Override public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Label").append('\t').append("Frequency");
		sb.append(String.format("%n"));
		for(Map.Entry<String, Integer> labelFreq: labelFreqMap.entrySet()) {
			sb.append(labelFreq.getKey()).append('\t');
			sb.append(labelFreq.getValue());
			sb.append(String.format("%n"));
		}
		return sb.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj.getClass() != this.getClass()) {
			return false;
		}
		SubgraphCollection other = (SubgraphCollection) obj;
		return other.labelFreqMap.equals(this.labelFreqMap);
	}
        

        
       

}
