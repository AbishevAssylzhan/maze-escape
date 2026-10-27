# Maze Escape — Assignment 3

**Name:** Assylzhan Abishev  
**Student ID:** 252223  
**Group:** SE-2512  
**Maze:** 9 × 10

## Run

JDK 17+. Run from the project folder:

```sh
java tools/Check.java
java -cp out Main
```

Open `output/maze.html`. Run Main after Check to restore your personal maze.

## Output

```text
Broken: 89
Kept: 72
Last broken wall: 131
MST cost: 26896
Heaviest broken wall: 672
Path length: 19
DFS visited: 64
Dead ends: 30
Zones: 90
Largest zone: 1
Is DAG: 1
Flow stages: 15
Fastest route cost: 29
```

![Maze](output/maze.png)

## Questions

**Mission 0.** Find returns the set representative; union joins different sets. Union by size and path compression give O(alpha(n)) amortized time.

**Mission 1.** A connected maze without cycles has RC−1 broken walls. Subtracting this from R(C−1)+C(R−1) gives (R−1)(C−1) kept walls.

**Mission 2.** Kruskal and Prim both give **26896**. Both use safe minimum edges to build an MST; distinct weights make it unique.

**Mission 3.** The maze is a tree, so each pair of cells has one simple path. An explicit stack avoids recursive stack overflow and preserves up/right/down/left order.

**Mission 4.** Decreasing finish order and reversed edges let the second pass isolate each SCC. With no self-loops, the graph is acyclic exactly when every SCC is a single cell: Zones = n.

**Mission 5.** Zone IDs are topological, so predecessors are processed first. Construction takes expected O(n+m), and the DP pass takes O(K+E).

## AI use

ChatGPT helped generate the implementation, tests and report, and ran checks and benchmarks. The supplied helper classes remain unchanged.

## Timing

Mean milliseconds over 5 runs of Bench, measured in the assistant's Linux/OpenJDK 17 environment.

| Maze | Kruskal | Prim |
|---|---:|---:|
| 100 × 100 | 15 | 11 |
| 300 × 300 | 130 | 97 |
| 1000 × 1000 | 3286 | 4909 |

Both algorithms become slower as the grid grows, consistent with their O(m log m) bounds. JVM warm-up, garbage collection and shared-machine load affect measurements.

Raw runs: [benchmark.txt](benchmark.txt).

## Complexity


n = cells; m = walls or slides; K = zones; E = distinct cross-zone edges.
Space includes auxiliary arrays and adjacency representations.

| Algorithm | Time | Space | Assumptions |
|---|---|---|---|
| DSU find / union | O(alpha(n)) amortized | O(n) overall, O(1) per call | Size + path compression |
| Input-order maze | O(n + m alpha(n)) | O(n) | Connected rectangular grid |
| Kruskal | O(n + m log m + m alpha(n)) | O(n+m) | Comparison sorting of edge indices |
| Prim | O(n + m log m) | O(n+m) | Lazy edge priority queue; O(log n) equivalent on grid |
| DFS | O(n+m) | O(n+m) | Iterative; at most 4 grid neighbours |
| Kosaraju | O(n+m) | O(n+m) | Two iterative passes |
| Zone graph construction | O(n+m) expected | O(K+E) extra | HashMap minimum per zone pair |
| Route / stages DP | O(K+E) | O(K) beyond graph | Topological zone IDs |

## Checks

All three supplied examples pass. Additional checks cover 200 random cases, a 1000 × 1000 DFS and million-vertex Kosaraju.
