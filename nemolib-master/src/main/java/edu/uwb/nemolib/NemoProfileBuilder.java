package edu.uwb.nemolib;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


/**
 * Created by user on 1/20/17.
 */
public class NemoProfileBuilder {

	// prevent default constructor from being called
	private NemoProfileBuilder() {throw new AssertionError();}

	public static SubgraphProfile buildwithPvalue(SubgraphProfile sp,
	                  RelativeFrequencyAnalyzer sa,
	                  double pThresh, String filename) {
		SubgraphProfile result = new SubgraphProfile();
		Map<String, Double> pValues = sa.getPValues();
                                
		for (Map.Entry<String, Double> labelPValue : pValues.entrySet()) {
			if (labelPValue.getValue() <= pThresh) {
				//System.out.println(labelPValue.getKey());
				result.addFrequencies(labelPValue.getKey(),
						sp.getFrequencies(labelPValue.getKey()));
			}
		}
                if (filename==null) filename = "NemoProfile.txt";
                writeNemoProfile(result, filename);
                
		return result;
	}
        
        public static SubgraphProfile buildwithZScore(SubgraphProfile sp,
	                  RelativeFrequencyAnalyzer sa,
	                  double zThresh, String filename) {
		SubgraphProfile result = new SubgraphProfile();
		Map<String, Double> zScores = sa.getZScores();
          
		for (Map.Entry<String, Double> labelZSore : zScores.entrySet()) {
			if (labelZSore.getValue() >= zThresh) {
				//System.out.println(labelPValue.getKey());
				result.addFrequencies(labelZSore.getKey(),
						sp.getFrequencies(labelZSore.getKey()));
			}
		}
                if (filename==null) filename = "NemoProfile.txt";
                writeNemoProfile(result, filename);
                
		return result;
	}
        
        private static void writeNemoProfile(SubgraphProfile sp, String filename)
        {
            SortedSet<Integer> vertices = new TreeSet<Integer>();
            SortedSet<String> labels=new TreeSet<String>();
            
            Map<String, Map<Integer, Integer>> profileMap = sp.getProfileMap();
            
            
            // Collect and sort all labels of network motifs
            labels.addAll(profileMap.keySet());
            
            
            // Collect and sort all vertices that are participated in network motifs
            for (String canlabel: labels){
                Map<Integer, Integer> vertexFreq = profileMap.get(canlabel);
                vertices.addAll(vertexFreq.keySet());                              
            }
            
            // Write to a file 
            try{
            BufferedWriter WriteFileBuffer = new BufferedWriter(new FileWriter(filename));
            // Write a first line as a label
            WriteFileBuffer.write("   ");
            for (String label:labels){
               WriteFileBuffer.write("\t"+label);
            }
            WriteFileBuffer.newLine();
            // Now write a vertex, and their frequencies corresponding each pattern
            for(Integer vertex: vertices){          
                              
                WriteFileBuffer.write(vertex.toString());
                
                for (String label:labels){
                    Integer freq = profileMap.get(label).get(vertex);
                    if (freq == null) freq=0;
                    WriteFileBuffer.write("\t\t"+freq);
                }
                
                WriteFileBuffer.newLine();
            }
            
            // Now write a vertex and frequency of each
           
                   
                    WriteFileBuffer.close();
            
            
            
            }
            catch(IOException Ex){
                System.out.println(Ex.getMessage());
            }
            
            
            
        }
       
}
