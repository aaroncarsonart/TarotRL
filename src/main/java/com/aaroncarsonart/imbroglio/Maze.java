package com.aaroncarsonart.imbroglio;

import com.aaroncarsonart.tarotrl.Globals;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

/**
 * Encapsulates all data for a Maze entity.
 *
 * @author Aaron Carson
 * @version Dec 18, 2014
 */
public class Maze
{
    // ************************************************************************
    // Fields
    // ************************************************************************

    public static final byte	PATH			= 0;
    public static final byte	WALL			= 1;
    public static final byte	OUT_OF_BOUNDS	= 2;

    private static final Random RANDOM = Globals.RANDOM;

    private int					width;
    private int					height;
    private byte[][]			cells;

    // ************************************************************************
    // Constructors
    // ************************************************************************

    /**
     * Create a Maze initialized to be all paths.
     *
     * @param width The width of the maze in cells.
     * @param height The height of the maze in cells.
     */
    public Maze(int width, int height) {
        this.width = width;
        this.height = height;
        cells = new byte[width][height];
    }

    /**
     * Create a Maze initialized to be all paths.
     *
     * @param cells A two-dimensional array of cells to initialize the maze
     *        with.
     */
    public Maze(byte[][] cells) {
        if (cells.length < 3)
            throw new IllegalArgumentException(
                    "Maze must be at least 3 cells wide.");
        if (cells[0].length < 3)
            throw new IllegalArgumentException(
                    "Maze must be at least 3 cells tall.");

        this.width = cells.length;
        this.height = cells[0].length;

        this.cells = cells;
    }

    /**
     * Create a Maze initialized to be all paths.
     *
     * @param cells A two-dimensional array of cells to initialize the maze
     *        with.
     * @param offsets The x and y offset from which to place the byte array.
     * @param width The total width of the maze to create.
     * @param height The total height of the maze to create.
     */
    public Maze(byte[][] cells, Position offsets, int width, int height) {
        if (cells.length < 3)
            throw new IllegalArgumentException(
                    "Maze must be at least 3 cells wide.");
        if (cells[0].length < 3)
            throw new IllegalArgumentException(
                    "Maze must be at least 3 cells tall.");

        width = cells.length;
        height = cells[0].length;

        cells = cells;
    }

    /**
     * Create a Maze initialized to a set value.
     *
     * @param width The width of the maze in cells.
     * @param height The height of the maze in cells.
     * @param initialValue The initial value (Maze.PATH or Maze.WALL);
     */
    public Maze(int width, int height, byte initialValue) {
        this.width = width;
        this.height = height;
        cells = new byte[width][height];
        fillWith(initialValue);
    }

    // ************************************************************************
    // Methods
    // ************************************************************************

    /**
     * Get the width of the maze.
     *
     * @return the number of cells.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Get the height of the maze.
     *
     * @return the number of cells.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Get the entire 2D array of bytes that represent each cell in the maze
     *
     * @return All cells of the maze.
     */
    public byte[][] getCells() {
        return cells;
    }

