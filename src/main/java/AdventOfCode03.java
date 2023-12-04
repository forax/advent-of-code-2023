import java.util.TreeMap;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.partitioningBy;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

public class AdventOfCode03 {
  private static final Pattern PATTERN = Pattern.compile("([0-9]+)|([*#+*$])");

  static int sumOfParts(String input) {
    var length = 1 + input.indexOf('\n');
    var map = PATTERN.matcher(input).results()
        .collect(partitioningBy(result -> result.start(1) != -1,
            toMap(MatchResult::start, MatchResult::group, (_1, _2) -> null, TreeMap::new)));
    return map.get(false).keySet().stream()
        .flatMap(start -> Stream.of(-length, 0, length)
            .flatMap(j -> IntStream.of(-1, 0, 1).mapToObj(i -> start + i + j)))
        .flatMap(start -> Stream.ofNullable(map.get(true).floorEntry(start)).filter(e -> start < e.getKey() + e.getValue().length()))
        .collect(collectingAndThen(toSet(), set -> set.stream().mapToInt(e -> parseInt(e.getValue())).sum()));
  }

  public static void main(String[] args) {
    var text = """
        467..114..
        ...*......
        ..35..633.
        ......#...
        617*......
        .....+.58.
        ..592.....
        ......755.
        ...$.*....
        .664.598..
        """;
    System.out.println(sumOfParts(text));
  }
}
