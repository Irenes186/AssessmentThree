package com.pathfinding;

import static com.config.Constants.*;

import java.util.HashSet;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;

/**
 * A set of useful mathematical functions, mostly used during pathfinding, but also useful outside of this.
 * 
 * @author jl3293
 *
 */
public final class PathFindUtil {
    /**
     * The heuristic function used during A* pathfinding.
     * This is currently set to calculate the diagonal distance using a single pythagoras calculation.
     * 
     * @param cell      The position in cell space from which to calculate the heuristic
     * @param target    The position in cell space to which cell is pathfinding
     * @return          The diagonal distance from cell to target (pythagoras)
     */
    public static float calculateHeuristic (Vector2 cell, Vector2 target) {
        return (float) Math.sqrt(Math.pow(cell.x - target.x, 2) + Math.pow(cell.y - target.y, 2));
    }
    
    /**
     * Given screen-space coordinates, find centre the coordinates within their game grid cell.
     * @param pos   The coordinates to centre
     * @return      The centre of the grid cell in which the coordinates reside
     */
    public static Vector2 centreCoordinatesInCell (Vector2 pos) {
        return getCentreOfCell(new Vector2(floorFloat(pos.x / TILE_DIMS),
                                            floorFloat(pos.y / TILE_DIMS)));
    }
    
    /**
     * Given grid-space coordinates, return the screen-space centre of the cell
     * @param pos   The coordinates of the grid cell
     * @return      The centre of the grid cell in terms of screen space
     */
    public static Vector2 getCentreOfCell (Vector2 pos) {
        return new Vector2((floorFloat(pos.x) + 0.5f) * TILE_DIMS, 
                            (floorFloat(pos.y) + 0.5f) * TILE_DIMS);
    }
    
    /**
     * Calls Math.floor with float casting.
     * @param num   The float to floor
     * @return      The float with decimal values truncated
     */
    public static float floorFloat (float num) {
        return (float) Math.floor((double) num);
    }
    
    /**
     * Tests whether given grid-space coordinates reference a valid, non-blocking grid cell.
     * @param cell              Coordinates for the cell to test
     * @param collisionLayer    The layer containing all currently present collision tiles
     * @return                  True if the cell is valid and not a collision tile, false otherwise
     */
    public static boolean validCell(Vector2 cell, TiledMapTileLayer collisionLayer) {
        TiledMapTileLayer.Cell targetCell = collisionLayer.getCell((int) cell.x, (int) cell.y);
        return 0 < cell.x && cell.x < MAP_WIDTH && 
                0 < cell.y && cell.y < MAP_HEIGHT &&
                (targetCell == null || targetCell.getTile() == null || !targetCell.getTile().getProperties().containsKey(COLLISION_TILE));
    }
    
    /**
     * Tests whether the given Vector is one of: (1, 0), (-1, 0), (0, 1) or (0, -1)
     * TODO: is this function necessary? we are changing direction vector2 to Constants.Direction
     * @param direction The vector to test
     * @return          True if direction is a unit vector directly along the y or x axis
     */
    public static boolean validQuadUnitVector (Vector2 direction) {
        return !direction.isZero() && (Math.abs(direction.x) == 1 ^ Math.abs(direction.y) == 1);
    }
    
    /**
     * Negates both components of a vector. In other words, reflect along the x axis and then along the y axis.
     * TODO: change direction vector2 to Constants.Direction
     * 
     * @param direction The vector to reverse
     * @return          A new instance of Vector2, with components equal to the negated components of direction
     */
    public static Vector2 reverseDir(Vector2 direction) {
        return new Vector2(-direction.x, -direction.y);
    }
    
    /**
     * Get all corners, crossroads and junctions etc, by tracing a line along a straight
     * line of grid cells, until a collision tile is found.
     * TODO: change direction vector2 to Constants.Direction
     * 
     * @param startCell         The cell to begin tracing from
     * @param direction         The direction along the road to trace, must abide by validQuadUnitVector
     * @param collisionLayer    The layer containing all currently present collision tiles
     * @return                  A HashSet containing all corners, crossroads and junctions etc, along the given direction on the road containing startCell
     */
    public static HashSet<Vector2> traceDirectionForCorners (Vector2 startCell, Vector2 direction, TiledMapTileLayer collisionLayer) {
        // The working list of corners
        HashSet<Vector2> corners = new HashSet<Vector2>();
        // If the given cell is blocked, or the direction is invalid, return the empty set
        if (!validCell(startCell, collisionLayer) || !validQuadUnitVector(direction)) {
            return corners;
        }
        
        // First try the cell that is one along the desired direction from startCell
        Vector2 currentCell = startCell.add(direction);
        // Direction vector perpendicular to the searching direction
        // Used to check the cell to the right of the current cell
        direction.rotate90(-1);
        Vector2 dir90 = direction;
        direction.rotate90(1);
        // Direction vector in reverse of dir90
        // Used to check the cell to the left of the current cell
        Vector2 dir90rev = reverseDir(dir90);
        // The cell currently being tested
        Vector2 testCell;
        
        // Until a collision tile is detected or the search goes out of range
        while (validCell(currentCell, collisionLayer)) {

            // Step right of the current cell
            currentCell.add(dir90);
            testCell = currentCell;
            currentCell.sub(dir90);
            // If this is in range and not a collision cell, add the current cell to the list of corners
            if (validCell(testCell, collisionLayer)) {
                corners.add(currentCell);

            } else {
                // Step left of the current cell
                currentCell.add(dir90rev);
                testCell = currentCell;
                currentCell.sub(dir90rev);
                // If this is in range and not a collision cell, add the current cell to the list of corners
                if (validCell(testCell, collisionLayer)) {
                    corners.add(currentCell);

                }
            }
            
            currentCell.add(direction);

        }
        // Return the complete list of corners along this road and direction (may be empty)
        return corners;
    }
    
    /**
     * Get all corners, crossroads and junctions along roads connecting in every direction from a cell
     * TODO: change direction vector2 to Constants.Direction
     * 
     * @param node              The source node to find corners from
     * @param collisionLayer    The layer containing all currently present collision cells
     * @return                  A HashSet containing all reachable corners, crossroads and junctions along the cell's road. Can be empty
     */
    public static HashSet<Vector2> getChildNodes (Vector2 node, TiledMapTileLayer collisionLayer) {

        // The working set of children
        HashSet<Vector2> children = new HashSet<Vector2>();
        
        // Check for corners to the right of the cell
        children.addAll(traceDirectionForCorners(node, new Vector2(1, 0), collisionLayer));
        // Check for corners to the left of the cell
        children.addAll(traceDirectionForCorners(node, new Vector2(-1, 0), collisionLayer));
        // Check above the cell
        children.addAll(traceDirectionForCorners(node, new Vector2(0, 1), collisionLayer));
        // Check below the cell
        children.addAll(traceDirectionForCorners(node, new Vector2(0, -1), collisionLayer));
        
        // Return the complete list of corners on this cell's road in any direction
        return children;
    }
}
