import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.IntStream.range;

public class AdventOfCode21 {

  record Pos(int x, int y) {
    Pos next(Dir dir) { return new Pos(x + dir.dx, y + dir.dy); }
  }
  enum Dir {
    NORTH(0, -1), EAST(1, 0), SOUTH(0, 1), WEST(-1, 0);
    private static final List<Dir> ALL = List.of(values());
    private final int dx, dy; Dir(int dx, int dy) { this.dx = dx; this.dy = dy; }
  }
  record Grid(String data, int width, int height) {
    boolean in(Pos pos) { return pos.x >= 0 && pos.x < width && pos.y >= 0 && pos.y < height; }
    char at(Pos pos) { return data.charAt(pos.x + pos.y * width); }
  }

  static Set<Pos> step(Grid grid, Set<Pos> positions) {
    return positions.stream()
        .flatMap(p -> Dir.ALL.stream().map(p::next))
        .filter(p -> grid.in(p) && grid.at(p) != '#')
        .collect(toSet());
  }

  static int locationCount(String input, int steps) {
    var data = input.lines().collect(joining());
    var width = input.indexOf('\n');
    var grid = new Grid(data, width, data.length() / width);
    var startIndex = data.indexOf('S');
    var start = new Pos(startIndex % width, startIndex / width);
    return range(0, steps).boxed()
        .reduce(Set.of(start), (set, __) -> step(grid, set), (_1, _2) -> null)
        .size();
  }

  public static void main(String[] args) {
    var input = """
        ...........
        .....###.#.
        .###.##..#.
        ..#.#...#..
        ....#.#....
        .##..S####.
        .##..#...#.
        .......##..
        .##.#.####.
        .##..##.##.
        ...........
        """;
    System.out.println(locationCount(input, 64));
  }
}
