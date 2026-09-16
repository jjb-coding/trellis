package com.github.jjbcoding.trellis.util.internal;

import java.util.ArrayList;
import java.util.List;

public class DataHelper {
    public static <T> List<T> flatten(List<List<T>> in) {
        List<T> out = new ArrayList<>();
        for (List<T> list : in)
            out.addAll(list);
        return out;
    }
}
