import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MapUtil {

    public static List<Pair<String, Integer>> integrateResults(List<Map<String, Integer>> crawlersLocalData) {
        Map<String, Integer> totalMap = crawlersLocalData.stream()
                .flatMap(m -> m.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey,
                        Map.Entry::getValue,
                        Integer::sum));
        return MapUtil.filterAndSort(totalMap);
    }


    private static     List<Pair<String, Integer>> filterAndSort(Map<String, Integer> totalMap) {
        return totalMap.entrySet().stream()
                .filter(e -> e.getValue() > 1)
                .map(e -> new ImmutablePair<>(e.getKey().substring(e.getKey().lastIndexOf('/')),
                        e.getValue()) )
                .sorted((p1, p2) -> p2.right.compareTo(p1.right)) // reverse order
                .collect(Collectors.toList());
    }
}
