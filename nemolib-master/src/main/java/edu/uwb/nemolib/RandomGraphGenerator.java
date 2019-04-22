package edu.uwb.nemolib;

import java.util.*;

/**
  * Generates random graphs from an input graph based on the degree sequence of 
  * the original graph.
  */
final class RandomGraphGenerator {

	/**
	 * Private constructor to prevent instantiation
	 */
	private RandomGraphGenerator() { throw new AssertionError(); }
       

	/**
	  * Generates random Graphs from an input Graph based on the degree
	  * sequence of the original Graph.
	  * @param inputGraph the Graph from which to derive the random Graphs
	  * @return a random Graph of the same getSize and order as the original
	  */
	static Graph generate(Graph inputGraph)
	{
		
                Graph randomGraph = new Graph(inputGraph.getDir());
                
                // It is a outdegree sequence vector if inputGraph is directed graph
                List<Integer> degreeSequenceVector = getDegreeSequenceVector(inputGraph);
                List<Integer> indegreeSeqVector = null;
                if (inputGraph.getDir()) indegreeSeqVector=getInDegreeSequenceVector(inputGraph);
                // will hard cord to give maximul trial to avoid self edge and existing edge
                int maxtrial=5;
                
		// generate randomized list of vertices
		// the vertexList is a set where each node is represented by a number
		// of elements equal to that vertex's degree
                // undirected graph
                // Note that the vertex index starts from 1.
                
                List<Integer> vertexList = new ArrayList<>();
                List<Integer> invertexList = new ArrayList<>();
		for (int vertex = 1; vertex < inputGraph.getSize()+1; ++vertex) {
			randomGraph.addVertex();
			for (int degree = 0; degree < degreeSequenceVector.get(vertex);
					++degree) {
				vertexList.add(vertex);
			}
                         // add for directed graph
                        if (inputGraph.getDir()){
                            for (int degree = 0; degree < indegreeSeqVector.get(vertex);
                                            ++degree) {
                                    invertexList.add(vertex);
                            }
                        }
		}
                		
		           
              
                Collections.shuffle(vertexList);
                if (inputGraph.getDir()) Collections.shuffle(invertexList);
 				
		// create edges
		int u, v;
                while (vertexList.size()>0){
			Random generator = new Random();
			u = generator.nextInt(vertexList.size());                              
                       
			
                        if (inputGraph.getDir()) 
                        {
                            v = generator.nextInt(invertexList.size());
                            int edgeVertexU = vertexList.get(u);
                            int edgeVertexV = invertexList.get(v);
                            vertexList.remove(u);
                            invertexList.remove(v);
                            randomGraph.addEdge(edgeVertexU, edgeVertexV);
                        }
                        else {
                            // make sure v does not equal u
                             while((v = generator.nextInt(vertexList.size())) == u) {}
                                if ( u > v) {
                                int temp = u;
                                u = v;
                                v = temp;
                            }                               
                            int edgeVertexV = vertexList.get(v);
                            int edgeVertexU = vertexList.get(u);
                            vertexList.remove(v);
                            vertexList.remove(u);
                            randomGraph.addEdge(edgeVertexV, edgeVertexU);                        
			
                        }
                           
                       
                        
		}
                
                
               
                
                
                
                
		return randomGraph;
	}
        /**
         * Edge Encode: When generate random graphs, try to avoid to add an existing edge. 
         **/
        /*private static long edge_code (int u, int v){
            return u<<20|v;
        }
        */
         
