package components.graph;

import styles.Palette;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GraphHandler implements Runnable {
    private static final int vertexSize = 30;
    private static final long pauseTime = 1000;

    int length;
    int[][] adjacencyMatrix;
    List<Vertex> vertices;
    List<Edge> edges;
    public int startIndex;
    Thread thread;

    public GraphHandler() {
        length = 0;
        adjacencyMatrix = new int[length][length];
        vertices = new ArrayList<>();
        edges = new ArrayList<>();
        startIndex = -1;
    }

    public Vertex getVertex(int index) {
        return vertices.get(index);
    }

    public void addVertex(int x, int y){
        Vertex v = new Vertex(x, y, vertexSize, Palette.unSelectedVertex);

        int [][] tempAdjacencyMatrix = new int[length + 1][length + 1];

        for(int i = 0; i < length; i++){
            if (length >= 0) System.arraycopy(this.adjacencyMatrix[i], 0, tempAdjacencyMatrix[i], 0, length);
        }

        for (int i = 0; i < length; i++){
            tempAdjacencyMatrix[i][length] = 0;
            tempAdjacencyMatrix[length][i] = 0;
        }
        tempAdjacencyMatrix[length][length] = 0;

        adjacencyMatrix = tempAdjacencyMatrix;
        vertices.add(v);
        length++;
    }

    public void deleteVertex(Vertex v){
        for (Edge e : this.VertexGetEdges(v)) {
            this.deleteEdge(e);
        }

        int index = this.findIndexWithPoint(v.getCenter().x, v.getCenter().y);
        int [][] tempAdjacencyMatrix = new int[length-1][length-1];

        for (int i = 0; i < index; i++){
            System.arraycopy(adjacencyMatrix[i], 0, tempAdjacencyMatrix[i], 0, index);
            if (length - (index + 1) >= 0)
                System.arraycopy(adjacencyMatrix[i], index + 1, tempAdjacencyMatrix[i], index + 1 - 1, length - (index + 1));
        }
        for (int i = index+1 ; i < length; i++){
            if (index >= 0) System.arraycopy(adjacencyMatrix[i], 0, tempAdjacencyMatrix[i - 1], 0, index);
            if (length - (index + 1) >= 0)
                System.arraycopy(adjacencyMatrix[i], index + 1, tempAdjacencyMatrix[i - 1], index + 1 - 1, length - (index + 1));
        }

        this.adjacencyMatrix = tempAdjacencyMatrix;
        vertices.remove(v);
        this.length--;
    }


    public void addEdge(Vertex v1, Vertex v2){
        Edge e = new Edge(v1, v2);
        e.askWeight();
        this.edges.add(e);

        int index1 = this.findIndexWithPoint(v1.getCenter().x, v1.getCenter().y);
        int index2 = this.findIndexWithPoint(v2.getCenter().x, v2.getCenter().y);
        adjacencyMatrix[index1][index2]++;
        adjacencyMatrix[index2][index1]++;
    }

    public void deleteEdge(Edge e){
        Vertex v1 = e.v1;
        Vertex v2 = e.v2;
        int index1 = this.findIndexWithPoint(v1.getCenter().x, v1.getCenter().y);
        int index2 = this.findIndexWithPoint(v2.getCenter().x, v2.getCenter().y);
        adjacencyMatrix[index1][index2]--;
        adjacencyMatrix[index2][index1]--;

        this.edges.remove(e);

    }


    public Vertex findVertexWithPoint(int x, int y) {
        for (Vertex v : this.vertices) {
            if (v.contains(x, y)) {
                return v;
            }
        }
        return null;
    }

    public int findIndexWithPoint(int x, int y) {
        for (int i = 0; i < this.length; i++) {
            Vertex v = this.vertices.get(i);
            if (v.contains(x, y)) {
                return i;
            }
        }
        return -1;
    }

    public Edge findEdgeWithPoint(int x, int y) {
        for (Edge e : this.edges) {
            if (e.edgeContains(x, y)) {
                return e;
            }
        }
        return null;
    }

    public List<Edge> VertexGetEdges(Vertex v) {
        List<Edge> edges = new ArrayList<>();
        for (Edge e : this.edges) {
            if (e.v1.getId() == v.getId() || e.v2.getId() == v.getId()) {
                edges.add(e);
            }
        }
        return edges;
    }

    public Edge getEdge(Vertex v1, Vertex v2) {
        for (Edge e : this.edges) {
            boolean p1 = e.v1.getId() == v1.getId() && e.v2.getId() == v2.getId();
            boolean p2 = e.v2.getId() == v1.getId() && e.v1.getId() == v2.getId();
            if (p1 || p2) {
                return e;
            }
        }
        return null;
    }

    public List<Vertex> neighbors(Vertex v1){
        List<Vertex> res = new ArrayList<>();
        for (Edge e : this.VertexGetEdges(v1)) {
            res.add(e.other(v1));
        }
        return res;
    }

    public void draw(Graphics2D g2){
        for (Edge e : this.edges) {
            e.draw(g2);
        }
        for (Vertex v : this.vertices) {
            v.drawVertex(g2);
        }thread = new Thread(this);
    }

    public void animate(){
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        List<Vertex> checked = new ArrayList<>();
        List<Vertex> checkable = new ArrayList<>();
        Vertex start = this.vertices.get(startIndex);
        checkable.add(start);
        while (true){
            checkable.removeIf(checked::contains);

            Vertex min = null;
            for (Vertex c: checkable) {
                if (min == null){
                    min = c;
                    continue;
                }
                if (c.cost < min.cost){
                    min = c;
                }
            }


            if (checkable.isEmpty()){
                break;
            }
            this.checkVertex(min, checked, checkable);

        }
    }

    private void checkVertex(Vertex v, List<Vertex> checked, List<Vertex> checkable){
        List<Vertex> nei = this.neighbors(v);
        nei.removeIf(checked::contains);
        v.setAsSelected();

        for (Vertex n : nei) {
            Edge conn = this.getEdge(v, n);
            n.setAsSelected();
            conn.setAsSelected();

//            System.out.println("n.cost" + n.cost + " v.cost" + v.cost + " conn.weight" + conn.weight);
            if (n.cost > v.cost + conn.weight) {
                n.cost = v.cost + conn.weight;
                n.routes = v.routes + "-" + n.getName();
            }
            pause(GraphHandler.pauseTime);
            n.setAsUnSelected();
            conn.setAsUnSelected();

            if (!checkable.contains(n)) {
                checkable.add(n);
            }
        }
        checked.add(v);
        v.setAsUnSelected();
    }

    public static void pause(long timeInMilliSeconds) {
        long timestamp = System.currentTimeMillis();

        do {
        } while (System.currentTimeMillis() < timestamp + timeInMilliSeconds);

    }

    public void reset(){
        if (thread.isAlive()) {
            thread.interrupt();
        }
        for (Vertex v: this.vertices){
            v.reset();
            v.setAsUnSelected();
        }
        for (Edge e: this.edges){
            e.setAsUnSelected();
        }
    }
}
