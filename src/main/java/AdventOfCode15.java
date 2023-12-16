import java.util.Arrays;

public class AdventOfCode15 {
  static int sumOfHashes(String input) {
    return Arrays.stream(input.split("[,\n]"))
        .mapToInt(text -> text.chars().reduce(0, (acc, c) -> ((acc + c) * 17) & 0xFF)).sum();
  }

  public static void main(String[] args) {
    var input = """
        rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7
        """;
    System.out.println(sumOfHashes(input));
  }
}
