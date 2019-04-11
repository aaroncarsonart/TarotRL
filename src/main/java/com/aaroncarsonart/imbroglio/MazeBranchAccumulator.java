package com.aaroncarsonart.imbroglio;

import com.aaroncarsonart.tarotrl.util.Logger;

import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

/**
 * MazeBranchAccumulator counts and maintains distance of each cell to a fork
 * in the maze, where a fork is a cell where there are 3 or more adjacent cells
 * of the same type.
 * <p>
 * MazeBranchAccumulator has two modes: PATH mode and WALL mode.  PATH mode
 * accumulates the connections of paths.  WALL mode accumulates the connections
 * of walls.  My first intuition was that counting paths made the most sense;
 * however, I found now that the best way to fetch walls to remove was to
 * accumulate the distances of wall sections to wall forks (and to dead ends);
 * this way, the highest distance walls if removed will lead to a more highly
 * connected maze.
 *
 * @author Aaron Carson
 * @version Mar 15, 2015
 */
public class MazeBranchAccumulator {
    private static final Logger LOG = new Logger(MazeBranchAccumulator.class).disabled();

    // *************************************************************************
    // Fields
    // *************************************************************************

    private int width;
    private int height;
    private Cell[][] cells;
    private PriorityQueue<Cell> wallQueue;
    private Maze maze;
    private byte accumulatorType;

    // *************************************************************************
    // INNER CLASS: Cell
    // *************************************************************************

    /**
     * Cell is used to track the data needed for each cell, incuding if it is a
     * path or wall and what it's current count is.
     *
     * @author Aaron Carson
     * @version Mar 15, 2015
     */
    public class Cell {
        int x;
        int y;
        int distance;
        byte type;

        /**
         * make a new Cell.
         * @param type Set to Maze.PATH or Maze.WALL, respectively.
         */
        public Cell(int x, int y, byte type) {
            this.x = x;
            this.y = y;
            this.type = type;
        }

        @Override
        public int hashCode(){
            return 1000 * x + y;
        }

        /**
         * Convenience method to get every neighbor of this origin, i.e. get
         * below(), right(), above(), and left(), called in that order.
         * @return An ArrayList of Positions.
         */
        public LinkedList<Cell> getNeighbors() {
            return getNeighbors(accumulatorType);
        }

        /**
         * Convenience method to get every neighbor of this origin, i.e. get
         * below(), right(), above(), and left(), called in that order.
         * @return An ArrayList of Positions.
         */
        public LinkedList<Cell> getNeighbors(byte type) {
            LinkedList<Cell> neighbors = new LinkedList<Cell>();

            // left
            if (y - 1 >= 0 && cells[x][y - 1].type == type) {
                neighbors.add(cells[x][y - 1]);
            }

            // down
            if (x + 1 < width && cells[x + 1][y].type == type) {
                neighbors.add(cells[x + 1][y]);
            }

            // right
            if (y + 1 < height && cells[x][y + 1].type == type) {
                neighbors.add(cells[x][y + 1]);
            }

            // up
            if (x - 1 >= 0 && cells[x - 1][y].type == type) {
                neighbors.add(cells[x - 1][y]);
            }
            return neighbors;
        }

        /**
         * A simple string representation of this Cell.
         */
        public String toString() {
            return "(" + x + "," + y + ")";
        }

        /**
         * get a string representing the distance with the specified padding.
         * @param width The desired width, in characters.
         * @return a String.
         */
        public String getPaddedDistance(int width) {
            String s = "";

            // CASE 1: The Cell is not a Path (print spaces)
            if (type != accumulatorType) {
                for (int i = 0; i < width; i++) {
                    s = ' ' + s;
                }
                return s;
            }

            // CASE 2: Print a padded string representation of the distance.
            else {
                s = String.valueOf(distance);
                int padding = Math.max(0, width - s.length());

                // add padding to front of string
                for (int i = 0; i < padding; i++) {
                    s = ' ' + s;
                }
                return s;
            }
        }
    }

    /**
     * get a string representing the distance with the specified padding.
     * @param width The desired width, in characters.
     * @return a String.
     */
    public String getPaddedNumber(int width, int number) {
        String s = "";
        s = String.valueOf(number);
        int padding = Math.max(0, width - s.length());

        // add padding to front of string
        for (int i = 0; i < padding; i++) {
            s = ' ' + s;
        }
        return s;
    }

    // *************************************************************************
    // Inner Class: CellComparator
    // *************************************************************************

    /**
     * Used for the Cell PriorityQueue, sorting small as a higher priority.
     *
     * @author Aaron Carson
     * @version Mar 15, 2015
     */
    class CellComparator implements Comparator<Cell> {

