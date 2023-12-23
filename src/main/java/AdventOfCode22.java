import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Gatherer;
import java.util.stream.Stream;

import static java.util.stream.Collectors.filtering;
import static java.util.stream.Collectors.flatMapping;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.teeing;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.IntStream.rangeClosed;

public class AdventOfCode22 {
  record Pos(int x, int y) {}
  record Brick(int x1, int x2, int y1, int y2, int z1, int z2) {
    Stream<Pos> positions() {
      return x1 == x2 ? rangeClosed(y1, y2).mapToObj(y -> new Pos(x1, y)) :
          rangeClosed(x1, x2).mapToObj(x -> new Pos(x, y1));
    }
  }
  record Level(Brick brick, int z) {}
  record Pair<U,V>(U u, V v) { }

  static int numberOfBrickWithChildrenWithAtLeastTwoParents(String input) {
    var grid = new HashMap<Pos, Level>();
    return input.lines()
        .map(l -> {
          var array = Arrays.stream(l.split(",|~")).mapToInt(Integer::parseInt).toArray();
          return new Brick(array[0], array[3], array[1], array[4], array[2], array[5]);
        })
        .collect(groupingBy(Brick::z1, TreeMap::new, toList()))
        .values().stream()
        .flatMap(List::stream)
        .map(brick -> {
          var onTop = brick.positions()
              .map(pos -> grid.getOrDefault(pos, new Level(null, 0)))
              .collect(groupingBy(Level::z, TreeMap::new, flatMapping(l -> Stream.ofNullable(l.brick), toSet())))
              .lastEntry();
          var level =  new Level(brick, 1 + brick.z2 - brick.z1 + onTop.getKey());
          brick.positions().forEach(pos -> grid.put(pos, level));
          return new Pair<>(brick, onTop.getValue());
        })
        .collect(teeing(
            toMap(Pair::u, pair -> pair.v().size()),
            flatMapping(pair -> pair.v().stream().map(b -> new Pair<>(b, pair.u())),
                groupingBy(Pair::u, mapping(Pair::v, toList()))),
            (parentMap, childrenMap) ->
                (int) parentMap.keySet().stream()
                    .filter(parent -> childrenMap.getOrDefault(parent, List.of()).stream()
                        .allMatch(child -> parentMap.get(child) > 1))
                    .count()
        ));
  }

  public static void main(String[] args) {
    var input = """
        1,0,1~1,2,1
        0,0,2~2,0,2
        0,2,3~2,2,3
        0,0,4~0,2,4
        2,0,5~2,2,5
        0,1,6~2,1,6
        1,1,8~1,1,9
        """;
    System.out.println(numberOfBrickWithChildrenWithAtLeastTwoParents(input));
  }
}
