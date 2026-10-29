package javacollectionframework.yardimcilar;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static javacollectionframework.ortak.Konsol.baslik;
import static javacollectionframework.ortak.Konsol.bolum;
import static javacollectionframework.ortak.Konsol.yaz;

/**
 * <h2>java.util.Arrays yardımcı sınıfı</h2>
 *
 * <p>{@link Arrays}, diziler (array) için sıralama, arama, karşılaştırma, doldurma, kopyalama ve
 * diziden liste/stream oluşturma metotları sunar. Diziler ile Collection Framework arasındaki
 * köprüdür.</p>
 *
 * <ul>
 *     <li>İlkel tipli diziler için {@code sort}: Dual-Pivot Quicksort (kararlı değil)</li>
 *     <li>Nesne dizileri için {@code sort}: TimSort (kararlı / stable)</li>
 * </ul>
 *
 * @author cemdirman
 */
public class ArraysSinifiOrnegi {

    public static void main(String[] args) {
        baslik("Arrays Yardımcı Sınıfı");

        bolum("Yazdırma");
        int[] sayilar = {42, 7, 19, 3, 25, 11};
        int[][] matris = {{1, 2}, {3, 4}};
        yaz("dizi.toString() (işe yaramaz)", sayilar.toString().startsWith("[I@"));
        yaz("Arrays.toString", Arrays.toString(sayilar));
        yaz("Arrays.deepToString (çok boyutlu)", Arrays.deepToString(matris));

        bolum("Sıralama ve ikili arama");
        int[] sirali = sayilar.clone();
        Arrays.sort(sirali);
        yaz("sort", Arrays.toString(sirali));
        yaz("binarySearch(19)", Arrays.binarySearch(sirali, 19));
        int[] kismi = sayilar.clone();
        Arrays.sort(kismi, 0, 3); // yalnızca [0, 3) aralığı
        yaz("sort(0, 3)", Arrays.toString(kismi));

        String[] kelimeler = {"koleksiyon", "java", "Liste", "map"};
        Arrays.sort(kelimeler, String.CASE_INSENSITIVE_ORDER);
        yaz("sort(CASE_INSENSITIVE_ORDER)", Arrays.toString(kelimeler));
        Arrays.sort(kelimeler, Comparator.comparingInt(String::length).reversed());
        yaz("sort(uzunluk azalan)", Arrays.toString(kelimeler));

        int[] buyuk = new int[1_000_000];
        Arrays.setAll(buyuk, i -> buyuk.length - i);
        Arrays.parallelSort(buyuk); // çok çekirdekli sıralama (Fork/Join)
        yaz("parallelSort ilk/son", buyuk[0] + " / " + buyuk[buyuk.length - 1]);

        bolum("Doldurma ve kopyalama");
        int[] doldur = new int[5];
        Arrays.fill(doldur, 7);
        yaz("fill(7)", Arrays.toString(doldur));
        Arrays.setAll(doldur, i -> i * i);
        yaz("setAll(i * i)", Arrays.toString(doldur));
        yaz("copyOf(dizi, 8) (genişletme)", Arrays.toString(Arrays.copyOf(doldur, 8)));
        yaz("copyOfRange(1, 3)", Arrays.toString(Arrays.copyOfRange(doldur, 1, 3)));

        bolum("Karşılaştırma");
        int[] a = {1, 2, 3};
        int[] b = {1, 2, 3};
        yaz("a == b (referans)", a == b);
        yaz("a.equals(b) (yine referans!)", a.equals(b));
        yaz("Arrays.equals(a, b) (içerik)", Arrays.equals(a, b));
        yaz("Arrays.deepEquals(matris, kopya)", Arrays.deepEquals(matris, new int[][]{{1, 2}, {3, 4}}));
        yaz("Arrays.hashCode(a) == hashCode(b)", Arrays.hashCode(a) == Arrays.hashCode(b));
        yaz("compare({1,2,3}, {1,2,4}) (Java 9+)", Arrays.compare(a, new int[]{1, 2, 4}));
        yaz("mismatch({1,2,3}, {1,9,3}) (Java 9+)", Arrays.mismatch(a, new int[]{1, 9, 3}));

        bolum("Diziden koleksiyona ve stream'e");
        String[] sehirler = {"Sinop", "Ordu", "Rize"};
        List<String> sabitBoyutlu = Arrays.asList(sehirler); // diziye bağlı, sabit boyut
        List<String> degistirilemez = List.of(sehirler);     // bağımsız kopya
        sehirler[0] = "Samsun";
        yaz("Arrays.asList (diziyi yansıtır)", sabitBoyutlu);
        yaz("List.of (kopya)", degistirilemez);
        yaz("Arrays.stream(sayilar).sum()", Arrays.stream(sayilar).sum());

        bolum("İlkel dizi tuzağı");
        // Arrays.asList(int[]) -> List<int[]> (TEK elemanlı!) döner, List<Integer> değil.
        List<int[]> yanlis = Arrays.asList(sayilar);
        yaz("Arrays.asList(int[]).size()", yanlis.size());
        List<Integer> dogru = Arrays.stream(sayilar).boxed().toList();
        yaz("Arrays.stream(int[]).boxed().toList()", dogru);

        bolum("Koleksiyondan diziye");
        List<String> liste = List.of("a", "b", "c");
        String[] dizi1 = liste.toArray(new String[0]);   // klasik yol
        String[] dizi2 = liste.toArray(String[]::new);   // Java 11+
        yaz("toArray(new String[0])", Arrays.toString(dizi1));
        yaz("toArray(String[]::new)", Arrays.toString(dizi2));
    }
}
