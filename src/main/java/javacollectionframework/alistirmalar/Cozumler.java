package javacollectionframework.alistirmalar;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * <h2>Alıştırmaların çözümleri</h2>
 *
 * <p>Soruları {@code docs/14-alistirmalar.md} dosyasında bulabilirsiniz. Önce kendiniz çözmeyi
 * deneyin, sonra buradaki çözümle karşılaştırın. Her çözümün doğruluğu
 * {@code src/test/java/.../alistirmalar/CozumlerTest.java} ile test edilir.</p>
 *
 * @author cemdirman
 */
public final class Cozumler {

    private Cozumler() {
    }

    /**
     * 1. İlk tekrar eden eleman.
     *
     * <p>{@link HashSet#add} eleman zaten varsa {@code false} döner; bu da "daha önce gördüm mü?"
     * sorusunu O(1)'de yanıtlar. Toplam: O(n) zaman, O(n) bellek.</p>
     */
    public static <T> Optional<T> ilkTekrarEden(List<T> liste) {
        Set<T> gorulenler = new HashSet<>();
        for (T eleman : liste) {
            if (!gorulenler.add(eleman)) {
                return Optional.of(eleman);
            }
        }
        return Optional.empty();
    }

    /**
     * 2. İki toplam: toplamı hedefe eşit olan iki elemanın indeksleri.
     *
     * <p>Her eleman için "tamamlayıcısını daha önce gördüm mü?" sorusu bir {@link HashMap} ile
     * O(1)'de yanıtlanır. İç içe döngü ile O(n²) olan çözüm O(n)'e iner.</p>
     */
    public static Optional<int[]> ikiToplam(int[] dizi, int hedef) {
        Map<Integer, Integer> degerIndeks = new HashMap<>();
        for (int i = 0; i < dizi.length; i++) {
            Integer tamamlayiciIndeks = degerIndeks.get(hedef - dizi[i]);
            if (tamamlayiciIndeks != null) {
                return Optional.of(new int[]{tamamlayiciIndeks, i});
            }
            degerIndeks.putIfAbsent(dizi[i], i);
        }
        return Optional.empty();
    }

    /**
     * 3. Anagram grupları.
     *
     * <p>Anagram olan kelimelerin harfleri sıralandığında aynı anahtar elde edilir
     * ("elma", "emla" → "aelm"). {@code computeIfAbsent} ile gruplanır; {@link LinkedHashMap}
     * grupların ilk görülme sırasını korur.</p>
     */
    public static List<List<String>> anagramGruplari(List<String> kelimeler) {
        Map<String, List<String>> gruplar = new LinkedHashMap<>();
        for (String kelime : kelimeler) {
            char[] harfler = kelime.toCharArray();
            Arrays.sort(harfler);
            gruplar.computeIfAbsent(new String(harfler), _ -> new ArrayList<>()).add(kelime);
        }
        return List.copyOf(gruplar.values());
    }

    /**
     * 4. İlk benzersiz karakter.
     *
     * <p>{@link LinkedHashMap} hem sayar hem de ilk görülme sırasını korur; ikinci geçişte
     * sayısı 1 olan ilk karakter bulunur. O(n).</p>
     */
    public static Optional<Character> ilkBenzersizKarakter(String metin) {
        Map<Character, Integer> sayac = new LinkedHashMap<>();
        for (char c : metin.toCharArray()) {
            sayac.merge(c, 1, Integer::sum);
        }
        return sayac.entrySet().stream()
                .filter(e -> e.getValue() == 1)
                .map(Map.Entry::getKey)
                .findFirst();
    }

    /**
     * 5. En sık geçen k kelime.
     *
     * <p>Önce {@code merge} ile frekanslar sayılır. Sonra boyutu k olan bir min-heap
     * ({@link PriorityQueue}) ile en sık k kelime O(n log k)'da seçilir. Eşit frekansta
     * alfabetik olarak önce gelen kelime öndedir.</p>
     */
    public static List<String> enSikKelimeler(String metin, int k) {
        Map<String, Integer> frekans = new HashMap<>();
        for (String kelime : metin.toLowerCase().split("\\P{L}+")) {
            if (!kelime.isEmpty()) {
                frekans.merge(kelime, 1, Integer::sum);
            }
        }
        Comparator<Map.Entry<String, Integer>> oncelik = Map.Entry.<String, Integer>comparingByValue()
                .thenComparing(Map.Entry.<String, Integer>comparingByKey().reversed());
        PriorityQueue<Map.Entry<String, Integer>> heap = new PriorityQueue<>(oncelik); // en "zayıf" başta
        for (Map.Entry<String, Integer> e : frekans.entrySet()) {
            heap.offer(e);
            if (heap.size() > k) {
                heap.poll();
            }
        }
        List<String> sonuc = new ArrayList<>(heap.size());
        while (!heap.isEmpty()) {
            sonuc.add(heap.poll().getKey());
        }
        Collections.reverse(sonuc);
        return sonuc;
    }

