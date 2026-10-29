package javacollectionframework.eszamanli;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.LongAdder;

import static javacollectionframework.ortak.Konsol.baslik;
import static javacollectionframework.ortak.Konsol.bolum;
import static javacollectionframework.ortak.Konsol.yaz;

/**
 * <h2>ConcurrentHashMap</h2>
 *
 * <p>{@link ConcurrentHashMap}, çok thread'li ortamlar için tasarlanmış, yüksek performanslı
 * hash tablosudur.</p>
 *
 * <ul>
 *     <li>Okumalar kilitsizdir; yazmalar yalnızca ilgili kovayı (bucket) kilitler. Böylece
 *         {@code Hashtable} veya {@code Collections.synchronizedMap}'teki gibi bütün tabloyu
 *         kilitleyen tek bir kilit yoktur.</li>
 *     <li>{@code merge}, {@code compute}, {@code computeIfAbsent}, {@code putIfAbsent} gibi
 *         metotlar <b>atomik</b>tir.</li>
 *     <li>{@code null} anahtar ve değer kabul etmez (get'in null dönmesi belirsizlik yaratırdı).</li>
 *     <li>Iterator'ları <b>weakly consistent</b>'tir; {@code ConcurrentModificationException}
 *         fırlatmaz.</li>
 *     <li>{@code forEach}, {@code search}, {@code reduce} gibi paralel toplu (bulk) işlemleri vardır.</li>
 * </ul>
 *
 * @author cemdirman
 */
public class ConcurrentHashMapOrnegi {

    private static final int THREAD_SAYISI = 1_000;
    private static final int ARTIRMA_SAYISI = 100;

    public static void main(String[] args) {
        baslik("ConcurrentHashMap Örneği");

        bolum("HashMap thread-safe değildir");
        Map<String, Integer> hashMap = new HashMap<>();
        Map<String, Integer> concurrent = new ConcurrentHashMap<>();
        say(hashMap);
        say(concurrent);
        int beklenen = THREAD_SAYISI * ARTIRMA_SAYISI;
        yaz("beklenen", beklenen);
        yaz("HashMap (yarış durumu, genelde eksik)", hashMap.get("sayac"));
        yaz("ConcurrentHashMap (her zaman doğru)", concurrent.get("sayac"));

        bolum("Atomik metotlar");
        ConcurrentMap<String, Integer> stok = new ConcurrentHashMap<>();
        stok.putIfAbsent("kalem", 10);
        stok.merge("kalem", 5, Integer::sum);
        stok.computeIfPresent("kalem", (k, v) -> v - 3);
        // replace(k, beklenenEski, yeni): yalnızca değer hâlâ 'beklenenEski' ise günceller (CAS)
        yaz("replace(kalem, 12, 20)", stok.replace("kalem", 12, 20));
        yaz("replace(kalem, 12, 30) (artık 20)", stok.replace("kalem", 12, 30));
        yaz("stok", stok);

        bolum("Yüksek çekişmede sayaç: LongAdder");
        // Değer olarak LongAdder kullanmak, Integer'ı her seferinde yeniden oluşturmaktan daha
        // hızlıdır; çok thread aynı anda artırırken çekişmeyi hücrelere dağıtır.
        ConcurrentHashMap<String, LongAdder> ziyaret = new ConcurrentHashMap<>();
        String[] sayfalar = {"/anasayfa", "/urunler", "/anasayfa", "/sepet", "/anasayfa", "/urunler"};
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) { // Java 21+ sanal thread
            for (int i = 0; i < 1_000; i++) {
                for (String sayfa : sayfalar) {
                    executor.submit(() -> ziyaret.computeIfAbsent(sayfa, k -> new LongAdder()).increment());
                }
            }
        } // try-with-resources: bütün görevlerin bitmesini bekler
        ziyaret.forEach((sayfa, adet) -> yaz("  " + sayfa, adet.sum()));

        bolum("Paralel toplu işlemler (bulk operations)");
        ConcurrentHashMap<String, Integer> nufus = new ConcurrentHashMap<>(Map.of(
                "İstanbul", 15_655_924, "Ankara", 5_803_482, "İzmir", 4_479_525, "Bursa", 3_214_571));
        // parallelismThreshold: eleman sayısı bu değerin üstündeyse paralel çalışır
        long toplam = nufus.reduceValuesToLong(1, Integer::longValue, 0L, Long::sum);
        String bes = nufus.search(1, (sehir, n) -> n > 5_000_000 && !sehir.equals("İstanbul") ? sehir : null);
        yaz("reduceValuesToLong (toplam)", toplam);
        yaz("search (> 5M, İstanbul hariç)", bes);
        yaz("mappingCount()", nufus.mappingCount());

        bolum("Eşzamanlı Set");
        var set = ConcurrentHashMap.<String>newKeySet();
        set.add("a");
        set.add("a");
        yaz("ConcurrentHashMap.newKeySet()", set);

        bolum("null kabul edilmez");
        try {
            nufus.put("Sinop", null);
        } catch (NullPointerException e) {
            yaz("put(k, null)", "NullPointerException");
        }
    }

    /** 1000 sanal thread'in her biri sayacı 100 kez artırır. */
    private static void say(Map<String, Integer> map) {
        map.put("sayac", 0);
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int t = 0; t < THREAD_SAYISI; t++) {
                executor.submit(() -> {
                    for (int i = 0; i < ARTIRMA_SAYISI; i++) {
                        try {
                            map.merge("sayac", 1, Integer::sum);
                        } catch (RuntimeException e) {
                            // HashMap eşzamanlı değiştirildiğinde iç yapısı bozulabilir
                        }
                        if (i % 10 == 0) {
                            Thread.yield(); // thread'lerin araya girmesini kolaylaştırır
                        }
                    }
                });
            }
        }
    }
}