        /**
	  * Generates a degree sequence vector for a given Graph
	  * @param inputGraph the Graph from which to derive the degree sequence
	  * vector
	  * @return a List representing the degree sequence vector
	  */
	private static List<Integer> getDegreeSequenceVector(Graph inputGraph)
	{
		List<Integer> degreeSequenceVector = new ArrayList<>();
                // the 0th index is invalid: give -1
                degreeSequenceVector.add(-1);
                
		for (int currentVertex = 1; currentVertex < inputGraph.getSize()+1;
				++currentVertex) {
                    int degree = 0;
                    if (inputGraph.getDir()){
                        CompactHashSet.Iter uIter = inputGraph.getAdjacencyList(currentVertex).iterator();
                         while (uIter.hasNext())
                        {
                            if (uIter.next()>0) degree++; 
                         }
                    }
                    else
			degree = inputGraph.getAdjacencyList(currentVertex).size();
                    
			degreeSequenceVector.add(degree);
		}
                
                
		return degreeSequenceVector;
                
                
               
                
                
                
                
                
                
                
                
	}
        // Generate IndegreesequenceVector for directed graph
        static List<Integer> getInDegreeSequenceVector(Graph inputGraph)
	{
		// return if undirected
            if (!inputGraph.getDir()) return null;
            List<Integer> indegreeV = new ArrayList<>();
            // The 0th index is invalide: add -1
            indegreeV.add(-1);

		for (int currentVertex = 1; currentVertex < inputGraph.getSize()+1;
				++currentVertex) {
                     int indegree = 0;
                    CompactHashSet.Iter uIter = inputGraph.getAdjacencyList(currentVertex).iterator();
                    while (uIter.hasNext())
                    {
                        if (uIter.next()<0) indegree++; 
                     }
			
                    indegreeV.add(indegree);
		}
               
		return indegreeV;
	}
        
        
       /* static Graph generate(Graph inputGraph)
	{
		List<Integer> degreeSequenceVector = getDegreeSequenceVector(inputGraph);

		Graph randomGraph = new Graph();
		// generate randomized list of vertices
		// the vertexList is a set where each node is represented by a number
		// of elements equal to that vertex's degree
		List<Integer> vertexList = new ArrayList<>();
		for (int vertex = 0; vertex < inputGraph.getSize(); ++vertex) {
			randomGraph.addVertex();
			for (int degree = 0; degree < degreeSequenceVector.get(vertex);
					++degree) {
				vertexList.add(vertex);
			}
		}
		
		Collections.shuffle(vertexList);
                System.out.println("vertexList=" + vertexList);
		
		// create edges
		int u, v;
		for (int c = 0; vertexList.size() > 1; ++c) {
			Random generator = new Random();
			u = generator.nextInt(vertexList.size());
			// make sure v does not equal u
			while((v = generator.nextInt(vertexList.size())) == u) {}
			// swap not factored because Java
			if ( u > v) {
				int temp = u;
				u = v;
				v = temp;
			}
			int edgeVertexV = vertexList.get(v);
			int edgeVertexU = vertexList.get(u);
			vertexList.remove(v);
			vertexList.remove(u);
			randomGraph.addEdge(edgeVertexU, edgeVertexV);
		}
               // System.out.println("randomGraph="+randomGraph);
		return randomGraph;
	}
        */
        
        /* Will not use it for now*/
        /*
        static Graph generateFromList(Graph randomGraph, List<Integer> vList)
	{
		

		List<Integer> vertexList=new ArrayList<Integer>(vList);
                
		
		// generate randomized list of vertices
		// the vertexList is a set where each node is represented by a number
		// of elements equal to that vertex's degree
		
		Collections.shuffle(vertexList);
 
				
		// create edges
		int u, v;
		for (int c = 0; vertexList.size() > 1; ++c) {
			Random generator = new Random();
			u = generator.nextInt(vertexList.size());
			// make sure v does not equal u
			while((v = generator.nextInt(vertexList.size())) == u) {}
                        
			// swap not factored because Java
			if ( u > v) {
				int temp = u;
				u = v;
				v = temp;
			}
			int edgeVertexV = vertexList.get(v);
			int edgeVertexU = vertexList.get(u);
			vertexList.remove(v);
			vertexList.remove(u);
			randomGraph.addEdge(edgeVertexU, edgeVertexV);
		}
                
                
                
		return randomGraph;
	}
        
        */

        /* Will not use it for now*/
        /*
	static Graph generate(Graph inputGraph, List<Integer> probs)
	{
		List<Integer> degreeSequenceVector = getDegreeSequenceVector(inputGraph);

		Graph randomGraph = new Graph(inputGraph.getDir());
		// generate randomized list of vertices
		// the vertexList is a set where each node is represented by a number
		// of elements equal to that vertex's degree
		List<Integer> vertexList = new ArrayList<>();
		for (int vertex = 1; vertex < inputGraph.getSize()+1; ++vertex) {
			randomGraph.addVertex();
			for (int degree = 0; degree < degreeSequenceVector.get(vertex);
			     ++degree) {
				vertexList.add(vertex);
			}
		}
		Collections.shuffle(vertexList);

		// create edges
		int u, v;
		Iterator<Integer> probItr = probs.iterator();
		for (int c = 0; vertexList.size() > 1; c++) {
			u = probItr.next();
			//System.out.println("c = " + c);
			//System.out.println("u = " + u);

			// make sure v does not equal u
			while((v = probItr.next()) == u) {}
			//System.out.println("v = " + v);
			// swap not factored because Java
			if ( u > v) {
				int temp = u;
				u = v;
				v = temp;
			}
			int edgeVertexV = vertexList.get(v);
			int edgeVertexU = vertexList.get(u);
			vertexList.remove(v);
			vertexList.remove(u);
			randomGraph.addEdge(edgeVertexU, edgeVertexV);
		}
		return randomGraph;
	}
        */

	
        
