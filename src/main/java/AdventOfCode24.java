import java.math.BigInteger;
import java.util.regex.Pattern;

import static java.lang.Long.parseLong;
import static java.util.stream.IntStream.range;

public class AdventOfCode24 {

  record Rat(BigInteger num, BigInteger den) implements Comparable<Rat> {
    Rat {
      if (den.signum() == -1) { num = num.negate(); den = den.negate(); }
      var gcd = num.gcd(den); num = num.divide(gcd); den = den.divide(gcd);
    }
    Rat(long num) { this(BigInteger.valueOf(num), BigInteger.ONE); }
    Rat mul(Rat rat) { return new Rat(num.multiply(rat.num), den.multiply(rat.den)); }
    Rat div(Rat rat) { return mul(new Rat(rat.den, rat.num)); }
    Rat add(Rat rat) { return new Rat(num.multiply(rat.den).add(rat.num.multiply(den)), den.multiply(rat.den)); }
    Rat sub(Rat rat) { return add(new Rat(rat.num.negate(), rat.den)); }
    public int compareTo(Rat rat) { return num.multiply(rat.den).compareTo(rat.num.multiply(den)); }
    public String toString() { return num + "/" + den; }
  }

  private static final Pattern PATTERN = Pattern.compile("[, @]+");

  static int cross(String input, Rat low, Rat high) {
    record HailStone(Rat x, Rat y, Rat vx, Rat vy) {}
    var stones = input.lines()
        .map(l -> {
          var data = PATTERN.splitAsStream(l).map(s -> new Rat(parseLong(s))).toArray(Rat[]::new);
          return new HailStone(data[0], data[1], data[3], data[4]);
        })
        .toList();
    return range(0, stones.size())
        .flatMap(i -> range(0, i).map(j -> {
          var a = stones.get(i);
          var b = stones.get(j);
          var u = b.x.sub(a.x).mul(a.vy).div(a.vx).add(a.y).sub(b.y).div(b.vy.sub(a.vy.mul(b.vx).div(a.vx)));
          var t = b.vx.mul(u).add(b.x).sub(a.x).div(a.vx);
          if (t.den.equals(BigInteger.ZERO) || t.num.signum() == -1 || u.num.signum() == -1) {
            return 0;
          }
          var x = a.vx.mul(t).add(a.x);
          var y = a.vy.mul(t).add(a.y);
          return (low.compareTo(x) <= 0 && high.compareTo(x) >= 0 &&
              low.compareTo(y) <= 0 && high.compareTo(y) >= 0) ? 1 : 0;
        }))
        .sum();
  }

  public static void main(String[] args) {
    var input = """
        19, 13, 30 @ -2,  1, -2
        18, 19, 22 @ -1, -1, -2
        20, 25, 34 @ -2, -2, -4
        12, 31, 28 @ -1, -2, -1
        20, 19, 15 @  1, -5, -3
        """;
    var low = new Rat(7);
    var hight = new Rat(27);
    System.out.println(cross(input, low, hight));
  }
}
