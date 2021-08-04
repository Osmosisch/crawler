import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MapUtilTest {

    @Test
    public void testIntegrateResults() {
        Map<String, Integer> map1 = new HashMap<>();
        map1.put("site/s1", 1);
        map1.put("site/s2", 3);
        Map<String, Integer> map2 = new HashMap<>();
        map2.put("site/s1", 2);
        map2.put("site/s3", 6);

        List<Map<String, Integer>> maps = Arrays.asList(map1, map2);
        List<Pair<String, Integer>> result = MapUtil.integrateResults(maps);

        List<Pair<String, Integer>> expected = Arrays.asList(
                new ImmutablePair<>("/s3", 6),
                new ImmutablePair<>("/s1", 3),
                new ImmutablePair<>("/s2", 3)
        );

        assertEquals(expected, result);
    }

}