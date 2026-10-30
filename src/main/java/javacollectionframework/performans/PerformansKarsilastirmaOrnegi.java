package javacollectionframework.performans;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Supplier;

import static javacollectionframework.ortak.Konsol.baslik;
import static javacollectionframework.ortak.Konsol.bolum;

/**
 * <h2>Koleksiyonların performans karşılaştırması</h2>
 *
 * <p>Dokümanlardaki Big-O tablolarını gerçek ölçümlerle gözlemlemek için basit bir karşılaştırma.
 * Her senaryo birkaç kez ısınma (JIT derleyicinin kodu optimize etmesi için) turundan sonra
 * ölçülür ve en iyi süre raporlanır.</p>
 *
 * <p><b>Uyarı:</b> Bu bir mikro-benchmark aracı değildir; sonuçlar makineye, JVM'e ve o anki
 * yüke göre değişir. Ciddi ölçümler için <a href="https://openjdk.org/projects/code-tools/jmh/">JMH</a>
 * kullanın. Burada amaç mutlak sayılar değil, <b>büyüklük farklarını</b> görmektir.</p>
 *
 * @author cemdirman
 */
public class PerformansKarsilastirmaOrnegi {

    private static final int ISINMA = 3;
    private static final int OLCUM = 5;

    /** JIT'in sonucu kullanılmayan hesapları silmesini önlemek için sonuçlar buraya eklenir. */
    private static long yutucu;

    public static void main(String[] args) {
        baslik("Performans Karşılaştırması");
        System.out.println("(süreler milisaniye, düşük olan daha iyi; " + OLCUM + " ölçümün en iyisi)");

        int n = 20_000;
        int[] rastgeleIndeksler = new Random(1907).ints(n, 0, n).toArray();

        bolum("Rastgele indeksle erişim: get(i)  [ArrayList O(1), LinkedList O(n)]");
        List<Integer> arrayList = new ArrayList<>(araliktanListe(n));
        List<Integer> linkedList = new LinkedList<>(araliktanListe(n));
        olc("ArrayList.get", () -> topla(arrayList, rastgeleIndeksler));
        olc("LinkedList.get", () -> topla(linkedList, rastgeleIndeksler));

        bolum("Başa ekleme: add(0, e)  [ArrayList O(n), LinkedList O(1)]");
        olc("ArrayList.add(0, e)", () -> basaEkle(new ArrayList<>(), n));
        olc("LinkedList.add(0, e)", () -> basaEkle(new LinkedList<>(), n));

        bolum("Arama: contains  [List O(n), HashSet O(1), TreeSet O(log n)]");
        List<Integer> liste = araliktanListe(n);
        Set<Integer> hashSet = new HashSet<>(liste);
        Set<Integer> treeSet = new TreeSet<>(liste);
        olc("ArrayList.contains", () -> ara(liste, rastgeleIndeksler));
        olc("HashSet.contains", () -> ara(hashSet, rastgeleIndeksler));
        olc("TreeSet.contains", () -> ara(treeSet, rastgeleIndeksler));

        bolum("Kuyruk: offer + poll  [ikisi de O(1), ArrayDeque daha az bellek ve nesne]");
        int kuyrukBoyutu = 1_000_000;
        olc("ArrayDeque", () -> kuyrukCalistir(new ArrayDeque<>(), kuyrukBoyutu));
        olc("LinkedList", () -> kuyrukCalistir(new LinkedList<>(), kuyrukBoyutu));

        bolum("Kapasite önceden verilirse  [yeniden boyutlandırma maliyeti]");
        int buyuk = 2_000_000;
        olc("new ArrayList<>()", () -> doldur(ArrayList::new, buyuk));
        olc("new ArrayList<>(n)", () -> doldur(() -> new ArrayList<>(buyuk), buyuk));

        if (yutucu == 42) {
            System.out.println(); // yutucu'nun okunduğunu derleyiciye göstermek için
        }
    }

    private static void olc(String etiket, Runnable is) {
        for (int i = 0; i < ISINMA; i++) {
            is.run();
        }
        long enIyi = Long.MAX_VALUE;
        for (int i = 0; i < OLCUM; i++) {
            long baslangic = System.nanoTime();
            is.run();
            enIyi = Math.min(enIyi, System.nanoTime() - baslangic);
        }
        System.out.printf("%-38s: %10.3f ms%n", etiket, enIyi / 1_000_000.0);
    }

    private static List<Integer> araliktanListe(int n) {
        List<Integer> liste = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            liste.add(i);
        }
        return liste;
    }

    private static void topla(List<Integer> liste, int[] indeksler) {
        long toplam = 0;
        for (int i : indeksler) {
            toplam += liste.get(i);
        }
        yutucu += toplam;
    }

    private static void basaEkle(List<Integer> liste, int n) {
        for (int i = 0; i < n; i++) {
            liste.add(0, i);
        }
        yutucu += liste.size();
    }

    private static void ara(java.util.Collection<Integer> koleksiyon, int[] degerler) {
        int bulunan = 0;
        for (int d : degerler) {
            if (koleksiyon.contains(d)) {
                bulunan++;
            }
        }
        yutucu += bulunan;
    }

    private static void kuyrukCalistir(Queue<Integer> kuyruk, int n) {
        for (int i = 0; i < n; i++) {
            kuyruk.offer(i);
        }
        long toplam = 0;
        Integer e;
        while ((e = kuyruk.poll()) != null) {
            toplam += e;
        }
        yutucu += toplam;
    }

    private static void doldur(Supplier<List<Integer>> uretici, int n) {
        List<Integer> liste = uretici.get();
        for (int i = 0; i < n; i++) {
            liste.add(i);
        }
        yutucu += liste.size();
    }
}
