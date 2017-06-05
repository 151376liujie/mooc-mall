package com.mall.util;

import com.google.common.collect.ImmutableSet;
import org.junit.Test;

/**
 * Author: jonny
 * Time: 2017-05-31 09:33.
 */
public class ImmutableSetTests {


    @Test
    public void testImmutableSet() {
        ImmutableSet<String> immutableSet = ImmutableSet.of("a", "b", "a");
        System.out.println(immutableSet);
    }
}
