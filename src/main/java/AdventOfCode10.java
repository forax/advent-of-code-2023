import java.util.Arrays;
import java.util.Map;
import java.util.function.UnaryOperator;

import static java.util.stream.Collectors.joining;

public class AdventOfCode10 {
  record Pos(int x, int y) {
    Pos next(Dir dir) { return new Pos(x + dir.dx, y + dir.dy); }
  }
  enum Dir {
    NORTH(0, -1), EAST(1, 0), SOUTH(0, 1), WEST(-1, 0);
    private final int dx, dy; Dir(int dx, int dy) { this.dx = dx; this.dy = dy; }
  }
  record Grid(String data, int width, int height) {
    boolean in(Pos pos) { return pos.x >= 0 && pos.x < width && pos.y >= 0 && pos.y < height; }
    char at(Pos pos) { return data.charAt(pos.x + pos.y * width); }
  }

  private static final Map<Character, UnaryOperator<Dir>> PIPE_MAP = Map.of(
      '|', d -> d == Dir.NORTH || d == Dir.SOUTH ? d : null,
      '-', d -> d == Dir.EAST || d == Dir.WEST ? d : null,
      'L', d -> d == Dir.SOUTH ? Dir.EAST : (d == Dir.WEST ? Dir.NORTH : null),
      'J', d -> d == Dir.SOUTH ? Dir.WEST : (d == Dir.EAST ? Dir.NORTH : null),
      '7', d -> d == Dir.NORTH ? Dir.WEST : (d == Dir.EAST ? Dir.SOUTH : null),
      'F', d -> d == Dir.NORTH ? Dir.EAST : (d == Dir.WEST ? Dir.SOUTH : null)
  );

  static int farthest(String input) {
    var data = input.lines().collect(joining());
    var width = input.indexOf('\n');
    var grid = new Grid(data, width, data.length() / width);
    var startIndex = data.indexOf('S');
    var start = new Pos(startIndex % width, startIndex / width);
    var dir = Arrays.stream(Dir.values())
        .filter(d -> grid.in(start.next(d)))
        .filter(d -> PIPE_MAP.getOrDefault(grid.at(start.next(d)), __ -> null).apply(d) != null)
        .findFirst().orElseThrow();
    var step = 0;
    var pos = start;
    for(;;) {
      pos = pos.next(dir);
      var symbol = grid.at(pos);
      if (symbol == 'S') {
        break;
      }
      dir = PIPE_MAP.get(symbol).apply(dir);
      step++;
    }
    return step / 2 + step % 2;
  }

  public static void main(String[] args){
    var input = """
      -L|F7
      7S-7|
      L|7||
      -L-J|
      L|-JF
      """;
    System.out.println(farthest(input));

    var input2 = """
      7-F7-
      .FJ|7
      SJLL7
      |F--J
      LJ.LJ
      """;
    System.out.println(farthest(input2));
  }
}
