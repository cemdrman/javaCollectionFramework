package javacollectionframework.collections.map;

import java.time.LocalTime;
import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import static javacollectionframework.ortak.Konsol.baslik;
import static javacollectionframework.ortak.Konsol.bolum;
import static javacollectionframework.ortak.Konsol.yaz;

/**
 * <h2>TreeMap</h2>
 *
 * <p>{@link TreeMap}, anahtarları <b>sıralı</b> tutan Map gerçekleştirimidir. Arka planda
 * <b>kırmızı-siyah ağaç</b> (kendini dengeleyen ikili arama ağacı) kullanır.</p>
 *
 * <ul>
 *     <li>{@code get / put / remove / containsKey}: <b>O(log n)</b></li>
 *     <li>Anahtarlar doğal sırasına ({@link Comparable}) ya da verilen {@link Comparator}'a göre
 *         sıralanır.</li>
 *     <li>{@code null} anahtar kabul etmez (değerler null olabilir).</li>
 *     <li>{@link NavigableMap} ile aralık ve en yakın anahtar sorguları yapılabilir.</li>
 * </ul>
 *
 * @author cemdirman
 */
public class TreeMapOrnegi {

    public static void main(String[] args) {
        baslik("TreeMap Örneği");

        bolum("Anahtarlar her zaman sıralı");
        Map<String, Integer> nufus = new TreeMap<>();
        nufus.put("İzmir", 4_479_525);
        nufus.put("Ankara", 5_803_482);
        nufus.put("Bursa", 3_214_571);
        nufus.put("Antalya", 2_696_249);
        yaz("doğal sıralama", nufus.keySet());

        Map<String, Integer> tersten = new TreeMap<>(Comparator.reverseOrder());
        tersten.putAll(nufus);
        yaz("reverseOrder", tersten.keySet());

        bolum("NavigableMap: not -> harf notu dönüşümü");
        NavigableMap<Integer, String> harfNotu = new TreeMap<>();
        harfNotu.put(0, "FF");
        harfNotu.put(50, "DD");
        harfNotu.put(60, "CC");
        harfNotu.put(70, "BB");
        harfNotu.put(85, "AA");
        for (int not : new int[]{42, 50, 67, 84, 100}) {
            // floorEntry: verilen anahtara eşit ya da ondan küçük en büyük anahtar
            yaz("not " + not, harfNotu.floorEntry(not).getValue());
        }

        bolum("NavigableMap metotları");
        yaz("firstKey() / lastKey()", harfNotu.firstKey() + " / " + harfNotu.lastKey());
        yaz("ceilingKey(65) (>= 65)", harfNotu.ceilingKey(65));
        yaz("higherKey(70) (> 70)", harfNotu.higherKey(70));
        yaz("lowerKey(50) (< 50)", harfNotu.lowerKey(50));
        yaz("headMap(60) (< 60)", harfNotu.headMap(60));
        yaz("tailMap(70) (>= 70)", harfNotu.tailMap(70));
        yaz("subMap(50, 85) [50, 85)", harfNotu.subMap(50, 85));
        yaz("descendingMap()", harfNotu.descendingMap());

        bolum("Uygulama: zaman çizelgesi");
        NavigableMap<LocalTime, String> program = new TreeMap<>();
        program.put(LocalTime.of(9, 0), "Günlük toplantı");
        program.put(LocalTime.of(10, 30), "Kod incelemesi");
        program.put(LocalTime.of(12, 0), "Öğle arası");
        program.put(LocalTime.of(14, 0), "Sprint planlama");
        program.put(LocalTime.of(16, 45), "Demo");
        LocalTime simdi = LocalTime.of(13, 15);
        yaz("şu an", simdi);
        yaz("devam eden/son etkinlik", program.floorEntry(simdi).getValue());
        yaz("sıradaki etkinlik", program.higherEntry(simdi).getValue());
        yaz("öğleden sonrakiler", program.tailMap(LocalTime.NOON, false).values());
        yaz("pollFirstEntry()", program.pollFirstEntry());
    }
}
