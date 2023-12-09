import java.util.Scanner;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toMap;

public class AdventOfCode08 {
  private static final Pattern PATTERN = Pattern.compile("(\\w+) = \\((\\w+), (\\w+)\\)");

  static int stepCount(String input) {
    var scanner = new Scanner(input);
    var instrs = scanner.nextLine().chars().map(c -> c == 'L' ? 0 : 1).toArray();
    record Node(String name, String... next) {}
    var map = scanner.findAll(PATTERN)
        .map(result -> new Node(result.group(1),result.group(2), result.group(3)))
        .collect(toMap(Node::name, n -> n));
    var cycle = Stream.iterate(0, i -> (i + 1) % instrs.length).iterator();
    return (int) Stream.iterate("AAA", not("ZZZ"::equals), n -> map.get(n).next[instrs[cycle.next()]]).count();
  }

  public static void main(String[] args) {
    var input = """
        LLR
                
        AAA = (BBB, BBB)
        BBB = (AAA, ZZZ)
        ZZZ = (ZZZ, ZZZ)
        """;
    System.out.println(stepCount(input));
  }
}
