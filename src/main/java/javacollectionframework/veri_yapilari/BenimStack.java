package javacollectionframework.veri_yapilari;

import java.util.Arrays;
import java.util.EmptyStackException;

/**
 * <h2>Stack (yığın) nasıl çalışır?</h2>
 *
 * <p>Dizi tabanlı, eğitim amaçlı bir <b>LIFO</b> yığın. Tepe elemanın indeksi {@code boyut - 1}'dir;
 * bütün işlemler dizinin sonunda yapıldığı için {@code push}, {@code pop} ve {@code peek}
 * <b>O(1)</b>'dir ({@code push} amortize).</p>
 *
 * <pre>
 *   push(3)        pop() -> 3
 *   ┌───┐           ┌───┐
 *   │ 3 │ ◄─ tepe   │   │
 *   │ 2 │           │ 2 │ ◄─ tepe
 *   │ 1 │           │ 1 │
 *   └───┘           └───┘
 * </pre>
 *
 * @param <E> eleman tipi
 * @author cemdirman
 */
public class BenimStack<E> {

    private Object[] elemanlar = new Object[8];
    private int boyut;

    public void push(E eleman) {
        if (boyut == elemanlar.length) {
            elemanlar = Arrays.copyOf(elemanlar, elemanlar.length * 2);
        }
        elemanlar[boyut++] = eleman;
    }

    public E pop() {
        E tepe = peek();
        elemanlar[--boyut] = null;
        return tepe;
    }

    @SuppressWarnings("unchecked")
    public E peek() {
        if (boyut == 0) {
            throw new EmptyStackException();
        }
        return (E) elemanlar[boyut - 1];
    }

    public boolean isEmpty() {
        return boyut == 0;
    }

    public int size() {
        return boyut;
    }
}
