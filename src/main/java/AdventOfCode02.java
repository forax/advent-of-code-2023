import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;
import static java.util.regex.Pattern.compile;
import static java.util.stream.IntStream.range;

public class AdventOfCode02 {
  private static final Pattern PATTERN = Pattern.compile("Game ([0-9]+)|([0-9]+) (red|green|blue)");

  static int sumOfGameId(String input, int expectedRed, int expectedGreen, int expectedBlue) {
    var expected = Map.of("red", expectedRed, "green", expectedGreen, "blue", expectedBlue);
    return input.lines()
        .mapToInt(line -> {
          var matcher = PATTERN.matcher(line);
          matcher.find();
          var gameId = parseInt(matcher.group(1));
          return matcher.results().allMatch(result -> parseInt(result.group(2)) <= expected.get(result.group(3))) ? gameId : 0;
        })
        .sum();
  }

  public static void main(String[] args) {
    var text = """
        Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
        Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
        Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
        Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
        Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green
        """;
    System.out.println(sumOfGameId(text, 12, 13, 14));
  }
}
