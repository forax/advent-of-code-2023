import java.util.Map;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;
import static java.util.stream.Collectors.toMap;

public class AdventOfCode19 {

  private static final Pattern ACTION = Pattern.compile("([a-z]+)([\\<\\>])(\\d+)\\:([a-zAR]+)|([a-zAR]+)");
  private static final Pattern PROPERTY = Pattern.compile("(\\w+)=(\\d+)");

  static int sumOfRatingNumbers(String input) {
    interface Action { String apply(Map<String, Integer> ratings); }
    var index = input.indexOf("\n\n");
    var actionMap = input.substring(0, index).lines()
        .collect(toMap(
            line -> line.substring(0, line.indexOf('{')),
            line -> ACTION.matcher(line).region(line.indexOf('{'), line.length()).results()
                .<Action>map(r -> {
                  if (r.start(5) != -1) {
                    return ratings -> r.group(5);
                  }
                  var variable = r.group(1);
                  var number = parseInt(r.group(3));
                  var target = r.group(4);
                  return switch (r.group(2)) {
                    case "<" -> ratings -> ratings.get(variable) < number ? target : null;
                    default -> ratings -> ratings.get(variable) > number ? target : null;
                  };
                })
                .toList()));
    return input.substring(index + 2).lines()
        .mapToInt(line -> {
          var ratings = PROPERTY.matcher(line).results().collect(toMap(r -> r.group(1), r -> parseInt(r.group(2))));
          loop: for(var state = "in";;) {
            var actions = actionMap.get(state);
            for(var action: actions) {
              switch (action.apply(ratings)) {
                case null -> {}
                case "A" -> { return ratings.values().stream().mapToInt(v -> v).sum(); }
                case "R" -> { return 0; }
                case String target -> { state = target; continue loop; }
              }
            }
          }
        })
        .sum();
  }

  public static void main(String[] args) {
    var input = """
        px{a<2006:qkq,m>2090:A,rfg}
        pv{a>1716:R,A}
        lnx{m>1548:A,A}
        rfg{s<537:gd,x>2440:R,A}
        qs{s>3448:A,lnx}
        qkq{x<1416:A,crn}
        crn{x>2662:A,R}
        in{s<1351:px,qqz}
        qqz{s>2770:qs,m<1801:hdj,R}
        gd{a>3333:R,R}
        hdj{m>838:A,pv}
                
        {x=787,m=2655,a=1222,s=2876}
        {x=1679,m=44,a=2067,s=496}
        {x=2036,m=264,a=79,s=2244}
        {x=2461,m=1339,a=466,s=291}
        {x=2127,m=1623,a=2188,s=1013}
        """;
    System.out.println(sumOfRatingNumbers(input));
  }
}
