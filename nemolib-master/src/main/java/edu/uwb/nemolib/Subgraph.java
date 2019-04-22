package edu.uwb.nemolib;

import java.io.UnsupportedEncodingException;
import java.util.BitSet;
import java.util.*;

/**
 * A specialized class for representing instances of subgraphs.
 */
public class Subgraph {

    private int[] nodes;
    private AdjacencyMatrix matrix;
    private int currentSize;
    private boolean directed;
 

	/**
	 * Construct a subgraph of a specified order (i.e. number of nodes)
	 * @param order the order of this subgraphs
         * undirected graph as default
	 */
    public Subgraph(int order) {
        // 'order' refers to the number of nodes the subgraph will contain
        this.currentSize = 0;
        this.nodes       = new int[order];
        this.matrix      = new AdjacencyMatrix(order);
        this.directed = false;
    }
    /**
	 * Construct a subgraph of a specified order (i.e. number of nodes)
	 * @param order the order of this subgraphs
         * @param dir directed
	 */
    public Subgraph(int order, boolean dir) {
       
        // 'order' refers to the number of nodes the subgraph will contain
        this.currentSize = 0;
        this.nodes       = new int[order];      
        this.directed = dir;  
         
        if(directed) this.matrix  = new AdjacencyMatrix(order, dir);
        else this.matrix = new AdjacencyMatrix(order);
    }
    
   
        

	/**
	 * Create a deep copy of this subgraph
	 * @return a deep copy of this subgraph
	 */
    public Subgraph copy() {
        Subgraph copy = new Subgraph(order());
        copy.currentSize = currentSize;
	System.arraycopy(nodes, 0, copy.nodes, 0, nodes.length);
        copy.matrix = this.matrix.copy();
        copy.directed = this.directed;
        return copy;
    }

	//TODO rename this. Size should refer to the number of edges
	/**
	 * Get the current getSize of this Subgraph.
	 * @return the getSize of this Subgraph.
	 */ 
    public int size() {
        return currentSize;
    }

	/**
	 * Get number of possible nodes in this Subgraph
	 * @return the maximum order of this Subgraph
	 */
    public int order() {
        return nodes.length;
    }

	/**
	 * Test whether this subgraph has been filled
	 * @return true if subgraph is full; false otherwise
	 */
    public boolean isComplete() {
        return size() == order();
    }

	/**
	 * Gets the id number of the first vertex added to this Subgraph
	 * @return the id of the root
	 */
    public int root() {
        return nodes[0];
    }

	/**
	 * Check whether a vertex exists in this Subgraph
	 * @param vertex the target vertex
	 * @return true if this subgraph contains the specified vertex, false
	 * otherwise
	 */
    public boolean contains(int vertex) {
        for (int i = 0; i < size(); i++) {
            if (nodes[i] == vertex) {
                return true;
            }
        }
        return false;
    }

	/**
	 * Add a vertex to this Subgraph
	 * @param vertex the vertex to addSubgraph to this Subgraph
	 * @param adjacencyList the adjacencyList of the vertex being added
	 */
    public void add(int vertex, AdjacencyList adjacencyList) {
        int index = vertex;
        nodes[currentSize] = index;       
         
        
       /* matrix is different from directed and undirected. 
        If directed, dir_adj lists for all the nodes are needed*/
       
       

        if (directed) {
            for (int i=0; i<currentSize;i++){
                // add row for the index (currentindex --> i)
                if (adjacencyList.contains(get(i))) matrix.addDirEdge(currentSize,i); // 
                
                // add column for index (i-->currentindex)
                int opposit = (-1)*get(i);
                if (adjacencyList.contains(opposit)) matrix.addDirEdge(i, currentSize); 
               

           /*     AdjacencyList prevAdj = this.dir_adjacencyLists.get(nodes[i]);
              
                // add column for index
                if(prevAdj.contains(index))
                    matrix.addDirEdge(i, currentSize);
                
                // add row for index
                
                if (dir_adjacencyLists.get(index).contains(nodes[i])) 
                    matrix.addDirEdge(currentSize, i);
                */
                
            }            
            

        } else {
            for (int i = 0; i < currentSize; i++) {
                if (adjacencyList.contains(get(i))) {
                    matrix.addEdge(i, currentSize);
                }
            }
        }
        currentSize++;
    }

	/**
	 * Get the nth node added to this Subgraph
	 * @return the nth node added to this Subgraph
	 */
    public int get(int n) {
        return nodes[n];
    }

	/**
	 * Get all the nodes in this Subgraph
	 * @return this Subgraph's nodes
	 */
    public int[] getNodes( ) {
        return nodes;
    }

	/**
	 * Return a string representation of this Subgraph. Should display in the 
	 * format [x, y, z], where x, y, and z represent vertices in this subgraph.
	 * @return a string representation of this subgraph
	 */
    @Override
    public String toString() {
        String s = "[";
        for (int i = 0; i < size()-1; i++) {
            s = s + get(i) + ", ";
        }
        
        s = s + get(size()-1)+ "]";
        return s;
    }

