import java.util.HashSet;
import java.util.Scanner;
import java.util.function.IntUnaryOperator;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;

public class AdventOfCode04 {
  static int winningPoints(String input) {
    return input.lines()
        .mapToInt(line -> {
          var winnings = new HashSet<Integer>();
          var state = new Object() { IntUnaryOperator action = v -> winnings.add(v) ? 0 : 0; };
          return Stream.iterate(new Scanner(line.substring(line.indexOf(':') + 1)), Scanner::hasNext, s -> s)
              .mapToInt(scanner -> switch (scanner.next()) {
                case "|" -> { state.action = v -> winnings.contains(v) ? 1 : 0; yield 0; }
                case String token -> state.action.applyAsInt(parseInt(token));
              })
              .reduce(0, (v1, v2) -> v1 == 0 && v2 == 1 ? 2 : v1 + v2);
        })
        .sum();
  }

  public static void main(String[] args) {
    var text = """
        Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
        Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
        Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
        Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
        Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
        Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11
        """;
    System.out.println(winningPoints(text));
  }
}
