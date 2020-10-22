/*
 * GNU General Public License v3.0
 *
 * Copyright (c) 2020, Miguel Gamboa (gamboa.pt)
 *
 *   All rights granted under this License are granted for the term of
 * copyright on the Program, and are irrevocable provided the stated
 * conditions are met.  This License explicitly affirms your unlimited
 * permission to run the unmodified Program.  The output from running a
 * covered work is covered by this License only if the output, given its
 * content, constitutes a covered work.  This License acknowledges your
 * rights of fair use or other equivalent, as provided by copyright law.
 *
 *   You may make, run and propagate covered works that you do not
 * convey, without conditions so long as your license otherwise remains
 * in force.  You may convey covered works to others for the sole purpose
 * of having them make modifications exclusively for you, or provide you
 * with facilities for running those works, provided that you comply with
 * the terms of this License in conveying all material for which you do
 * not control copyright.  Those thus making or running the covered works
 * for you must do so exclusively on your behalf, under your direction
 * and control, on terms that prohibit them from making any copies of
 * your copyrighted material outside their relationship with you.
 *
 *   Conveying under any other circumstances is permitted solely under
 * the conditions stated below.  Sublicensing is not allowed; section 10
 * makes it unnecessary.
 */

package pt.isel.mpd.util;

import pt.isel.mpd.util.iterators.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class LazyQueries {

    // Factory Operations
    public static <T> Iterable<T> generate(Supplier<T> sup) {
        return () -> new IteratorGenerate<>(sup);
    }

    public static <T> Iterable<T> iterate(T seed, UnaryOperator<T> acc) {
        return () -> new IteratorIterate<>(seed, acc);
    }

    // Middle Operations
    public static <T> Iterable<T> filter(Iterable<T> src, Predicate<T> pred) {
        return () -> new IteratorFilter<>(src, pred);
    }

    public static <T> Iterable<T> skip(Iterable<T> src, int nr) {
        return () -> new IteratorSkip<>(src, nr);
    }

    public static <T> Iterable<T> limit(Iterable<T> src, int nr) {
        return () -> new IteratorLimit<>(src, nr);
    }

    public static <T, R> Iterable<R> map(Iterable<T> src, Function<T, R> mapper) {
        return () -> new IteratorMap<>(src, mapper);
    }

    public static <T> Iterable<T> takeWhile(Iterable<T> src, Predicate<T> pred) {
        return () -> new IteratorTakeWhile<>(src, pred);
    }

    public static <T, R> Iterable<R> flatMap(Iterable<T> src, Function<T, Iterable<R>> mapper) {
        return () -> new IteratorFlatMap<>(src, mapper);
    }

    public static <T> Iterable<T> distinct(Iterable<T> src){
        return () -> new IteratorDistinct<>(src);
    }


    public static <T> Iterable<T> cache(Iterable<T> src) {
        return new IterableCache<>(src);
    }


    // Terminal Operations
    public static <T> int count(Iterable<T> src) {
        int count = 0;
        for (T item : src) {
            count++;
        }
        return count;
    }

    public static <T> Object[] toArray(Iterable<T> src) {
        List<T> container = new ArrayList<>();
        src.forEach(container::add);
        return container.toArray();
    }

    public static <T> Optional<T> first(Iterable<T> src) {
        if (!src.iterator().hasNext()) return Optional.empty();
        return Optional.of(src.iterator().next());
    }

    public static <T extends Comparable<T>> Optional<T> max(Iterable<T> src) {
        Iterator<T> iter = src.iterator();
        if(!iter.hasNext()) return Optional.empty();
        T first = iter.next();
        while(iter.hasNext()) {
            T curr = iter.next();
            if(curr.compareTo(first) > 0)
                first = curr;
        }
        return Optional.of(first);
    }

    public static <T> Optional<T> last(Iterable<T> src) {
        Iterator<T> iter = src.iterator();
        if (!iter.hasNext()) return Optional.empty();
        T last;
        do {
            last = iter.next();
        }
        while (iter.hasNext());
        return Optional.of(last);
    }

    public static <T> boolean isEmpty(Iterable<T> src) {
        return !src.iterator().hasNext();
    }

    public static <T> boolean isNotEmpty(Iterable<T> src) {
        return !isEmpty(src);
    }
}
