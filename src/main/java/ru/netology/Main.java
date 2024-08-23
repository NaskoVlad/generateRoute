package ru.netology;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        final Map<Integer, Integer> sizeToFreq = new HashMap<>();
        char charR = 'R';

        for (int i = 0; i < 1000; i++) {
            String list = generateRoute("RLRFR", 100);
            int s = StringUtils.countMatches(list, charR);

            new Thread(() -> {
                synchronized (sizeToFreq) {
                    if (!sizeToFreq.containsKey(s)) {
                        sizeToFreq.put(s, 0);
                        sizeToFreq.notifyAll();
                    }
                }
            }).start();

            new Thread(() -> {
                synchronized (sizeToFreq) {
                    if (sizeToFreq.containsKey(s)) {
                        sizeToFreq.put(s, sizeToFreq.get(s) + 1);
                        try {
                            sizeToFreq.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }).start();
        }
        list(sizeToFreq);
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }

    static void list(Map<Integer, Integer> sizeToFreq) {
        int key = 0;
        int max = 0;
        int sum = 0;
        for (int i : sizeToFreq.keySet()) {
            int a = sizeToFreq.get(i);
            if (a > max) {
                max = a;
                key = i;
            }
        }

        System.out.println("Самое частое количество повторений " + key + " (встретилось " + max + " раз)");
        System.out.println("Другие размеры:");

        for (int i : sizeToFreq.keySet()) {
            int a = sizeToFreq.get(i);
            System.out.println("- " + i + " (" + a + " раз)");
            sum = sum + a;
        }
    }


}
