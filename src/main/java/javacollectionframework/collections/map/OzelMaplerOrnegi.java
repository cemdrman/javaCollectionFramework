package javacollectionframework.collections.map;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.WeakHashMap;

import static javacollectionframework.ortak.Konsol.baslik;
import static javacollectionframework.ortak.Konsol.bolum;
import static javacollectionframework.ortak.Konsol.yaz;

/**
 * <h2>Özel amaçlı ve legacy Map'ler</h2>
 *
 * <ul>
 *     <li>{@link IdentityHashMap}: anahtarları {@code equals} yerine <b>referans eşitliği</b>
 *         ({@code ==}) ile karşılaştırır. Nesne grafı dolaşma, serileştirme gibi özel durumlar içindir.</li>
 *     <li>{@link WeakHashMap}: anahtarları <b>zayıf referans</b> ile tutar. Anahtara başka hiçbir yerden
 *         güçlü referans kalmadığında, girdi Garbage Collector tarafından otomatik silinebilir.
 *         Nesnelere ek bilgi (metadata) iliştirmek için kullanılır.</li>
 *     <li>{@link Hashtable}: Java 1.0'dan kalma, senkronize hash tablosu. {@code null} anahtar/değer
 *         kabul etmez. Yeni kodda {@link java.util.concurrent.ConcurrentHashMap} kullanılmalıdır.</li>
 *     <li>{@link Properties}: Hashtable'dan türeyen, {@code String} anahtar-değer yapılandırma sınıfı.</li>
 * </ul>
 *
 * @author cemdirman
 */
public class OzelMaplerOrnegi {

    public static void main(String[] args) throws InterruptedException {
        baslik("Özel Amaçlı ve Legacy Map Örnekleri");

        bolum("IdentityHashMap: == ile karşılaştırma");
        // 'new String' ile içerikleri aynı ama referansları farklı iki nesne oluşturuyoruz.
        String a = new String("anahtar");
        String b = new String("anahtar");
        Map<String, Integer> normal = new HashMap<>();
        normal.put(a, 1);
        normal.put(b, 2);
        Map<String, Integer> kimlik = new IdentityHashMap<>();
        kimlik.put(a, 1);
        kimlik.put(b, 2);
        yaz("a.equals(b) / a == b", a.equals(b) + " / " + (a == b));
        yaz("HashMap size()", normal.size());
        yaz("IdentityHashMap size()", kimlik.size());

        bolum("WeakHashMap: GC ile silinen girdiler");
        Map<Object, String> zayif = new WeakHashMap<>();
        Object gecici = new Object();
        Object kalici = new Object();
        zayif.put(gecici, "geçici nesnenin bilgisi");
        zayif.put(kalici, "kalıcı nesnenin bilgisi");
        yaz("GC öncesi size()", zayif.size());
        gecici = null; // tek güçlü referansı bırakıyoruz
        for (int i = 0; i < 10 && zayif.size() > 1; i++) {
            System.gc();       // GC'yi garanti etmez, yalnızca önerir
            Thread.sleep(50);
        }
        yaz("GC sonrası size() (genellikle 1)", zayif.size());
        yaz("kalıcı nesne hâlâ var", zayif.containsKey(kalici));

        bolum("Hashtable (legacy)");
        Hashtable<String, Integer> tablo = new Hashtable<>();
        tablo.put("bir", 1);
        tablo.put("iki", 2);
        try {
            tablo.put("null", null);
        } catch (NullPointerException e) {
            yaz("put(k, null)", "NullPointerException");
        }
        Enumeration<String> anahtarlar = tablo.keys();
        yaz("keys() Enumeration -> list", Collections.list(anahtarlar));

        bolum("Properties");
        Properties ayarlar = new Properties();
        ayarlar.setProperty("db.url", "jdbc:postgresql://localhost/test");
        ayarlar.setProperty("db.havuz", "10");
        yaz("getProperty(db.url)", ayarlar.getProperty("db.url"));
        yaz("getProperty(db.timeout, 30)", ayarlar.getProperty("db.timeout", "30"));
        yaz("Java sürümü (System properties)", System.getProperty("java.version"));
    }
}
