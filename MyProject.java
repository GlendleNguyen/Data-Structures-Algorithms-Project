/**  
 * Aaron Lim (22513032)
 * Glendle Nguyen (22575354)
**/

import java.util.*;

public class MyProject implements Project {
    private final int WHITE = 0;
    private final int GREY = 1;

    public MyProject() {
        // zero argument constructor 
    }

    /**
     * A method to determine if all devices in the network are connected. Devices are considered 
     * connected if they can transmit to every other device in the network. 
     * @param adjlist the network as a graph input
     * @return true if all devices connected, else false. 
     */
    public boolean allDevicesConnected(int[][] adjlist) {
        boolean[] stronglyConnected = new boolean[adjlist.length]; 
        boolean test = true; 

        for(int origin = 0; origin < adjlist.length; origin++) {
            Stack<Integer> stack = new Stack<>(); 
            boolean[] visited = new boolean[adjlist.length]; 

            stack.push(origin); 
            visited[origin] = true; 

            while(!stack.isEmpty()) {
                Integer element = stack.pop(); 
                int[] destinations = adjlist[element]; 

                for(Integer node: destinations) {
                    if(!visited[node]) {
                        stack.push(node);
                        visited[node] = true;
                    }
                }
            }

            for (boolean value: visited) {
                if (value) {
                stronglyConnected[origin] = true; 
                } else {
                return false; 
                }
            }
        }
        return test; 
    }

    /**
     * A method to determine the number of different paths a packet can take in the network to get
     * from a transmitting device to a receiving device. A device will only transmit a packet to a 
     * device that is closer to the destination, where the distance to the destination is the minimum
     * number of hops between a device and the destination. 
     * @param adjlist
     * @param src
     * @param dst
     * @return int number of paths 
     */
    public int numPaths(int[][] adjlist, int src, int dst) {
        if (src == dst) {
            return 1; 
        } else { 
            // Breadth First Search from DST
            int[][] edgeMatrix = adjlist;
            int graphSize = adjlist.length;
            int[] colours = new int[graphSize];
            int[] distances = new int[graphSize];
            Queue<Integer> distanceQueue = new LinkedList<>(); 

            Arrays.fill(colours, WHITE);
            Arrays.fill(distances, -1); 

            int startVertex = dst; 
            distances[startVertex] = 0;
            colours[startVertex] = GREY; 
            distanceQueue.offer(startVertex);

            while(!distanceQueue.isEmpty()) {
                int currentVertex2 = (int) distanceQueue.poll();
                int[] currentEdges = edgeMatrix[currentVertex2];

                for(int i = 0; i < currentEdges.length; i++) {
                    if(colours[currentEdges[i]] == WHITE) {
                        colours[currentEdges[i]] = GREY;
                        distances[currentEdges[i]] = distances[currentVertex2] + 1;
                        distanceQueue.offer(currentEdges[i]);
                    }
                }
            }
            // Breadth First Search from SRC
            int paths = 0;
            for(int i = 0; i < graphSize; i++) {
                colours[i] = WHITE;
            }
            startVertex = src; 
            colours[startVertex] = GREY; 
            distanceQueue.offer(startVertex); 

            while(!distanceQueue.isEmpty()) {
                int currentVertex2 = (int) distanceQueue.poll(); 
                int[] currentEdges  = edgeMatrix[currentVertex2]; 

                for(int i = 0; i < currentEdges.length; i++) {
                    if(colours[currentEdges[i]] == WHITE && distances[i] >= distances[currentEdges[i]]) {
                        if(currentEdges[i] == dst) {
                            paths++; 
                        } else {
                        colours[currentEdges[i]] = GREY; 
                        distanceQueue.offer(currentEdges[i]); 
                        }
                    }
                }
            }
            return paths; 
        }
    }

