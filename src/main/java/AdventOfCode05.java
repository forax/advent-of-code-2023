import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.IntFunction;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class AdventOfCode05 {
  private static final Pattern NUMBER = Pattern.compile("(\\d+)");
  private static final Pattern TO = Pattern.compile("(\\w+)-to-(\\w+)");
  private static final Pattern TRANSLATION = Pattern.compile("(\\d+)\\h+(\\d+)\\h+(\\d+)");

  static int lowestLocationNumber(String input) {
    var scanner = new Scanner(input);
    var values = NUMBER.matcher(scanner.nextLine()).results().map(r -> parseInt(r.group())).collect(toList());
    record Recipe(String from, String to, int location) {}
    var recipeMap = scanner.findAll(TO)
        .map(result -> new Recipe(result.group(1), result.group(2), result.end()))
        .collect(toMap(Recipe::from, r -> r, (_1, _2) -> null, LinkedHashMap::new));
    for(var category = "seed"; !category.equals("location");) {
      var recipe = recipeMap.get(category);
      var end = recipeMap.values().stream().dropWhile(r -> r != recipe).skip(1).mapToInt(Recipe::location).findFirst().orElseGet(input::length);
      var translations = TRANSLATION.matcher(input).region(recipe.location, end).results()
          .<IntFunction<Optional<Integer>>>map(result -> {
            var destination = parseInt(result.group(1));
            var source = parseInt(result.group(2));
            var length = parseInt(result.group(3));
            return v -> Optional.of(v).filter(x -> x >= source && x < source + length).map(x -> x - source + destination);
          })
          .toList();
      values.replaceAll(v -> translations.stream().flatMap(t -> t.apply(v).stream()).findFirst().orElse(v));
      category = recipe.to;
    }
    return values.stream().mapToInt(l -> l).min().orElseThrow();
  }

  public static void main(String[] args) {
    var input = """
        seeds: 79 14 55 13
                
        seed-to-soil map:
        50 98 2
        52 50 48
                
        soil-to-fertilizer map:
        0 15 37
        37 52 2
        39 0 15
                
        fertilizer-to-water map:
        49 53 8
        0 11 42
        42 0 7
        57 7 4
                
        water-to-light map:
        88 18 7
        18 25 70
                
        light-to-temperature map:
        45 77 23
        81 45 19
        68 64 13
                
        temperature-to-humidity map:
        0 69 1
        1 0 69
                
        humidity-to-location map:
        60 56 37
        56 93 4
        """;
    System.out.println(lowestLocationNumber(input));
  }
}

