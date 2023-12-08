import java.util.Map;
import java.util.Scanner;
import java.util.stream.Stream;

import static java.util.Comparator.comparingLong;
import static java.util.Comparator.reverseOrder;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.teeing;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.IntStream.range;

public class AdventOfCode07 {
  private static final Map<Integer, Integer> MAP =
      range(0, 13).boxed().collect(toMap(i -> (int) "AKQJT98765432".charAt(i), i -> 12 - i));

  static int totalWinnings(String input) {
    record Pair(long hash, int bid) {}
    var bids = Stream.iterate(new Scanner(input), Scanner::hasNext, s -> s)
        .map(scanner -> scanner.next().chars().mapToObj(MAP::get)
            .collect(teeing(toList(),
                  collectingAndThen(groupingBy(v -> v,counting()),
                      m -> m.values().stream().sorted(reverseOrder()).limit(2).toList()),
                  (hand, counts) -> new Pair(((long) counts.hashCode()) << 32 | hand.hashCode(), scanner.nextInt()))))
        .sorted(comparingLong(Pair::hash))
        .map(Pair::bid)
        .toList();
    return range(0, bids.size()).map(i -> (i + 1) * bids.get(i)).sum();
  }

  public static void main(String[] args) {
    var input = """
        32T3K 765
        T55J5 684
        KK677 28
        KTJJT 220
        QQQJA 483
        """;
    System.out.println(totalWinnings(input));
  }
}
