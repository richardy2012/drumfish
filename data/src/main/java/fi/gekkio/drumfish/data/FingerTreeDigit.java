package fi.gekkio.drumfish.data;

import java.io.Serializable;
import java.util.Iterator;

import javax.annotation.Nullable;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Value;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;

import fi.gekkio.drumfish.data.FingerTree.Printer;
import fi.gekkio.drumfish.lang.Function2;
import fi.gekkio.drumfish.lang.Option;

abstract class FingerTreeDigit<T> implements Iterable<T>, Serializable {
    private static final long serialVersionUID = 751532813120711150L;

    private FingerTreeDigit() {
    }

    public abstract FingerTreeDigit<T> prepend(T value);

    public abstract FingerTreeDigit<T> append(T value);

    public abstract <V> V measure(FingerTreeFactory<V, T> factory);

    public abstract <O> FingerTreeDigit<O> map(Function<? super T, O> f);

    public abstract T getHead();

    public abstract FingerTreeDigit<T> getTail();

    public abstract FingerTreeDigit<T> getInit();

    public abstract T getLast();

    public abstract <V> DigitSplit<T> split(FingerTreeFactory<V, T> factory, Predicate<? super V> p, V accum);

    public abstract <V> FingerTreeNode<V, T> toTailNode(FingerTreeFactory<V, T> factory);

    public abstract <V> FingerTreeNode<V, T> toInitNode(FingerTreeFactory<V, T> factory);

    public abstract <V> FingerTree<V, T> toTree(FingerTreeFactory<V, T> factory);

    public abstract void print(StringBuilder sb, String padding, Printer<? super T> printer);

    public abstract int getSize();

    public abstract Iterator<T> reverseIterator();

    public abstract FingerTreeDigit<T> reverseAndMap(Function<T, T> f);

    public abstract <U> U foldLeft(@Nullable U initial, Function2<U, T, U> f);

    public abstract <V> Option<T> find(FingerTreeFactory<V, T> factory, Predicate<? super V> p);

    @Value
    static class DigitSplit<T> implements Serializable {
        private static final long serialVersionUID = -5364760129719273207L;

        @Nullable
        public final FingerTreeDigit<T> left;
        public final T pivot;
        @Nullable
        public final FingerTreeDigit<T> right;
    }

    public static <T> FingerTreeDigit<T> digit(T a) {
        return new Digit1<T>(a);
    }

    public static <T> FingerTreeDigit<T> digit(T a, T b) {
        return new Digit2<T>(a, b);
    }

    public static <T> FingerTreeDigit<T> digit(T a, T b, T c) {
        return new Digit3<T>(a, b, c);
    }