        /**
	  * Generates random Graphs from an input Graph based on the degree
	  * sequence of the original Graph.
	  * @param inputGraph the Graph from which to derive the random Graphs
	  * @return a random Graph of the same getSize and order as the original
	  */
       /* static Graph generateDir(Graph inputGraph)
	{
		
            // Unlike the undirected graph, directed grpah needs to 
            List<Integer> outdV=new ArrayList<Integer>();
            List<Integer> inV=new ArrayList<Integer>();
                 outdV.add(-1);
                 inV.add(-1);
                getDegreeSequenceVectorDir(inputGraph, outdV,inV);

		Graph randomGraph = new Graph(inputGraph.getDir());
                
                                     
                
                
		// generate randomized list of vertices
		// the vertexList is a set where each node is represented by a number
		// of elements equal to that vertex's degree
		List<Integer> outvertexList = new ArrayList<>();
                List<Integer> invertexList = new ArrayList<>();
		for (int vertex = 1; vertex < inputGraph.getSize()+1; ++vertex) {
                    randomGraph.addVertex();
			for (int degree = 0; degree < outdV.get(vertex-1);
					++degree) {
				outvertexList.add(vertex);
			}
                        for (int degree = 0; degree < inV.get(vertex-1);
					++degree) {
				invertexList.add(vertex);
			}
		}
                
                
		
                
		
		return generateFromListDir(randomGraph, outvertexList, invertexList);
	}
        */
	
        
        /**
	  * Generates a degree sequence vector for a given directed Graph
	  * @param inputGraph the Graph from which to derive the degree sequence
	  * vector. The inputGraph is directed graph
          * @param outdegreeV is the outdegree distribution vector
          * @param indegreeV is the indgree distribution vector
	  * @return a List representing the degree sequence vector
	  */
        
        /*  static void getDegreeSequenceVectorDir(Graph inputGraph, List<Integer> outdegreeV, List<Integer> indegreeV)
	{
		// return if undirected
            if (!inputGraph.getDir()) return;

		for (int currentVertex = 1; currentVertex < inputGraph.getSize()+1;
				++currentVertex) {
                    int outdegree = 0;
                    int indegree = 0;
                    CompactHashSet.Iter uIter = inputGraph.getAdjacencyList(currentVertex).iterator();
                    while (uIter.hasNext())
                    {
                        if (uIter.next()>0) outdegree++; 
                        else indegree++;
                     }
			
		    outdegreeV.add(outdegree);
                    indegreeV.add(indegree);
		}
               
		return;
	}
          */
          
          
          /*static Graph generateFromListDir(Graph randomGraph, List<Integer> outV, List<Integer> inV)
	{
		

		List<Integer> outvertexList=new ArrayList<Integer>(outV);
                List<Integer> invertexList=new ArrayList<Integer>(inV);
                
		
		// generate randomized list of vertices
		// the vertexList is a set where each node is represented by a number
		// of elements equal to that vertex's degree
		
		Collections.shuffle(outvertexList);
                Collections.shuffle(invertexList);
 
		
		
		// create edges
		int u, v;
		for (int c = 0; outvertexList.size() > 1; ++c) {
			Random generator = new Random();
			u = generator.nextInt(outvertexList.size());
                        v = generator.nextInt(invertexList.size());			                        
			
			int edgeVertexV = outvertexList.get(v);
			int edgeVertexU = invertexList.get(u);
			outvertexList.remove(v);
			invertexList.remove(u);
			randomGraph.addEdge(edgeVertexU, edgeVertexV);
		}
                
                
                
		return randomGraph;
	}*/


}