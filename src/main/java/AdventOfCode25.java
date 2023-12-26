import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;

public class AdventOfCode25 {

  record Edge(int src, int dst) {}

  static void reachableFrom(Map<Integer, List<Edge>> graph, int node, BitSet visited) {
    if (visited.get(node)) {
      return;
    }
    visited.set(node);
    for(var edge : graph.get(node)) {
      reachableFrom(graph, edge.dst, visited);
    }
  }

  static int tryCut(List<Edge> edges, int depth) {
    if (depth == 0) {
      var bits = new BitSet();
      var graph = edges.stream().flatMap(e -> Stream.of(e, new Edge(e.dst, e.src))).collect(groupingBy(Edge::src));
      reachableFrom(graph, 0, bits);
      if (bits.cardinality() != graph.size()) {
        return bits.cardinality() * (graph.size() - bits.cardinality());
      }
      return -1;
    }
    for (var i = 0; i < edges.size(); i++) {
      var newEdged = new ArrayList<>(edges);
      newEdged.remove(i);
      var cut = tryCut(newEdged, depth - 1);
      if (cut != -1) {
        return cut;
      }
    }
    return -1;
  }

  static int productOfConnectedNodes(String input) {
    var map = new HashMap<String, Integer>();
    var edges = input.lines()
        .flatMap(line -> {
          var scanner = new Scanner(line).useDelimiter("[: ]+");
          var srcText = scanner.next();
          var src = map.computeIfAbsent(srcText, __ -> map.size());
          return Stream.iterate(scanner, Scanner::hasNext, s -> s)
              .map(s -> new Edge(src, map.computeIfAbsent(s.next(), __ -> map.size())));
        })
        .toList();

    return tryCut(edges, 3);
  }

  public static void main(String[] args) {
    var input = """
        jqt: rhn xhk nvd
        rsh: frs pzl lsr
        xhk: hfx
        cmg: qnr nvd lhk bvb
        rhn: xhk bvb hfx
        bvb: xhk hfx
        pzl: lsr hfx nvd
        qnr: nvd
        ntq: jqt hfx bvb xhk
        nvd: lhk
        lsr: lhk
        rzs: qnr cmg lsr rsh
        frs: qnr lhk lsr
        """;

    System.out.println(productOfConnectedNodes(input));
  }
}
