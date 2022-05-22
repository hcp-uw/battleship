package utils;

/**
 * Immutable storage class to relate three things
 * @param <A> type of first thing
 * @param <B> type of second thing
 * @param <C> types of third thing
 */
public class Triple<A, B, C> {

    private final A fst;
    private final B snd;
    private final C thd;

    public Triple(A first, B second, C third) {
        fst = first;
        snd = second;
        thd = third;
    }

    public A getFirst() {
        return fst;
    }

    public B getSecond() {
        return snd;
    }

    public C getThird() {
        return thd;
    }
}
