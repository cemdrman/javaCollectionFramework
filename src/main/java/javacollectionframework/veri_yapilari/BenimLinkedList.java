package javacollectionframework.veri_yapilari;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * <h2>LinkedList nasıl çalışır?</h2>
 *
 * <p>{@link java.util.LinkedList}'in sadeleştirilmiş, eğitim amaçlı <b>çift yönlü bağlı liste</b>
 * gerçekleştirimi.</p>
 *
 * <pre>
 *   bas                                     son
 *    │                                       │
 *    ▼                                       ▼
 *  ┌──────┐  sonraki  ┌──────┐  sonraki  ┌──────┐
 *  │  A   │ ────────► │  B   │ ────────► │  C   │ ──► null
 *  │      │ ◄──────── │      │ ◄──────── │      │
 *  └──────┘  onceki   └──────┘  onceki   └──────┘
 * </pre>
 *
 * <ul>
 *     <li>Baş ve sona ekleme/silme yalnızca birkaç referans değiştirir: <b>O(1)</b>.</li>
 *     <li>İndeks ile erişimde düğümler tek tek dolaşılır: <b>O(n)</b>. Asıl LinkedList gibi,
 *         indeks listenin ikinci yarısındaysa dolaşmaya sondan başlanır.</li>
 * </ul>
 *
 * @param <E> eleman tipi
 * @author cemdirman
 */
public class BenimLinkedList<E> implements Iterable<E> {

    private static final class Dugum<E> {
        E deger;
        Dugum<E> onceki;
        Dugum<E> sonraki;

        Dugum(Dugum<E> onceki, E deger, Dugum<E> sonraki) {
            this.onceki = onceki;
            this.deger = deger;
            this.sonraki = sonraki;
        }
    }

    private Dugum<E> bas;
    private Dugum<E> son;
    private int boyut;

    /** O(1) */
    public void addFirst(E eleman) {
        Dugum<E> eskiBas = bas;
        Dugum<E> yeni = new Dugum<>(null, eleman, eskiBas);
        bas = yeni;
        if (eskiBas == null) {
            son = yeni;           // liste boştu
        } else {
            eskiBas.onceki = yeni;
        }
        boyut++;
    }

    /** O(1) */
    public void addLast(E eleman) {
        Dugum<E> eskiSon = son;
        Dugum<E> yeni = new Dugum<>(eskiSon, eleman, null);
        son = yeni;
        if (eskiSon == null) {
            bas = yeni;           // liste boştu
        } else {
            eskiSon.sonraki = yeni;
        }
        boyut++;
    }

    public void add(E eleman) {
        addLast(eleman);
    }

    /** O(n) - önce düğüm bulunur, sonra O(1) ile araya eklenir. */
    public void add(int indeks, E eleman) {
        Objects.checkIndex(indeks, boyut + 1);
        if (indeks == boyut) {
            addLast(eleman);
        } else if (indeks == 0) {
            addFirst(eleman);
        } else {
            Dugum<E> sagdaki = dugum(indeks);
            Dugum<E> soldaki = sagdaki.onceki;
            Dugum<E> yeni = new Dugum<>(soldaki, eleman, sagdaki);
            soldaki.sonraki = yeni;
            sagdaki.onceki = yeni;
            boyut++;
        }
    }

    /** O(n) */
    public E get(int indeks) {
        Objects.checkIndex(indeks, boyut);
        return dugum(indeks).deger;
    }

    public E getFirst() {
        if (bas == null) {
            throw new NoSuchElementException();
        }
        return bas.deger;
    }

    public E getLast() {
        if (son == null) {
            throw new NoSuchElementException();
        }
        return son.deger;
    }

    /** O(1) */
    public E removeFirst() {
        if (bas == null) {
            throw new NoSuchElementException();
        }
        return bagiKopar(bas);
    }

    /** O(1) */
    public E removeLast() {
        if (son == null) {
            throw new NoSuchElementException();
        }
        return bagiKopar(son);
    }

    /** O(n) */
    public E remove(int indeks) {
        Objects.checkIndex(indeks, boyut);
        return bagiKopar(dugum(indeks));
    }

    public boolean contains(Object o) {
        for (Dugum<E> d = bas; d != null; d = d.sonraki) {
            if (Objects.equals(o, d.deger)) {
                return true;
            }
        }
        return false;
    }

    public int size() {
        return boyut;
    }

    public boolean isEmpty() {
        return boyut == 0;
    }

    /** Listeyi yerinde ters çevirir: her düğümün önceki/sonraki referansları yer değiştirir. O(n). */
    public void tersCevir() {
        Dugum<E> d = bas;
        while (d != null) {
            Dugum<E> sonraki = d.sonraki;
            d.sonraki = d.onceki;
            d.onceki = sonraki;
            d = sonraki;
        }
        Dugum<E> eskiBas = bas;
        bas = son;
        son = eskiBas;
    }

    /** İndeks yakın olan uçtan başlayarak düğümü bulur. */
    private Dugum<E> dugum(int indeks) {
        if (indeks < (boyut >> 1)) {
            Dugum<E> d = bas;
            for (int i = 0; i < indeks; i++) {
                d = d.sonraki;
            }
            return d;
        }
        Dugum<E> d = son;
        for (int i = boyut - 1; i > indeks; i--) {
            d = d.onceki;
        }
        return d;
    }

    /** Düğümü komşularından ayırır ve komşuları birbirine bağlar. O(1). */
    private E bagiKopar(Dugum<E> d) {
        Dugum<E> onceki = d.onceki;
        Dugum<E> sonraki = d.sonraki;
        if (onceki == null) {
            bas = sonraki;
        } else {
            onceki.sonraki = sonraki;
        }
        if (sonraki == null) {
            son = onceki;
        } else {
            sonraki.onceki = onceki;
        }
        E deger = d.deger;
        d.deger = null;
        d.onceki = null;
        d.sonraki = null;
        boyut--;
        return deger;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<>() {
            private Dugum<E> siradaki = bas;

            @Override
            public boolean hasNext() {
                return siradaki != null;
            }

            @Override
            public E next() {
                if (siradaki == null) {
                    throw new NoSuchElementException();
                }
                E deger = siradaki.deger;
                siradaki = siradaki.sonraki;
                return deger;
            }
        };
    }

    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner(", ", "[", "]");
        for (E e : this) {
            sj.add(String.valueOf(e));
        }
        return sj.toString();
    }
}
