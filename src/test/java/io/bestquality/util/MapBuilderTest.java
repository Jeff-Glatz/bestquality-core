package io.bestquality.util;

import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static io.bestquality.util.MapBuilder.newMap;
import static org.assertj.core.api.Assertions.assertThat;

public class MapBuilderTest {

    @Test
    public void shouldBuildLinkedHashMapByDefault() {
        Map<String, String> expected = new HashMap<>();
        expected.put("key", "value");

        Map<String, String> actual = newMap(String.class, String.class)
                .with("key", "value")
                .build();

        assertThat(actual)
                .isEqualTo(expected);
        assertThat(actual)
                .isInstanceOf(LinkedHashMap.class);
    }

    @Test
    public void shouldBuildSpecifiedMap() {
        Map<String, String> expected = new HashMap<>();
        expected.put("key", "value");

        Map<String, String> actual = newMap(new HashMap<String, String>())
                .with("key", "value")
                .build();

        assertThat(actual)
                .isEqualTo(expected);
        assertThat(actual)
                .isInstanceOf(HashMap.class);
    }
}
