public class DisjointSet {
    private final int[] parent, size;
    private int sets;
    public DisjointSet(int n) {
        parent = new int[n]; size = new int[n]; sets = n;
        for (int i = 0; i < n; i++) { parent[i] = i; size[i] = 1; }
    }
    public int find(int x) {
        int root = x;
        while (parent[root] != root) root = parent[root];
        while (parent[x] != x) {
            int next = parent[x]; parent[x] = root; x = next;
        }
        return root;
    }
    public boolean union(int a, int b) {
        a = find(a); b = find(b);
        if (a == b) return false;
        if (size[a] < size[b]) { int t = a; a = b; b = t; }
        parent[b] = a; size[a] += size[b]; sets--;
        return true;
    }
    public boolean connected(int a, int b) { return find(a) == find(b); }
    public int count() { return sets; }
}
