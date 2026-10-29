package javacollectionframework.veri_yapilari;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BenimArrayListTest {

    @Test
    @DisplayName("Kapasite dolunca %50 büyür ve elemanlar korunur")
    void kapasiteArtisi() {
        BenimArrayList<Integer> liste = new BenimArrayList<>(4);
        for (int i = 0; i < 5; i++) {
            liste.add(i);
        }
        assertEquals(6, liste.kapasite());
        assertEquals(5, liste.size());
        assertEquals("[0, 1, 2, 3, 4]", liste.toString());
    }

    @Test
    @DisplayName("Araya ekleme ve silme elemanları kaydırır")
    void arayaEklemeVeSilme() {
        BenimArrayList<String> liste = new BenimArrayList<>();
        liste.add("a");
        liste.add("c");
        liste.add(1, "b");
        liste.add(3, "d");
        assertEquals("[a, b, c, d]", liste.toString());
        assertEquals("b", liste.remove(1));
        assertTrue(liste.remove("d"));
        assertFalse(liste.remove("yok"));
        assertEquals("[a, c]", liste.toString());
    }

    @Test
    @DisplayName("Geçersiz indeks IndexOutOfBoundsException fırlatır")
    void gecersizIndeks() {
        BenimArrayList<String> liste = new BenimArrayList<>();
        liste.add("a");
        assertThrows(IndexOutOfBoundsException.class, () -> liste.get(1));
        assertThrows(IndexOutOfBoundsException.class, () -> liste.add(2, "x"));
        assertThrows(IndexOutOfBoundsException.class, () -> liste.remove(-1));
    }

    @Test
    @DisplayName("Iterator.remove güvenle siler, dışarıdan değişiklik fail-fast hata verir")
    void iterator() {
        BenimArrayList<Integer> liste = new BenimArrayList<>();
        for (int i = 1; i <= 6; i++) {
            liste.add(i);
        }
        for (Iterator<Integer> it = liste.iterator(); it.hasNext(); ) {
            if (it.next() % 2 == 0) {
                it.remove();
            }
        }
        assertEquals("[1, 3, 5]", liste.toString());

        assertThrows(ConcurrentModificationException.class, () -> {
            for (Integer i : liste) {
                liste.add(i);
            }
        });
    }

    @Test
    @DisplayName("Rastgele işlemlerde java.util.ArrayList ile aynı sonucu verir")
    void arrayListIleAyniDavranis() {
        Random rnd = new Random(1907);
        BenimArrayList<Integer> benim = new BenimArrayList<>(1);
        List<Integer> referans = new ArrayList<>();
        for (int adim = 0; adim < 5_000; adim++) {
            int islem = rnd.nextInt(4);
            if (islem <= 1 || referans.isEmpty()) {
                int indeks = rnd.nextInt(referans.size() + 1);
                int deger = rnd.nextInt(100);
                benim.add(indeks, deger);
                referans.add(indeks, deger);
            } else if (islem == 2) {
                int indeks = rnd.nextInt(referans.size());
                assertEquals(referans.remove(indeks), benim.remove(indeks));
            } else {
                int deger = rnd.nextInt(100);
                assertEquals(referans.indexOf(deger), benim.indexOf(deger));
            }
            assertEquals(referans.size(), benim.size());
        }
        assertEquals(referans.toString(), benim.toString());
    }
}
