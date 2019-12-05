package com.company.ch1.collecting;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CollectingResults {
    public static Stream<String> noVowels() throws IOException {
        String contents = new String(
                Files.readAllBytes(
                        Paths.get("C:\\Users\\15101\\Desktop\\qqq.txt")
                ),
                StandardCharsets.UTF_8
        );
        List<String> wordList = Arrays.asList(contents.split("\\PL+"));
        Stream<String> words = wordList.stream();
        return words.map(s -> s.replaceAll("[aeiouAEIOU]", ""));
    }
    public static <T> void show(String label, Set<T> set) throws IOException {
        System.out.print(label + ": " + set.getClass().getName());
        System.out.println("[" + set.stream().limit(10).map(Objects::toString)
                .collect(Collectors.joining(", ")) + "]");
    }
    public static void main(String[] args) throws IOException {
        Iterator<Integer> iter = Stream.iterate(0, n -> n + 1).limit(10).iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next());
        }
        Object[] numbers = Stream.iterate(0, n -> n + 1).limit(10).toArray();
        System.out.println("Object array: " + numbers);
        try {
            Integer number = (Integer)numbers[0];
            System.out.println("number: " + number);
            System.out.println("The following: ");
            Integer[] numbers2 = (Integer[])numbers;
        } catch (ClassCastException e) {
            System.out.println(e);
        }
        Integer[] numbers3 = Stream.iterate(0, n -> n + 1).limit(10).toArray(Integer[]::new);
        System.out.println("Integer array: " + numbers3);
        Set<String> noVowelSet = noVowels().collect(Collectors.toSet());
        show("noVowelSet", noVowelSet);
        TreeSet<String> noVowelTreeSet = noVowels().collect(Collectors.toCollection(TreeSet::new));
        show("noVowelTreeSet", noVowelTreeSet);
        String result = noVowels().limit(10).collect(Collectors.joining());
        System.out.println("Joining: " + result);
        result = noVowels().limit(10).collect(Collectors.joining(", "));
        System.out.println("Joining with commas: " + result);
        IntSummaryStatistics summary = noVowels().collect(Collectors.summarizingInt(String::length));
        double averageWordLength = summary.getAverage();
        double maxWordLength = summary.getMax();
        noVowels().limit(10).forEach(System.out::println);
    }
}
