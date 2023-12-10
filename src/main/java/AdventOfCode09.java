import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.IntStream.range;

public class AdventOfCode09 {
  static int analysisOasisAndReportSum(String input) {
    return input.lines()
        .map(line -> Arrays.stream(line.split(" ")).map(Integer::parseInt).toList())
        .mapToInt(history -> Stream.iterate(history,
              h -> !h.stream().allMatch(v -> v == 0),
              h -> range(1, h.size()).mapToObj(i -> h.get(i) - h.get(i - 1)).toList())
            .mapToInt(List::getLast)
            .sum())
        .sum();
  }

  public static void main(String[] args) {
    var input = """
        0 3 6 9 12 15
        1 3 6 10 15 21
        10 13 16 21 30 45
        """;
    System.out.println(analysisOasisAndReportSum(input));
  }
}