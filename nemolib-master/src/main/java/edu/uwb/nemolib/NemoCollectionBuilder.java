/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uwb.nemolib;

import java.util.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.io.*;
import java.util.*;
import java.nio.channels.*;
/**
 *
 * @author wkim
 */
public class NemoCollectionBuilder {

    public NemoCollectionBuilder() {
        throw new AssertionError();
    }

    /* File copy whcih copy the SubgraphCollectG6 to SubgraphCollect with canonical label*/
    private static void copyFileUsingChannel(File source, File dest) throws IOException {
    FileChannel sourceChannel = null;
    FileChannel destChannel = null;
    try {
        sourceChannel = new FileInputStream(source).getChannel();
        destChannel = new FileOutputStream(dest).getChannel();
        destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
       }finally{
           sourceChannel.close();
           destChannel.close();
   }
}
    /*Current SubgraphCollectionResult.txt has g6 label. To get canonical label subgraphcollection, call this function*/
    public static void buildSubgraphCollection(SubgraphCollection sp, String can_filename) {
        System.out.println("Replacing g6 label to canonical label for each subgraph");
        
        String sourcefilename = sp.getFilename();
        File source = new File(sourcefilename);
        File dest = new File(can_filename);       

 
        try {
            copyFileUsingChannel(source, dest);
            
            Map<String, String> g6_toCan = sp.getg6CanLabelMap();
            

            for (String g6label : g6_toCan.keySet()) {
                String canLabel = g6_toCan.get(g6label);
                Path path = Paths.get(can_filename);
                Stream<String> lines = Files.lines(path);
                List<String> replaced = lines.map(line -> line.replace("g6:" + g6label + ":", canLabel)).collect(Collectors.toList());
                Files.write(path, replaced);
                lines.close();

            }
        } catch (IOException Ex) {
            System.out.println(Ex.getMessage());
        }

    }
    
    public static void buildSubgraphCollection(SubgraphCollection sp, String can_filename, Map nametoIndex) {
        System.out.println("Replacing g6 label to canonical label for each subgraph");
        
        String sourcefilename = sp.getFilename();
        File source = new File(sourcefilename);
        File dest = new File(can_filename);       

 
        try {
            copyFileUsingChannel(source, dest);
            
            Map<String, String> g6_toCan = sp.getg6CanLabelMap();
            

            for (String g6label : g6_toCan.keySet()) {
                String canLabel = g6_toCan.get(g6label);
                Path path = Paths.get(can_filename);
                Stream<String> lines = Files.lines(path);
                List<String> replaced = lines.map(line -> line.replace("g6:" + g6label + ":", canLabel)).collect(Collectors.toList());
                Files.write(path, replaced);
                lines.close();

            }
        } catch (IOException Ex) {
            System.out.println(Ex.getMessage());
        }

    }

    /*private static Map<String, Set<String>> getCantoG6Map(SubgraphCollection sp) {
        Map<String, Set<String>> result = new HashMap<String, Set<String>>();
        Map<String, String> g6tocan = sp.getg6CanLabelMap();

        for (String g6 : g6tocan.keySet()) {
            Set<String> g6set = new HashSet<String>();
            String cankey = g6tocan.get(g6);
            if (result.keySet().contains(cankey)) {
                g6set = result.get(cankey);
            }
            g6set.add(g6);
            result.put(cankey, g6set);

        }

        return result;
    }
*/

