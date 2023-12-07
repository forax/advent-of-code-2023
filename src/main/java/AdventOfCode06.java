import java.util.regex.Pattern;
import java.util.stream.IntStream;

import static java.lang.Integer.parseInt;

public class AdventOfCode06 {
  private static final Pattern NUMBER = Pattern.compile("(\\d+)");

  static int productOfPossibilities(String input) {
    var lines = input.lines().toList();
    var time = NUMBER.matcher(lines.getFirst()).results().map(r -> parseInt(r.group())).toList();
    var distance = NUMBER.matcher(lines.get(1)).results().map(r -> parseInt(r.group())).toList();
    return IntStream.range(0, time.size())
        .map(i -> {
          var t = time.get(i);
          var d = distance.get(i);
          return IntStream.range(1, t).map(x -> - x * x + t * x > d ? 1 : 0).sum();
        })
        .reduce(1, (a, b) -> a * b);
  }

  public static void main(String[] args) {
    var input = """
        Time:      7  15   30
        Distance:  9  40  200
        """;

    System.out.println(productOfPossibilities(input));
  }
}
