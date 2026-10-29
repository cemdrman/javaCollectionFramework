package javacollectionframework.veri_yapilari;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Random;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BenimLinkedListTest {

    @Test
    @DisplayName("Baş ve sona ekleme/silme")
    void basVeSon() {
        BenimLinkedList<String> liste = new BenimLinkedList<>();
        liste.addLast("b");
        liste.addFirst("a");
        liste.addLast("c");
        assertEquals("[a, b, c]", liste.toString());
        assertEquals("a", liste.getFirst());
        assertEquals("c", liste.getLast());
        assertEquals("a", liste.removeFirst());
        assertEquals("c", liste.removeLast());
        assertEquals("b", liste.removeLast());
        assertTrue(liste.isEmpty());
        assertThrows(NoSuchElementException.class, liste::removeFirst);
        assertThrows(NoSuchElementException.class, liste::getLast);
    }

    @Test
    @DisplayName("tersCevir bağlantıları doğru günceller")
    void tersCevir() {
        BenimLinkedList<Integer> liste = new BenimLinkedList<>();
        for (int i = 1; i <= 5; i++) {
            liste.add(i);
        }
        liste.tersCevir();
        assertEquals("[5, 4, 3, 2, 1]", liste.toString());
        assertEquals(5, liste.getFirst());
        assertEquals(1, liste.getLast());
        liste.add(0);
        assertEquals(0, liste.get(5));
        assertTrue(liste.contains(3));
        assertFalse(liste.contains(9));
    }

    @Test
    @DisplayName("Rastgele işlemlerde java.util.LinkedList ile aynı sonucu verir")
    void linkedListIleAyniDavranis() {
        Random rnd = new Random(1994);
        BenimLinkedList<Integer> benim = new BenimLinkedList<>();
        LinkedList<Integer> referans = new LinkedList<>();
        for (int adim = 0; adim < 5_000; adim++) {
            int islem = rnd.nextInt(5);
            int deger = rnd.nextInt(1000);
            switch (islem) {
                case 0 -> {
                    benim.addFirst(deger);
                    referans.addFirst(deger);
                }
                case 1 -> {
                    int indeks = rnd.nextInt(referans.size() + 1);
                    benim.add(indeks, deger);
                    referans.add(indeks, deger);
                }
                case 2 -> {
                    if (!referans.isEmpty()) {
                        int indeks = rnd.nextInt(referans.size());
                        assertEquals(referans.remove(indeks), benim.remove(indeks));
                    }
                }
                case 3 -> {
                    if (!referans.isEmpty()) {
                        assertEquals(referans.removeLast(), benim.removeLast());
                    }
                }
                default -> {
                    if (!referans.isEmpty()) {
                        int indeks = rnd.nextInt(referans.size());
                        assertEquals(referans.get(indeks), benim.get(indeks));
                    }
                }
            }
            assertEquals(referans.size(), benim.size());
        }
        assertEquals(referans.toString(), benim.toString());
    }
}
