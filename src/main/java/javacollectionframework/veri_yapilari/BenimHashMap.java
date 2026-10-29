package javacollectionframework.veri_yapilari;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * <h2>HashMap nasıl çalışır?</h2>
 *
 * <p>{@link java.util.HashMap}'in sadeleştirilmiş, eğitim amaçlı bir gerçekleştirimi
 * (<b>ayrı zincirleme</b> - separate chaining yöntemi).</p>
 *
 * <pre>
 *  kovalar (tablo)
 *  ┌───┐
 *  │ 0 │ ──► null
 *  ├───┤
 *  │ 1 │ ──► [elma=3] ──► [kivi=8] ──► null      (aynı kovaya düşen anahtarlar: çakışma)
 *  ├───┤
 *  │ 2 │ ──► [armut=5] ──► null
 *  ├───┤
 *  │...│
 *  └───┘
 * </pre>
 *
 * <ol>
 *     <li>{@code hash(anahtar)}: {@code hashCode()}'un üst bitleri alt bitlere karıştırılır
 *         ({@code h ^ (h >>> 16)}). Böylece yalnızca üst bitleri farklı olan anahtarlar da
 *         farklı kovalara dağılır.</li>
 *     <li>{@code indeks = hash & (kapasite - 1)}: kapasite 2'nin kuvveti olduğu için bu işlem
 *         {@code hash % kapasite} ile aynıdır ama çok daha hızlıdır.</li>
 *     <li>Kovadaki zincir dolaşılır, anahtar {@code equals} ile aranır.</li>
 *     <li>{@code boyut > kapasite * yukFaktoru} olunca tablo iki katına çıkarılır ve bütün
 *         girdiler yeni kovalarına dağıtılır (rehash).</li>
 * </ol>
 *
 * <p>Asıl HashMap ayrıca, bir kovadaki zincir 8 elemanı geçince onu kırmızı-siyah ağaca
 * dönüştürür (treeify). Bu sade sürümde o optimizasyon yoktur.</p>
 *
 * @param <K> anahtar tipi
 * @param <V> değer tipi
 * @author cemdirman
 */
public class BenimHashMap<K, V> {

    private static final int VARSAYILAN_KAPASITE = 16;
    private static final float YUK_FAKTORU = 0.75f;

    /** Zincirdeki tek bir anahtar-değer çifti. */
    private static final class Girdi<K, V> {
        final int hash;
        final K anahtar;
        V deger;
        Girdi<K, V> sonraki;

        Girdi(int hash, K anahtar, V deger, Girdi<K, V> sonraki) {
            this.hash = hash;
            this.anahtar = anahtar;
            this.deger = deger;
            this.sonraki = sonraki;
        }
    }

    private Girdi<K, V>[] tablo;
    private int boyut;

    public BenimHashMap() {
        tablo = yeniTablo(VARSAYILAN_KAPASITE);
    }

    static int hash(Object anahtar) {
        int h;
        return anahtar == null ? 0 : (h = anahtar.hashCode()) ^ (h >>> 16);
    }

    /** Ortalama O(1). Anahtar zaten varsa değeri günceller ve eski değeri döner. */
    public V put(K anahtar, V deger) {
        int h = hash(anahtar);
        int i = h & (tablo.length - 1);
        for (Girdi<K, V> g = tablo[i]; g != null; g = g.sonraki) {
            if (g.hash == h && Objects.equals(g.anahtar, anahtar)) {
                V eski = g.deger;
                g.deger = deger;
                return eski;
            }
        }
        tablo[i] = new Girdi<>(h, anahtar, deger, tablo[i]); // zincirin başına ekle
        if (++boyut > tablo.length * YUK_FAKTORU) {
            yenidenBoyutlandir();
        }
        return null;
    }

    /** Ortalama O(1). */
    public V get(Object anahtar) {
        Girdi<K, V> g = girdiBul(anahtar);
        return g == null ? null : g.deger;
    }

    public V getOrDefault(Object anahtar, V varsayilan) {
        Girdi<K, V> g = girdiBul(anahtar);
        return g == null ? varsayilan : g.deger;
    }

    public boolean containsKey(Object anahtar) {
        return girdiBul(anahtar) != null;
    }

    /** Ortalama O(1). */
    public V remove(Object anahtar) {
        int h = hash(anahtar);
        int i = h & (tablo.length - 1);
        Girdi<K, V> onceki = null;
        for (Girdi<K, V> g = tablo[i]; g != null; onceki = g, g = g.sonraki) {
            if (g.hash == h && Objects.equals(g.anahtar, anahtar)) {
                if (onceki == null) {
                    tablo[i] = g.sonraki;
                } else {
                    onceki.sonraki = g.sonraki;
                }
                boyut--;
                return g.deger;
            }
        }
        return null;
    }

    public int size() {
        return boyut;
    }

    public boolean isEmpty() {
        return boyut == 0;
    }

    /** Eğitim amaçlı: tablodaki kova sayısı. */
    public int kapasite() {
        return tablo.length;
    }

    public List<K> keys() {
        List<K> anahtarlar = new ArrayList<>(boyut);
        for (Girdi<K, V> kova : tablo) {
            for (Girdi<K, V> g = kova; g != null; g = g.sonraki) {
                anahtarlar.add(g.anahtar);
            }
        }
        return anahtarlar;
    }

    private Girdi<K, V> girdiBul(Object anahtar) {
        int h = hash(anahtar);
        for (Girdi<K, V> g = tablo[h & (tablo.length - 1)]; g != null; g = g.sonraki) {
            if (g.hash == h && Objects.equals(g.anahtar, anahtar)) {
                return g;
            }
        }
        return null;
    }

    /** Kapasiteyi ikiye katlar ve her girdiyi yeni indeksine taşır. O(n). */
    private void yenidenBoyutlandir() {
        Girdi<K, V>[] eski = tablo;
        tablo = yeniTablo(eski.length * 2);
        for (Girdi<K, V> kova : eski) {
            Girdi<K, V> g = kova;
            while (g != null) {
                Girdi<K, V> sonraki = g.sonraki;
                int i = g.hash & (tablo.length - 1);
                g.sonraki = tablo[i];
                tablo[i] = g;
                g = sonraki;
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static <K, V> Girdi<K, V>[] yeniTablo(int kapasite) {
        return (Girdi<K, V>[]) new Girdi<?, ?>[kapasite];
    }

    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner(", ", "{", "}");
        for (Girdi<K, V> kova : tablo) {
            for (Girdi<K, V> g = kova; g != null; g = g.sonraki) {
                sj.add(g.anahtar + "=" + g.deger);
            }
        }
        return sj.toString();
    }
}