    public static void buildwithPvalue(SubgraphCollection sp,
            RelativeFrequencyAnalyzer sa,
            double pThresh, String writefilename) {
        
        // If subgraphcollection needs to be obtained, 
        Map<String, Double> pValues = sa.getPValues();

        // Collect the canonical label of network motifs
        Set<String> nm_can_g6 = new HashSet<String>();

        for (Map.Entry<String, Double> labelPValue : pValues.entrySet()) {
            if (labelPValue.getValue() <= pThresh) {
                //System.out.println(labelPValue.getKey());
                String nm_label = labelPValue.getKey();
                nm_can_g6.add(nm_label);
            }
        }

        // Now write to a separate file to save network motifs
        try {
            Map<String, String> g6tocan = sp.getg6CanLabelMap();

            // Create read and write filebuffer
            BufferedWriter WriteFileBuffer
                    = new BufferedWriter(new FileWriter(writefilename));

            BufferedReader reader = new BufferedReader(new FileReader(sp.getFilename()));
            String currentLine = null;
            
           
            while ((currentLine=reader.readLine())!=null) {
                String [] arrOfStr=currentLine.split(":");
                String g6 = arrOfStr[1];
                String nextline = reader.readLine();
                if(nm_can_g6.contains(g6tocan.get(g6))){
                      WriteFileBuffer.write(g6tocan.get(g6));
                     WriteFileBuffer.write("\n");
                     WriteFileBuffer.write(nextline);
                     WriteFileBuffer.write("\n");
                }               

            }
            reader.close();
            WriteFileBuffer.close();           

        } catch (IOException Ex) {
            System.out.println(Ex.getMessage());
        }

        return;
    }
    
     public static void buildwithPvalue(SubgraphCollection sp,
            RelativeFrequencyAnalyzer sa,
            double pThresh, String writefilename,
            Map nametoIndex) {
         
         // If the map is not given, write with indices
         if (nametoIndex==null) {
                buildwithPvalue(sp, sa, pThresh, writefilename);
                return;
            }
        
        // If subgraphcollection needs to be obtained, 
        Map<String, Double> pValues = sa.getPValues();

        // Collect the canonical label of network motifs
        Set<String> nm_can_g6 = new HashSet<String>();

        for (Map.Entry<String, Double> labelPValue : pValues.entrySet()) {
            if (labelPValue.getValue() <= pThresh) {
                //System.out.println(labelPValue.getKey());
                String nm_label = labelPValue.getKey();
                nm_can_g6.add(nm_label);
            }
        }
        
        
        
        // Swap the key and value of nametoIndex
            HashMap<Integer, String> indextoname = new HashMap<Integer, String>();
            for (Object key:nametoIndex.keySet()){
                Object index=nametoIndex.get(key);
                indextoname.put((Integer)index, (String) key);
            }   
            

        // Now write to a separate file to save network motifs
        try {
            Map<String, String> g6tocan = sp.getg6CanLabelMap();

            // Create read and write filebuffer
            BufferedWriter WriteFileBuffer
                    = new BufferedWriter(new FileWriter(writefilename));

            BufferedReader reader = new BufferedReader(new FileReader(sp.getFilename()));
            String currentLine = null;            
           
            while ((currentLine=reader.readLine())!=null) {
                String [] arrOfStr=currentLine.split(":");
                String g6 = arrOfStr[1];
                String nextline = reader.readLine().replaceAll("\\[|\\]", "");
                String [] vertices = nextline.split("\\,|WWs");
                String vertexline="[";
                for (int i=0;i<vertices.length-1;i++){
                     Integer idx = Integer.parseInt(vertices[i]);                    
                    vertexline=vertexline+indextoname.get(idx)+",";
                }
                Integer idx = Integer.parseInt(vertices[vertices.length-1]);
                vertexline=vertexline+indextoname.get(idx)+"]";              
                if(nm_can_g6.contains(g6tocan.get(g6))){
                      WriteFileBuffer.write(g6tocan.get(g6));
                     WriteFileBuffer.write("\n");
                     WriteFileBuffer.write(vertexline);
                     WriteFileBuffer.write("\n");
                }               

            }
            reader.close();
            WriteFileBuffer.close();           

        } catch (IOException Ex) {
            System.out.println(Ex.getMessage());
        }

        return;
    }
        


