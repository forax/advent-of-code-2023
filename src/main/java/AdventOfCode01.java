public class AdventOfCode01 {
  public static void main(String[] args) {
    var text = """
        1abc2
        pqr3stu8vwx
        a1b2c3d4e5f
        treb7uchet
        """;
    var result = text.lines()
        .map(line -> line.chars().filter(c -> c >= '0' && c <= '9').mapToObj(c -> c - '0').toList())
        .mapToInt(list -> 10 * list.getFirst() + list.getLast())
        .sum();
    System.out.println(result);
  }
}
