package javacollectionframework.veri_yapilari;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * <h2>ArrayList nasıl çalışır?</h2>
 *
 * <p>{@link java.util.ArrayList}'in sadeleştirilmiş, eğitim amaçlı bir gerçekleştirimi.
 * Asıl sınıf ile aynı temel fikirleri kullanır:</p>
 * <ul>
 *     <li>Elemanlar bir {@code Object[]} dizisinde tutulur.</li>
 *     <li>Dizi dolunca kapasite <b>%50 artırılır</b> ve elemanlar yeni diziye kopyalanır.
 *         Kopyalama O(n) olsa da seyrek yapıldığı için sona ekleme <b>amortize O(1)</b>'dir.</li>
 *     <li>Araya ekleme ve silmede {@link System#arraycopy} ile elemanlar kaydırılır: O(n).</li>
 *     <li>{@code modSayaci} sayesinde iterator <b>fail-fast</b> davranır.</li>
 * </ul>
 *
 * @param <E> eleman tipi
 * @author cemdirman
 */
public class BenimArrayList<E> implements Iterable<E> {

    private static final int VARSAYILAN_KAPASITE = 10;

    private Object[] elemanlar;
    private int boyut;
    private int modSayaci; // yapısal değişiklik sayacı (fail-fast iterator için)

    public BenimArrayList() {
        this(VARSAYILAN_KAPASITE);
    }

    public BenimArrayList(int baslangicKapasitesi) {
        if (baslangicKapasitesi < 0) {
            throw new IllegalArgumentException("Kapasite negatif olamaz: " + baslangicKapasitesi);
        }
        elemanlar = new Object[baslangicKapasitesi];
    }

    /** Sona ekler. Amortize O(1). */
    public void add(E eleman) {
        kapasiteyiGaranticle(boyut + 1);
        elemanlar[boyut++] = eleman;
        modSayaci++;
    }

    /** Verilen indekse ekler, sağdaki elemanları kaydırır. O(n). */
    public void add(int indeks, E eleman) {
        Objects.checkIndex(indeks, boyut + 1);
        kapasiteyiGaranticle(boyut + 1);
        System.arraycopy(elemanlar, indeks, elemanlar, indeks + 1, boyut - indeks);
        elemanlar[indeks] = eleman;
        boyut++;
        modSayaci++;
    }

    /** O(1) */
    @SuppressWarnings("unchecked")
    public E get(int indeks) {
        Objects.checkIndex(indeks, boyut);
        return (E) elemanlar[indeks];
    }

    /** O(1). Eski değeri döner. */
    public E set(int indeks, E eleman) {
        E eski = get(indeks);
        elemanlar[indeks] = eleman;
        return eski;
    }

    /** İndeksteki elemanı siler, sağdakileri sola kaydırır. O(n). */
    public E remove(int indeks) {
        E silinen = get(indeks);
        int tasinacak = boyut - indeks - 1;
        if (tasinacak > 0) {
            System.arraycopy(elemanlar, indeks + 1, elemanlar, indeks, tasinacak);
        }
        elemanlar[--boyut] = null; // referansı bırak: bellek sızıntısını (memory leak) önler
        modSayaci++;
        return silinen;
    }

    /** {@code equals} ile ilk eşleşen elemanı siler. O(n). */
    public boolean remove(Object o) {
        int indeks = indexOf(o);
        if (indeks < 0) {
            return false;
        }
        remove(indeks);
        return true;
    }

    /** O(n) doğrusal arama. */
    public int indexOf(Object o) {
        for (int i = 0; i < boyut; i++) {
            if (Objects.equals(o, elemanlar[i])) {
                return i;
            }
        }
        return -1;
    }

    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    public int size() {
        return boyut;
    }

    public boolean isEmpty() {
        return boyut == 0;
    }

    /** Eğitim amaçlı: iç dizinin o anki uzunluğu. */
    public int kapasite() {
        return elemanlar.length;
    }

    public void clear() {
        Arrays.fill(elemanlar, 0, boyut, null);
        boyut = 0;
        modSayaci++;
    }

    private void kapasiteyiGaranticle(int gerekenKapasite) {
        if (gerekenKapasite > elemanlar.length) {
            int yeniKapasite = Math.max(gerekenKapasite, elemanlar.length + (elemanlar.length >> 1)); // x1.5
            elemanlar = Arrays.copyOf(elemanlar, yeniKapasite);
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<>() {
            private int imlec;
            private int sonDonen = -1;
            private int beklenenModSayaci = modSayaci;

            @Override
            public boolean hasNext() {
                return imlec < boyut;
            }

            @Override
            public E next() {
                degisiklikKontrolu();
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                sonDonen = imlec++;
                return get(sonDonen);
            }

            @Override
            public void remove() {
                if (sonDonen < 0) {
                    throw new IllegalStateException("Önce next() çağrılmalı");
                }
                degisiklikKontrolu();
                BenimArrayList.this.remove(sonDonen);
                imlec = sonDonen;
                sonDonen = -1;
                beklenenModSayaci = modSayaci; // kendi değişikliğimiz, hata değil
            }

            private void degisiklikKontrolu() {
                if (modSayaci != beklenenModSayaci) {
                    throw new ConcurrentModificationException();
                }
            }
        };
    }

    @Override
    public String toString() {
        return Arrays.toString(Arrays.copyOf(elemanlar, boyut));
    }
}
