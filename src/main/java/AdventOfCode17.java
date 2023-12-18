import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Stream;

import static java.util.stream.IntStream.rangeClosed;

public class AdventOfCode17 {
  enum Dir {
    NORTH(0, -1), EAST(1, 0), SOUTH(0, 1), WEST(-1, 0);
    private final int dx, dy; Dir(int dx, int dy) { this.dx = dx; this.dy = dy; }
    List<Dir> next() {
      return switch (this) {
        case NORTH -> List.of(WEST, NORTH, EAST);
        case EAST -> List.of(NORTH, EAST, SOUTH);
        case SOUTH -> List.of(EAST, SOUTH, WEST);
        case WEST -> List.of(SOUTH, WEST, NORTH);
      };
    }
  }

  static int minHeatLost(String input) {
    var grid = input.lines().map(line -> line.chars().map(c -> c - '0').toArray()).toArray(int[][]::new);
    var width = grid[0].length;
    var height = grid.length;
    record Walk(int x, int y, Dir dir, int dist) {}
    record Work(int x, int y, Dir dir, int dist, int heat) {}
    var queue = new PriorityQueue<Work>(Comparator.comparing(Work::heat));
    queue.offer(new Work(0, 0, Dir.EAST, 0, 0));
    var heatMap = new HashMap<Walk, Integer>();
    while(!queue.isEmpty()) {
      var work = queue.poll();
      var x = work.x;
      var y = work.y;
      var heat = work.heat;
      var dir = work.dir;
      var dist = work.dist;
      var walk = new Walk(x, y, dir, dist);
      var seenHeat = heatMap.getOrDefault(walk, Integer.MAX_VALUE);
      if (seenHeat <= heat) {
        continue;
      }
      heatMap.put(walk, heat);
      if (x == width - 1 && y == height - 1) {
        continue;
      }
      for(Dir nextDir : dir.next()) {
        if (nextDir == dir && dist == 3) {
          continue;
        }
        var nextX = x + nextDir.dx;
        var nextY = y + nextDir.dy;
        if (nextX < 0 || nextX >= width || nextY < 0 || nextY >= height) {
          continue;
        }
        var nextHeat = grid[nextY][nextX] + heat;
        queue.offer(new Work(nextX, nextY, nextDir, nextDir == dir ? dist + 1 : 1, nextHeat));
      }
    }
    return Arrays.stream(Dir.values())
        .flatMap(d -> rangeClosed(1, 3).boxed().flatMap(i -> Stream.ofNullable(heatMap.get(new Walk(width - 1, height - 1, d, i)))))
        .mapToInt(v -> v)
        .min().orElseThrow();
  }

  public static void main(String[] args) {
    var input = """
        2413432311323
        3215453535623
        3255245654254
        3446585845452
        4546657867536
        1438598798454
        4457876987766
        3637877979653
        4654967986887
        4564679986453
        1224686865563
        2546548887735
        4322674655533
        """;
    System.out.println(minHeatLost(input));
  }
}
