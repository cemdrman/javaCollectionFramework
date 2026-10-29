package javacollectionframework.collections.sequenced;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.SequencedCollection;
import java.util.SequencedMap;
import java.util.SequencedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import static javacollectionframework.ortak.Konsol.baslik;
import static javacollectionframework.ortak.Konsol.bolum;
import static javacollectionframework.ortak.Konsol.yaz;

/**
 * <h2>Sequenced Collections (Java 21+, JEP 431)</h2>
 *
 * <p>Java 21'den önce "ilk/son elemanı al" gibi basit bir işlem için her koleksiyonun kendine
 * özgü bir yolu vardı:</p>
 * <pre>
 *                      İlk eleman                Son eleman
 *   List               list.get(0)               list.get(list.size() - 1)
 *   Deque              deque.getFirst()          deque.getLast()
 *   SortedSet          set.first()               set.last()
 *   LinkedHashSet      set.iterator().next()     // tek yol: sonuna kadar dolaşmak!
 * </pre>
 *
 * <p>Java 21 ile sırası tanımlı koleksiyonlar için üç yeni arayüz eklendi:</p>
 * <ul>
 *     <li>{@link SequencedCollection}: {@code getFirst, getLast, addFirst, addLast,
 *         removeFirst, removeLast, reversed}</li>
 *     <li>{@link SequencedSet}: {@code reversed()} bir SequencedSet döner</li>
 *     <li>{@link SequencedMap}: {@code firstEntry, lastEntry, putFirst, putLast, pollFirstEntry,
 *         pollLastEntry, reversed, sequencedKeySet, sequencedValues, sequencedEntrySet}</li>
 * </ul>
 *
 * <p>{@code reversed()} bir kopya değil, asıl koleksiyonun <b>ters sıralı görünümüdür</b>;
 * görünüm üzerinden yapılan değişiklikler asıl koleksiyona yansır.</p>
 *
 * @author cemdirman
 */
public class SequencedCollectionOrnegi {

    public static void main(String[] args) {
        baslik("Sequenced Collections (Java 21+)");

        bolum("Tek bir API ile farklı koleksiyonlar");
        List<SequencedCollection<Integer>> koleksiyonlar = List.of(
                new ArrayList<>(List.of(1, 2, 3)),
                new ArrayDeque<>(List.of(1, 2, 3)),
                new LinkedHashSet<>(List.of(1, 2, 3)),
                new TreeSet<>(List.of(1, 2, 3)));
        for (SequencedCollection<Integer> k : koleksiyonlar) {
            yaz(k.getClass().getSimpleName(),
                    "ilk=" + k.getFirst() + " son=" + k.getLast() + " ters=" + k.reversed());
        }

        bolum("reversed() bir görünümdür");
        List<String> liste = new ArrayList<>(List.of("a", "b", "c"));
        List<String> ters = liste.reversed();
        ters.addFirst("z");          // tersin başı = asıl listenin sonu
        yaz("ters.addFirst(z) -> liste", liste);
        ters.removeLast();           // tersin sonu = asıl listenin başı
        yaz("ters.removeLast() -> liste", liste);

        bolum("TreeSet'te addFirst/addLast desteklenmez");
        TreeSet<Integer> sirali = new TreeSet<>(List.of(5, 1, 3));
        try {
            sirali.addFirst(0); // konumu sıralama belirler, kullanıcı değil
        } catch (UnsupportedOperationException e) {
            yaz("TreeSet.addFirst(0)", "UnsupportedOperationException");
        }
        yaz("removeFirst()", sirali.removeFirst());

        bolum("SequencedMap");
        SequencedMap<String, Integer> skorlar = new LinkedHashMap<>();
        skorlar.put("Ayşe", 90);
        skorlar.put("Ali", 75);
        skorlar.putFirst("Zeynep", 88);   // en başa
        skorlar.putLast("Ayşe", 95);      // var olan anahtarı sona taşır
        yaz("skorlar", skorlar);
        yaz("firstEntry() / lastEntry()", skorlar.firstEntry() + " / " + skorlar.lastEntry());
        yaz("sequencedValues().reversed()", skorlar.sequencedValues().reversed());

        SequencedMap<Integer, String> agac = new TreeMap<>(java.util.Map.of(3, "üç", 1, "bir", 2, "iki"));
        yaz("TreeMap.reversed()", agac.reversed());

        bolum("Değiştirilemez sıralı görünümler");
        yaz("unmodifiableSequencedCollection",
                Collections.unmodifiableSequencedCollection(liste).getClass().getSimpleName());
        yaz("unmodifiableSequencedMap.firstEntry()",
                Collections.unmodifiableSequencedMap(skorlar).firstEntry());
    }
}