	/**
	 * Get the g6 label (byteString) for this Subgraph
	 * @return the g6 label for this Subgraph
	 */
    public String getByteString() {
        
        try {
            return new String(matrix.toBytes(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            System.err.println("Unable to convert to graph6 format...");
            System.exit(-1);
            return null;
        }
    }

    private class AdjacencyMatrix {

        private int order;
        private BitSet matrix;
        private boolean directed; // add for directed matrix

        AdjacencyMatrix(int order) {
            this.order = order;
            this.directed = false;
            this.matrix = new BitSet((order * (order - 1)) / 2);
        }
        
        /* Add for directed graph matrix */
        
         AdjacencyMatrix(int order, boolean dir) {
            this.order = order;
            this.directed = dir;
            if (directed){
               this.matrix = new BitSet(order * order);
            }
            else{
              this.matrix = new BitSet((order * (order - 1)) / 2);
            }
           
        }

        private AdjacencyMatrix(AdjacencyMatrix source) {
            this.order = source.order;
            this.matrix = (BitSet)source.matrix.clone();
            this.directed = source.directed;
        }

        void addEdge(int x, int y) {
            if (x == y) {
                return;
            }
            if (directed) matrix.set(indexForDir(x,y));
            else matrix.set(indexFor(x, y));
        }
        
         void addDirEdge(int x, int y) {
            if (x == y) {
                return;
            }
            matrix.set(indexForDir(x, y));
        }


        boolean hasEdge(int x, int y)
        {
            if (directed) return x == y || matrix.get(indexForDir(x, y));
            else return x == y || matrix.get(indexFor(x, y));
        }       
        

        AdjacencyMatrix copy() {
            return new AdjacencyMatrix(this);
        }

        private int indexFor(int x, int y) {
            int n = Math.max(x, y);
            return ((n * (n - 1)) / 2) + Math.min(x, y);
        }
        
        private int indexForDir(int x, int y) {
            return x * order  + y;
        }

        byte[] toBytes() {
            // Byte representation is per the graph6 format specfication
            // (http://cs.anu.edu.au/~bdm/nauty/nug25.pdf page 74)

            // code adapted from Vartika Verma's Nemo Finder project (UWB 2014)
            byte[] orderBytes = convertOrderToBytes(order);   
            
            // this is for directed graph
            byte[] prefix;
            prefix = new byte[1];
           
                        
            int bitVectorLength = (order * (order - 1)) / 2;
            if (directed) bitVectorLength=order*order;
            
            int outputLength = orderBytes.length + (bitVectorLength / 6)
                    + ((bitVectorLength % 6) > 0 ? 1 : 0);
            if(directed){  // For directed graph, you need "&" before the orderbyte   
                
                prefix[0] = (byte) (38);
                outputLength = prefix.length+ orderBytes.length + (bitVectorLength / 6)
                    + ((bitVectorLength % 6) > 0 ? 1 : 0);
            }

            byte[] output = new byte[outputLength];
            
            if(directed) {
                System.arraycopy(prefix, 0, output, 0, prefix.length);
                System.arraycopy(orderBytes, 0, output, 1, orderBytes.length);
            }
            else System.arraycopy(orderBytes, 0, output, 0, orderBytes.length);

            int currentBit = 0;
            int currentIndex = orderBytes.length;
            if(directed) currentIndex = prefix.length+orderBytes.length;
            byte currentByte = 0;
           
            
           
            for (int i = 0; i < bitVectorLength; i++) {
                 if(matrix.get(i))
                     currentByte = (byte)(currentByte | (1 << (5 - currentBit)));
                                 
  

                    // increment the bit
                    currentBit = (currentBit + 1) % 6;

                    if (currentBit == 0) {
                        // addSubgraph byte to output (increment by 63
                        // according to the graph6 algorithm)
                        output[currentIndex] = (byte)(currentByte + 63);
                        currentIndex++;
                        currentByte = 0;
                    }
                }
            
            
            
            
            /* for (int col = 1; col < order; col++) {
                for (int row = 0; row < col; row++) {
                    if (hasEdge(row, col)) {
                        currentByte = (byte)(currentByte | (1 << (5 - currentBit)));
                    }

                    // increment the bit
                    currentBit = (currentBit + 1) % 6;

                    if (currentBit == 0) {
                        // addSubgraph byte to output (increment by 63
                        // according to the graph6 algorithm)
                        output[currentIndex] = (byte)(currentByte + 63);
                        currentIndex++;
                        currentByte = 0;
                    }
                }
            }*/            
            
                       

            // complete last byte
            if (currentIndex < outputLength) {
                output[currentIndex] = (byte) (currentByte + 63);
            }
 
            

            return output;
        }

        private byte[] convertOrderToBytes(int order) {
            // Per the graph6 format specfication
            // (http://cs.anu.edu.au/~bdm/nauty/nug25.pdf page 74)

            // code adapted from Vartika Verma's Nemo Finder project (UWB 2014)

            byte[] bytes;

            if (order <= 62) {
                bytes = new byte[1];
                bytes[0] = (byte) (order + 63);
            } else if (order <= 258047) {
                bytes = new byte[4];
                bytes[0] = 126;
                bytes[1] = (byte) ((order >>> 12) & 63);
                bytes[2] = (byte) ((order >>> 6) & 63);
                bytes[3] = (byte) (order & 63);
            } else {
                bytes = new byte[8];
                bytes[0] = 126;
                bytes[1] = 126;
                bytes[2] = (byte) ((order >>> 30) & 63);
                bytes[3] = (byte) ((order >>> 24) & 63);
                bytes[4] = (byte) ((order >>> 18) & 63);
                bytes[5] = (byte) ((order >>> 12) & 63);
                bytes[6] = (byte) ((order >>> 6) & 63);
                bytes[7] = (byte) (order & 63);
            }

            return bytes;
        }
    }

}
