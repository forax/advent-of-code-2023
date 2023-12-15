import static java.util.stream.IntStream.range;

public class AdventOfCode14 {
  static int totalLoad(String input) {
    var lines = input.lines().toList();
    var width = lines.getFirst().length();
    var height = lines.size();
    var next = new int[width];
    return range(0, height)
        .map(row -> range(0, width)
            .map(column -> switch (lines.get(row).charAt(column)) {
              case '#' -> { next[column] = row + 1; yield 0; }
              case 'O' -> height - next[column]++;
              default -> 0;
            })
            .sum())
        .sum();
  }

  public static void main(String[] args) {
    var input = """
        O....#....
        O.OO#....#
        .....##...
        OO.#O....O
        .O.....O#.
        O.#..O.#.#
        ..O..#O..O
        .......O..
        #....###..
        #OO..#....
        """;
    System.out.println(totalLoad(input));
  }
}
