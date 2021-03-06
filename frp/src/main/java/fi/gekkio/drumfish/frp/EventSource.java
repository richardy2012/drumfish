package fi.gekkio.drumfish.frp;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;

import fi.gekkio.drumfish.lang.Effect;

/**
 * An event stream implementation that also functions as an event sink.
 * 
 * This class is thread-safe.
 * 
 * @param <E>
 *            event type
 */
public class EventSource<E> extends EventStreamBase<E> implements EventSink<E> {

    private static final long serialVersionUID = -2046636095523847664L;

    private volatile ImmutableSet<Effect<? super E>> listeners = ImmutableSet.of();

    @Override
    public EventStream<E> foreach(final Effect<? super E> e, CancellationToken token) {
        Preconditions.checkNotNull(e, "effect cannot be null");
        Preconditions.checkNotNull(token, "token cannot be null");
        addListener(e);

        if (token.canBeCancelled()) {
            token.onCancel(new Runnable() {
                @Override
                public void run() {
                    removeListener(e);
                }
            });
        }

        if (token.isCancelled()) {
            removeListener(e);
        }

        return this;
    }

    void removeListener(Effect<? super E> e) {
        synchronized (this) {
            ImmutableSet.Builder<Effect<? super E>> b = ImmutableSet.builder();

            for (Effect<? super E> listener : listeners) {
                if (!Objects.equal(e, listener))
                    b.add(listener);
            }

            listeners = b.build();
        }
    }

    void addListener(Effect<? super E> e) {
        synchronized (this) {
            ImmutableSet.Builder<Effect<? super E>> b = ImmutableSet.builder();

            b.addAll(listeners).add(e);

            listeners = b.build();
        }
    }

    /**
     * Creates a new event source.
     * 
     * @return event source
     */
    public static <E> EventSource<E> create() {
        return new EventSource<E>();
    }

    @Override
    public void fire(E event) {
        for (Effect<? super E> listener : listeners) {
            listener.apply(event);
        }
    }

    /**
     * Checks if any listeners are registered
     * 
     * @return boolean
     */
    public boolean hasListeners() {
        return !listeners.isEmpty();
    }

}