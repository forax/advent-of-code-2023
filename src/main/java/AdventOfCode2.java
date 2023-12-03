import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;
import static java.util.regex.Pattern.compile;
import static java.util.stream.IntStream.range;

public class AdventOfCode2 {
  private static final Pattern PATTERN = compile("(?:Game ([0-9]+))|(?:([0-9]+) red)|(?:([0-9]+) green)|(?:([0-9]+) blue)|(;)");

  static int sumOfGameId(String text, int expectedRed, int expectedGreen, int expectedBlue) {
    return text.lines()
        .mapToInt(l -> {
          var matcher = PATTERN.matcher(l);
          matcher.find();
          var id = parseInt(matcher.group(1));
          record Draw(int red, int green, int blue) {}
          return Stream.of(l)
              .<Draw>mapMulti((line, consumer) -> {
                int red = 0, green = 0, blue = 0;
                while (matcher.find()) {
                  var kind = range(0, matcher.groupCount()).filter(i -> matcher.start(i + 1) != -1).findFirst().orElse(-1);
                  switch (kind) {
                    case 1 -> red = parseInt(matcher.group(2));
                    case 2 -> green = parseInt(matcher.group(3));
                    case 3 -> blue = parseInt(matcher.group(4));
                    case 4 -> {
                      consumer.accept(new Draw(red, green, blue));
                      red = green = blue = 0;
                    }
                  }
                }
                consumer.accept(new Draw(red, green, blue));
              })
              .allMatch(draw -> draw.red <= expectedRed && draw.green <= expectedGreen && draw.blue <= expectedBlue) ? id : 0;
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
