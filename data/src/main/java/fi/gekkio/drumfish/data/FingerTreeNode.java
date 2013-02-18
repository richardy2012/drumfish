package fi.gekkio.drumfish.data;

import static fi.gekkio.drumfish.data.FingerTreeDigit.digit;

import java.io.Serializable;
import java.util.Iterator;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.UnmodifiableIterator;

import fi.gekkio.drumfish.data.FingerTree.Printer;
import fi.gekkio.drumfish.data.FingerTreeDigit.DigitSplit;

abstract class FingerTreeNode<V, T> implements Iterable<T>, Serializable {
    private static final long serialVersionUID = -9130857121651032905L;

    private FingerTreeNode() {
    }

    public abstract V measure();

    public abstract <U, O> FingerTreeNode<U, O> map(FingerTreeFactory<U, O> factory, Function<T, O> f);

    public abstract DigitSplit<T> split(FingerTreeFactory<V, T> factory, Predicate<V> p, V accum);

    public abstract FingerTreeDigit<T> toDigit();

    public abstract void print(StringBuilder sb, String padding, Printer<? super T> printer);

    public abstract int getSize();

    @RequiredArgsConstructor
    @EqualsAndHashCode(callSuper = false, exclude = "measure")
    @ToString(callSuper = false)
    static final class Node2<V, T> extends FingerTreeNode<V, T> {
        private static final long serialVersionUID = 7669550720868602973L;

        public final V measure;
        public final T a;
        public final T b;

        @Override
        public V measure() {
            return measure;
        }

        @SuppressWarnings("unchecked")
        @Override
        public Iterator<T> iterator() {
            return Iterators.forArray(a, b);
        }

        @Override
        public <U, O> FingerTreeNode<U, O> map(FingerTreeFactory<U, O> factory, Function<T, O> f) {
            return factory.node(f.apply(a), f.apply(b));
        }

        @Override
        public DigitSplit<T> split(FingerTreeFactory<V, T> factory, Predicate<V> p, V accum) {
            V accumA = factory.mappend(accum, factory.measure(a));
            if (p.apply(accumA))
                return new DigitSplit<T>(null, a, digit(b));
            return new DigitSplit<T>(digit(a), b, null);
        }

        @Override
        public FingerTreeDigit<T> toDigit() {
            return digit(a, b);
        }

        @Override
        public void print(StringBuilder sb, String padding, Printer<? super T> printer) {
            sb.append("*\n");

            sb.append(padding);
            sb.append("|+");
            printer.print(sb, padding + "  ", a);
            sb.append('\n');

            sb.append(padding);
            sb.append("\\+");
            printer.print(sb, padding + "  ", b);
        }

        @Override
        public int getSize() {
            return 2;
        }
    }

    @RequiredArgsConstructor
    @EqualsAndHashCode(callSuper = false, exclude = "measure")
    @ToString(callSuper = false)
    static final class Node3<V, T> extends FingerTreeNode<V, T> {
        private static final long serialVersionUID = -3973692276446284002L;

        public final V measure;
        public final T a;
        public final T b;
        public final T c;

        @Override
        public V measure() {
            return measure;
        }

        @SuppressWarnings("unchecked")
        @Override
        public Iterator<T> iterator() {
            return Iterators.forArray(a, b, c);
        }

        @Override
        public <U, O> FingerTreeNode<U, O> map(FingerTreeFactory<U, O> factory, Function<T, O> f) {
            return factory.node(f.apply(a), f.apply(b), f.apply(c));
        }

        @Override
        public DigitSplit<T> split(FingerTreeFactory<V, T> factory, Predicate<V> p, V accum) {
            V accumA = factory.mappend(accum, factory.measure(a));
            if (p.apply(accumA))
                return new DigitSplit<T>(null, a, digit(b));
            V accumB = factory.mappend(accumA, factory.measure(b));
            if (p.apply(accumB))
                return new DigitSplit<T>(digit(a), b, digit(c));
            return new DigitSplit<T>(digit(a, b), c, null);
        }

        @Override
        public FingerTreeDigit<T> toDigit() {
            return digit(a, b, c);
        }

        @Override
        public void print(StringBuilder sb, String padding, Printer<? super T> printer) {
            sb.append("*\n");

            sb.append(padding);
            sb.append("|+");
            printer.print(sb, padding + "  ", a);
            sb.append('\n');

            sb.append(padding);
            sb.append("|+");
            printer.print(sb, padding + "  ", b);
            sb.append('\n');

            sb.append(padding);
            sb.append("\\+");
            printer.print(sb, padding + "  ", c);
        }

        @Override
        public int getSize() {
            return 3;
        }

    }

    @RequiredArgsConstructor
    static final class FtNodeMapper<V, T, U, O> implements Function<FingerTreeNode<V, T>, FingerTreeNode<U, O>>, Serializable {
        private static final long serialVersionUID = 71975552025750738L;

        private final FingerTreeFactory<U, O> factory;
        private final Function<T, O> f;

        @Override
        public FingerTreeNode<U, O> apply(FingerTreeNode<V, T> input) {
            return input.map(factory, f);
        }

    }

    @RequiredArgsConstructor
    static final class FtNodeIterator<V, T> extends UnmodifiableIterator<T> {
        private final FingerTree<V, FingerTreeNode<V, T>> tree;

        private Iterator<FingerTreeNode<V, T>> node;

        private Iterator<T> inner;

        @Override
        public boolean hasNext() {
            if (inner == null)
                return !tree.isEmpty();
            return inner.hasNext() || node.hasNext();
        }

        @Override
        public T next() {
            if (node == null)
                node = tree.iterator();
            if (inner == null || !inner.hasNext())
                inner = node.next().iterator();
            return inner.next();
        }
    }

}
