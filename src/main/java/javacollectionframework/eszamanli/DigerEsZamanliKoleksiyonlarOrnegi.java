package javacollectionframework.eszamanli;

import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executors;

import static javacollectionframework.ortak.Konsol.baslik;
import static javacollectionframework.ortak.Konsol.bolum;
import static javacollectionframework.ortak.Konsol.yaz;

/**
 * <h2>Diğer eşzamanlı (concurrent) koleksiyonlar</h2>
 *
 * <ul>
 *     <li>{@link CopyOnWriteArrayList} / {@link CopyOnWriteArraySet}: Her yazma işleminde iç dizinin
 *         <b>yeni bir kopyası</b> oluşturulur. Okumalar kilitsizdir, iterator anlık görüntü üzerinde
 *         çalışır. Okuma çok, yazma az olan durumlar (ör. dinleyici/listener listeleri) içindir.</li>
 *     <li>{@link ConcurrentSkipListMap} / {@link ConcurrentSkipListSet}: TreeMap / TreeSet'in
 *         thread-safe karşılığı. <b>Skip list</b> veri yapısı ile sıralı tutar, O(log n).</li>
 *     <li>{@link ConcurrentLinkedQueue}: CAS (compare-and-swap) tabanlı, <b>kilitsiz</b> ve sınırsız
 *         FIFO kuyruk. Bloklama gerekmeyen durumlar için.</li>
 * </ul>
 *
 * @author cemdirman
 */
public class DigerEsZamanliKoleksiyonlarOrnegi {

    interface Dinleyici {
        void olay(String mesaj);
    }

    public static void main(String[] args) {
        baslik("Diğer Eşzamanlı Koleksiyonlar");

        bolum("CopyOnWriteArrayList: dinleyici listesi");
        List<Dinleyici> dinleyiciler = new CopyOnWriteArrayList<>();
        var log = new StringBuilder();
        dinleyiciler.add(m -> log.append("[log] ").append(m).append(' '));
        dinleyiciler.add(new Dinleyici() {
            @Override
            public void olay(String m) {
                log.append("[tek-seferlik] ").append(m).append(' ');
                dinleyiciler.remove(this); // dolaşırken kendini silebilir, exception yok
            }
        });
        for (Dinleyici d : dinleyiciler) {
            d.olay("1");
        }
        for (Dinleyici d : dinleyiciler) {
            d.olay("2");
        }
        yaz("çıktı", log.toString().trim());
        yaz("kalan dinleyici sayısı", dinleyiciler.size());

        bolum("CopyOnWriteArraySet");
        Set<String> abonelikler = new CopyOnWriteArraySet<>(List.of("spor", "ekonomi", "spor"));
        yaz("ekleme sırası korunur, tekrar yok", abonelikler);

        bolum("ConcurrentSkipListMap: eşzamanlı sıralı map");
        NavigableMap<Integer, String> liderlik = new ConcurrentSkipListMap<>(java.util.Comparator.reverseOrder());
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            Map.of("Ayşe", 870, "Ali", 920, "Zeynep", 990, "Can", 640, "Elif", 905)
                    .forEach((oyuncu, puan) -> executor.submit(() -> liderlik.put(puan, oyuncu)));
        }
        yaz("liderlik tablosu (puan azalan)", liderlik);
        yaz("ilk 3", liderlik.headMap(900, true).values());
        yaz("900 puana en yakın üstteki", liderlik.lowerEntry(900));

        bolum("ConcurrentSkipListSet");
        Set<String> etiketler = new ConcurrentSkipListSet<>();
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (String e : List.of("java", "koleksiyon", "thread", "java", "stream", "map")) {
                executor.submit(() -> etiketler.add(e));
            }
        }
        yaz("sıralı ve benzersiz", etiketler);

        bolum("ConcurrentLinkedQueue: kilitsiz kuyruk");
        Queue<Integer> isler = new ConcurrentLinkedQueue<>();
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < 1_000; i++) {
                int no = i;
                executor.submit(() -> isler.offer(no));
            }
        }
        yaz("size() (O(n) dikkat!)", isler.size());
        long toplam = 0;
        Integer is;
        while ((is = isler.poll()) != null) {
            toplam += is;
        }
        yaz("tüm işlerin toplamı (0..999)", toplam);
    }
}
