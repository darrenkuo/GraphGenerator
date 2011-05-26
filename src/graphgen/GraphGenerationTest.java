package graphgen;

import java.io.File;
import java.io.FileOutputStream;

public class GraphGenerationTest {
    public static void main(String[] args) {
	GraphGenerator gen = new GraphGenerator(30, 3);
	try {
	    gen.outputGraph(new FileOutputStream(new File("graph.out")));
	    gen.outputCluster(new FileOutputStream(new File("cluster.out")));
	} catch(Exception e) {
	    e.printStackTrace();
	}
    }
}