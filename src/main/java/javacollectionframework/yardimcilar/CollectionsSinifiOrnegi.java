package javacollectionframework.yardimcilar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import static javacollectionframework.ortak.Konsol.baslik;
import static javacollectionframework.ortak.Konsol.bolum;
import static javacollectionframework.ortak.Konsol.yaz;

/**
 * <h2>java.util.Collections yardımcı sınıfı</h2>
 *
 * <p>{@link Collections}, koleksiyonlar üzerinde çalışan <b>static</b> metotlardan oluşan bir
 * yardımcı sınıftır ({@link java.util.Collection} arayüzü ile karıştırmayın). Başlıca gruplar:</p>
 * <ul>
 *     <li>Sıralama ve arama: {@code sort, binarySearch, min, max}</li>
 *     <li>Düzenleme: {@code reverse, shuffle, swap, rotate, fill, copy, nCopies}</li>
 *     <li>Sorgulama: {@code frequency, disjoint, indexOfSubList}</li>
 *     <li>Sarmalayıcılar (wrapper): {@code unmodifiable*, synchronized*, checked*}</li>
 *     <li>Hazır koleksiyonlar: {@code emptyList, singletonList, nCopies}</li>
 * </ul>
 *
 * @author cemdirman
 */
public class CollectionsSinifiOrnegi {

    public static void main(String[] args) {
        baslik("Collections Yardımcı Sınıfı");

        List<Integer> sayilar = new ArrayList<>(List.of(42, 7, 19, 3, 25, 7, 11));

        bolum("Sıralama, arama, min/max");
        yaz("liste", sayilar);
        yaz("min / max", Collections.min(sayilar) + " / " + Collections.max(sayilar));
        Collections.sort(sayilar);
        yaz("sort", sayilar);
        // binarySearch yalnızca SIRALI listede doğru çalışır. O(log n)
        yaz("binarySearch(19)", Collections.binarySearch(sayilar, 19));
        // Bulunamazsa (-(eklenme noktası) - 1) döner
        yaz("binarySearch(20)", Collections.binarySearch(sayilar, 20));
        yaz("frequency(7)", Collections.frequency(sayilar, 7));

        bolum("Düzenleme");
        Collections.reverse(sayilar);
        yaz("reverse", sayilar);
        Collections.swap(sayilar, 0, sayilar.size() - 1);
        yaz("swap(0, son)", sayilar);
        Collections.rotate(sayilar, 2);
        yaz("rotate(2)", sayilar);
        Collections.shuffle(sayilar, new Random(1907)); // sabit tohum -> tekrar edilebilir
        yaz("shuffle(Random(1907))", sayilar);

        List<String> doldur = new ArrayList<>(List.of("a", "b", "c"));
        Collections.fill(doldur, "x");
        yaz("fill(x)", doldur);
        yaz("nCopies(3, \"ha\")", Collections.nCopies(3, "ha"));
        List<String> harfler = new ArrayList<>(List.of("a", "b"));
        Collections.addAll(harfler, "c", "d", "e");
        yaz("addAll(liste, c, d, e)", harfler);

        bolum("Küme sorguları");
        yaz("disjoint([1,2], [3,4]) (ortak yok mu)", Collections.disjoint(List.of(1, 2), List.of(3, 4)));
        yaz("indexOfSubList", Collections.indexOfSubList(List.of(1, 2, 3, 2, 3), List.of(2, 3)));
        yaz("lastIndexOfSubList", Collections.lastIndexOfSubList(List.of(1, 2, 3, 2, 3), List.of(2, 3)));

        bolum("Boş ve tekil koleksiyonlar (değiştirilemez)");
        List<String> bos = Collections.emptyList();
        Set<Integer> tek = Collections.singleton(1);
        Map<String, Integer> tekMap = Collections.singletonMap("anahtar", 1);
        yaz("emptyList / singleton / singletonMap", bos + " / " + tek + " / " + tekMap);

        bolum("Sarmalayıcılar (wrapper)");
        List<String> senkron = Collections.synchronizedList(new ArrayList<>());
        senkron.add("a");
        // synchronized* koleksiyonlarda dolaşma işlemi ELLE kilitlenmelidir:
        synchronized (senkron) {
            for (String s : senkron) {
                yaz("synchronizedList elemanı", s);
            }
        }
        Map<String, Integer> salt = Collections.unmodifiableMap(new TreeMap<>(Map.of("a", 1)));
        try {
            salt.put("b", 2);
        } catch (UnsupportedOperationException e) {
            yaz("unmodifiableMap.put", "UnsupportedOperationException");
        }

        // checked*: ham tip (raw type) kullanan eski kodlara karşı çalışma zamanı tip kontrolü
        List<String> kontrollu = Collections.checkedList(new ArrayList<>(), String.class);
        try {
            eskiKod(kontrollu);
        } catch (ClassCastException e) {
            yaz("checkedList'e Integer eklemek", "ClassCastException");
        }

        bolum("Ters sıralı comparator");
        List<String> kelimeler = new ArrayList<>(List.of("elma", "armut", "kiraz"));
        kelimeler.sort(Collections.reverseOrder());
        yaz("sort(Collections.reverseOrder())", kelimeler);
    }

    /** Generic'ler öncesi yazılmış, tip kontrolü yapmayan bir kodu temsil eder. */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void eskiKod(Collection liste) {
        liste.add(42);
    }
}
