import java.util.Arrays;
public class Escape {
    public record Result(int[] path, int visited, int deadEnds) {}
    public static Result escape(Input.Walls w, int[] broken) {
        int n = w.cells();
        int[][] g = Graphs.open(w, broken);
        int dead = 0;
        for (int[] neighbors : g) if (neighbors.length == 1) dead++;
        // Each frame remembers which direction to try next, just like recursive DFS.
        int[] stack = new int[n], nextDirection = new int[n], parent = new int[n];
        Arrays.fill(parent, -1);
        boolean[] seen = new boolean[n];
        int top = 0, visited = 1;
        seen[0] = true; stack[0] = 0;
        while (top >= 0 && !seen[n - 1]) {
            int u = stack[top];
            if (nextDirection[top] == 4) { top--; continue; }
            int direction = nextDirection[top]++;
            int v = switch (direction) {
                case 0 -> u - w.cols();
                case 1 -> u % w.cols() + 1 < w.cols() ? u + 1 : -1;
                case 2 -> u + w.cols();
                default -> u % w.cols() > 0 ? u - 1 : -1;
            };
            if (v < 0 || v >= n || seen[v]) continue;
            boolean open = false;
            for (int neighbor : g[u]) if (neighbor == v) { open = true; break; }
            if (!open) continue;
            seen[v] = true; visited++; parent[v] = u;
            stack[++top] = v; nextDirection[top] = 0;
        }
        if (!seen[n - 1]) return new Result(new int[0], visited, dead);
        int length = 0;
        for (int v = n - 1; v != -1; v = parent[v]) length++;
        int[] path = new int[length];
        for (int v = n - 1, i = length - 1; v != -1; v = parent[v]) path[i--] = v;
        return new Result(path, visited, dead);
    }
}
