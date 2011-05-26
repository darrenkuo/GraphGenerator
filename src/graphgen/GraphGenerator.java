package graphgen;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Random;

public class GraphGenerator {

    public final double SAME_CLUSTER_PROB = 0.2;
    public final double DIFF_CLUSTER_PROB = 0.1;

    static class NodeMapping {
	private int[] clusterInfo;
	public int[] idToName;
	private int clusters;

	public NodeMapping(int totalNodes, int clusters) {
	    this.clusterInfo = new int[totalNodes];
	    this.idToName = new int[totalNodes];
	    this.clusters = clusters;
	    
	    Random random = new Random();
	    boolean[] used = new boolean[totalNodes];
	    for (int i = 0; i < totalNodes; i ++) {
		int id = 0;
		while (used[id]) {
		    id = random.nextInt(totalNodes);
		}
		used[id] = true;

		this.clusterInfo[i] = id + 1;
		this.idToName[id] = i;
	    }
	    
	}
	private int getCluster(int id) {
	    return this.clusterInfo[id] / 
		(this.clusterInfo.length / clusters);
	}
    }

    static class Node {
	public ArrayList<Node> neighbors = new ArrayList<Node>();
	public int id;
	public Node(int id) {
	    this.id = id;
	}
    }

    NodeMapping mapping;
    ArrayList<Node> nodes;
    Random random;
    int numberOfEdges = 0;
    int clusters;

    public GraphGenerator(int totalNodes, int clusters) {
	this.clusters = clusters;
	this.mapping = new NodeMapping(totalNodes, clusters);
	this.nodes = new ArrayList<Node>();
	this.random = new Random();
	
	for (int i = 0; i < totalNodes; i ++) {
	    this.nodes.add(new Node(i+1));
	}

	for (int i = 0; i < totalNodes; i ++) {
	    for (int j = i + 1; j < totalNodes; j ++) {
		double prob = random.nextDouble();
		if (this.mapping.getCluster(i) == 
		    this.mapping.getCluster(j)) {
		    if (prob <= SAME_CLUSTER_PROB) {
			this.nodes.get(i).neighbors.add(this.nodes.get(j));
			numberOfEdges ++;
		    }
		} else if (prob <= DIFF_CLUSTER_PROB) {
		    this.nodes.get(i).neighbors.add(this.nodes.get(j));
		    numberOfEdges ++;
		}
		
	    }
	}
    }

    public void outputGraph(OutputStream stream) {
	try {
	    BufferedWriter writer = 
		new BufferedWriter(new OutputStreamWriter(stream));
	    
	    writer.write(this.nodes.size() + " " + numberOfEdges + "\n");
	    for (Node node : this.nodes) {
		for (Node n : node.neighbors) {
		    writer.write(n.id + " 1.0 ");
		}
		writer.write("\n");
	    }
	    writer.close();
	} catch(Exception e) {
	    e.printStackTrace();
	}
    }
    
    public void outputCluster(OutputStream stream) {
	try {
	    BufferedWriter writer = 
		new BufferedWriter(new OutputStreamWriter(stream));
	    
	    int[] idToName = this.mapping.idToName;
	    int clusterSize = idToName.length / clusters;
	    for (int i = 0; i < idToName.length; i ++) {
		writer.write(idToName[i] + "\n");
	    }
	    writer.close();
	} catch(Exception e) {
	    e.printStackTrace();
	}
    }
}