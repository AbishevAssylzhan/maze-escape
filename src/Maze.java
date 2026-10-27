/**
 * Missions 1 and 2. Building the maze out of the walls.
 * A wall k joins the cells w.a()[k] and w.b()[k] and costs w.cost()[k]. Indexes start from 0,
 * the line number in walls.txt is the index + 1. The cells are connected by DisjointSet.
 */
public class Maze {

    /**
     * Mission 1. Take the walls in the order of the file. Break a wall when its two cells are in different sets.
     * Returns the indexes of the broken walls, in the order of breaking.
     */
    public static int[] breakWalls(Input.Walls w) {
        DisjointSet d = new DisjointSet(w.cells());
        int[] result = new int[Math.max(0, w.cells() - 1)];
        int used = 0;
        for (int k = 0; k < w.count(); k++)
            if (d.union(w.a()[k], w.b()[k])) result[used++] = k;
        return java.util.Arrays.copyOf(result, used);
    }

    /**
     * Mission 2. Kruskal: the same, but take the walls from the cheapest to the most expensive.
     * Returns the indexes of the broken walls.
     */
    public static int[] cheapestMaze(Input.Walls w) {
        Integer[] order = new Integer[w.count()];
        for (int i = 0; i < order.length; i++) order[i] = i;
        java.util.Arrays.sort(order, java.util.Comparator.comparingInt(i -> w.cost()[i]));
        DisjointSet d = new DisjointSet(w.cells());
        int[] result = new int[Math.max(0, w.cells() - 1)];
        int used = 0;
        for (int k : order) {
            if (d.union(w.a()[k], w.b()[k])) result[used++] = k;
            if (d.count() == 1) break;
        }
        return java.util.Arrays.copyOf(result, used);
    }

    /**
     * Mission 2. Prim with a priority queue. Returns the total cost of the cheapest maze.
     * It must be equal to the total cost of cheapestMaze.
     */
    public static long primCost(Input.Walls w) {
        int n = w.cells();
        if (n == 0) return 0;
        int[] degree = new int[n];
        for (int k = 0; k < w.count(); k++) { degree[w.a()[k]]++; degree[w.b()[k]]++; }
        int[][] edges = new int[n][];
        for (int u = 0; u < n; u++) edges[u] = new int[degree[u]];
        java.util.Arrays.fill(degree, 0);
        for (int k = 0; k < w.count(); k++) {
            edges[w.a()[k]][degree[w.a()[k]]++] = k;
            edges[w.b()[k]][degree[w.b()[k]]++] = k;
        }
        boolean[] seen = new boolean[n];
        java.util.PriorityQueue<Integer> pq = new java.util.PriorityQueue<>(
            java.util.Comparator.comparingInt(k -> w.cost()[k]));
        seen[0] = true;
        for (int k : edges[0]) pq.add(k);
        long total = 0;
        int reached = 1;
        while (!pq.isEmpty() && reached < n) {
            int k = pq.remove();
            int v = seen[w.a()[k]] ? w.b()[k] : w.a()[k];
            if (seen[v]) continue;
            seen[v] = true; reached++; total += w.cost()[k];
            for (int e : edges[v])
                if (!seen[w.a()[e]] || !seen[w.b()[e]]) pq.add(e);
        }
        if (reached != n) throw new IllegalArgumentException("Disconnected maze");
        return total;
    }
}
