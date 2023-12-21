import java.util.ArrayDeque;
import java.util.BitSet;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;
import static java.util.stream.IntStream.range;

public class AdventOfCode20 {
  enum Pulse { LOW, HIGH }
  record Event(String source, Pulse pulse, String dest) {}
  static final class State { int index; }
  enum EntityKind { BROADCASTER, FLIP_FLOP, CONJUNCTION, OUTPUT }
  record Entity(String name, EntityKind kind, Map<String, Integer> ins, List<String> outs, State state) {
    void send(Deque<Event> queue, Pulse pulse) {
      outs.forEach(o -> queue.offer(new Event(name, pulse, o)));
    }
  }
  record Counter(int low, int high) {
    Counter add(Counter c) { return new Counter(low + c.low, high + c.high); }
  }

  static Counter execute(Map<String, Entity> map, BitSet states) {
    int low = 0, high = 0;
    var queue = new ArrayDeque<Event>();
    queue.add(new Event("button", Pulse.LOW, "broadcaster"));
    while(!queue.isEmpty()) {
      var event = queue.poll();
      if (event.pulse == Pulse.LOW) { low++; } else { high++; }
      var entity = map.get(event.dest);
      switch (entity.kind) {
        case BROADCASTER -> entity.send(queue, event.pulse);
        case FLIP_FLOP -> {
          if (event.pulse == Pulse.LOW) {
            var state = states.get(entity.state.index);
            states.set(entity.state.index, !state);
            entity.send(queue, state ? Pulse.LOW : Pulse.HIGH);
          }
        }
        case CONJUNCTION -> {
          states.set(entity.state.index + entity.ins.get(event.source), event.pulse == Pulse.HIGH);
          var allHigh = range(0, entity.ins.size()).allMatch(i -> states.get(entity.state.index + i));
          entity.send(queue, allHigh ? Pulse.LOW : Pulse.HIGH);
        }
      }
    }
    return new Counter(low, high);
  }

  static Counter steps(String input, int times) {
    var map = input.lines()
        .map(line -> {
          var parts = line.split("->");
          var name = parts[0].substring(1).strip();
          var outputs = List.of(parts[1].strip().split(", "));
          var kind = switch (parts[0].charAt(0)) {
            case '%' -> EntityKind.FLIP_FLOP;
            case '&' -> EntityKind.CONJUNCTION;
            default -> { name = "broadcaster"; yield EntityKind.BROADCASTER; }
          };
          return new Entity(name, kind, new HashMap<>(), outputs, new State());
        })
        .collect(toMap(Entity::name, e -> e));
    map.put("output", new Entity("output", EntityKind.OUTPUT, new HashMap<>(), List.of(), null));
    map.values().forEach(e -> e.outs.stream().map(o -> map.get(o).ins).forEach(ins -> ins.put(e.name, ins.size())));
    var stateCount = 0;
    for(var e : map.values()) {
      switch (e.kind) {
        case FLIP_FLOP -> e.state.index = stateCount++;
        case CONJUNCTION -> { e.state.index = stateCount; stateCount += e.ins.size(); }
      }
    }
    var set = new BitSet(stateCount);
    return range(0, times).mapToObj(__ -> execute(map, set)).reduce(new Counter(0, 0), Counter::add);
  }

  public static void main(String[] args) {
    var input = """
        broadcaster -> a, b, c
        %a -> b
        %b -> c
        %c -> inv
        &inv -> a
        """;
    System.out.println(steps(input, 1_000));

    var input2 = """
        broadcaster -> a
        %a -> inv, con
        &inv -> b
        %b -> con
        &con -> output
        """;
    System.out.println(steps(input2, 1_000));
  }
}
