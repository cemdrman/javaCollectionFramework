package javacollectionframework.siralama;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static javacollectionframework.ortak.Konsol.baslik;
import static javacollectionframework.ortak.Konsol.bolum;
import static javacollectionframework.ortak.Konsol.yaz;

/**
 * <h2>Comparable ve Comparator</h2>
 *
 * <ul>
 *     <li>{@link Comparable}: Sınıfın <b>doğal sıralamasını</b> sınıfın içinde tanımlar
 *         ({@code compareTo}). Bir sınıfın yalnızca bir doğal sıralaması olabilir.
 *         {@code String}, {@code Integer}, {@code LocalDate} gibi sınıflar Comparable'dır.</li>
 *     <li>{@link Comparator}: Sıralamayı sınıfın <b>dışında</b> tanımlar ({@code compare}).
 *         Aynı sınıf için istenildiği kadar farklı sıralama yazılabilir.</li>
 * </ul>
 *
 * <p>Her ikisi de şu sözleşmeye uyar: sonuç <b>negatif</b> ise ilk eleman önce, <b>sıfır</b> ise
 * eşit, <b>pozitif</b> ise ikinci eleman önce gelir.</p>
 *
 * @author cemdirman
 */
public class ComparableComparatorOrnegi {

    /** Doğal sıralaması öğrenci numarasına göre olan record. */
    record Ogrenci(int numara, String ad, String bolum, double ortalama) implements Comparable<Ogrenci> {

        @Override
        public int compareTo(Ogrenci diger) {
            // DİKKAT: 'this.numara - diger.numara' yazmayın; büyük sayılarda taşma (overflow)
            // nedeniyle yanlış sonuç verir. Integer.compare güvenlidir.
            return Integer.compare(numara, diger.numara);
        }
    }

    public static void main(String[] args) {
        baslik("Comparable ve Comparator");

        List<Ogrenci> ogrenciler = new ArrayList<>(List.of(
                new Ogrenci(104, "Zeynep", "Bilgisayar", 3.45),
                new Ogrenci(101, "Ahmet", "Elektrik", 2.90),
                new Ogrenci(103, "Ayşe", "Bilgisayar", 3.80),
                new Ogrenci(102, "Mehmet", "Makine", 3.45),
                new Ogrenci(105, "Elif", "Elektrik", 3.10)));

        bolum("Comparable - doğal sıralama (numara)");
        ogrenciler.sort(null); // null -> doğal sıralama (Collections.sort(liste) ile aynı)
        ogrenciler.forEach(o -> yaz("  " + o.numara(), o.ad()));

        bolum("Comparator.comparing - ada göre");
        ogrenciler.sort(Comparator.comparing(Ogrenci::ad));
        yaz("ad", adlar(ogrenciler));

        bolum("reversed - ortalamaya göre azalan");
        ogrenciler.sort(Comparator.comparingDouble(Ogrenci::ortalama).reversed());
        ogrenciler.forEach(o -> yaz("  " + o.ad(), o.ortalama()));

        bolum("thenComparing - önce bölüm, sonra ortalama (azalan), sonra ad");
        ogrenciler.sort(Comparator.comparing(Ogrenci::bolum)
                .thenComparing(Ogrenci::ortalama, Comparator.reverseOrder())
                .thenComparing(Ogrenci::ad));
        ogrenciler.forEach(o -> yaz("  " + o.bolum(), o.ad() + " (" + o.ortalama() + ")"));

        bolum("Lambda ile Comparator (eski yöntemle karşılaştırma)");
        // Java 8 öncesi: anonim sınıf
        Comparator<String> eskiYontem = new Comparator<>() {
            @Override
            public int compare(String a, String b) {
                return Integer.compare(a.length(), b.length());
            }
        };
        // Java 8+: lambda ve hazır yardımcılar
        Comparator<String> lambda = (a, b) -> Integer.compare(a.length(), b.length());
        Comparator<String> modern = Comparator.comparingInt(String::length);
        String[] kelimeler = {"koleksiyon", "java", "liste", "map"};
        Arrays.sort(kelimeler, modern);
        yaz("uzunluğa göre", Arrays.toString(kelimeler));
        yaz("üç comparator da aynı sonucu verir",
                eskiYontem.compare("ab", "abc") == lambda.compare("ab", "abc")
                        && lambda.compare("ab", "abc") == modern.compare("ab", "abc"));

        bolum("null değerlerle sıralama");
        List<String> nulllu = new ArrayList<>(Arrays.asList("muz", null, "elma", null, "armut"));
        nulllu.sort(Comparator.nullsFirst(Comparator.naturalOrder()));
        yaz("nullsFirst", nulllu);
        nulllu.sort(Comparator.nullsLast(Comparator.<String>naturalOrder().reversed()));
        yaz("nullsLast + ters", nulllu);

        bolum("Comparator'dan min/max");
        Ogrenci enBasarili = ogrenciler.stream()
                .max(Comparator.comparingDouble(Ogrenci::ortalama)).orElseThrow();
        yaz("en yüksek ortalama", enBasarili.ad());
        yaz("Collections.max(List.of(3, 9, 4))", java.util.Collections.max(List.of(3, 9, 4)));

        bolum("compareTo ve equals tutarlılığı");
        // BigDecimal'de compareTo ve equals tutarsızdır: 2.0 ve 2.00 compareTo'ya göre eşit,
        // equals'a göre farklıdır. Bu yüzden HashSet ve TreeSet farklı sonuç verir.
        var a = new java.math.BigDecimal("2.0");
        var b = new java.math.BigDecimal("2.00");
        yaz("compareTo == 0 / equals", (a.compareTo(b) == 0) + " / " + a.equals(b));
        yaz("HashSet size()", new java.util.HashSet<>(List.of(a, b)).size());
        yaz("TreeSet size()", new java.util.TreeSet<>(List.of(a, b)).size());
        yaz("Objects.equals(a, b)", Objects.equals(a, b));
    }

    private static List<String> adlar(List<Ogrenci> liste) {
        return liste.stream().map(Ogrenci::ad).toList();
    }
}
