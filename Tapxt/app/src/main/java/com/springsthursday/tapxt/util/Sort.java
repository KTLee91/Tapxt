package com.springsthursday.tapxt.util;

import com.springsthursday.tapxt.item.EpisodeItem;

import java.util.Comparator;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

public class Sort {

    public static class AscSorting<E> implements Comparator<EpisodeItem>{

        @Override
        public boolean equals(Object obj) {
            return false;
        }

        @Override
        public int compare(EpisodeItem o1, EpisodeItem o2) {
            if (o1.getSequence() < o2.getSequence()) {
                return -1;
            } else if (o1.getSequence() > o2.getSequence()) {
                return 1;
            }
            return 0;
        }

        @Override
        public Comparator<EpisodeItem> reversed() {
            return null;
        }

        @Override
        public Comparator<EpisodeItem> thenComparing(Comparator<? super EpisodeItem> other) {
            return null;
        }

        @Override
        public <U> Comparator<EpisodeItem> thenComparing(Function<? super EpisodeItem, ? extends U> keyExtractor, Comparator<? super U> keyComparator) {
            return null;
        }

        @Override
        public <U extends Comparable<? super U>> Comparator<EpisodeItem> thenComparing(Function<? super EpisodeItem, ? extends U> keyExtractor) {
            return null;
        }

        @Override
        public Comparator<EpisodeItem> thenComparingInt(ToIntFunction<? super EpisodeItem> keyExtractor) {
            return null;
        }

        @Override
        public Comparator<EpisodeItem> thenComparingLong(ToLongFunction<? super EpisodeItem> keyExtractor) {
            return null;
        }

        @Override
        public Comparator<EpisodeItem> thenComparingDouble(ToDoubleFunction<? super EpisodeItem> keyExtractor) {
            return null;
        }
    }

    public static  class DescdingSort<E> implements Comparator<EpisodeItem>{

        @Override
        public boolean equals(Object obj) {
            return false;
        }

        @Override
        public int compare(EpisodeItem o1, EpisodeItem o2) {
            if (o1.getSequence() > o2.getSequence()) {
                return -1;
            } else if (o1.getSequence() < o2.getSequence()) {
                return 1;
            }
            return 0;
        }

        @Override
        public Comparator<EpisodeItem> reversed() {
            return null;
        }

        @Override
        public Comparator<EpisodeItem> thenComparing(Comparator<? super EpisodeItem> other) {
            return null;
        }

        @Override
        public <U> Comparator<EpisodeItem> thenComparing(Function<? super EpisodeItem, ? extends U> keyExtractor, Comparator<? super U> keyComparator) {
            return null;
        }

        @Override
        public <U extends Comparable<? super U>> Comparator<EpisodeItem> thenComparing(Function<? super EpisodeItem, ? extends U> keyExtractor) {
            return null;
        }

        @Override
        public Comparator<EpisodeItem> thenComparingInt(ToIntFunction<? super EpisodeItem> keyExtractor) {
            return null;
        }

        @Override
        public Comparator<EpisodeItem> thenComparingLong(ToLongFunction<? super EpisodeItem> keyExtractor) {
            return null;
        }

        @Override
        public Comparator<EpisodeItem> thenComparingDouble(ToDoubleFunction<? super EpisodeItem> keyExtractor) {
            return null;
        }
    }
}
