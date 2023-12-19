import java.util.Scanner;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summarizingInt;
import static java.util.stream.IntStream.rangeClosed;

public class AdventOfCode18 {
  static int sumOfFilledPool(String input) {
    record Pos(int x, int y) {}
    var state = new Object() { int x, y; };
    return Stream.concat(Stream.of(new Pos(0, 0)), Stream.iterate(new Scanner(input), Scanner::hasNext, s -> s)
        .flatMap(scanner -> {
          var letter = scanner.next().charAt(0);
          var distance = scanner.nextInt();
          scanner.next();  // skip color
          return switch (letter) {
            case 'R', 'L' -> {
              var order = letter == 'R' ? 1 : -1;
              var x = state.x; state.x += order * distance;
              yield IntStream.of(1, distance).mapToObj(i -> new Pos(x + order * i, state.y));
            }
            default -> { // case 'D', 'U'
              var order = letter == 'D' ? 1 : -1;
              var y = state.y; state.y += order * distance;
              yield rangeClosed(1, distance).mapToObj(i -> new Pos(state.x, y + order * i));
            }
          };
        })
      ).collect(groupingBy(Pos::y, summarizingInt(Pos::x)))
      .values().stream().mapToInt(stat -> 1 + stat.getMax() - stat.getMin()).sum();
  }

  public static void main(String[] args) {
    var input = """
        R 6 (#70c710)
        D 5 (#0dc571)
        L 2 (#5713f0)
        D 2 (#d2c081)
        R 2 (#59c680)
        D 2 (#411b91)
        L 5 (#8ceee2)
        U 2 (#caa173)
        L 1 (#1b58a2)
        U 2 (#caa171)
        R 2 (#7807d2)
        U 3 (#a77fa3)
        L 2 (#015232)
        U 2 (#7a21e3)
        """;
    System.out.println(sumOfFilledPool(input));
  }
}
