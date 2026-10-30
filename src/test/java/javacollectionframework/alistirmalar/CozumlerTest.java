package javacollectionframework.alistirmalar;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CozumlerTest {

    @Test
    @DisplayName("1. İlk tekrar eden eleman")
    void ilkTekrarEden() {
        assertEquals(Optional.of(3), Cozumler.ilkTekrarEden(List.of(5, 3, 8, 3, 5)));
        assertEquals(Optional.of("a"), Cozumler.ilkTekrarEden(List.of("a", "b", "a")));
        assertEquals(Optional.empty(), Cozumler.ilkTekrarEden(List.of(1, 2, 3)));
        assertEquals(Optional.empty(), Cozumler.ilkTekrarEden(List.of()));
    }

    @Test
    @DisplayName("2. İki toplam")
    void ikiToplam() {
        assertArrayEquals(new int[]{0, 1}, Cozumler.ikiToplam(new int[]{2, 7, 11, 15}, 9).orElseThrow());
        assertArrayEquals(new int[]{1, 2}, Cozumler.ikiToplam(new int[]{3, 2, 4}, 6).orElseThrow());
        assertArrayEquals(new int[]{0, 1}, Cozumler.ikiToplam(new int[]{3, 3}, 6).orElseThrow());
        assertTrue(Cozumler.ikiToplam(new int[]{1, 2, 3}, 100).isEmpty());
    }

    @Test
    @DisplayName("3. Anagram grupları")
    void anagramGruplari() {
        assertEquals(
                List.of(List.of("elma", "emla", "male"), List.of("kitap", "kapit"), List.of("java")),
                Cozumler.anagramGruplari(List.of("elma", "kitap", "emla", "java", "male", "kapit")));
        assertEquals(List.of(), Cozumler.anagramGruplari(List.of()));
    }

    @Test
    @DisplayName("4. İlk benzersiz karakter")
    void ilkBenzersizKarakter() {
        assertEquals(Optional.of('j'), Cozumler.ilkBenzersizKarakter("java"));
        assertEquals(Optional.of('l'), Cozumler.ilkBenzersizKarakter("koleksiyon"));
        assertEquals(Optional.of('b'), Cozumler.ilkBenzersizKarakter("aabcc"));
        assertEquals(Optional.of('ş'), Cozumler.ilkBenzersizKarakter("kekeşk"));
        assertEquals(Optional.empty(), Cozumler.ilkBenzersizKarakter("aabb"));
    }

    @Test
    @DisplayName("5. En sık geçen k kelime (eşitlikte alfabetik)")
    void enSikKelimeler() {
        String metin = "Bir berber bir berbere gel beraber bir berber dükkânı açalım demiş. "
                + "Berber, berbere: dükkânı açalım!";
        // berber ve bir 3'er kez; berbere, dükkânı ve açalım 2'şer kez geçer -> alfabetik olarak "açalım"
        assertEquals(List.of("berber", "bir", "açalım"), Cozumler.enSikKelimeler(metin, 3));
        assertEquals(List.of("a", "b"), Cozumler.enSikKelimeler("c b a b a", 2));
    }

    @Test
    @DisplayName("6. Tekrarsız en uzun alt metin")
    void tekrarsizEnUzunAltMetin() {
        assertEquals("abc", Cozumler.tekrarsizEnUzunAltMetin("abcabcbb"));
        assertEquals("b", Cozumler.tekrarsizEnUzunAltMetin("bbbbb"));
        assertEquals("wke", Cozumler.tekrarsizEnUzunAltMetin("pwwkew"));
        assertEquals("leksiyon", Cozumler.tekrarsizEnUzunAltMetin("koleksiyon"));
        assertEquals("", Cozumler.tekrarsizEnUzunAltMetin(""));
    }

    @Test
    @DisplayName("7. Sıralı listeleri birleştirme")
    void siraliListeleriBirlestir() {
        assertEquals(List.of(1, 1, 2, 3, 4, 4, 5, 6),
                Cozumler.siraliListeleriBirlestir(List.of(List.of(1, 4, 5), List.of(1, 3, 4), List.of(2, 6))));
        assertEquals(List.of(7), Cozumler.siraliListeleriBirlestir(List.of(List.of(), List.of(7))));
        assertEquals(List.of(), Cozumler.siraliListeleriBirlestir(List.of()));
    }

    @Test
    @DisplayName("8. BFS ile en kısa yol")
    void enKisaYol() {
        Map<String, List<String>> ucuslar = Map.of(
                "Sinop", List.of("Samsun", "İstanbul"),
                "Samsun", List.of("Ankara"),
                "İstanbul", List.of("Ankara", "İzmir"),
                "Ankara", List.of("Antalya"),
                "İzmir", List.of("Antalya"));
        assertEquals(List.of("Sinop", "İstanbul", "İzmir"), Cozumler.enKisaYol(ucuslar, "Sinop", "İzmir"));
        assertEquals(4, Cozumler.enKisaYol(ucuslar, "Sinop", "Antalya").size()); // 2 aktarmalı
        assertEquals(List.of("Ankara"), Cozumler.enKisaYol(ucuslar, "Ankara", "Ankara"));
        assertEquals(List.of(), Cozumler.enKisaYol(ucuslar, "Antalya", "Sinop"));
    }
}