    public static <T> FingerTreeDigit<T> digit(T a, T b, T c, T d) {
        return new Digit4<T>(a, b, c, d);
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @EqualsAndHashCode(callSuper = false)
    @ToString(callSuper = false)
    static final class Digit1<T> extends FingerTreeDigit<T> {
        private static final long serialVersionUID = -667222115592102172L;

        final T a;

        @Override
        public FingerTreeDigit<T> prepend(T value) {
            return digit(value, a);
        }

        @Override
        public FingerTreeDigit<T> append(T value) {
            return digit(a, value);
        }

        @Override
        public <O> FingerTreeDigit<O> map(Function<? super T, O> f) {
            return digit(f.apply(a));
        }

        @Override
        public <V> V measure(FingerTreeFactory<V, T> factory) {
            return factory.measure(a);
        }

        @Override
        public Iterator<T> iterator() {
            return Iterators.singletonIterator(a);
        }

        @Override
        public Iterator<T> reverseIterator() {
            return Iterators.singletonIterator(a);
        }

        @Override
        public T getHead() {
            return a;
        }

        @Override
        public FingerTreeDigit<T> getTail() {
            throw new UnsupportedOperationException("Cannot get tail from Digit1");
        }

        @Override
        public <V> FingerTreeNode<V, T> toTailNode(FingerTreeFactory<V, T> factory) {
            throw new UnsupportedOperationException("Cannot get tail node from Digit1");
        }

        @Override
        public FingerTreeDigit<T> getInit() {
            throw new UnsupportedOperationException("Cannot get init from Digit1");
        }

        @Override
        public <V> FingerTreeNode<V, T> toInitNode(FingerTreeFactory<V, T> factory) {
            throw new UnsupportedOperationException("Cannot get init node from Digit1");
        }

        @Override
        public T getLast() {
            return a;
        }

        @Override
        public <V> DigitSplit<T> split(FingerTreeFactory<V, T> factory, Predicate<? super V> p, V accum) {
            return new DigitSplit<T>(null, a, null);
        }

        @Override
        public <V> FingerTree<V, T> toTree(FingerTreeFactory<V, T> factory) {
            return factory.tree(a);
        }

        @Override
        public void print(StringBuilder sb, String padding, Printer<? super T> printer) {
            sb.append('\n');

            sb.append(padding);
            sb.append("\\-");
            printer.print(sb, padding + "  ", a);
        }

        @Override
        public int getSize() {
            return 1;
        }

        @Override
        public FingerTreeDigit<T> reverseAndMap(Function<T, T> f) {
            return digit(f.apply(a));
        }

        @Override
        public <U> U foldLeft(@Nullable U initial, Function2<U, T, U> f) {
            U accum = initial;
            accum = f.apply(accum, a);
            return accum;
        }

        @Override
        public <V> Option<T> find(FingerTreeFactory<V, T> factory, Predicate<? super V> p) {
            if (p.apply(factory.measure(a)))
                return Option.some(a);
            return Option.none();
        }

    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @EqualsAndHashCode(callSuper = false)
    @ToString(callSuper = false)
    static final class Digit2<T> extends FingerTreeDigit<T> {
        private static final long serialVersionUID = 3093918006332086954L;

        final T a;
        final T b;

        @Override
        public FingerTreeDigit<T> prepend(T value) {
            return digit(value, a, b);
        }

        @Override
        public FingerTreeDigit<T> append(T value) {
            return digit(a, b, value);
        }

        @Override
        public <O> FingerTreeDigit<O> map(Function<? super T, O> f) {
            return digit(f.apply(a), f.apply(b));
        }

        @Override
        public <V> V measure(FingerTreeFactory<V, T> factory) {
            return factory.measure(a, b);
        }

        @SuppressWarnings("unchecked")
        @Override
        public Iterator<T> iterator() {
            return Iterators.forArray(a, b);
        }

        @SuppressWarnings("unchecked")
        @Override
        public Iterator<T> reverseIterator() {
            return Iterators.forArray(b, a);
        }

        @Override
        public T getHead() {
            return a;
        }

        @Override
        public FingerTreeDigit<T> getTail() {
            return digit(b);
        }

        @Override
        public <V> FingerTreeNode<V, T> toTailNode(FingerTreeFactory<V, T> factory) {
            throw new UnsupportedOperationException("Cannot get tail node from Digit2");
        }

        @Override
        public FingerTreeDigit<T> getInit() {
            return digit(a);
        }

        @Override
        public <V> FingerTreeNode<V, T> toInitNode(FingerTreeFactory<V, T> factory) {
            throw new UnsupportedOperationException("Cannot get init node from Digit2");
        }

        @Override
        public T getLast() {
            return b;
        }

        @Override
        public <V> DigitSplit<T> split(FingerTreeFactory<V, T> factory, Predicate<? super V> p, V accum) {
            V accumA = factory.mappend(accum, factory.measure(a));
            if (p.apply(accumA))
                return new DigitSplit<T>(null, a, digit(b));
            return new DigitSplit<T>(digit(a), b, null);
        }

        @Override
        public <V> FingerTree<V, T> toTree(FingerTreeFactory<V, T> factory) {
            return factory.deep(digit(a), factory.nodeFactory().emptyTree, digit(b));
        }

        @Override
        public void print(StringBuilder sb, String padding, Printer<? super T> printer) {
            sb.append('\n');

            sb.append(padding);
            sb.append("|-");
            printer.print(sb, padding + "  ", a);
            sb.append('\n');

            sb.append(padding);
            sb.append("\\-");
            printer.print(sb, padding + "  ", b);
        }

        @Override
        public int getSize() {
            return 2;
        }

        @Override
        public FingerTreeDigit<T> reverseAndMap(Function<T, T> f) {
            return digit(f.apply(b), f.apply(a));
        }

        @Override
        public <U> U foldLeft(@Nullable U initial, Function2<U, T, U> f) {
            U accum = initial;
            accum = f.apply(accum, a);
            accum = f.apply(accum, b);
            return accum;
        }

        @Override
        public <V> Option<T> find(FingerTreeFactory<V, T> factory, Predicate<? super V> p) {
            if (p.apply(factory.measure(a)))
                return Option.some(a);
            if (p.apply(factory.measure(b)))
                return Option.some(b);
            return Option.none();
        }

    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @EqualsAndHashCode(callSuper = false)
    @ToString(callSuper = false)
    static final class Digit3<T> extends FingerTreeDigit<T> {
        private static final long serialVersionUID = -782252146232630363L;

        final T a;
        final T b;
        final T c;

        @Override
        public FingerTreeDigit<T> prepend(T value) {
            return digit(value, a, b, c);
        }

        @Override
        public FingerTreeDigit<T> append(T value) {
            return digit(a, b, c, value);
        }

        @Override
        public <O> FingerTreeDigit<O> map(Function<? super T, O> f) {
            return digit(f.apply(a), f.apply(b), f.apply(c));
        }

        @Override
        public <V> V measure(FingerTreeFactory<V, T> factory) {
            return factory.measure(a, b, c);
        }

        @SuppressWarnings("unchecked")
        @Override
        public Iterator<T> iterator() {
            return Iterators.forArray(a, b, c);
        }

        @SuppressWarnings("unchecked")
        @Override
        public Iterator<T> reverseIterator() {
            return Iterators.forArray(c, b, a);
        }

        @Override
        public T getHead() {
            return a;
        }

        @Override
        public FingerTreeDigit<T> getTail() {
            return digit(b, c);
        }

        @Override
        public <V> FingerTreeNode<V, T> toTailNode(FingerTreeFactory<V, T> factory) {
            return factory.node(b, c);
        }

        @Override
        public FingerTreeDigit<T> getInit() {
            return digit(a, b);
        }

        @Override
        public <V> FingerTreeNode<V, T> toInitNode(FingerTreeFactory<V, T> factory) {
            return factory.node(a, b);
        }

        @Override
        public T getLast() {
            return c;
        }

        @Override
        public <V> DigitSplit<T> split(FingerTreeFactory<V, T> factory, Predicate<? super V> p, V accum) {
            V accumA = factory.mappend(accum, factory.measure(a));
            if (p.apply(accumA))
                return new DigitSplit<T>(null, a, digit(b, c));
            V accumB = factory.mappend(accumA, factory.measure(b));
            if (p.apply(accumB))
                return new DigitSplit<T>(digit(a), b, digit(c));
            return new DigitSplit<T>(digit(a, b), c, null);
        }

        @Override
        public <V> FingerTree<V, T> toTree(FingerTreeFactory<V, T> factory) {
            return factory.deep(digit(a, b), factory.nodeFactory().emptyTree, digit(c));
        }

        @Override
        public void print(StringBuilder sb, String padding, Printer<? super T> printer) {
            sb.append('\n');

            sb.append(padding);
            sb.append("|-");
            printer.print(sb, padding + "  ", a);
            sb.append('\n');

            sb.append(padding);
            sb.append("|-");
            printer.print(sb, padding + "  ", b);
            sb.append('\n');

            sb.append(padding);
            sb.append("\\-");
            printer.print(sb, padding + "  ", c);
        }

        @Override
        public int getSize() {
            return 3;
        }

        @Override
        public FingerTreeDigit<T> reverseAndMap(Function<T, T> f) {
            return digit(f.apply(c), f.apply(b), f.apply(a));
        }

        @Override
        public <U> U foldLeft(@Nullable U initial, Function2<U, T, U> f) {
            U accum = initial;
            accum = f.apply(accum, a);
            accum = f.apply(accum, b);
            accum = f.apply(accum, c);
            return accum;
        }

        @Override
        public <V> Option<T> find(FingerTreeFactory<V, T> factory, Predicate<? super V> p) {
            if (p.apply(factory.measure(a)))
                return Option.some(a);
            if (p.apply(factory.measure(b)))
                return Option.some(b);
            if (p.apply(factory.measure(c)))
                return Option.some(c);
            return Option.none();
        }
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @EqualsAndHashCode(callSuper = false)
    @ToString(callSuper = false)
    static final class Digit4<T> extends FingerTreeDigit<T> {
        private static final long serialVersionUID = 1441409605217019592L;

        final T a;
        final T b;
        final T c;
        final T d;

        @Override
        public FingerTreeDigit<T> prepend(T value) {
            throw new UnsupportedOperationException("Cannot prepend to Digit4");
        }

        @Override
        public FingerTreeDigit<T> append(T value) {
            throw new UnsupportedOperationException("Cannot append to Digit4");
        }

        @Override
        public <O> FingerTreeDigit<O> map(Function<? super T, O> f) {
            return digit(f.apply(a), f.apply(b), f.apply(c), f.apply(d));
        }

        @Override
        public <V> V measure(FingerTreeFactory<V, T> factory) {
            return factory.measure(a, b, c, d);
        }

        @SuppressWarnings("unchecked")
        @Override
        public Iterator<T> iterator() {
            return Iterators.forArray(a, b, c, d);
        }

        @SuppressWarnings("unchecked")
        @Override
        public Iterator<T> reverseIterator() {
            return Iterators.forArray(d, c, b, a);
        }

        @Override
        public T getHead() {
            return a;
        }

        @Override
        public FingerTreeDigit<T> getTail() {
            return digit(b, c, d);
        }

        @Override
        public <V> FingerTreeNode<V, T> toTailNode(FingerTreeFactory<V, T> factory) {
            return factory.node(b, c, d);
        }

        @Override
        public FingerTreeDigit<T> getInit() {
            return digit(a, b, c);
        }

        @Override
        public <V> FingerTreeNode<V, T> toInitNode(FingerTreeFactory<V, T> factory) {
            return factory.node(a, b, c);
        }

        @Override
        public T getLast() {
            return d;
        }

        @Override
        public <V> DigitSplit<T> split(FingerTreeFactory<V, T> factory, Predicate<? super V> p, V accum) {
            V accumA = factory.mappend(accum, factory.measure(a));
            if (p.apply(accumA))
                return new DigitSplit<T>(null, a, digit(b, c, d));
            V accumB = factory.mappend(accumA, factory.measure(b));
            if (p.apply(accumB))
                return new DigitSplit<T>(digit(a), b, digit(c, d));
            V accumC = factory.mappend(accumB, factory.measure(c));
            if (p.apply(accumC))
                return new DigitSplit<T>(digit(a, b), c, digit(d));
            return new DigitSplit<T>(digit(a, b, c), d, null);
        }

        @Override
        public <V> FingerTree<V, T> toTree(FingerTreeFactory<V, T> factory) {
            return factory.deep(digit(a, b), factory.nodeFactory().emptyTree, digit(c, d));
        }

        @Override
        public void print(StringBuilder sb, String padding, Printer<? super T> printer) {
            sb.append('\n');

            sb.append(padding);
            sb.append("|-");
            printer.print(sb, padding + "  ", a);
            sb.append('\n');

            sb.append(padding);
            sb.append("|-");
            printer.print(sb, padding + "  ", b);
            sb.append('\n');

            sb.append(padding);
            sb.append("|-");
            printer.print(sb, padding + "  ", c);
            sb.append('\n');

            sb.append(padding);
            sb.append("\\-");
            printer.print(sb, padding + "  ", d);
        }

        @Override
        public int getSize() {
            return 4;
        }

        @Override
        public FingerTreeDigit<T> reverseAndMap(Function<T, T> f) {
            return digit(f.apply(d), f.apply(c), f.apply(b), f.apply(a));
        }

        @Override
        public <U> U foldLeft(@Nullable U initial, Function2<U, T, U> f) {
            U accum = initial;
            accum = f.apply(accum, a);
            accum = f.apply(accum, b);
            accum = f.apply(accum, c);
            accum = f.apply(accum, d);
            return accum;
        }

        @Override
        public <V> Option<T> find(FingerTreeFactory<V, T> factory, Predicate<? super V> p) {
            if (p.apply(factory.measure(a)))
                return Option.some(a);
            if (p.apply(factory.measure(b)))
                return Option.some(b);
            if (p.apply(factory.measure(c)))
                return Option.some(c);
            if (p.apply(factory.measure(d)))
                return Option.some(d);
            return Option.none();
        }
    }

}
