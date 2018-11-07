package ch.beerpro.domain.utils;

import android.util.Pair;

import org.apache.commons.lang3.tuple.Triple;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

public class LiveDataExtensions {

    public static <A, B> LiveData<Pair<A, B>> zip(LiveData<A> as, LiveData<B> bs) {
        return new MediatorLiveData<Pair<A, B>>() {

            A lastA = null;
            B lastB = null;

            {
                {
                    addSource(as, (A a) -> {
                        lastA = a;
                        update();
                    });
                    addSource(bs, (B b) -> {
                        lastB = b;
                        update();
                    });
                }
            }

            private void update() {
                this.setValue(new Pair<>(lastA, lastB));
            }
        };
    }

    public static <A, B> LiveData<Pair<A, B>> combineLatest(LiveData<A> as, LiveData<B> bs) {
        return new MediatorLiveData<Pair<A, B>>() {

            A lastA = null;
            B lastB = null;

            {
                {
                    addSource(as, (A a) -> {
                        lastA = a;
                        update();
                    });
                    addSource(bs, (B b) -> {
                        lastB = b;
                        update();
                    });
                }
            }

            private void update() {
                if (lastA != null && lastB != null) {
                    this.setValue(new Pair<>(lastA, lastB));
                }
            }
        };
    }

    public static <A, B, C> LiveData<Triple<A, B, C>> combineLatest(LiveData<A> as, LiveData<B> bs, LiveData<C> cs) {
        return new MediatorLiveData<Triple<A, B, C>>() {

            A lastA = null;
            B lastB = null;
            C lastC = null;

            {
                {
                    addSource(as, (A a) -> {
                        lastA = a;
                        update();
                    });
                    addSource(bs, (B b) -> {
                        lastB = b;
                        update();
                    });
                    addSource(cs, (C c) -> {
                        lastC = c;
                        update();
                    });
                }
            }

            private void update() {
                if (lastA != null && lastB != null && lastC != null) {
                    this.setValue(Triple.of(lastA, lastB, lastC));
                }
            }
        };
    }

    public static <A, B, C, D, E> LiveData<Quintuple<A, B, C, D, E>> combineLatest(LiveData<A> as, LiveData<B> bs,
                                                                                   LiveData<C> cs, LiveData<D> ds,
                                                                                   LiveData<E> es) {
        return new MediatorLiveData<Quintuple<A, B, C, D, E>>() {

            A lastA = null;
            B lastB = null;
            C lastC = null;
            D lastD = null;
            E lastE = null;

            {
                {
                    addSource(as, (A a) -> {
                        lastA = a;
                        update();
                    });
                    addSource(bs, (B b) -> {
                        lastB = b;
                        update();
                    });
                    addSource(cs, (C c) -> {
                        lastC = c;
                        update();
                    });
                    addSource(ds, (D d) -> {
                        lastD = d;
                        update();
                    });
                    addSource(es, (E e) -> {
                        lastE = e;
                        update();
                    });
                }
            }

            private void update() {
                if (lastA != null && lastB != null && lastC != null && lastD != null && lastE != null) {
                    this.setValue(new Quintuple<>(lastA, lastB, lastC, lastD, lastE));
                }
            }
        };
    }
}
