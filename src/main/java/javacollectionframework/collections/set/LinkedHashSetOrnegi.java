package javacollectionframework.collections.set;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.SequencedSet;
import java.util.Set;

import static javacollectionframework.ortak.Konsol.baslik;
import static javacollectionframework.ortak.Konsol.bolum;
import static javacollectionframework.ortak.Konsol.yaz;

/**
 * <h2>LinkedHashSet</h2>
 *
 * <p>{@link LinkedHashSet}, {@link HashSet}'in <b>ekleme sırasını koruyan</b> halidir. Hash tablosuna
 * ek olarak elemanları birbirine bağlayan çift yönlü bir liste tutar.</p>
 *
 * <ul>
 *     <li>{@code add / remove / contains}: ortalama <b>O(1)</b> (HashSet'ten biraz yavaş)</li>
 *     <li>Dolaşma sırası = ekleme sırası. Var olan elemanı tekrar eklemek sırasını değiştirmez.</li>
 *     <li>Java 21+ ile {@link SequencedSet} arayüzünü uygular: {@code getFirst}, {@code getLast},
 *         {@code addFirst}, {@code addLast}, {@code reversed}.</li>
 * </ul>
 *
 * @author cemdirman
 */
public class LinkedHashSetOrnegi {

    public static void main(String[] args) {
        baslik("LinkedHashSet Örneği");

        List<String> girdi = List.of("muz", "elma", "kiraz", "elma", "armut", "muz", "çilek");

        bolum("HashSet vs LinkedHashSet");
        yaz("girdi", girdi);
        yaz("HashSet (sırasız)", new HashSet<>(girdi));
        yaz("LinkedHashSet (ekleme sırası)", new LinkedHashSet<>(girdi));

        bolum("Sırayı koruyarak tekrarları silme");
        List<String> benzersizListe = List.copyOf(new LinkedHashSet<>(girdi));
        yaz("List.copyOf(new LinkedHashSet<>(...))", benzersizListe);

        bolum("SequencedSet metotları (Java 21+)");
        SequencedSet<String> meyveler = new LinkedHashSet<>(girdi);
        yaz("getFirst()", meyveler.getFirst());
        yaz("getLast()", meyveler.getLast());

        meyveler.addFirst("kivi");     // en başa
        meyveler.addLast("elma");      // var olan elemanı SONA taşır
        yaz("addFirst(kivi), addLast(elma)", meyveler);
        yaz("reversed()", meyveler.reversed());
        yaz("removeFirst()", meyveler.removeFirst());
        yaz("son durum", meyveler);

        bolum("Normal add() var olan elemanın yerini değiştirmez");
        Set<Integer> sayilar = new LinkedHashSet<>(List.of(1, 2, 3));
        sayilar.add(1);
        yaz("add(1) sonrası", sayilar);
    }
}
