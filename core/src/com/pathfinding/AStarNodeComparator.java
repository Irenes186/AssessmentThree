package com.pathfinding;

import java.util.Comparator;

/**
 * Comparator implementation to decide a total ordering over AStarNodes.
 *
 * @author jl3293
 */
public class AStarNodeComparator implements Comparator<AStarNode> {
    
    @Override
    /**
     * If two AStarNodes are of the same source and target, return
     * -1 if arg0 has a lower heuristic value than arg1
     * 0 if the heuristic values are equal
     * 1 if arg1 has a lower heuristic value than arg0
     */
    public int compare(AStarNode arg0, AStarNode arg1) {
        if (!arg0.getTarget().equals(arg1.getTarget())) {
            throw new RuntimeException ("Attempted to compare AStarNodes with different targets: arg0=" + arg0 + " >>> arg1=" + arg1);
        }
        else if (!arg0.getSource().equals(arg1.getSource())) {
            throw new RuntimeException ("Attempted to compare AStarNodes with different sources: arg0=" + arg0 + " >>> arg1=" + arg1);
        } else {
            return Float.compare(arg0.getHeuristic(), arg1.getHeuristic());
        }
    }
}
