import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class AdventOfCode12 {
  private static final Pattern PATTERN = Pattern.compile("[#]+");

  static int combination(String line, List<Integer> damages) {
    var index = line.indexOf('?');
    if (index == -1) {
      return damages.equals(PATTERN.matcher(line).results().map(r -> r.group().length()).toList()) ? 1 : 0;
    }
    var prefix = line.substring(0, index);
    var suffix = index == line.length() - 1 ? "" : line.substring(index + 1);
    return combination( prefix + "." + suffix, damages) + combination(prefix + "#" + suffix, damages);
  }

  static int sumOfCombinations(String input) {
    return input.lines()
        .map(line -> line.split(" "))
        .mapToInt(pair -> combination(pair[0], Arrays.stream(pair[1].split(",")).map(Integer::parseInt).toList()))
        .sum();
  }

  public static void main(String[] args){
    var input = """
      ???.### 1,1,3
      .??..??...?##. 1,1,3
      ?#?#?#?#?#?#?#? 1,3,1,6
      ????.#...#... 4,1,1
      ????.######..#####. 1,6,5
      ?###???????? 3,2,1
      """;

    System.out.println(sumOfCombinations(input));
  }
}
