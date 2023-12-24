import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

public class AdventOfCode23 {
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

  static int walk(Pos pos, Set<Pos> visited, Grid grid, Pos end) {
    if (pos.equals(end)) {
      return visited.size();
    }
    visited.add(pos);
    return Dir.ALL.stream()
        .flatMap(dir -> Stream.of(pos.next(dir))
            .filter(p -> grid.in(p) && !visited.contains(p) && switch (grid.at(p)) {
              case '#' -> false;
              case '<' -> dir == Dir.WEST;
              case '>' -> dir == Dir.EAST;
              case '^' -> dir == Dir.NORTH;
              case 'v' -> dir == Dir.SOUTH;
              default -> true;  // case '.'
            }))
        .mapToInt(p -> walk(p, new HashSet<>(visited), grid, end))
        .max()
        .orElse(0);
  }

  static int longestHike(String input) {
    var data = input.lines().collect(joining());
    var width = input.indexOf('\n');
    var grid = new Grid(data, width, data.length() / width);
    var startIndex = data.indexOf('.');
    var start = new Pos(startIndex % width, startIndex / width);
    var endIndex = data.lastIndexOf('.');
    var end = new Pos(endIndex % width, endIndex / width);
    return walk(start, new HashSet<>(), grid, end);
  }

  public static void main(String[] args) {
    var input = """
        #.#####################
        #.......#########...###
        #######.#########.#.###
        ###.....#.>.>.###.#.###
        ###v#####.#v#.###.#.###
        ###.>...#.#.#.....#...#
        ###v###.#.#.#########.#
        ###...#.#.#.......#...#
        #####.#.#.#######.#.###
        #.....#.#.#.......#...#
        #.#####.#.#.#########v#
        #.#...#...#...###...>.#
        #.#.#v#######v###.###v#
        #...#.>.#...>.>.#.###.#
        #####v#.#.###v#.#.###.#
        #.....#...#...#.#.#...#
        #.#########.###.#.#.###
        #...###...#...#...#.###
        ###.###.#.###v#####v###
        #...#...#.#.>.>.#.>.###
        #.###.###.#.###.#.#v###
        #.....###...###...#...#
        #####################.#
        """;
    System.out.println(longestHike(input));
  }
}
