package javacollectionframework.collections.queue;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import static javacollectionframework.ortak.Konsol.baslik;
import static javacollectionframework.ortak.Konsol.bolum;
import static javacollectionframework.ortak.Konsol.yaz;

/**
 * <h2>PriorityQueue</h2>
 *
 * <p>{@link PriorityQueue}, elemanları ekleme sırasına göre değil <b>önceliğe</b> göre çıkaran
 * kuyruktur. Arka planda dizi üzerinde tutulan bir <b>ikili yığın</b> (binary heap) kullanır.</p>
 *
 * <ul>
 *     <li>Varsayılan olarak <b>min-heap</b>'tir: {@code poll()} her zaman en küçük elemanı verir.</li>
 *     <li>{@code offer / poll}: <b>O(log n)</b>, {@code peek}: <b>O(1)</b>,
 *         {@code contains / remove(Object)}: <b>O(n)</b></li>
 *     <li>{@code toString()} veya iterator ile dolaşma <b>sıralı değildir</b>; yalnızca baştaki
 *         elemanın en öncelikli olduğu garanti edilir.</li>
 *     <li>{@code null} kabul etmez, thread-safe değildir
 *         (çok thread için {@link java.util.concurrent.PriorityBlockingQueue}).</li>
 * </ul>
 *
 * @author cemdirman
 */
public class PriorityQueueOrnegi {

    record Gorev(String ad, int oncelik) {
    }

    record Hasta(String ad, int aciliyet, int gelisSirasi) {
    }

    public static void main(String[] args) {
        baslik("PriorityQueue Örneği");

        bolum("Min-heap (varsayılan)");
        PriorityQueue<Integer> min = new PriorityQueue<>(List.of(42, 7, 19, 3, 25, 11));
        yaz("toString() (sıralı DEĞİL!)", min);
        yaz("peek()", min.peek());
        yaz("poll() sırası", tumunuCek(min));

        bolum("Max-heap");
        Queue<Integer> max = new PriorityQueue<>(Comparator.reverseOrder());
        max.addAll(List.of(42, 7, 19, 3, 25, 11));
        yaz("poll() sırası", tumunuCek(max));

        bolum("Nesnelerle öncelik");
        Queue<Gorev> gorevler = new PriorityQueue<>(Comparator.comparingInt(Gorev::oncelik));
        gorevler.offer(new Gorev("e-posta yanıtla", 3));
        gorevler.offer(new Gorev("canlı hata düzelt", 1));
        gorevler.offer(new Gorev("kod incelemesi", 2));
        gorevler.offer(new Gorev("dokümantasyon", 5));
        while (!gorevler.isEmpty()) {
            Gorev g = gorevler.poll();
            yaz("öncelik " + g.oncelik(), g.ad());
        }

        bolum("Birden fazla kritere göre: acil servis");
        // Önce aciliyet (büyük olan önce), eşitse önce gelen önce.
        Queue<Hasta> acil = new PriorityQueue<>(
                Comparator.comparingInt(Hasta::aciliyet).reversed()
                        .thenComparingInt(Hasta::gelisSirasi));
        acil.offer(new Hasta("Ayşe", 2, 1));
        acil.offer(new Hasta("Mehmet", 5, 2));
        acil.offer(new Hasta("Zeynep", 2, 3));
        acil.offer(new Hasta("Ali", 5, 4));
        var sira = new ArrayList<String>();
        while (!acil.isEmpty()) {
            sira.add(acil.poll().ad());
        }
        yaz("muayene sırası", sira);

        bolum("Uygulama: en büyük k eleman (boyutu k olan min-heap)");
        int[] skorlar = {88, 12, 95, 47, 73, 99, 61, 84, 30};
        yaz("en büyük 3 skor", enBuyukK(skorlar, 3));
    }

    private static <T> List<T> tumunuCek(Queue<T> kuyruk) {
        List<T> sonuc = new ArrayList<>();
        while (!kuyruk.isEmpty()) {
            sonuc.add(kuyruk.poll());
        }
        return sonuc;
    }

    /**
     * Bütün diziyi sıralamak O(n log n) iken, k boyutlu bir min-heap ile O(n log k)'da çözülür.
     * Heap'in başında her zaman "şimdiye kadarki en büyük k elemanın en küçüğü" durur.
     */
    static List<Integer> enBuyukK(int[] dizi, int k) {
        PriorityQueue<Integer> heap = new PriorityQueue<>(k);
        for (int sayi : dizi) {
            heap.offer(sayi);
            if (heap.size() > k) {
                heap.poll();
            }
        }
        List<Integer> sonuc = new ArrayList<>(heap);
        sonuc.sort(Comparator.reverseOrder());
        return sonuc;
    }
}