    /**
     * 6. Tekrarsız en uzun alt metin.
     *
     * <p><b>Kayan pencere</b> (sliding window): Her karakterin son görüldüğü indeks bir
     * {@link HashMap}'te tutulur. Pencere içinde tekrar eden bir karakter görülünce pencerenin
     * başı o karakterin bir sonrasına taşınır. O(n).</p>
     */
    public static String tekrarsizEnUzunAltMetin(String metin) {
        Map<Character, Integer> sonIndeks = new HashMap<>();
        int pencereBasi = 0;
        int enIyiBas = 0;
        int enIyiUzunluk = 0;
        for (int i = 0; i < metin.length(); i++) {
            Integer onceki = sonIndeks.put(metin.charAt(i), i);
            if (onceki != null && onceki >= pencereBasi) {
                pencereBasi = onceki + 1;
            }
            if (i - pencereBasi + 1 > enIyiUzunluk) {
                enIyiUzunluk = i - pencereBasi + 1;
                enIyiBas = pencereBasi;
            }
        }
        return metin.substring(enIyiBas, enIyiBas + enIyiUzunluk);
    }

    /**
     * 7. Sıralı listeleri birleştirme.
     *
     * <p>Her listenin o anki en küçük elemanı bir {@link PriorityQueue}'da tutulur. En küçük
     * çekilir, ait olduğu listenin bir sonraki elemanı kuyruğa eklenir. Toplam N eleman ve
     * k liste için O(N log k).</p>
     */
    public static List<Integer> siraliListeleriBirlestir(List<List<Integer>> listeler) {
        record Konum(int deger, int liste, int indeks) {
        }
        PriorityQueue<Konum> heap = new PriorityQueue<>(Comparator.comparingInt(Konum::deger));
        for (int i = 0; i < listeler.size(); i++) {
            if (!listeler.get(i).isEmpty()) {
                heap.offer(new Konum(listeler.get(i).getFirst(), i, 0));
            }
        }
        List<Integer> sonuc = new ArrayList<>();
        while (!heap.isEmpty()) {
            Konum k = heap.poll();
            sonuc.add(k.deger());
            List<Integer> kaynak = listeler.get(k.liste());
            if (k.indeks() + 1 < kaynak.size()) {
                heap.offer(new Konum(kaynak.get(k.indeks() + 1), k.liste(), k.indeks() + 1));
            }
        }
        return sonuc;
    }

    /**
     * 8. Graf üzerinde en kısa yol (genişlik öncelikli arama - BFS).
     *
     * <p>Graf, komşuluk listesi olarak {@code Map<String, List<String>>} ile temsil edilir.
     * BFS bir {@link ArrayDeque} kuyruğu kullanır; ziyaret edilen düğümler ve nereden
     * gelindiği bir {@link HashMap}'te tutulur. Ağırlıksız graflarda bulunan ilk yol en kısadır.</p>
     */
    public static List<String> enKisaYol(Map<String, List<String>> graf, String baslangic, String hedef) {
        Map<String, String> nereden = new HashMap<>();
        nereden.put(baslangic, null);
        Deque<String> kuyruk = new ArrayDeque<>();
        kuyruk.offer(baslangic);
        while (!kuyruk.isEmpty()) {
            String dugum = kuyruk.poll();
            if (dugum.equals(hedef)) {
                List<String> yol = new ArrayList<>();
                for (String d = hedef; d != null; d = nereden.get(d)) {
                    yol.addFirst(d);
                }
                return yol;
            }
            for (String komsu : graf.getOrDefault(dugum, List.of())) {
                if (!nereden.containsKey(komsu)) {
                    nereden.put(komsu, dugum);
                    kuyruk.offer(komsu);
                }
            }
        }
        return List.of();
    }
}