        @Override
        public int compare(Cell c1, Cell c2) {
            return c1.distance - c2.distance;
        }

    }

    /**
     * Used for the Cell PriorityQueue. This sorts large as higher priority.
     *
     * @author Aaron Carson
     * @version Mar 15, 2015
     */
    class ReverseCellComparator implements Comparator<Cell> {

        @Override
        public int compare(Cell c1, Cell c2) {
            return c2.distance - c1.distance;
        }

    }

    // *************************************************************************
    // Constructor
    // *************************************************************************

    /**
     * Create a new MazeBranchAccumulator, based off of a given maze and
     * accumulation Type.  It has no accumulated values when first created.
     * @param maze The maze to accumulate.
     * @param accumulatorType Determines if this BranchAccumulator accumulates
     *                        the distances of walls or paths. (Set to
     *                        Maze.PATH or Maze.WALL)
     */
    public MazeBranchAccumulator(Maze maze, byte accumulatorType) {
        this.maze = maze;
        this.width = maze.getWidth();
        this.height = maze.getHeight();
        this.accumulatorType = accumulatorType;

        // initialize cell array
        cells = new Cell[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Cell cell = new Cell(x, y, maze.getCell(x, y));
                cells[x][y] = cell;
            }
        }
        wallQueue = new PriorityQueue<Cell>();
    }

    // *************************************************************************
    // Methods
    // *************************************************************************

    /**
     * Creates a graph traversal of this maze.  It catches each connected
     * component (so it will contain a forest if the graph is not entirely
     * connected).
     * @return A graph of positional data.
     */
    public Graph<Cell> getCellGraph() {

        // track which cells have been visited
        HashSet<Cell> visited = new HashSet<Cell>();

        // holds the nextInt cells to be visited.
        Queue<Cell> queue = new LinkedList<Cell>();

        // The underlying graph structure holding all cells of this maze.
        Graph<Cell> graph = new Graph<Cell>();

        // check each cell in the maze.
        for (int x = 0; x < width; x++){
            for (int y = 0; y < height; y++){
                Cell cell = cells[x][y];
                // if it is the correct type, and has not been visited
                if (cell.type == accumulatorType && !visited.contains(cell)){

                    visited.add(cell);	// mark as visited
                    queue.add(cell);	// add to queue
                    graph.addVertex(cell);  // add vertex to graph

                    // find each component connected to the cell
                    while (!queue.isEmpty()) {
                        Cell current = queue.remove();
                        LinkedList<Cell> neighbors = current.getNeighbors(accumulatorType);

                        // for all neighbors
                        for (Cell neighbor : neighbors) {

                            // ignore backtracking
                            if (visited.contains(neighbor)) {
                                continue;
                            }

                            // add to queue
                            else {
                                // mark this origin as added
                                visited.add(neighbor);

                                graph.addVertex(neighbor);
                                graph.addEdge(current, neighbor);

                                // add this state to the state queue.
                                queue.add(neighbor);
                            }
                        }

                    }


                }
            }
        }
        // start at the first cell



        return graph;
    }

    /**
     * This sets the cell's distance values according to their distance from a
     * forking cell starting with the cell at origin (1,1) (the top-left
     * corner). A forking cell is any cell that is a path and has more than two
     * neighbors.
     * <p>
     * This gives a default terminalOffset equal to the width of the maze, as
     * a general scalable heuristic to ensure that the ends of long, winding
     * "rivers" are of a higher priority to be connected.
     */
    private Set<Cell> countBranchDistances() {
        return countBranchDistances(1, 1, width);
    }

    /**
     * This sets the cell's distance values according to their distance from a
     * forking cell. A forking cell is any cell that is a path and has more than
     * two neighbors. This algorithm begins from the input coordinates.
     * <p>
     * This will begin by assigning the initial cell to a zero distance, to
     * ensure this method will fix altered tables; however, if the initial cell
     * is NOT a fork, this will be fixed when it hits a fork and backtracks to
     * the initial origin again.
     * <p>
     * @param x The x origin of the starting cell.
     * @param y The y origin of the starting cell.
     * @param terminalOffset the amount to increase the distance of terminal
     *        cells (cells with only one neighbor) by.  Having a value greater
     *        than zero will increase the likelihood of these single-neighbor
     *        cells to be picked first as candidates for having walls removed.
     */
    public Set<Cell> countBranchDistances(int x, int y, int terminalOffset) {
        Graph<Cell> graph = getCellGraph();
        Set<Cell> visited = new HashSet<Cell>();
        Queue<Cell> queue = new LinkedList<Cell>();

        Cell initial = cells[x][y];
        initial.distance = 0;
        LOG.trace("initial type: " + initial.type);

        queue.add(initial);
        visited.add(initial);

        while (!queue.isEmpty()) {
            Cell current = queue.poll();
            Set<Cell> neighbors = graph.getEdges(current);

            // reset count if branch occurs (branch has more than 2 neighbors)
            if (accumulatorType == Maze.PATH && neighbors.size() > 2){
                current.distance = 0;
            }
            // offset terminal cells (cells with ONLY one neighbor)
            else if (neighbors != null && neighbors.size() == 1){
                // offset dead-end passages if in path mode
                if(accumulatorType == Maze.PATH){
                    current.distance += terminalOffset;
                }
                // make terminal walls if in wall mode
                else if (accumulatorType == Maze.WALL){
                    current.distance = 0;
                }
            }

            for (Cell neighbor : neighbors) {

                // ignore if already visited and distance is less than current
                if (visited.contains(neighbor)
                        && neighbor.distance < current.distance + 1) {
                    continue;
                }

                // add to queue, changing the distance value.
                // (this will cause backtracking when a fork is reached)
                else {
                    neighbor.distance = current.distance + 1;
                    queue.add(neighbor);
                    visited.add(neighbor);
                }
            }
        }
        return visited;
    }

    /**
     * A String representation of this MazeBranchAccumulator.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int padding = 2;

        // append initial padding
        sb.append(' ');
        for (int i = 0; i < padding; i++){
            sb.append(' ');
        }
        sb.append(" |");
        // append x indices
        for (int x = 0; x < width; x++){
            sb.append(' ');
            sb.append(getPaddedNumber(padding, x));
        }
        sb.append('\n');
        for (int x = 0; x < width + 2; x++){
            sb.append("-");
            for (int i = 0; i < padding; i++){
                sb.append('-');
            }

        }
        sb.append('\n');

        // for each row
        for (int y = 0; y < height; y++) {

            // append y indices
            sb.append(' ');
            sb.append(getPaddedNumber(padding, y));
            sb.append(" |");

            // for each column
            for (int x = 0; x < width; x++) {
                sb.append(' ');
                sb.append(cells[x][y].getPaddedDistance(padding));
                // sb.append(')
            }
            sb.append('\n');
        }
        return sb.toString();

    }

    /**
     * Get a PriorityQueue for the current state of this MazeBranchAccumulator
     * of ONLY the removable walls (walls that are not on the perimeter of the
     * maze, and malls that could have been removed by the maze generation
     * algorithm).
     *
     * @return A new PriorityQueue of ONLY the removable walls.
     */
    public PriorityQueue<Cell> getRemovableWallsPriorityQueue() {
        wallQueue = new PriorityQueue<Cell>(width * height,
                new ReverseCellComparator());

        boolean oddx;
        boolean oddy;

        // for each row
        for (int x = 1; x < width - 1; x++) {
            if (x % 2 == 1) oddx = true;
            else oddx = false;

            // for each column
            for (int y = 1; y < height - 1; y++) {
                if (y % 2 == 1) oddy = true;
                else oddy = false;
                Cell c = cells[x][y];
                // if current row and column are not BOTH odd, AND it is a wall
                if (((oddx && !oddy) || (!oddx && oddy)) && c.type == Maze.WALL) {


                    // if it accumulates paths, then the wall value is the
                    // surrounding paths.
                    if (accumulatorType == Maze.PATH){
                        for (Cell neighbor : c.getNeighbors(Maze.PATH)) {
                            c.distance += neighbor.distance;
                        }
                    }
                    // otherwise, it is just the value of the wall.
                    wallQueue.add(c);
                }
            }
        }

        return wallQueue;
    }

    /**
     * This iterates over a set of Cells, and remove/adds them only if they
     * already exist in the queue.
     * @param cells The cells to add and remove.
     */
    public void removeAndAddToPriorityQueue(Set<Cell> cells){
        for(Cell cell: cells){
            if(wallQueue.contains(cell)){
                wallQueue.remove(cell);
                wallQueue.add(cell);
            }
        }
    }

    // *************************************************************************
    // TESTING
    // *************************************************************************

    public static void testAccumulatorWithPaths(int height, int width) {

        // 1. create Maze and BranchAccumulator
        Maze m = Maze.generateRandomWalledMaze(height, width);
        MazeBranchAccumulator mba = new MazeBranchAccumulator(m, Maze.PATH);

        // 2. count branch distances
        long time = System.currentTimeMillis();
        mba.countBranchDistances();
        time = System.currentTimeMillis() - time;
        LOG.trace(mba);
        LOG.trace("Time to run: " + time / 1000.0f + " seconds");

        // 3. test wall queue
        PriorityQueue<Cell> wallQueue = mba.getRemovableWallsPriorityQueue();
        for (int i = 0; i < 5 && !wallQueue.isEmpty(); i++) {
            Cell wall = wallQueue.poll();
            LOG.trace(wall + " " + wall.distance);
        }
    }

    public static void testAccumulatorWithWalls(int height, int width) {

        // 1. create Maze and BranchAccumulator
        Maze m = Maze.generateRandomWalledMaze(height, width);
        MazeBranchAccumulator mba = new MazeBranchAccumulator(m, Maze.WALL);

        // 2. count branch distances
        long time = System.currentTimeMillis();
        mba.countBranchDistances(0,0,width);
        time = System.currentTimeMillis() - time;
        LOG.trace(mba);
        LOG.trace("Time to run: " + time / 1000.0f + " seconds");

        // 3. test wall queue
        PriorityQueue<Cell> wallQueue = mba.getRemovableWallsPriorityQueue();
        for (int i = 0; i < 5 && !wallQueue.isEmpty(); i++) {
            Cell wall = wallQueue.poll();
            LOG.trace(wall + " " + wall.distance);
        }
    }

    /**
     * Test the functionality of the PriorityQueue methods.
     * @param mba The MazeBranchAccumulator to test the PriorityQueue for.
     */
    public static void testPriorityQueue(MazeBranchAccumulator mba){
        PriorityQueue<Cell> wallQueue = mba.getRemovableWallsPriorityQueue();
        for (int i = 0; i < 5 && !wallQueue.isEmpty(); i++) {
            Cell wall = wallQueue.poll();
            LOG.trace(wall + " " + wall.distance);
        }
    }

    /**
     * Test the accumulator logic for both WALLs and PATHs of the same maze.
     * @param height The width to test.
     * @param width The height to test.
     */
    public static void testAccumulator(int height, int width) {

        // 1. create Maze
        Maze m = Maze.generateRandomWalledMaze(height, width);
        long time = 0;

		/*
		// ************************************
		// 2. create and test PATH accumulator
		// ************************************
		LOG.trace("\nPath Accumulator:\n");
		MazeBranchAccumulator pathAccumulator = new MazeBranchAccumulator(m, Maze.PATH);
		time = System.currentTimeMillis();
		pathAccumulator.countBranchDistances(1,1,0);
		time = System.currentTimeMillis() - time;
		LOG.trace(pathAccumulator);
		LOG.trace("Time to run: " + time / 1000.0f + " seconds");
		testPriorityQueue(pathAccumulator);
		*/

        // ************************************
        // 3. create and test WALL accumulator
        // ************************************

        LOG.trace("Wall Accumulator:\n");
        MazeBranchAccumulator wallAccumulator = new MazeBranchAccumulator(m, Maze.WALL);
        time = System.currentTimeMillis();
        wallAccumulator.countBranchDistances(0,0,0);
        time = System.currentTimeMillis() - time;
        LOG.trace(wallAccumulator);
        LOG.trace("Time to run: " + time / 1000.0f + " seconds");

        PriorityQueue<Cell> wallQueue = wallAccumulator.getRemovableWallsPriorityQueue();

        // remove 5 walls
        for (int i = 0; i < 5 && !wallQueue.isEmpty(); i++) {
            // 1. get a wall from the priority queue
            MazeBranchAccumulator.Cell cell = wallQueue.poll();
            LOG.trace("Remove: " + cell + " " + cell.distance);

            // 2. make it a path
            //maze[cell.x][cell.y] = PATH;
            cell.type = Maze.PATH;
            // 3. fix the neighboring walls to the cell with only one neighbor
            for(MazeBranchAccumulator.Cell neighbor : cell.getNeighbors()){
                if(neighbor.getNeighbors().size() == 1){
                    Set<MazeBranchAccumulator.Cell> changed = wallAccumulator.countBranchDistances(neighbor.x, neighbor.y, 0);
                    wallAccumulator.removeAndAddToPriorityQueue(changed);
                }
            }

            LOG.trace(wallAccumulator);
        }

    }

    // *************************************************************************
    // MAIN
    // *************************************************************************

    /**
     * Print a test MazeBranchAccumulator's values.
     * @param args does nothing.
     */
    public static void main(String[] args) {
        // TODO: implement a method for removing walls, one at a time
        // TODO: algorithm should fix the wall

        testAccumulator(20,20);
    }
}
