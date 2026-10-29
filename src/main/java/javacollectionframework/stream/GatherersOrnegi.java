package javacollectionframework.stream;

import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Gatherer;
import java.util.stream.Gatherers;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static javacollectionframework.ortak.Konsol.baslik;
import static javacollectionframework.ortak.Konsol.bolum;
import static javacollectionframework.ortak.Konsol.yaz;

/**
 * <h2>Stream Gatherers (Java 24+, JEP 485)</h2>
 *
 * <p>{@code Collector}'lar uç işlemleri özelleştirmeye izin verirken, <b>ara işlemleri</b>
 * özelleştirmenin bir yolu yoktu. Java 24 ile gelen {@link Gatherer} arayüzü ve
 * {@code stream.gather(...)} metodu bu boşluğu doldurur.</p>
 *
 * <p>Hazır gatherer'lar ({@link Gatherers}):</p>
 * <ul>
 *     <li>{@code windowFixed(n)}: elemanları n'li sabit gruplara böler</li>
 *     <li>{@code windowSliding(n)}: n boyutlu kayan pencereler üretir</li>
 *     <li>{@code fold(baslangic, f)}: bütün stream'i tek değere indirger (sıralı reduce)</li>
 *     <li>{@code scan(baslangic, f)}: ara (kümülatif) sonuçların hepsini üretir</li>
 *     <li>{@code mapConcurrent(n, f)}: en fazla n eşzamanlı sanal thread ile map uygular,
 *         sırayı korur</li>
 * </ul>
 *
 * @author cemdirman
 */
public class GatherersOrnegi {

    record Siparis(String musteri, int tutar) {
    }

    public static void main(String[] args) {
        baslik("Stream Gatherers (Java 24+)");

        bolum("windowFixed: sayfalama / gruplama");
        List<List<Integer>> sayfalar = IntStream.rangeClosed(1, 10).boxed()
                .gather(Gatherers.windowFixed(3))
                .toList();
        yaz("1..10, 3'erli", sayfalar);

        bolum("windowSliding: hareketli ortalama");
        List<Integer> fiyatlar = List.of(100, 104, 98, 110, 115, 109, 120);
        List<Double> ortalama = fiyatlar.stream()
                .gather(Gatherers.windowSliding(3))
                .map(p -> Math.round(p.stream().mapToInt(Integer::intValue).average().orElseThrow() * 10) / 10.0)
                .toList();
        yaz("fiyatlar", fiyatlar);
        yaz("3 günlük hareketli ortalama", ortalama);

        bolum("scan: kümülatif toplam");
        List<Integer> gunlukSatis = List.of(5, 3, 8, 2, 7);
        List<Integer> kumulatif = gunlukSatis.stream()
                .gather(Gatherers.scan(() -> 0, Integer::sum))
                .toList();
        yaz("günlük satış", gunlukSatis);
        yaz("kümülatif (scan)", kumulatif);

        bolum("fold: tek değere indirgeme");
        String birlesik = Stream.of("j", "a", "v", "a")
                .gather(Gatherers.fold(() -> "", String::concat))
                .findFirst().orElseThrow();
        yaz("fold ile birleştirme", birlesik);

        bolum("mapConcurrent: sanal thread'lerle eşzamanlı map");
        long baslangic = System.nanoTime();
        List<String> sonuclar = IntStream.rangeClosed(1, 8).boxed()
                .gather(Gatherers.mapConcurrent(4, GatherersOrnegi::yavasServis))
                .toList();
        long sureMs = Duration.ofNanos(System.nanoTime() - baslangic).toMillis();
        yaz("sonuçlar (sıra korunur)", sonuclar);
        yaz("8 x 100ms iş, 4 eşzamanlı -> ~200ms", sureMs < 700 ? "evet (< 700ms)" : sureMs + "ms");

        bolum("Kendi gatherer'ımız: anahtara göre benzersiz (distinctBy)");
        List<Siparis> siparisler = List.of(
                new Siparis("cem", 120), new Siparis("ayşe", 80),
                new Siparis("cem", 300), new Siparis("can", 50), new Siparis("ayşe", 10));
        List<Siparis> ilkSiparisler = siparisler.stream()
                .gather(anahtaraGoreBenzersiz(Siparis::musteri))
                .toList();
        yaz("her müşterinin ilk siparişi", ilkSiparisler);

        bolum("Gatherer'ları zincirleme");
        List<Integer> sonuc = IntStream.rangeClosed(1, 12).boxed()
                .gather(Gatherers.windowFixed(4))
                .map(grup -> grup.stream().mapToInt(Integer::intValue).sum())
                .gather(Gatherers.scan(() -> 0, Integer::sum))
                .toList();
        yaz("4'lü grup toplamlarının kümülatifi", sonuc);
    }

    /**
     * Daha önce görülen anahtarları bir HashSet'te tutan sıralı (sequential) gatherer.
     * <ul>
     *     <li>initializer: her stream için yeni bir durum (state) oluşturur</li>
     *     <li>integrator: her eleman için çağrılır; {@code asagi.push} elemanı bir sonraki aşamaya
     *         gönderir. {@code ofGreedy}, gatherer'ın akışı kendisi kesmeyeceğini belirtir.</li>
     * </ul>
     */
    static <T, K> Gatherer<T, ?, T> anahtaraGoreBenzersiz(Function<? super T, ? extends K> anahtar) {
        return Gatherer.<T, HashSet<K>, T>ofSequential(
                HashSet::new,
                Gatherer.Integrator.ofGreedy((gorulenler, eleman, asagi) ->
                        !gorulenler.add(anahtar.apply(eleman)) || asagi.push(eleman)));
    }

    private static String yavasServis(int id) {
        try {
            Thread.sleep(100); // ağ çağrısını taklit eder
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return "yanıt-" + id;
    }
}
