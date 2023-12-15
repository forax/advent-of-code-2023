import java.util.List;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

public class AdventOfCode13 {

  static int mirror(List<String> lines) {
    return range(0, lines.size()).boxed()
        .collect(groupingBy(lines::get, toList()))
        .values().stream()
        .<Integer>flatMap(indexes -> {
          var state = new Object() { int index = -2; };
          return indexes.stream().sorted().mapMulti((index, consumer) -> {
            if (state.index + 1 == index) {
              consumer.accept(state.index);
            }
            state.index = index;
          });
        })
        .filter(index -> range(0, index).filter(i -> index - i >= 0 && index + 1 + i < lines.size())
            .allMatch(i -> lines.get(index - i).equals(lines.get(index + 1 + i))))
        .findFirst().map(index -> 1 + index).orElseThrow();
  }

  static int summarizingMirrors(String input) {
    var puzzles = input.split("\n\n");
    var lines = puzzles[0].lines().toList();
    var puzzle1Columns = range(0, lines.getFirst().length())
        .mapToObj(i -> lines.stream().map(line -> "" + line.charAt(i)).collect(joining()))
        .toList();
    var puzzle2Lines = puzzles[1].lines().toList();
    return mirror(puzzle1Columns) + 100 * mirror(puzzle2Lines);
  }

  public static void main(String[] args) {
    var input = """
        #.##..##.
        ..#.##.#.
        ##......#
        ##......#
        ..#.##.#.
        ..##..##.
        #.#.##.#.
        
        #...##..#
        #....#..#
        ..##..###
        #####.##.
        #####.##.
        ..##..###
        #....#..#
        """;
    System.out.println(summarizingMirrors(input));
  }
}
