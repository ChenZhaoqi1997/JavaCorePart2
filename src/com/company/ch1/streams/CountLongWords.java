package com.company.ch1.streams;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class CountLongWords {
    public static void main(String[] args) throws IOException {
        String address = "../gutenberg/alice30.txt";
        String contents = new String(Files.readAllBytes(Paths.get(address)), StandardCharsets.UTF_8);
        List<String> words = Arrays.asList(contents.split("\\PL+"));
        //1
        long count = 0;
        for (String w : words) {
            if (w.length() > 12) {
                count++;
            }
            System.out.println(count);
        }
        //2
        count = words.stream().filter(w -> w.length() > 12).count();
        System.out.println(count);
        //3
        count = words.parallelStream().filter(w -> w.length() > 12).count();
        System.out.println(count);
    }
}