    public static void buildwithZScore(SubgraphCollection sp,
            RelativeFrequencyAnalyzer sa,
            double zThresh, String writefilename) {

        Map<String, Double> zScores = sa.getZScores();

        // Collect the canonical label of network motifs
        Set<String> nm_can_g6 = new HashSet<String>();

        for (Map.Entry<String, Double> labelZValue : zScores.entrySet()) {
            if (labelZValue.getValue() >= zThresh) {
                //System.out.println(labelPValue.getKey());
                String nm_label = labelZValue.getKey();
                nm_can_g6.add(nm_label);
            }
        }

        // Now write to a separate file to save network motifs
        try {
            Map<String, String> g6tocan = sp.getg6CanLabelMap();

            // Create read and write filebuffer
            BufferedWriter WriteFileBuffer
                    = new BufferedWriter(new FileWriter(writefilename));

            BufferedReader reader = new BufferedReader(new FileReader(sp.getFilename()));
            String currentLine = null;
            
           
            while ((currentLine=reader.readLine())!=null) {
                String [] arrOfStr=currentLine.split(":");
                String g6 = arrOfStr[1];
                String nextline = reader.readLine();
                if(nm_can_g6.contains(g6tocan.get(g6))){
                      WriteFileBuffer.write(g6tocan.get(g6));
                     WriteFileBuffer.write("\n");
                     WriteFileBuffer.write(nextline);
                     WriteFileBuffer.write("\n");
                }               

            }
            reader.close();
            WriteFileBuffer.close();           

        } catch (IOException Ex) {
            System.out.println(Ex.getMessage());
        }
        return;
    }
    
    public static void buildwithZScore(SubgraphCollection sp,
            RelativeFrequencyAnalyzer sa,
            double zThresh, String writefilename,
            Map nametoIndex) {

          // If the map is not given, write with indices
         if (nametoIndex==null) {
                buildwithZScore(sp, sa, zThresh, writefilename);
                return;
            }
        Map<String, Double> zScores = sa.getZScores();

        // Collect the canonical label of network motifs
        Set<String> nm_can_g6 = new HashSet<String>();

        for (Map.Entry<String, Double> labelZValue : zScores.entrySet()) {
            if (labelZValue.getValue() >= zThresh) {
                //System.out.println(labelPValue.getKey());
                String nm_label = labelZValue.getKey();
                nm_can_g6.add(nm_label);
            }
        }
        
          // Swap the key and value of nametoIndex
            HashMap<Integer, String> indextoname = new HashMap<Integer, String>();
            for (Object key:nametoIndex.keySet()){
                Object index=nametoIndex.get(key);
                indextoname.put((Integer)index, (String) key);
            }   
            

         try {
            Map<String, String> g6tocan = sp.getg6CanLabelMap();

            // Create read and write filebuffer
            BufferedWriter WriteFileBuffer
                    = new BufferedWriter(new FileWriter(writefilename));

            BufferedReader reader = new BufferedReader(new FileReader(sp.getFilename()));
            String currentLine = null;            
           
            while ((currentLine=reader.readLine())!=null) {
                String [] arrOfStr=currentLine.split(":");
                String g6 = arrOfStr[1];
                String nextline = reader.readLine().replaceAll("\\[|\\]", "");
                String [] vertices = nextline.split("\\,|WWs");
                String vertexline="[";
                for (int i=0;i<vertices.length-1;i++){
                     Integer idx = Integer.parseInt(vertices[i]);                    
                    vertexline=vertexline+indextoname.get(idx)+",";
                }
                Integer idx = Integer.parseInt(vertices[vertices.length-1]);
                vertexline=vertexline+indextoname.get(idx)+"]";              
                if(nm_can_g6.contains(g6tocan.get(g6))){
                      WriteFileBuffer.write(g6tocan.get(g6));
                     WriteFileBuffer.write("\n");
                     WriteFileBuffer.write(vertexline);
                     WriteFileBuffer.write("\n");
                }               

            }
            reader.close();
            WriteFileBuffer.close();           

        } catch (IOException Ex) {
            System.out.println(Ex.getMessage());
        }

        return;
    }

}
