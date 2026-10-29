package javacollectionframework.collections.map;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.SequencedMap;

import static javacollectionframework.ortak.Konsol.baslik;
import static javacollectionframework.ortak.Konsol.bolum;
import static javacollectionframework.ortak.Konsol.yaz;

/**
 * <h2>LinkedHashMap</h2>
 *
 * <p>{@link LinkedHashMap}, {@link HashMap}'e ek olarak girdileri birbirine bağlayan çift yönlü bir
 * liste tutar. Böylece dolaşma sırası öngörülebilir olur:</p>
 * <ul>
 *     <li><b>Ekleme sırası</b> (varsayılan): var olan anahtara yeniden {@code put} sırayı değiştirmez.</li>
 *     <li><b>Erişim sırası</b> ({@code accessOrder = true}): {@code get/put} yapılan girdi sona taşınır.
 *         Bu, <b>LRU (Least Recently Used) önbellek</b> yapmak için idealdir.</li>
 * </ul>
 * <p>Java 21+ ile {@link SequencedMap} arayüzünü uygular.</p>
 *
 * @author cemdirman
 */
public class LinkedHashMapOrnegi {

    /**
     * En az kullanılan girdiyi otomatik silen sabit kapasiteli önbellek.
     * {@code removeEldestEntry}, her {@code put} sonrasında çağrılır.
     */
    static final class LruOnbellek<K, V> extends LinkedHashMap<K, V> {
        private final int kapasite;

        LruOnbellek(int kapasite) {
            super(16, 0.75f, true); // accessOrder = true
            this.kapasite = kapasite;
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry<K, V> enEski) {
            return size() > kapasite;
        }
    }

    public static void main(String[] args) {
        baslik("LinkedHashMap Örneği");

        bolum("HashMap vs LinkedHashMap");
        Map<String, Integer> hash = new HashMap<>();
        Map<String, Integer> linked = new LinkedHashMap<>();
        for (String gun : new String[]{"Pazartesi", "Salı", "Çarşamba", "Perşembe", "Cuma"}) {
            hash.put(gun, gun.length());
            linked.put(gun, gun.length());
        }
        yaz("HashMap", hash);
        yaz("LinkedHashMap (ekleme sırası)", linked);

        bolum("SequencedMap metotları (Java 21+)");
        SequencedMap<String, Integer> sirali = new LinkedHashMap<>(linked);
        yaz("firstEntry()", sirali.firstEntry());
        yaz("lastEntry()", sirali.lastEntry());
        sirali.putFirst("Pazar", 5);
        yaz("putFirst(Pazar)", sirali.sequencedKeySet());
        yaz("pollLastEntry()", sirali.pollLastEntry());
        yaz("reversed().keySet()", sirali.reversed().keySet());

        bolum("LRU önbellek (accessOrder = true)");
        LruOnbellek<String, String> onbellek = new LruOnbellek<>(3);
        onbellek.put("/anasayfa", "<html>1</html>");
        onbellek.put("/hakkimizda", "<html>2</html>");
        onbellek.put("/iletisim", "<html>3</html>");
        yaz("başlangıç", onbellek.keySet());
        onbellek.get("/anasayfa");                      // en son kullanılan oldu
        yaz("get(/anasayfa) sonrası", onbellek.keySet());
        onbellek.put("/blog", "<html>4</html>");        // kapasite aşıldı -> en eski silinir
        yaz("put(/blog) sonrası", onbellek.keySet());
    }
}