    /**
     * Compute the minimum number of hops required to reach a device in each subnet query. Each
     * device has an associated IP address. An IP address is here represented as an array of exactly
     * four integers between 0 and 255 inclusive (for example, {192, 168, 1, 1} ). Each query
     * specifies a subnet address. A subnet address is specified as an array of up to four integers
     * between 0 and 255. An IP address is considered to be in a subnet if the subnet address is a
     * prefix of the IP address (for example, {192, 168, 1, 1} is in subnet {192, 168} but not in
     * {192, 168, 2} ). For each query, compute the minimum number of hops required to reach some
     * device in the specified subnet. If no device in that subnet is reachable, return
     * Integer.MAX_VALUE .
     * @param adjlist
     * @param addrs
     * @param src
     * @param queries
     * @return int[] of number of hops required to reach each subnet from src .
     */
    public int[] closestInSubnet(int[][] adjlist, short[][] addrs, int src, short[][] queries) {
        int[] hops = new int[queries.length];
        
        // BFS from src to determine the distances of every other node
        // if a node unreachable, its distance is -1
        int[][] edgeMatrix = adjlist;
        int graphSize = adjlist.length;
        int[] colours = new int[graphSize];
        int[] distances = new int[graphSize];
        Queue<Integer> queue = new LinkedList<>(); 

        Arrays.fill(colours, WHITE);
        Arrays.fill(distances, -1);         

        int startVertex = src; 
        distances[startVertex] = 0;
        colours[startVertex] = GREY; 
        queue.offer(startVertex);

        while(!queue.isEmpty()) {
            int currentDevice = (int) queue.poll();
            int[] currentEdges = edgeMatrix[currentDevice];

            for(int i = 0; i < currentEdges.length; i++) {
                if(colours[currentEdges[i]] == WHITE) {
                    colours[currentEdges[i]] = GREY;
                    distances[currentEdges[i]] = distances[currentDevice] + 1;
                    queue.offer(currentEdges[i]);
                }
            }
        }

        queue.clear(); 

        // BFS from src to find devices in subnet query
        // if a compatible device is found, hops = distance
        // else, hops = Integer.MAX_VALUE
        ArrayList<Integer> devicesSeen = new ArrayList<>();
        for (int i = 0; i < queries.length; i++) {
            queue.offer(src);
            boolean valid = false;
            boolean inSubnet = false; 

            while(!queue.isEmpty()) {
                int currentDevice = queue.poll();
                
                int queryLength = queries[i].length;
                short[] slice = Arrays.copyOfRange(addrs[currentDevice], 0, queryLength);
                if ((Arrays.equals(slice, queries[i]) || (queries[i].length == 0))) {
                    inSubnet = true;
                    }
                if(inSubnet) {
                    hops[i] = distances[currentDevice]; 
                    queue.clear();
                    devicesSeen.clear(); 
                    valid = true; 
                }   else {
                    // offer to the queue each neighbour device for judgment
                    for (int j = 0; j < adjlist[currentDevice].length; j++) {
                        if(!devicesSeen.contains(adjlist[currentDevice][j])) {
                            queue.offer(adjlist[currentDevice][j]);
                            devicesSeen.add(adjlist[currentDevice][j]);
                        }
                    }
                }
            }
            if(!valid) {
                hops[i] = Integer.MAX_VALUE; 
                }
        }
        return hops; 
    }

    public boolean bfs(int [][] residual, int src, int dst, int[] parent){
        int size = residual.length;
        boolean checked[] = new boolean[size];
        for(int i=0; i<size; i++){
            checked[i] = false; //sets all nodes to unchecked
    }

            LinkedList<Integer> queue = new LinkedList<Integer>();
            queue.add(src);
            checked[src]= true;
            parent[src]= -1; //src has no parent

            while(!queue.isEmpty()){
                int current = queue.poll();
                for(int j=0; j<size; j++){
                    if(checked[j]==false && residual[current][j]>0){
                        queue.add(j);
                        parent[j]= current;
                        checked[j]=true;
                    }
                } 
            }
            return(checked[dst]==true);
    }

public int maxDownloadSpeed(int[][] adjlist, int[][] speeds, int src, int dst) {
    /**Download speed between 2 devices
     * May travel via multiple paths simultaneously (Series circuit)
     * -1 if devices are the same
     * speed in one direction may not be same as speed in another direction 
     * Could think of it as a weighed graph in terms of speed?
     * This method uses an implementation of Edmond Karps algorithm
     */
    if(src==dst) {
        return -1;
    }
    else{
    //Trivial variables to use for EK Algorithm
        int u;
        int v;
        int size= adjlist.length;
        //Create a residual graph
        int[][] residual= new int[size][size];
        int[] parent= new int[size];
        //Cumulative speed to be returned
        //int maxSpeed=0;
        for(int i=0; i<size; i++){
            for(int j=0; j<adjlist[i].length; j++){ //here is my error
                residual[i][adjlist[i][j]]= speeds[i][j]; //Retrieve the effective weight(speed) between two devices
            }
        }
    
        //int[] parent= new int[size];
    int maxSpeed=0;
    while(bfs(residual, src, dst, parent)==true){
        int restrictiveSpeed= Integer.MAX_VALUE;
        for(v=dst; v!= src; v=parent[v]){
            u=parent[v];
            if(restrictiveSpeed> residual[u][v]){
                //Ensures that the desired speed does not exceed the effective capacity
                restrictiveSpeed= residual[u][v];
            }
        }
        for(v=dst; v!=src; v=parent[v]){
            u=parent[v];
            residual[u][v]-= restrictiveSpeed;
            residual[v][u]+= restrictiveSpeed;
        }
            maxSpeed+= restrictiveSpeed;
        }
        return maxSpeed;    
        }
    }
}