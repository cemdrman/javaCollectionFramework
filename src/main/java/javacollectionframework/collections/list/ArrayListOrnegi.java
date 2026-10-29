package javacollectionframework.collections.list;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import static javacollectionframework.ortak.Konsol.baslik;
import static javacollectionframework.ortak.Konsol.bolum;
import static javacollectionframework.ortak.Konsol.yaz;

/**
 * <h2>ArrayList</h2>
 *
 * <p>{@link ArrayList}, {@link List} arayüzünün <b>dinamik dizi</b> (resizable array) ile
 * gerçekleştirilmiş halidir. Arka planda bir {@code Object[]} tutar; dizi dolduğunda
 * yaklaşık %50 daha büyük yeni bir dizi oluşturup elemanları kopyalar.</p>
 *
 * <ul>
 *     <li>İndeks ile erişim ({@code get/set}): <b>O(1)</b></li>
 *     <li>Sona ekleme ({@code add}): amortize <b>O(1)</b></li>
 *     <li>Araya ekleme / silme: <b>O(n)</b> (elemanlar kaydırılır)</li>
 *     <li>Arama ({@code contains/indexOf}): <b>O(n)</b></li>
 *     <li>Sıralıdır (ekleme sırası korunur), tekrar eden elemana ve {@code null}'a izin verir.</li>
 *     <li>Thread-safe <b>değildir</b>.</li>
 * </ul>
 *
 * <p>Çoğu durumda {@code List} ihtiyacı için ilk tercih {@code ArrayList} olmalıdır.</p>
 *
 * @author cemdirman
 */
public class ArrayListOrnegi {

    public static void main(String[] args) {
        baslik("ArrayList Örneği");

        bolum("Oluşturma ve ekleme");
        // Java 7+ diamond (<>) operatörü, Java 10+ 'var' ile tip çıkarımı.
        // Değişkeni arayüz tipiyle (List) tanımlamak iyi bir alışkanlıktır.
        List<Integer> sayilar = new ArrayList<>();
        sayilar.add(1907);
        sayilar.add(1994);
        sayilar.add(23);
        sayilar.add(3);
        sayilar.add(47);
        sayilar.add(57);
        sayilar.add(5, 24); // 5. indekse ekler, sonraki elemanlar sağa kayar
        yaz("sayilar", sayilar);
        yaz("size()", sayilar.size());

        bolum("Erişim");
        yaz("get(0)", sayilar.get(0));
        yaz("getFirst() (Java 21+)", sayilar.getFirst());
        yaz("getLast()  (Java 21+)", sayilar.getLast());
        yaz("indexOf(23)", sayilar.indexOf(23));
        yaz("contains(1907)", sayilar.contains(1907));

        bolum("Güncelleme ve silme");
        sayilar.set(1, 2000);                 // indeksteki elemanı değiştirir
        yaz("set(1, 2000) sonrası", sayilar);
        sayilar.remove(0);                    // int -> indekse göre siler
        sayilar.remove(Integer.valueOf(3));   // Object -> değere göre siler
        yaz("remove(0), remove(Integer 3)", sayilar);
        sayilar.removeIf(s -> s > 1000);      // Java 8+: koşula göre silme
        yaz("removeIf(s > 1000)", sayilar);
        sayilar.replaceAll(s -> s * 10);      // Java 8+: her elemanı dönüştürme
        yaz("replaceAll(s * 10)", sayilar);

        bolum("Sıralama");
        List<Integer> karisik = new ArrayList<>(List.of(5, 3, 9, 1, 7));
        Collections.sort(karisik);            // klasik yol
        yaz("Collections.sort", karisik);
        karisik.sort(Comparator.reverseOrder()); // Java 8+: List.sort
        yaz("sort(reverseOrder)", karisik);
        yaz("reversed() görünümü (Java 21+)", karisik.reversed());

        bolum("Dolaşma (iteration) yolları");
        List<Character> harfler = new ArrayList<>(List.of('A', 'B', 'C', 'D', 'E'));

        // 1) Klasik for - indeks gerektiğinde
        var sb = new StringBuilder();
        for (int i = 0; i < harfler.size(); i++) {
            sb.append(i).append('=').append(harfler.get(i)).append(' ');
        }
        yaz("klasik for", sb);

        // 2) for-each - en okunaklı yol
        sb.setLength(0);
        for (char harf : harfler) {
            sb.append(harf);
        }
        yaz("for-each", sb);

        // 3) Iterator - dolaşırken güvenli silme için
        // NOT: Eski sürümdeki örnekte döngü koşulu yanlış iterator'ü kontrol ediyordu.
        // Her zaman döngüde oluşturulan iterator'ün (it) hasNext() metodunu kullanın.
        for (Iterator<Character> it = harfler.iterator(); it.hasNext(); ) {
            if (it.next() == 'C') {
                it.remove();
            }
        }
        yaz("Iterator ile 'C' silindi", harfler);

        // 4) forEach + method reference (Java 8+)
        sb.setLength(0);
        harfler.forEach(sb::append);
        yaz("forEach(sb::append)", sb);

        bolum("Kapasite");
        // Eleman sayısı önceden biliniyorsa başlangıç kapasitesi vermek
        // gereksiz dizi kopyalamalarını önler.
        ArrayList<String> buyukListe = new ArrayList<>(10_000);
        buyukListe.ensureCapacity(20_000);
        buyukListe.add("tek eleman");
        buyukListe.trimToSize(); // iç diziyi eleman sayısına küçültür
        yaz("size()", buyukListe.size());

        bolum("Alt liste ve dönüşümler");
        List<String> sehirler = new ArrayList<>(List.of("İstanbul", "Ankara", "İzmir", "Bursa", "Antalya"));
        List<String> alt = sehirler.subList(1, 4); // [1, 4) aralığının GÖRÜNÜMÜ
        yaz("subList(1, 4)", alt);
        alt.clear();                              // görünüm üzerindeki değişiklik asıl listeye yansır
        yaz("subList.clear() sonrası asıl liste", sehirler);
        String[] dizi = sehirler.toArray(String[]::new); // Java 11+
        yaz("toArray(String[]::new).length", dizi.length);
    }
}
