import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class AdventOfCode16 {
  enum Dir {
    NORTH(0, -1), EAST(1, 0), SOUTH(0, 1), WEST(-1, 0);
    private static final List<Dir> ALL = List.of(values());
    private final int dx, dy; Dir(int dx, int dy) { this.dx = dx; this.dy = dy; }
    Dir step(int step) { return ALL.get((ordinal() + step) % ALL.size()); }
    int orient() { return 1 + (ordinal() & 1); }
  }

  private static final int VERTICAL = 1, HORIZONTAL = 2;
  private static final Map<Character, Function<Dir, List<Dir>>> MAP = Map.of(
      '|', d -> d.orient() == HORIZONTAL ? List.of(Dir.NORTH, Dir.SOUTH) : List.of(d),
      '-', d -> d.orient() == VERTICAL ? List.of(Dir.WEST, Dir.EAST) : List.of(d),
      '/', d -> List.of(d.orient() == HORIZONTAL ? d.step(3) : d.step(1)),
      '\\', d -> List.of(d.step(1))
  );


  static void move(int x, int y, Dir dir, char[][] grid, int[][] energy) {
    int orient;
    if (x < 0 || x >= grid[0].length || y < 0 || y >= grid.length || ((orient  = dir.orient()) & energy[y][x]) != 0) {
      return;
    }
    energy[y][x] |= orient;
    var letter = grid[y][x];
    for(var newDir : MAP.getOrDefault(letter, __ -> List.of(dir)).apply(dir)) {
      move(x + newDir.dx, y + newDir.dy, newDir, grid, energy);
    }
  }

  static int sumOfEnergized(String input) {
    var grid = input.lines().map(String::toCharArray).toArray(char[][]::new);
    var energy = new int[grid[0].length][grid.length];
    move(0, 0, Dir.EAST, grid, energy);
    return Arrays.stream(energy).flatMapToInt(Arrays::stream).filter(e -> e != 0).sum();
  }

  public static void main(String[] args) {
    var input = """
        .|...\\....
        |.-.\\.....
        .....|-...
        ........|.
        ..........
        .........\\
        ..../.\\\\..
        .-.-/..|..
        .|....-|.\\
        ..//.|....
        """;
    System.out.println(sumOfEnergized(input));
  }
}
