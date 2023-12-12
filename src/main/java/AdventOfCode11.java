import static java.util.stream.Collectors.joining;
import static java.util.stream.IntStream.range;

public class AdventOfCode11 {
  static int sumOfShortestPaths(String input) {
    var data = input.lines().collect(joining());
    var notCols = new boolean[input.indexOf('\n')];
    var notRows = new boolean[data.length() / notCols.length];
    class Galaxy {
      final int x, y; Galaxy(int x, int y) { this.x = x; this.y = y; }
      int distance(Galaxy g) {
        // I'm not sure, it's the correct way to compute the length between galaxies
        return ((x == g.x) ? 0 : ((x < g.x) ? range(x, g.x) : range(g.x, x)).map(i -> notCols[i] ? 1 : 2).sum()) +
            ((y == g.y) ? 0 : ((y < g.y) ? range(y, g.y) : range(g.y, y)).map(j -> notRows[j] ? 1 : 2).sum());
      }
    }
    var galaxies = range(0, data.length())
        .filter(i -> data.charAt(i) == '#')
        .mapToObj(i -> new Galaxy(i % notCols.length, i / notCols.length))
        .peek(g -> { notCols[g.x] = true; notRows[g.y] = true; })
        .toList();
    return range(0, galaxies.size()).boxed()
        .flatMapToInt(i -> range(0, i).map(j -> galaxies.get(i).distance(galaxies.get(j))))
        .sum();
  }

  public static void main(String[] args){
    var input = """
      ...#......
      .......#..
      #.........
      ..........
      ......#...
      .#........
      .........#
      ..........
      .......#..
      #...#.....
      """;
    System.out.println(sumOfShortestPaths(input));
  }
}
