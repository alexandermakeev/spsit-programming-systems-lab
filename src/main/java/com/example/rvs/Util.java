package com.example.rvs;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

public class Util {
    public static List<List<Double>> toList(double[][] arr) {
        return Arrays.stream(arr)
                .map(a -> DoubleStream.of(a).boxed().collect(Collectors.toList()))
                .collect(Collectors.toList());
    }

    public static double[][] toArray(List<List<Double>> list) {
        return list.stream()
                .map(l -> l.stream().mapToDouble(v -> v).toArray())
                .toArray(double[][]::new);
    }
}
