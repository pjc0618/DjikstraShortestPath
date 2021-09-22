/* Time spent on a6: 2 hours and 00 minutes.
* When you change the above, please do it carefully. Change hh to the hours and mm
* to the minutes and leave everything else as is. If the minutes are 0, change mm
* to 0. This will help us in extracting times and giving you the average and max.
* 
* Name: Philip Cipollina
* Netid (s):pjc272
* What I thought about this assignment: 
*
*
*/
package student;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import graph.Edge;
import graph.Graph;
import graph.Node;

/** This class contains the shortest-path algorithm and other methods */
public class Paths {

    /** Return the shortest path from node v to node end ---or the empty list
     * if a path does not exist.
     * Note: The empty list is NOT "null"; it is a list with 0 elements. */
    public static List<Node> minPath(Node v , Node end) {
        /* TODO Read Piazza note Assignment A6 for ALL details. */
        Heap<Node> F= new Heap<Node>(false); // As in abstract algorithm in @700.
        HashMap<Node, DistBack>info= new HashMap<Node, DistBack>(); //This Hashmap contains all
        //nodes who are not in the far off set as the keys to a DistBack object containing the 
        //shortest known distance to the node and the node's backpointer
        F.insert(v, 0); Node f=null;
        HashMap<Node, DistBack> S=new HashMap<Node, DistBack>();
        // invariant:
        //For a Node in info, a shortest v->s path exists containing only nodes in info;
        //the value DistBack contains the distance and the backpointer
        //For a Node in F, a v->f path exists with only nodes in info and f itself;
        //f is added to info with DistBack containing the distance and f's backpointer
        //All edges leaving info end in F
     while (F.size()>0) {
        f=F.poll();
        if (f==v) info.put(v, new DistBack(null,0));//it's here so invariant is true at start
        if (f==end) return getPath(info,end);
        for(Edge e : f.getExits()) {
			Node w = e.getOther(f);
			int path=e.length+info.get(f).distance;
            if (!info.containsKey(w)) {
               DistBack dw=new DistBack(f,path);
               F.insert(w,path);
               info.put(w, dw);
            } else  {
            	DistBack dw=info.get(w);
            	if (path <dw.distance) {
            		dw.distance= path;
            		dw.bkptr=f;
            		F.changePriority(w, path);
            	}
            }
        }
     }
        
        // no path from v to end
        return new LinkedList<Node>();
    }


    /** Return the path from the beginning node to node end.
     *  Precondition: info contains all the necessary information about
     *  the path. */
    public static List<Node> getPath(HashMap<Node, DistBack> info, Node end) {
        List<Node> path= new LinkedList<Node>();
        Node p= end;
        // invariant: All the nodes from p's successor to the end are in
        //            path, in reverse order.
        while (p != null) {
            path.add(0, p);
            p= info.get(p).bkptr;
        }
        return path;
    }

    /** Return the sum of the weights of the edges on path pa.
     * Precondition: pa contains at least 1 node. If 1, it's a path of length 0,
     * i.e. with no edges. */
    public static int sumPath(List<Node> pa) {
        synchronized(pa) {
            Iterator<Node> iter= pa.iterator();
            Node node= iter.next();  // First node on path
            int sum= 0;
            // invariant: s = sum of weights of edges from start to v
            while (iter.hasNext()) {
                Node q= iter.next();
                sum= sum + node.getEdge(q).length;
                node= q;
            }
            return sum;
        }
    }

    /** An instance contains information about a node: the previous node
     *  on a shortest path from the start node to this node and the distance
     *  of this node from the start node. */
    private static class DistBack {
        private int distance; // distance from start node to this one
        private Node bkptr; // backpointer on path from start node to this one

        /** Constructor: an instance with backpointer p and
         * distance d from the start node.*/
        private DistBack(Node p, int d) {
            distance= d;     // Distance from start node to this one.
            bkptr= p;  // Backpointer on the path (null if start node)
        }

        /** return a representation of this instance. */
        public String toString() {
            return "dist " + distance + ", bckptr " + bkptr;
        }
    }
    
    
}
