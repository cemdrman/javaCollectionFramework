package javacollectionframework.collections.set;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;

import static javacollectionframework.ortak.Konsol.baslik;
import static javacollectionframework.ortak.Konsol.bolum;
import static javacollectionframework.ortak.Konsol.yaz;

/**
 * <h2>TreeSet</h2>
 *
 * <p>Set interface olduğu için somut bir nesne oluşturulamaz. Set, matematikteki küme
 * kavramına benzer: aynı elemandan birden fazla olamaz. "Tree" ise elemanların sıralı
 * tutulmasını sağlar. {@link TreeSet} hem Tree hem de Set'in özelliklerini taşır; böylece
 * içindeki elemanlar hem <b>sıralı</b> hem de <b>benzersiz</b> olur.</p>
 *
 * <ul>
 *     <li>Arka planda {@link java.util.TreeMap}, yani bir <b>kırmızı-siyah ağaç</b> (red-black tree)
 *         kullanır.</li>
 *     <li>{@code add / remove / contains}: <b>O(log n)</b></li>
 *     <li>Sıralama elemanların doğal sırasına ({@link Comparable}) veya verilen
 *         {@link Comparator}'a göredir.</li>
 *     <li>Benzersizlik {@code equals} ile değil, <b>{@code compareTo/compare == 0}</b> ile
 *         belirlenir.</li>
 *     <li>{@code null} elemana izin vermez.</li>
 *     <li>{@link NavigableSet} arayüzü sayesinde aralık sorguları yapılabilir.</li>
 * </ul>
 *
 * @author cemdirman
 */
public class TreeSetOrnegi {

    public static void main(String[] args) {
        baslik("TreeSet Örneği");

        bolum("Doğal sıralama (String.compareTo)");
        Set<String> isimler = new TreeSet<>();
        isimler.add("cem");
        isimler.add("dırman");
        isimler.add("emir");
        isimler.add("asaf");
        isimler.add("asya");
        isimler.add("cem");
        isimler.add("Cem");
        isimler.add("Asya");
        isimler.add("cem");
        yaz("isimler", isimler);
        /*
         * Koleksiyona birden fazla "cem" eklendi; çıktıda ise yalnızca bir tane "cem" vardır.
         * "cem".equals("Cem") false olduğu için "Cem" ayrı bir elemandır. String'lerin doğal
         * sıralaması Unicode değerine göredir ve büyük harfler küçük harflerden önce gelir; bu
         * yüzden "Asya" ve "Cem" en başta yer alır.
         */

        bolum("Büyük/küçük harf duyarsız sıralama");
        Set<String> duyarsiz = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        duyarsiz.addAll(isimler);
        // compare("cem", "Cem") == 0 olduğundan ikisi AYNI eleman sayılır!
        yaz("CASE_INSENSITIVE_ORDER", duyarsiz);

        bolum("Türkçe alfabeye göre sıralama (Collator)");
        Set<String> sehirler = new TreeSet<>(Collator.getInstance(Locale.of("tr", "TR")));
        sehirler.addAll(Set.of("Çorum", "Ankara", "İzmir", "Iğdır", "Şırnak", "Sinop", "Ordu", "Uşak"));
        yaz("Collator(tr)", sehirler);
        yaz("Doğal sıralama (Unicode, yanlış)", new TreeSet<>(sehirler.stream().toList()));

        bolum("Comparator ile özel sıralama");
        TreeSet<String> uzunlugaGore = new TreeSet<>(
                Comparator.comparingInt(String::length).thenComparing(Comparator.naturalOrder()));
        uzunlugaGore.addAll(Set.of("kivi", "elma", "karpuz", "üzüm", "muz", "ananas"));
        yaz("uzunluk, sonra alfabe", uzunlugaGore);

        bolum("NavigableSet ile aralık sorguları");
        NavigableSet<Integer> notlar = new TreeSet<>(Set.of(45, 50, 62, 70, 78, 85, 90, 100));
        yaz("notlar", notlar);
        yaz("first() / last()", notlar.first() + " / " + notlar.last());
        yaz("floor(75)   (<= 75 en büyük)", notlar.floor(75));
        yaz("ceiling(75) (>= 75 en küçük)", notlar.ceiling(75));
        yaz("lower(70)   (< 70 en büyük)", notlar.lower(70));
        yaz("higher(70)  (> 70 en küçük)", notlar.higher(70));
        yaz("headSet(60) (< 60)", notlar.headSet(60));
        yaz("tailSet(85) (>= 85)", notlar.tailSet(85));
        yaz("subSet(50, true, 78, true)", notlar.subSet(50, true, 78, true));
        yaz("descendingSet()", notlar.descendingSet());
        yaz("pollFirst() (en küçüğü çıkar)", notlar.pollFirst());
        yaz("son durum", notlar);
    }
}
