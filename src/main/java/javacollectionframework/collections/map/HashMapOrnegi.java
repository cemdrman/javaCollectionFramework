package javacollectionframework.collections.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static javacollectionframework.ortak.Konsol.baslik;
import static javacollectionframework.ortak.Konsol.bolum;
import static javacollectionframework.ortak.Konsol.yaz;

/**
 * <h2>HashMap</h2>
 *
 * <p>{@link Map}, <b>anahtar-değer</b> (key-value) çiftlerini tutar. Her anahtar benzersizdir ve en
 * fazla bir değere karşılık gelir. Map, {@link java.util.Collection} arayüzünden türemez ama
 * Collection Framework'ün bir parçasıdır.</p>
 *
 * <p>{@link HashMap}, Map'in <b>hash tablosu</b> ile gerçekleştirimidir:</p>
 * <ul>
 *     <li>Anahtarın {@code hashCode()} değerinden bir kova (bucket) indeksi hesaplanır.</li>
 *     <li>Aynı kovaya düşen anahtarlar önce bağlı liste olarak tutulur; bir kovada 8'den fazla
 *         eleman birikirse (Java 8+) liste <b>kırmızı-siyah ağaca</b> dönüştürülür.</li>
 *     <li>Eleman sayısı {@code kapasite * loadFactor} (varsayılan 16 * 0.75 = 12) değerini aşınca
 *         tablo iki katına çıkarılır (rehash).</li>
 *     <li>{@code get / put / remove / containsKey}: ortalama <b>O(1)</b></li>
 *     <li>Bir {@code null} anahtar ve birden çok {@code null} değere izin verir.</li>
 *     <li>Sıra garanti edilmez, thread-safe değildir.</li>
 * </ul>
 *
 * @author cemdirman
 */
public class HashMapOrnegi {

    public static void main(String[] args) {
        baslik("HashMap Örneği");

        bolum("Temel işlemler");
        Map<String, Integer> plakalar = new HashMap<>();
        plakalar.put("İstanbul", 34);
        plakalar.put("Ankara", 6);
        plakalar.put("İzmir", 35);
        yaz("plakalar", plakalar);
        yaz("get(\"Ankara\")", plakalar.get("Ankara"));
        yaz("get(\"Sinop\") (yok)", plakalar.get("Sinop"));
        yaz("getOrDefault(\"Sinop\", -1)", plakalar.getOrDefault("Sinop", -1));
        yaz("put(\"Ankara\", 66) eski değer", plakalar.put("Ankara", 66)); // eskisi döner
        yaz("containsKey(\"İzmir\")", plakalar.containsKey("İzmir"));
        yaz("containsValue(34)", plakalar.containsValue(34));
        yaz("remove(\"İzmir\")", plakalar.remove("İzmir"));
        yaz("size()", plakalar.size());

        bolum("Görünümler: keySet, values, entrySet");
        Map<String, Integer> stok = new HashMap<>(Map.of("elma", 40, "armut", 0, "kiraz", 12));
        yaz("keySet()", stok.keySet());
        yaz("values()", stok.values());
        // En verimli dolaşma: entrySet (anahtar başına ayrıca get() çağrılmaz)
        for (Map.Entry<String, Integer> e : stok.entrySet()) {
            yaz("  " + e.getKey(), e.getValue());
        }
        // Java 8+ forEach + BiConsumer
        var sb = new StringBuilder();
        stok.forEach((meyve, adet) -> sb.append(meyve).append('=').append(adet).append(' '));
        yaz("forEach", sb.toString().trim());
        // Görünüm üzerinden silme Map'e yansır
        stok.values().removeIf(adet -> adet == 0);
        yaz("values().removeIf(0) sonrası", stok);

        bolum("Java 8+ varsayılan (default) metotlar");
        Map<String, Integer> puan = new HashMap<>();
        puan.putIfAbsent("cem", 10);            // yoksa ekle
        puan.putIfAbsent("cem", 99);            // varsa dokunma
        yaz("putIfAbsent", puan);

        puan.merge("cem", 5, Integer::sum);     // varsa birleştir: 10 + 5
        puan.merge("emir", 7, Integer::sum);    // yoksa 7 olarak ekle
        yaz("merge(..., Integer::sum)", puan);

        puan.compute("cem", (k, v) -> v == null ? 1 : v * 2);
        yaz("compute (v * 2)", puan);
        puan.computeIfPresent("emir", (k, v) -> v + 100);
        yaz("computeIfPresent (+100)", puan);
        puan.computeIfPresent("emir", (k, v) -> null); // null dönerse anahtar silinir
        yaz("computeIfPresent -> null (silindi)", puan);
        puan.replaceAll((k, v) -> v * 10);
        yaz("replaceAll (* 10)", puan);

        bolum("Uygulama: kelime sayma (merge)");
        String metin = "bir berber bir berbere gel beraber bir berber dükkanı açalım demiş";
        Map<String, Integer> frekans = new HashMap<>();
        for (String kelime : metin.split("\\s+")) {
            frekans.merge(kelime, 1, Integer::sum);
        }
        yaz("frekans", frekans);

        bolum("Uygulama: gruplama (computeIfAbsent)");
        List<String> isimler = List.of("Ahmet", "Ayşe", "Burak", "Bora", "Cem", "Ali");
        Map<Character, List<String>> gruplar = new HashMap<>();
        for (String isim : isimler) {
            // Anahtar yoksa yeni liste oluşturur ve döner; varsa mevcut listeyi döner.
            gruplar.computeIfAbsent(isim.charAt(0), k -> new ArrayList<>()).add(isim);
        }
        yaz("baş harfe göre", gruplar);

        bolum("null anahtar ve değer");
        Map<String, String> nullOrnek = new HashMap<>();
        nullOrnek.put(null, "null anahtar");
        nullOrnek.put("bos", null);
        yaz("get(null)", nullOrnek.get(null));
        // get() null döndüğünde anahtar mı yok, değer mi null? Ayırt etmek için containsKey.
        yaz("get(\"bos\") / containsKey", nullOrnek.get("bos") + " / " + nullOrnek.containsKey("bos"));

        bolum("Kapasite (Java 19+)");
        Map<Integer, String> hazir = HashMap.newHashMap(1_000); // 1000 eleman için rehash yok
        hazir.put(1, "bir");
        yaz("HashMap.newHashMap(1000).size()", hazir.size());
    }
}
