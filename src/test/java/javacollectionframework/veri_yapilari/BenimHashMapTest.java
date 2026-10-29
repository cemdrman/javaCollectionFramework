package javacollectionframework.veri_yapilari;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BenimHashMapTest {

    /** Bütün örnekleri aynı kovaya düşen anahtar. */
    record CakisanAnahtar(int id) {
        @Override
        public int hashCode() {
            return 7;
        }
    }

    @Test
    @DisplayName("put var olan anahtarın değerini günceller ve eski değeri döner")
    void putGunceller() {
        BenimHashMap<String, Integer> map = new BenimHashMap<>();
        assertNull(map.put("elma", 1));
        assertEquals(1, map.put("elma", 2));
        assertEquals(2, map.get("elma"));
        assertEquals(1, map.size());
        assertEquals(-1, map.getOrDefault("armut", -1));
    }

    @Test
    @DisplayName("null anahtar desteklenir")
    void nullAnahtar() {
        BenimHashMap<String, String> map = new BenimHashMap<>();
        map.put(null, "boş");
        assertTrue(map.containsKey(null));
        assertEquals("boş", map.remove(null));
        assertFalse(map.containsKey(null));
    }

    @Test
    @DisplayName("Yük faktörü aşılınca kapasite iki katına çıkar ve veriler korunur")
    void rehash() {
        BenimHashMap<Integer, Integer> map = new BenimHashMap<>();
        for (int i = 0; i < 12; i++) {
            map.put(i, i * i);
        }
        assertEquals(16, map.kapasite());
        map.put(12, 144);
        assertEquals(32, map.kapasite());
        for (int i = 0; i <= 12; i++) {
            assertEquals(i * i, map.get(i));
        }
    }

    @Test
    @DisplayName("Aynı kovaya düşen anahtarlar zincirde doğru yönetilir")
    void cakisma() {
        BenimHashMap<CakisanAnahtar, String> map = new BenimHashMap<>();
        for (int i = 0; i < 5; i++) {
            map.put(new CakisanAnahtar(i), "d" + i);
        }
        assertEquals("d3", map.get(new CakisanAnahtar(3)));
        assertEquals("d0", map.remove(new CakisanAnahtar(0))); // zincirin sonu
        assertEquals("d4", map.remove(new CakisanAnahtar(4))); // zincirin başı
        assertEquals("d2", map.remove(new CakisanAnahtar(2))); // zincirin ortası
        assertEquals(2, map.size());
        assertEquals("d1", map.get(new CakisanAnahtar(1)));
        assertNull(map.get(new CakisanAnahtar(2)));
    }

    @Test
    @DisplayName("Rastgele işlemlerde java.util.HashMap ile aynı sonucu verir")
    void hashMapIleAyniDavranis() {
        Random rnd = new Random(23);
        BenimHashMap<Integer, Integer> benim = new BenimHashMap<>();
        Map<Integer, Integer> referans = new HashMap<>();
        for (int adim = 0; adim < 20_000; adim++) {
            int anahtar = rnd.nextInt(2_000);
            switch (rnd.nextInt(3)) {
                case 0 -> assertEquals(referans.put(anahtar, adim), benim.put(anahtar, adim));
                case 1 -> assertEquals(referans.remove(anahtar), benim.remove(anahtar));
                default -> assertEquals(referans.get(anahtar), benim.get(anahtar));
            }
            assertEquals(referans.size(), benim.size());
        }
        assertEquals(referans.keySet(), new java.util.HashSet<>(benim.keys()));
    }
}
