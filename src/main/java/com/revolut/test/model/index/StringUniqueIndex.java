package com.revolut.test.model.index;

import java.util.Collection;
import java.util.function.Function;

public class StringUniqueIndex<V> extends UniqueIndex<String, V> {

    public StringUniqueIndex(Function<V, String> extractor) {
        super(extractor);
    }

    public Collection<V> search(String like) {
        int length = like.length();
        String s = like.substring(0, length - 1) + (char) (like.charAt(length - 1) + 1);
        return range(like, s);
    }

}
