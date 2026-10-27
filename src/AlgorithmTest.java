import java.util.*;
/** Extra checks beyond the supplied examples. Run: java -cp out AlgorithmTest */
public class AlgorithmTest {
    static void require(boolean ok, String message) {
        if (!ok) throw new AssertionError(message);
    }
    public static void main(String[] args) {
        Random rnd = new Random(252223);
        for (int trial = 0; trial < 200; trial++) {
            int n = 2 + rnd.nextInt(18);
            List<Integer> f = new ArrayList<>(), t = new ArrayList<>(), w = new ArrayList<>();
            for (int u = 0; u < n; u++) for (int v = 0; v < n; v++)
                if (u != v && (v == u + 1 || rnd.nextInt(8) == 0)) {
                    f.add(u); t.add(v); w.add(1 + rnd.nextInt(100));
                }
            // Include parallel edges to test minimum-cost collapsing.
            f.add(0); t.add(1); w.add(200);
            Input.Slides s = new Input.Slides(n, f.stream().mapToInt(i->i).toArray(),
                t.stream().mapToInt(i->i).toArray(), w.stream().mapToInt(i->i).toArray());
            boolean[][] reach = new boolean[n][n];
            for (int u = 0; u < n; u++) reach[u][u] = true;
            for (int i = 0; i < s.count(); i++) reach[s.from()[i]][s.to()[i]] = true;
            for (int k = 0; k < n; k++) for (int u = 0; u < n; u++)
                for (int v = 0; v < n; v++) reach[u][v] |= reach[u][k] && reach[k][v];
            int[] z = Zones.kosaraju(s);
            int count = Arrays.stream(z).max().orElse(-1) + 1;
            for (int u = 0; u < n; u++) for (int v = 0; v < n; v++)
                require((z[u] == z[v]) == (reach[u][v] && reach[v][u]), "SCC reachability");
            long[] dist = new long[count];
            Arrays.fill(dist, Long.MAX_VALUE); dist[z[0]] = 0;
            int[] stages = new int[count]; Arrays.fill(stages, 1);
            // Repeated relaxation is an independent oracle; no reliance on zone ordering.
            for (int pass = 0; pass < count; pass++) for (int i = 0; i < s.count(); i++) {
                int a = z[s.from()[i]], b = z[s.to()[i]];
                if (a == b) continue;
                require(a < b, "topological IDs");
                if (dist[a] != Long.MAX_VALUE) dist[b] = Math.min(dist[b], dist[a] + s.time()[i]);
                stages[b] = Math.max(stages[b], stages[a] + 1);
            }
            long[] route = Zones.routes(s, z, count);
            require(route[1] == dist[z[n - 1]], "route cost");
            require(route[0] == Arrays.stream(stages).max().orElse(0), "flow stages");
            Input.Walls walls = Generator.generate(1 + rnd.nextInt(12), 1 + rnd.nextInt(12), trial);
            int[] mst = Maze.cheapestMaze(walls);
            long cost = 0; for (int e : mst) cost += walls.cost()[e];
            require(cost == Maze.primCost(walls), "MST agreement");
            int[] broken = Maze.breakWalls(walls);
            require(broken.length == walls.cells() - 1, "tree edge count");
            int[][] open = Graphs.open(walls, broken);
            int[] distance = new int[walls.cells()]; Arrays.fill(distance, -1);
            ArrayDeque<Integer> q = new ArrayDeque<>(); q.add(0); distance[0] = 0;
            while (!q.isEmpty()) { int u = q.remove();
                for (int v : open[u]) if (distance[v] < 0) { distance[v] = distance[u] + 1; q.add(v); }
            }
            var escape = Escape.escape(walls, broken);
            require(escape.path().length - 1 == distance[walls.cells() - 1], "DFS path versus BFS");
        }
        // A million-cell serpentine path on an actual 1000 x 1000 grid.
        int side = 1000, n = side * side;
        int[] a = new int[n-1], b = new int[n-1], costs = new int[n-1], broken = new int[n-1];
        int previous = 0;
        for (int i = 1; i < n; i++) {
            int row = i / side, col = i % side;
            int cell = row * side + (row % 2 == 0 ? col : side - 1 - col);
            a[i-1] = previous; b[i-1] = cell; costs[i-1] = i; broken[i-1] = i-1; previous = cell;
        }
        var result = Escape.escape(new Input.Walls(side, side, a, b, costs), broken);
        require(result.path().length == 999001 && result.visited() == 999001, "million-cell DFS");
        require(result.deadEnds() == 2, "snake dead ends");
        int[] chainA = new int[n-1], chainB = new int[n-1];
        for (int i = 0; i < n-1; i++) { chainA[i] = i; chainB[i] = i+1; }
        int[] zones = Zones.kosaraju(new Input.Slides(n, chainA, chainB, costs));
        require(zones[0] == 0 && zones[n-1] == n-1, "million-cell Kosaraju");
        System.out.println("PASS: 200 random cases, 1000x1000 DFS, million-vertex Kosaraju.");
    }
}