    /**
     * Iterate over the cells and count the paths, runtime of width * height.
     *
     * @return The number of paths in the maze.
     */
    public int countPaths() {
        int count = 0;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < width; y++) {
                if (getCell(x,y) == PATH) count++;
            }
        }
        return count;
    }

    /**
     * Iterate over the cells and count the walls, runtime of width * height.
     *
     * @return The number of walls in the maze.
     */
    public int countWalls() {
        int count = 0;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < width; y++) {
                if (getCell(x,y) == WALL) count++;
            }
        }
        return count;
    }

    /**
     * Check if the given coordinates are contained within this maze (counting
     * by cell)
     *
     * @param x The horizontal coordinate.
     * @param y The vertical coordinate.
     * @return True, if the coordinates are within the maze.
     */
    public boolean withinBounds(int x, int y) {
        return 0 <= x && x < width && 0 <= y && y < height;
    }

    /**
     * Check if the given coordinates are contained within this maze (counting
     * by cell)
     *
     * @param pos The Position containing the coordinates to check.
     * @return True, if the coordinates are within the maze.
     */
    public boolean withinBounds(Position pos) {
        return withinBounds(pos.x(), pos.y());
    }

    /**
     * Get the cell at (x, y), represented by a byte.
     *
     * @param x The x coordinate.
     * @param y The y coordinate
     * @return A single cell of the maze with a value of either Maze.PATH or
     *         Maze.WALL, or Maze.OUT_OF_BOUNDS if the point is out of bounds.
     */
    public byte getCell(int x, int y) {
        if (withinBounds(x, y)) return cells[x][y];
        else return OUT_OF_BOUNDS;
    }

    /**
     * Get the cell at the given Position, represented by a byte.
     *
     * @param pos the Position to check against.
     * @return A single cell of the maze with a value of either Maze.PATH or
     *         Maze.WALL, or Maze.OUT_OF_BOUNDS if the point is out of bounds.
     */
    public byte getCell(Position pos) {
        return getCell(pos.x(), pos.y());
    }

    public void fillWithPaths() {
        fillWith(PATH);
    }

    public void fillWithWalls() {
        fillWith(WALL);
    }

    public void fillWith(byte b) {
        for (int w = 0; w < width; w++) {
            for (int h = 0; h < height; h++) {
                cells[w][h] = b;
            }
        }
    }

    /*
     * (non-Javadoc) Uses the overloaded ToString(char, char) to build a string
     * representation of the maze using ' ' for paths and '#" for walls.
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return toBorderedString(' ', '#');
    }

    /**
     * Creates a string representation of the maze with the given char values to
     * represent a path and a wall of the maze.
     *
     * @param path The char to print for a path.
     * @param wall The char to print for a wall.
     * @return A string representation of the maze.
     */
    public String toString(char path, char wall) {
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (cells[x][y] == PATH) sb.append(path);
                else if (cells[x][y] == WALL) sb.append(wall);
                else sb.append(cells[x][y]);
                if (x < width - 1) sb.append(' ');
            }

            sb.append('\n');
        }
        return sb.toString();
    }

    /**
     * Creates a string representation of the maze with the given char values to
     * represent a path and a wall of the maze.
     *
     * @param path The char to print for a path.
     * @param wall The char to print for a wall.
     * @return A string representation of the maze.
     */
    public String toBorderedString(char path, char wall) {
        StringBuilder sb = new StringBuilder();

        sb.append('+');
        for (int x = 0; x < width; x++) {
            sb.append(" -");
        }
        sb.append(" +\n");

        for (int y = 0; y < height; y++) {
            sb.append('|');
            for (int x = 0; x < width; x++) {
                sb.append(' ');
                if (cells[x][y] == PATH) sb.append(path);
                else if (cells[x][y] == WALL) sb.append(wall);
                else sb.append(cells[x][y]);
            }
            sb.append(" |\n");
        }

        sb.append('+');
        for (int x = 0; x < width; x++) {
            sb.append(" -");
        }
        sb.append(" +\n");

        return sb.toString();
    }

    // ************************************************************************
    // Static Methods
    // ************************************************************************

    /**
     * Create a random maze, with paths taht extend to the edges. If width or
     * height or not odd numbers, they will be incremented to become odd.
     *
     * @param width
     * @param height
     * @return A new randomly generated maze, using a depth-first search.
     */
    public static Maze generateRandomMaze(int width, int height) {
        if (width % 2 == 0) width++;
        if (height % 2 == 0) height++;
        Maze m = new Maze(width, height);
        m.fillWithWalls();

        ArrayList<Position> checklist = new ArrayList<Position>();
        int halfWidth = width / 2 + 1;
        int halfHeight = height / 2 + 1;
        boolean visited[][] = new boolean[halfWidth][halfHeight];

        boolean digging = true;
        Random rand = RANDOM;
        Position digger = new Position(0, 0);
        visited[digger.x()][digger.y()] = true;

        while (digging) {
            ArrayList<Position> neighbors = new ArrayList<Position>();
            neighbors.add(digger.above());
            neighbors.add(digger.below());
            neighbors.add(digger.left());
            neighbors.add(digger.right());

            Iterator<Position> it = neighbors.iterator();

            while (it.hasNext()) {
                Position pos = it.next();
                int x = pos.x();
                int y = pos.y();
                if (!(0 <= x && x < halfWidth && 0 <= y && y < halfHeight)
                        || visited[x][y]) it.remove();

            }

            if (neighbors.size() > 0) {
                checklist.add(new Position(digger));
                Position target = neighbors.get(rand.nextInt(neighbors.size()));

                int digX;
                if (digger.x() == target.x()) digX = digger.x() * 2;
                else digX = Math.min(digger.x(), target.x()) * 2 + 1;

                int digY;
                if (digger.y() == target.y()) digY = digger.y() * 2;
                else digY = Math.min(digger.y(), target.y()) * 2 + 1;

                m.cells[digX][digY] = PATH;
                m.cells[target.x() * 2][target.y() * 2] = PATH;

                visited[target.x()][target.y()] = true;
                digger = new Position(target);

            }
            else {
                checklist.remove(digger);
                if (checklist.size() == 0) return m;
                else digger = checklist.get(rand.nextInt(checklist.size()));
            }
        }
        return null;
    }

    /**
     * Creates a random maze, surrounded by a "wall" of walls. If width or
     * height or not odd numbers, they will be incremented to become odd.
     *
     * @param width
     * @param height
     * @return A new randomly generated maze, using a depth-first search .
     */
    public static Maze generateRandomWalledMaze(int width, int height) {

        // ensure odd number dimensions
        if (width % 2 == 0) width++;
        if (height % 2 == 0) height++;
        Maze m = new Maze(width, height);

        // initialize values of maze
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (y % 2 == 0) m.cells[x][y] = WALL;
                else if (x % 2 == 0) m.cells[x][y] = WALL;
                else m.cells[x][y] = PATH;
            }
        }

        // local values needed for digging out the maze
        ArrayList<Position> checklist = new ArrayList<Position>();
        int halfWidth = width / 2;
        int halfHeight = height / 2;
        boolean visited[][] = new boolean[halfWidth][halfHeight];

        boolean digging = true;
        Random rand = RANDOM;
        Position digger = new Position(0, 0);
        visited[digger.x()][digger.y()] = true;

        // dig out the maze
        while (digging) {

            // get neighboring positions
            ArrayList<Position> neighbors = new ArrayList<Position>();
            neighbors.add(digger.above());
            neighbors.add(digger.below());
            neighbors.add(digger.left());
            neighbors.add(digger.right());

            Iterator<Position> it = neighbors.iterator();

            // remove neighbors outside of maze
            while (it.hasNext()) {
                Position pos = it.next();
                int x = pos.x();
                int y = pos.y();

                // remove if position has already been visited, or is out of
                // bounds
                if (!(0 <= x && x < halfWidth && 0 <= y && y < halfHeight)
                        || visited[x][y]) it.remove();

            }

            // if there exist at least one valid neighbor to dig to
            if (neighbors.size() > 0) {
                checklist.add(new Position(digger));

                // randomly select a new digging target
                Position target = neighbors.get(rand.nextInt(neighbors.size()));

                // calculate the coordinates to dig out of the maze array
                int x1 = digger.x() * 2 + 1;
                int x2 = target.x() * 2 + 1;

                int digX;
                if (x1 == x2) digX = x1;
                else digX = Math.min(x1, x2) + 1;

                int y1 = digger.y() * 2 + 1;
                int y2 = target.y() * 2 + 1;

                int digY;
                if (y1 == y2) digY = y1;
                else digY = Math.min(y1, y2) + 1;

                // dig out the path inbetween digger and target
                m.cells[digX][digY] = PATH;

                // mark as visited
                visited[target.x()][target.y()] = true;

                // set target position as new digger
                digger = new Position(target);

            }

            // otherwise
            else {

                // digger position is removed from the checklist
                checklist.remove(digger);

                // finished if the algorithm is empty; otherwise, get another
                // random position
                if (checklist.size() == 0) return m;
                else digger = checklist.get(rand.nextInt(checklist.size()));
            }
        }
        return null;
    }

    /**
     * Creates a graph traversal of this maze.
     *
     * @return A graph of positional data.
     */
    public Graph<Position> getPositionGraph() {
        HashSet<Position> visited = new HashSet<Position>();
        Queue<Position> queue = new LinkedList<Position>();
        Graph<Position> graph = new Graph<Position>();

        Position start = findFirstUnvisitedCell(PATH, visited);

        while (start != null) {
            // add them to the queue, and begin searching.
            visited.add(start);
            queue.add(start);
            graph.addVertex(start);

            // find all connected components to this element.
            while (!queue.isEmpty()) {
                Position current = queue.remove();
                for (Position neighbor : current.getNeighbors()) {

                    // case 1: been here already.
                    if (visited.contains(neighbor)) {
                        //System.out.println("already visited");
                        continue;
                    }

                    // case 2: it is a wall
                    else if (getCell(neighbor.x(), neighbor.y()) != Maze.PATH) {
                        //System.out.println("found wall");
                        continue;
                    }

                    // case 4: it is an open path
                    else {
                        //System.out.println("found path from " + current + " to " + neighbor);

                        // mark this position as added
                        visited.add(neighbor);

                        graph.addVertex(neighbor);
                        graph.addUndirectedEdge(current, neighbor);

                        // add this state to the state queue.
                        queue.add(neighbor);
                    }
                }
            }
            // start = null;
            start = findFirstUnvisitedCell(PATH, visited);
        }

        return graph;
    }

    /**
     * Creates a graph traversal of this maze.
     *
     * @return A graph of positional data.
     */
    public List<Set<Position>> getConnectedComponents() {

        List<Set<Position>> components = new ArrayList<Set<Position>>();
        HashSet<Position> visited = new HashSet<Position>();
        Queue<Position> queue = new LinkedList<Position>();
        //Graph<Position> graph = new Graph<Position>();

        Position start = findFirstUnvisitedCell(PATH, visited);

        while (start != null) {
            Set<Position> component = new HashSet<Position>();
            components.add(component);
            // add them to the queue, and begin searching.
            visited.add(start);
            queue.add(start);
            //graph.addVertex(start);
            component.add(start);

            // find all connected components to this element.
            while (!queue.isEmpty()) {
                Position current = queue.remove();
                for (Position neighbor : current.getNeighbors()) {

                    // case 1: been here already.
                    if (visited.contains(neighbor)) {
                        //System.out.println("already visited");
                        continue;
                    }

                    // case 2: it is a wall
                    else if (getCell(neighbor.x(), neighbor.y()) != Maze.PATH) {
                        //System.out.println("found wall");
                        continue;
                    }

                    // case 4: it is an open path
                    else {
                        //System.out.println("found path from " + current + " to " + neighbor);

                        // mark this position as added
                        visited.add(neighbor);

                        //graph.addVertex(neighbor);
                        //graph.addUndirectedEdge(current, neighbor);
                        component.add(neighbor);

                        // add this state to the state queue.
                        queue.add(neighbor);
                    }
                }
            }
            // start = null;
            start = findFirstUnvisitedCell(PATH, visited);
        }

        return components;
    }


    /**
     * Using a graph, finds all connected components, then connects them.
     */
    public void connectDisconnectedComponents() {

        // ***************************************
        // 1. get disconnected components.
        // ***************************************
        //Graph<Position> graph = getPositionGraph();
        //List<Set<Position>> components = graph.getDisconnectedComponents();
        List<Set<Position>> components = this.getConnectedComponents();
        System.out.printf("Disconnected Components: %d\n", components.size());
        System.out.printf("Paths: %d\n", countPaths());

        // *******************************************************
        // 2. remove the front set, as a starting point.
        // ********************************************************
        Iterator<Set<Position>> setIterator = components.iterator();
        Set<Position> result = null;
        if(setIterator.hasNext()){
            result = setIterator.next();
            setIterator.remove();
        }

        // connect each other component to this one.
        while(setIterator.hasNext()){
            // get and remove the next element.
            Set<Position> nextComponent = setIterator.next();
            setIterator.remove();

            // dig a path from this set to the result.
            //TODO: dig to the next thing.
            boolean connected = false;
            Iterator<Position> it = nextComponent.iterator();
            Position digger = null;
            if (it.hasNext()){
                digger = it.next();
                it.remove();
            }
			/*
			while(!connected){

			}
			*/

            // find a path to the result.
            List<Position> path = findPathToComponent(result, digger);

            // make the path positions, and add them to the result.
            for(Position p : path){
                cells[p.x()][p.y()] = PATH;
                result.add(p);
            }

            // add all the positions in this set to the result set.
            for(Position p : nextComponent){
                result.add(p);
            }
        }

    }

    /**
     * Get all neighboring positions that are within bounds of the maze.
     * @param position
     * @return
     */
    public List<Position> getNeighbors(Position position){
        // get all neighbors.
        List<Position> neighbors = position.getNeighbors();
        Iterator<Position> it = neighbors.iterator();
        while(it.hasNext()){
            // if the next element is not within bounds, remove it.
            if(!withinBounds(it.next())) it.remove();
        }
        return neighbors;
    }


    /**
     * This method finds a path from a starting point to a set of Positions.
     * This path may meander over existing paths or walls to get there.  It
     * does not modify the underlying data structure.
     * @param component The set of Positions to find a path to.
     * @param start The starting position.
     * @return
     */
    public List<Position> findPathToComponent(Set<Position> component, Position start){
        Random r = RANDOM;
        HashSet<Position> visited = new HashSet<Position>();
        LinkedList<Position> path = new LinkedList<Position>();
        path.add(start);
        visited.add(start);

        // run search
        while(!path.isEmpty()){
            Position current = path.peek();

            //check neighboring positions.
            List<Position> neighbors = current.getNeighbors();
            Iterator<Position> it = neighbors.iterator();
            while(it.hasNext()){
                Position neighbor = it.next();

                // found a collect_treasure (finished)
                if(component.contains(neighbor)) {
                    path.push(neighbor);
                    return path;
                }
                // otherwise, filter invalid positions.
                else if(!withinBounds(neighbor) || visited.contains(neighbor)){
                    it.remove();
                }
            }

            switch(neighbors.size()){
                case 0:
                    // position is exhausted.
                    path.pop();
                    break;

                default:
                    // push a random neighboring Position onto the stack.
                    Position next = neighbors.get(r.nextInt(neighbors.size()));
                    path.push(next);
                    visited.add(next);
                    break;
            }
        }

        // if reached, no treasures were found.
        return new LinkedList<Position>();


		/*
		Random r = new Random();
		HashSet<Position> visited = new HashSet<Position>();
		Stack<Position> stack = new Stack<Position>();
		stack.push(start);

		List<Position> path = new LinkedList<Position>();

		while(!stack.isEmpty()){
			Position next = stack.pop();
			if(!visited.contains(next)){
				visited.add(next);
				List<Position> neighbors = getNeighbors(next);
				for(int i = 0; i < neighbors.size(); i++){
					Position neighbor = neighbors.remove(r.nextInt(neighbors.size()));
					stack.push(neighbor);
				}
			}
		}

		/*
		List<Position> path = new LinkedList<Position>();
		Set<Position> visited = new HashSet<Position>();
		Position digger = new Position(start);
		visited.add(digger);
		boolean complete = false;
		while(!complete){
			List<Position> neighbors = getNeighbors(digger);
			Position neighbor = neighbors.get(r.nextInt(neighbors.size()));

		}
		*/
        //return path;
    }

    /**
     * Get the first unvisited cell from this Maze of the given type, using a
     * boolean visited array that is tracking what positions have been visited.
     *
     * @param cellType The type of cell to find.
     * @param visited The array of booleans that contain visited data
     * @return The top, left-most unvisited position, or null if every position
     *         has been visited.
     */
    public Position findFirstUnvisitedCell(byte cellType, boolean[][] visited) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (!visited[x][y] && cells[x][y] == cellType) {
                    return new Position(x, y);
                }
            }
        }
        return null;
    }

    /**
     * Get the first unvisited cell from this Maze of the given type, using a
     * boolean visited array that is tracking what positions have been visited.
     *
     * @param cellType The type of cell to find.
     * @param visited The array of booleans that contain visited data
     * @return The top, left-most unvisited position, or null if every position
     *         has been visited.
     */
    public Position findFirstUnvisitedCell(byte cellType,
                                           HashSet<Position> visited) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (!visited.contains(new Position(x, y))
                        && cells[x][y] == cellType) {
                    return new Position(x, y);
                }
            }
        }
        return null;
    }

    /**
     * Get the top-left most cell from this Maze of the given type.
     *
     * @param cellType The type of cell to find.
     * @return The top, left-most unvisited position, or null if not found.
     */
    public Position findFirstOccurrence(byte cellType) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (cells[x][y] == cellType) {
                    return new Position(x, y);
                }
            }
        }
        return null;
    }

    /**
     * Intended for use with randomly-generated mazes only. This removes a
     * number of walls to create more connected passages, and make it easier to
     * navigate the maze.
     */
    public void setDifficulty(Difficulty difficulty) {
        // TODO make me remove walls in various ways.

        // instantiate the accumulator
        MazeBranchAccumulator wallAccumulator = new MazeBranchAccumulator(this,
                WALL);
        wallAccumulator.countBranchDistances(0, 0, width);
        PriorityQueue<MazeBranchAccumulator.Cell> walls = wallAccumulator
                .getRemovableWallsPriorityQueue();
        // used to calculate how many paths to remove
        int multiplier = 0;

        // set addedPaths according to difficulty level
        switch (difficulty) {
            case EASY:
                multiplier = 2;
                break;
            case NORMAL:
                multiplier = 1;
                break;
            case HARD:
                multiplier = 0;
                break;
        }

        // create a list of valid cells to iterate over
        // remove from only what were the original "connecting walls"
        int addedPaths = ((width) / 4) * multiplier;

        // Iterate up to the amount of added paths
        for (int i = 0; i < addedPaths; i++) {
            // 1. get a wall from the priority queue
            MazeBranchAccumulator.Cell cell = walls.poll();
            System.out.println("Remove: " + cell + " " + cell.distance);

            // 2. make it a path
            cells[cell.x][cell.y] = PATH;
            cell.type = Maze.PATH;
            // 3. fix the neighboring walls to the cell with only one neighbor
            for (MazeBranchAccumulator.Cell neighbor : cell.getNeighbors()) {
                if (neighbor.getNeighbors().size() == 1) {
                    Set<MazeBranchAccumulator.Cell> changed = wallAccumulator
                            .countBranchDistances(neighbor.x, neighbor.y, 0);
                    wallAccumulator.removeAndAddToPriorityQueue(changed);
                }
            }

        }
        // removeIslands();
    }

    /**
     * Intended for use with randomly-generated mazes only. This removes a
     * number of walls to create more connected passages, and make it easier to
     * navigate the maze.
     */
    public void setDifficultyOLD(Difficulty difficulty) {
        // used to calculate how many paths to remove
        int multiplier = 0;

        // set addedPaths according to difficulty level
        switch (difficulty) {
            case EASY:
                multiplier = 2;
                break;
            case NORMAL:
                multiplier = 1;
                break;
            case HARD:
                multiplier = 0;
                break;
        }

        // create a list of valid cells to iterate over
        ArrayList<Position> checkList = new ArrayList<Position>();
        for (int x = 1; x < width - 1; x++) {
            for (int y = 1; y < height - 1; y++) {
                // if the coordinate is a wall
                if (cells[x][y] == 1) {
                    // add it to the list to iterate over
                    checkList.add(new Position(x, y));
                }
            }
        }
        int addedPaths = ((width + height) / 4) * multiplier;
        Random random = RANDOM;

        // Iterate up to the amount of added paths
        for (int i = 0; i < addedPaths; i++) {
            int x = 1, y = 1;
            boolean validPanel = false;
            while (!validPanel && checkList.size() != 0) {

                // generate random number within the outer border
                Position temp = checkList.get(random.nextInt(checkList.size()));
                x = temp.x();
                y = temp.y();

				/*
				 * if the panel is a wall, and it is between two other panels on
				 * either the x or y axis that are both paths ...
				 */

                validPanel = (cells[x][y + 1] == 0 && cells[x][y - 1] == 0)
                        || (cells[x + 1][y] == 0 && cells[x - 1][y] == 0);
                checkList.remove(temp);
            }

            // remove the panel if is a valid location
            if (validPanel) cells[x][y] = PATH;

        }
        removeIslands();
    }

    /**
     * Removes panels that is surrounded by whitespace, or removes a panel if it
     * only has one other panel in one of the four corners.
     */
    private void removeIslands() {
        for (int x = 1; x < width - 1; x++) {
            for (int y = 1; y < height - 1; y++) {
                int counter = 0;
                // if the position is a wall
                if ((cells[x - 1][y + 1] == 0 && cells[x][y + 1] == 0
                        && cells[x + 1][y + 1] == 0 && cells[x - 1][y] == 0
                        && cells[x][y] == 1 && cells[x + 1][y] == 0
                        && cells[x - 1][y - 1] == 0 && cells[x][y - 1] == 0 && cells[x + 1][y - 1] == 0)
                        || (cells[x - 1][y + 1] == 1 && cells[x][y + 1] == 0
                        && cells[x + 1][y + 1] == 0
                        && cells[x - 1][y] == 0 && cells[x][y] == 1
                        && cells[x + 1][y] == 0
                        && cells[x - 1][y - 1] == 0
                        && cells[x][y - 1] == 0 && cells[x + 1][y - 1] == 0)
                        || (cells[x - 1][y + 1] == 0 && cells[x][y + 1] == 0
                        && cells[x + 1][y + 1] == 1
                        && cells[x - 1][y] == 0 && cells[x][y] == 1
                        && cells[x + 1][y] == 0
                        && cells[x - 1][y - 1] == 0
                        && cells[x][y - 1] == 0 && cells[x + 1][y - 1] == 0)
                        || (cells[x - 1][y + 1] == 0 && cells[x][y + 1] == 0
                        && cells[x + 1][y + 1] == 0
                        && cells[x - 1][y] == 0 && cells[x][y] == 1
                        && cells[x + 1][y] == 0
                        && cells[x - 1][y - 1] == 1
                        && cells[x][y - 1] == 0 && cells[x + 1][y - 1] == 0)
                        || (cells[x - 1][y + 1] == 0 && cells[x][y + 1] == 0
                        && cells[x + 1][y + 1] == 0
                        && cells[x - 1][y] == 0 && cells[x][y] == 1
                        && cells[x + 1][y] == 0
                        && cells[x - 1][y - 1] == 0
                        && cells[x][y - 1] == 0 && cells[x + 1][y - 1] == 1)) {
                    cells[x][y] = PATH;
                }
            }
        }
    }

    /**
     * Create a new Dungeon with the given parameters.
     *
     * @param width The width in tiles.
     * @param height The height in tiles.
     * @param rooms The number of rectangular rooms to randomly generate in the
     *        space.
     * @return a new dungeon of given size with the given number of
     *         interconnected rooms.
     */
    public static final Maze createWalledDungeon(int width, int height,
                                                 int rooms) {

        // 0. make dungeon with room for an outer wall.
        Maze m = new Maze(width + 2, height + 2);

        // 4. Depth first search through the dungeon like the maze.
		/*
		 * //local values needed for digging out the maze ArrayList<Position>
		 * checklist = new ArrayList<Position>(); int halfWidth = width / 2; int
		 * halfHeight = height / 2; boolean visited[][] = new
		 * boolean[halfWidth][halfHeight];
		 *
		 * boolean digging = true; Random rand = new Random(); Position digger =
		 * new Position(0, 0); visited[digger.x()][digger.y()] = true;
		 *
		 * //dig out the maze while (digging) {
		 *
		 * //get neighboring positions ArrayList<Position> neighbors = new
		 * ArrayList<Position>(); neighbors.add(digger.above());
		 * neighbors.add(digger.below()); neighbors.add(digger.left());
		 * neighbors.add(digger.right());
		 *
		 * Iterator<Position> it = neighbors.iterator();
		 *
		 * //remove neighbors outside of maze while (it.hasNext()) { Position
		 * pos = it.next(); int x = pos.x(); int y = pos.y();
		 *
		 * // remove if position has already been visited, or is out of bounds
		 * if (!(0 <= x && x < halfWidth && 0 <= y && y < halfHeight) ||
		 * visited[x][y]) it.remove();
		 *
		 * }
		 *
		 * //if there exist at least one valid neighbor to dig to if
		 * (neighbors.size() > 0) { checklist.add(new Position(digger));
		 *
		 * //randomly select a new digging target Position target =
		 * neighbors.get(rand.nextInt(neighbors.size()));
		 *
		 * //calculate the coordinates to dig out of the maze array int x1 =
		 * digger.x() * 2 + 1; int x2 = target.x() * 2 + 1;
		 *
		 * int digX; if (x1 == x2) digX = x1; else digX = Math.min(x1, x2) + 1;
		 *
		 * int y1 = digger.y() * 2 + 1; int y2 = target.y() * 2 + 1;
		 *
		 * int digY; if (y1 == y2) digY = y1; else digY = Math.min(y1, y2) + 1;
		 *
		 * // dig out the path inbetween digger and target m.maze[digX][digY] =
		 * PATH;
		 *
		 * // mark as visited visited[target.x()][target.y()] = true;
		 *
		 * // set target position as new digger digger = new Position(target);
		 *
		 * }
		 *
		 * // otherwise else {
		 *
		 * //digger position is removed from the checklist
		 * checklist.remove(digger);
		 *
		 * // finished if the algorithm is empty; otherwise, get another random
		 * position if (checklist.size() == 0) return m; else digger =
		 * checklist.get(rand.nextInt(checklist.size())); } }
		 */
        // 5. Done.
        return m;
    }

    /**
     * Create a room by randomly digging out rooms on top of a maze.
     *
     * @param width
     * @param height
     * @param rooms
     * @return
     */
    public static Maze createRandomWalledRoomedMaze(int width, int height,
                                                    int rooms) {

        // step 1: create a walled maze.
        Maze m = generateRandomWalledMaze(width, height);

        Random random = RANDOM;
        // boolean[][] visited = new boolean[width + 2][height + 2];
        ArrayList<Quad> quads = new ArrayList<Quad>(rooms);

        int minRoomSize = 3;
        int maxRoomSize = 10;
        int offsetWidth = Math.max(maxRoomSize, width / 4) - minRoomSize;
        int offsetHeight = Math.max(maxRoomSize, height / 4 - minRoomSize);

        // System.out.printf("maxWidth: %d maxHeight: %d\n", maxWidth,
        // maxHeight);

        // 2. remove rooms and make as paths and visited.
        for (int i = 0; i < rooms; i++) {
            int y1 = 1 + random.nextInt(height - minRoomSize);
            int y2 = Math.min(height,
                    y1 + minRoomSize + random.nextInt(offsetWidth));
            int x1 = 1 + random.nextInt(width - minRoomSize);
            int x2 = Math.min(width,
                    x1 + minRoomSize + random.nextInt(offsetHeight));
            double xSize = Math.abs(x1 - x2);
            double ySize = Math.abs(y1 - y2);
            double hw = xSize / 2;
            double hh = xSize / 2;
            double cx = Math.min(x1, x2) + hw;
            double cy = Math.min(y1, y2) + hh;

            Quad quad = new Quad(cx, cy, hw, hh);
            for (Quad q : quads) {
                quad.fixIfCollides(q);
            }
            if (!quad.offScreen(width, height)) {
                quads.add(quad);
                for (int x = (int) quad.getLeftX(); x < quad.getRightX(); x++) {
                    for (int y = (int) quad.getTopY(); y < quad.getBottomY(); y++) {
                        m.cells[x][y] = PATH;
                    }
                }
            }
        }

        // 3. Add 1 - 2 unvisited paths on the perimeter of each room.
        for (int i = 0; i < quads.size(); i++) {
            Quad q = quads.get(i);
            int k = random.nextInt(4);
            int x = 0, y = 0;
            switch (k) {
                // add to left
                case 0:
                    x = (int) q.getLeftX() - 1;
                    y = random.nextInt((int) q.getHeight()) + (int) q.getTopY();
                    break;

                // add to right
                case 1:
                    x = (int) q.getRightX() + 1;
                    y = random.nextInt((int) q.getHeight()) + (int) q.getTopY();
                    break;

                // add to above
                case 2:
                    y = (int) q.getTopY() - 1;
                    x = random.nextInt((int) q.getWidth()) + (int) q.getLeftX();
                    break;

                // add to below
                case 3:
                    y = (int) q.getBottomY() + 1;
                    x = random.nextInt((int) q.getWidth()) + (int) q.getLeftX();
                    break;
            }

            m.cells[x][y] = PATH;
        }

        return m;

    }

    /**
     * Generate a room that randomly grows until a percentage is reached.
     *
     * @param width The width of the cells to save.
     * @param height The height of the cells to save.
     * @param percent
     * @return
     */
    public static Maze generateRandomShapedRoom(int width, int height,
                                                double percent, boolean tunnels) {

        Maze m = new Maze(width, height, WALL);
        Random r = RANDOM;
        boolean[][] visited = new boolean[width][height];

        // dig out the center square.
        Position digger = new Position(width / 2, height / 2);
        m.cells[digger.x()][digger.y()] = PATH;
        visited[digger.x()][digger.y()] = true;
        int count = 1;

        int max = width * height;

        // create a room of max size of
        while (count / max < percent) {
            count++;
            // 1. find a bordering neighbor.
            // dig out any unoccupied neighbors.
            List<Position> neighbors = new ArrayList<Position>();
            neighbors.add(digger.above());
            neighbors.add(digger.below());
            neighbors.add(digger.left());
            neighbors.add(digger.right());
            neighbors.add(new Position(digger.x() - 1, digger.y() - 1));
            neighbors.add(new Position(digger.x() - 1, digger.y() + 1));
            neighbors.add(new Position(digger.x() + 1, digger.y() - 1));
            neighbors.add(new Position(digger.x() + 1, digger.y() + 1));

            for (int i = neighbors.size() - 1; i >= 0; i--) {
                Position next = neighbors.get(i);
                if (m.withinBounds(next)) {
                    if (visited[next.x()][next.y()]) {
                        if (tunnels) neighbors.remove(i);
                        continue;
                    }
                    else if (m.cells[next.x()][next.y()] == WALL) {
                        digger = next;
                        m.cells[next.x()][next.y()] = PATH;
                        visited[next.x()][next.y()] = true;
                        count++;
                    }
                }
            }

            if (neighbors.isEmpty()) break;
            digger = neighbors.get(r.nextInt(neighbors.size()));

        }

        return m;
    }

    /**
     * Generate a room by filling with random content, then using cellular
     * automata iterations to form the room.
     *
     * @param width The width of the room boundary in cells.
     * @param height The height of the room boundary in cells.
     * @return A Maze that contains a room generated by the cellular automata
     *         system.
     */
    public static Maze generateCellularAutomataRoom(int width, int height) {

        Maze m = new Maze(width, height);
        Random r = RANDOM;

        // randomly fill with paths or walls.
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                m.cells[x][y] = r.nextBoolean() ? WALL : PATH;
            }
        }

        System.out.printf("%d:\n%s\n", 0, m.toBorderedString('.', '#'));
        // run a number of cellular automata iterations
        int iterations = 6;
        int wallThreshold = WALL_THRESHOLD;
        int pathThreshold = PATH_THESHOLD;
        for (int i = 0; i < iterations; i++) {
            m.cellularAutomataIteration(wallThreshold, pathThreshold);
        }

        return m;
    }

    public static final int WALL_THRESHOLD = 4;
    public static final int PATH_THESHOLD = 5;

    /**
     * Runs one iteration of cellular automata with the default WALL_THRESHOLD
     * and PATH_THRESHOLD values.
     */
    public void cellularAutomataIteration(){
        cellularAutomataIteration(WALL_THRESHOLD, PATH_THESHOLD);
    }

    /**
     * Run one iteration of cellular automata with the given threshold values.
     * For each position, if the neighboring cells' wall count is beneath the
     * threshold, the cell becomes a wall; otherwise, it becomes a path.
     * @param wallThreshold The minimum neighboring WALLs for a WALL cell.
     * @param pathThreshold The minimum neighboring WALLs for a PATH cell.
     */
    public void cellularAutomataIteration(int wallThreshold, int pathThreshold){
        byte[][] current = new byte[width][height];

        // set each cell
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int neighboringWalls = 8 - countSurroundingPaths(x, y);

                // case 1: cell is a WALL.
                if (getCell(x, y) == WALL) {
                    current[x][y] = neighboringWalls < wallThreshold ? PATH: WALL;
                }

                // case 2: cell is a PATH
                else {
                    current[x][y] = neighboringWalls < pathThreshold ? PATH: WALL;

                }
            }
        }

        // replace the maze's cell array.
        cells = current;
    }

    /**
     * Check all 8 neighboring cells, and count any cells that are PATHs.
     *
     * @param x The x coordinate of the cell to check.
     * @param y The y coordinate of the cell to check.
     * @return The number of neighboring cells that are PATHs.
     */
    public int countSurroundingPaths(int x, int y) {

        // check each cell in the Moore neighborhood, and count the paths.
        int paths = 0;

        // top
        paths += getCell(x - 1, y + 1) == PATH ? 1 : 0;
        paths += getCell(x, y + 1) == PATH ? 1 : 0;
        paths += getCell(x + 1, y + 1) == PATH ? 1 : 0;

        // middle
        paths += getCell(x - 1, y) == PATH ? 1 : 0;
        paths += getCell(x + 1, y) == PATH ? 1 : 0;

        // bottom
        paths += getCell(x - 1, y - 1) == PATH ? 1 : 0;
        paths += getCell(x, y - 1) == PATH ? 1 : 0;
        paths += getCell(x + 1, y - 1) == PATH ? 1 : 0;

        return paths;
    }

    /**
     * Convenience method for hard-coding generated mazes in code.
     *
     * @param path The string to represent a path as.
     * @param wall The string to represent a wall as.
     * @return a 2d array block {{}}.
     */
    public String getTwoDimensionalArrayText(String path, String wall) {
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        for (int y = 0; y < height; y++) {
            sb.append('{');
            for (int x = 0; x < width; x++) {
                if (cells[x][y] == Maze.PATH) sb.append(path);
                else sb.append(wall);
                sb.append(", ");
            }
            sb.append("},\n");
        }
        sb.append('}');
        return sb.toString();
    }

    private static void testRandom(int width, int height) {
        Maze m;
        m = Maze.createWalledDungeon(width, height, 20);
        // m = Maze.generateRandomWalledMaze(width, height);
        // m.setDifficulty(Difficulty.EASY);
        System.out.println(m.toString());

    }

    public static final int	AND		= 0;
    public static final int	NAND	= 1;
    public static final int	OR		= 2;
    public static final int	XOR		= 3;

    /**
     * Combine a set of mazes according to boolean rules.
     * <p>
     * WALL counts as true, and PATH counts as false.
     *
     * @param mazes The set of mazes to combine.
     * @param rule The rule to use.
     * @return
     */
    public static Maze combineMazes(Maze[] mazes, Position[] offsets, int rule) {
        int width = 0, height = 0;

        // calculate final maze dimensions.
        for (int i = 0; i < mazes.length; i++) {
            width = Math.max(mazes[i].width + offsets[i].x(), width);
            height = Math.max(mazes[i].height + offsets[i].y(), height);
        }

        // set composite to the max dimensions, and initialize with first maze.
        Maze initial = mazes[0];
        int ix = offsets[0].x();
        int iy = offsets[0].y();
        Maze composite = new Maze(width, height, PATH);
        for (int x = 0; x < initial.width; x++) {
            for (int y = 0; y < initial.height; y++) {
                composite.cells[x + ix][y + iy] = initial.cells[x][y];
            }
        }

        for (int x = 1; x < width; x++) {
            for (int y = 0; y < height; y++) {

                boolean wall = composite.cells[x][y] == WALL;
                // combine each maze.
                for (int i = 1; i < mazes.length; i++) {

                    // get the current cell (with offset), true if a wall.
                    boolean current = !(mazes[i].getCell(x - offsets[i].x(), y
                            - offsets[i].y()) == PATH);
                    switch (rule) {
                        case AND:
                            wall = wall & current;
                            break;
                        case NAND:
                            wall = !(wall & current);
                            break;
                        case OR:
                            wall = wall | current;
                            break;
                        case XOR:
                            wall = wall ^ current;
                            break;
                    }
                }

                composite.cells[x][y] = wall ? WALL : PATH;
            }
        }

        return composite;
    }

    /**
     * Get a scaled copy of a maze.
     * @param original The original maze to scale.
     * @param scale The value to scale by, must be greater than zero.
     * @return A new maze.
     */
    public static Maze createScaledCopy(Maze original, int scale){
        // ensure proper arguments
        if (scale < 1){
            throw new IllegalArgumentException("Scale must be greater than zero.");
        }

        Maze maze = new Maze(original.width * scale, original.height * scale);

        // loop through original maze
        for(int x = 0; x < original.width; x++){
            for(int y = 0; y < original.height; y++){

                // loop through 0 to scale for x and y, setting new maze values
                for(int x2 = x * scale; x2 < (x + 1) * scale; x2++){
                    for(int y2 = y * scale; y2 < (y + 1) * scale; y2++){
                        maze.cells[x2][y2] = original.cells[x][y];
                    }
                }
            }
        }

        return maze;
    }

    // ************************************************************************
    // MAIN
    // ************************************************************************

    /**
     * Used for testing.
     *
     * @param args
     */
    public static void main(String[] args) {
        // Try main args first
        if (args.length == 2) {
            try {
                int width = Integer.parseInt(args[0]);
                int height = Integer.parseInt(args[1]);
                testRandom(width, height);
            }
            catch (NumberFormatException ex) {
                System.out.print("Invalid input; ");
                System.out.println("please pass 2 ints for width and height.");
            }
        }

        // use default values
        else {
            // testRandom(100, 100);
            boolean tunnels = true;
            boolean cavern = false;

            testDisconnectedComponents();
			/*
			 * Maze[] mazes = { Maze.generateRandomWalledMaze(60,60),
			 * Maze.generateCellularAutomataRoom(40,40), };
			 *
			 * Position[] offsets = { new Position(0,0), //new Position(0,0),
			 * new Position(10,10), };
			 *
			 * Maze composite = Maze.combineMazes(mazes, offsets, AND);
			 * System.out.printf("Final:\n%s\n",composite.toString());
			 */
            // System.out.printf("%b %b\n", true & false, true && false);
            // System.out.printf("%b %b\n", true | false, true || false);

			/*
			 * for (double p = 0.1; p < 1.0; p += 0.1){ long t =
			 * System.currentTimeMillis();
			 * System.out.printf("\n%2.2f%% Cavern\n", p); m =
			 * generateRandomShapedRoom(50,50, p, cavern);
			 * System.out.println(m.toString());
			 *
			 * System.out.printf("\n%2.2f%% Tunnels\n", p); m =
			 * generateRandomShapedRoom(50,50, p, tunnels);
			 * System.out.println(m.toString());
			 * System.out.printf("elapsed: %.5f s.\n",
			 * (System.currentTimeMillis() - t) / 1000.0); }
			 */

        }
    }

    /**
     * Get a maze generated to test bordered tile rendering.
     * @return A maze.
     */
    public static Maze getTestMaze(){
        return  new Maze(new byte[][]{
                {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1,1,1,1,1,0,1,1,1,1,1,1,1,1,0,1,1,1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1,1,1,1,1,0,1,1,1,1,1,1,1,1,0,1,1,1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1,1,1,1,1,0,1,1,1,1,1,1,1,1,0,1,1,1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,1,1,1,0,1,1,1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1,1,1,1,1,0,1,1,0,1,0,1,1,1,0,1,1,1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,1,0,1,1,1,0,1,1,1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1,1,1,1,1,0,0,1,0,1,0,0,1,1,0,1,1,1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1,1,1,1,1,0,1,1,1,0,1,0,1,1,0,1,1,1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1,1,1,1,1,0,0,1,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,1,0,1,1,1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1,1,1,1,1,0,0,1,0,0,0,0,0,1,0,1,1,1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1,1,1,1,1,0,1,0,0,1,1,1,0,1,0,1,1,1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
        });
    }

    public static void testDisconnectedComponents() {
        // Maze m = Maze.generateRandomWalledMaze(30, 30);

        Maze m = Maze.generateCellularAutomataRoom(50, 50);
        m.connectDisconnectedComponents();
        System.out.println(m);

    }

}
