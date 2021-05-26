package ru.vsu.cs.skofenko;

import java.lang.reflect.Array;
import java.util.*;

public class MapTest {

    public static final int TEST_MAP_SIZE = 1000;
    public static final int MAX_KEY_VALUE = 1000000;

    private static <K, V> String toString(Map<K, V> map, boolean ordered) {
        if (map.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        Map.Entry<K, V> firstEntry = map.entrySet().iterator().next();
        Map.Entry<K, V>[] entries = map.entrySet().stream().toArray(
                size -> (Map.Entry<K, V>[]) Array.newInstance(firstEntry.getClass(), size)
        );
        if (!ordered && firstEntry.getKey() instanceof Comparable) {
            Arrays.sort(entries, (a, b) -> ((Comparable) a.getKey()).compareTo(b.getKey()));
        }
        for (Map.Entry<K, V> kv : entries) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(String.format("(%s: %s)", kv.getKey(), kv.getValue()));
        }
        return sb.toString();
    }

    private static <K, V> void printMapsState(Map<K, V> rightMap, Map<K, V> testMap, boolean ordered) {
        System.out.printf("%d, %d, %s%n", testMap.size(), rightMap.size(), testMap.size() == rightMap.size());
        String s1 = toString(rightMap, true);
        String s2 = toString(testMap, ordered);
        System.out.println(s1);
        System.out.println(s2);
        System.out.printf("%s%n%n", s1.equals(s2));
    }

    public static void correctTest(Map<Integer, Integer> testMap, boolean ordered) {
        Map<Integer, Integer> rightMap = new TreeMap<>();
        testMap.clear();
        Random rnd = new Random();

        // добавление элементов
        for (int i = 0; i < TEST_MAP_SIZE; i++) {
            int key = rnd.nextInt(MAX_KEY_VALUE);
            int value = rnd.nextInt(MAX_KEY_VALUE);
            rightMap.put(key, value);
            testMap.put(key, value);
            //System.out.printf("(%s, %s)%n", key, value);
        }
        printMapsState(rightMap, testMap, ordered);

        // удаление некторых элементов
        Integer[] keys = rightMap.keySet().toArray(new Integer[0]);
        for (Integer key : keys) {
            if (rnd.nextInt(2) == 0) {
                rightMap.remove(key);
                testMap.remove(key);
                //System.out.printf("(%s)%n", key);
            }
        }
        printMapsState(rightMap, testMap, ordered);

        // повторное добавление элементов
        for (int i = 0; i < TEST_MAP_SIZE / 2; i++) {
            int key = rnd.nextInt(MAX_KEY_VALUE);
            int value = rnd.nextInt(MAX_KEY_VALUE);
            rightMap.put(key, value);
            testMap.put(key, value);
        }
        printMapsState(rightMap, testMap, ordered);

        if (testMap instanceof TreapMap) {
            for (int i = 0; i < 5; i++) {
                int k = rnd.nextInt(testMap.size());
                System.out.printf("%d -ый/ой/ий ключ по возрастанию: ", k);
                int key1 = findInOrderKey(k, testMap);
                System.out.print(key1+" ");
                int key2 = ((TreapMap<Integer, Integer>) testMap).findInOrderKey(k);
                System.out.print(key2+" ");
                System.out.println(key1 == key2);
            }
        }
    }

    private static int findInOrderKey(int k, Map<Integer, Integer> map) {
        Iterator<Integer> iterator = map.keySet().iterator();
        Integer answer = 0;
        for (int i = 0; i <= k; i++) {
            answer = iterator.next();
        }
        return answer;
    }

    public static void correctTest(Map<Integer, Integer> testMap) {
        correctTest(testMap, true);
    }
}
