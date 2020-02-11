package com.pathfinding;

import com.badlogic.gdx.math.Vector2;

/**
 * Node used in A* pathfinding.
 *
 * @author jl3293
 */
public class AStarNode {
    // Position of this node in space
    private Vector2 pos;
    // Position from which pathfinding began
    private Vector2 source;
    // Position to pathfind to
    private Vector2 target;
    // g(x): the distance travelled from the source so far. A value of -1 indicates that g has not yet been calculated, use incrDistFromSource
    private float gVal;
    // h(x): the value of the heuristic function (defined in com.pathfinding.AStarNodeComparator.java)
    private float hVal;
    // f(x): g(x) + h(x). A value of -1 indicates that g has not yeet been calculated, use incrDistFromSource
    private float fVal;
    // The parent node. Null if this is the root of the tree
    private AStarNode parent;
    
    /**
     * Constructor calculating the heuristic, and initialising f and g as invalid.
     * 
     * @param source    Position from which pathfinding began
     * @param pos       Position of this node in space
     * @param target    Position to pathfind to
     */
    public AStarNode(Vector2 source, Vector2 pos, Vector2 target, AStarNode parent, float distFromParent) {
        // Save arguments locally
        this.pos = pos;
        this.source = source;
        this.target = target;
        this.parent = parent;
        
        // Set g and f to null values, and calculate the heuristic
        gVal = distFromParent;
        hVal = PathFindUtil.calculateHeuristic(pos, target);
        fVal = gVal + hVal;
    }
    
    /**
     * Get the value of the heuristic function for this node.
     * 
     * @return  The value of the heuristic function for this node.
     */
    public float getHeuristic() {
        return hVal;
    }
    
    /**
     * Get the total A* weight for this node (f = g + h)
     * 
     * @return  The A* weighting for this node
     */
    public float getWeight() {
        return fVal;
    }
    
    /**
     * Get the current distance from the source node
     * 
     * @return  The current distance from the source node
     */
    public float getDistFromSource () {
        return gVal;
    }
    
    /**
     * Get the position of this node in space
     * 
     * @return  The position of this node in space
     */
    public Vector2 getPos() {
        return pos;
    }
    
    /**
     * Get the position from which pathfanding began
     * 
     * @return  The position from which pathfinding began
     */
    public Vector2 getSource() {
        return source;
    }
    
    /**
     * Get the position the A* algorithm is pathfinding to
     * 
     * @return  The position pathfinding is attempting to reach
     */
    public Vector2 getTarget() {
        return target;
    }
    
    /**
     * Get the parent of this node. Null if this is the root of the tree.
     * @return  The parent of this node
     */
    public AStarNode getParent() {
        return parent;
    }
    
    /**
     * Get a representation of this node in string form, including all relevant information.
     */
    public String toString() {
        return "AStarNode (" + source.x + ", " + source.y + ") -> " + 
                        "[" + pos.x + ", " + pos.y + "] -> " + 
                        "(" + target.x + ", " + target.y + ") | " + 
                        "g=" + gVal + " h=" + hVal + " f=" + fVal;
    }
    
    @Override
    /**
     * Test whether two AStarNodes are equal, based on their source, position, target, and g values.
     * If an object other than an AStarNode is passed, false is returned.
     */
    public boolean equals(Object other) {
        if (other instanceof AStarNode) {
            return source.equals(((AStarNode) other).source) && 
                    pos.equals(((AStarNode) other).pos) && 
                    target.equals(((AStarNode) other).target) && 
                    gVal == ((AStarNode) other).getDistFromSource();
        }
        return false;
    }
}
