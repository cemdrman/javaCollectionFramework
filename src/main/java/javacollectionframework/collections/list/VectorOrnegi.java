package javacollectionframework.collections.list;

import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;

import static javacollectionframework.ortak.Konsol.baslik;
import static javacollectionframework.ortak.Konsol.bolum;
import static javacollectionframework.ortak.Konsol.yaz;

/**
 * <h2>Vector</h2>
 *
 * <p>{@link Vector}, Java 1.0'dan kalma (legacy) dinamik dizi sınıfıdır. Java 1.2'de Collection
 * Framework'e uyarlanarak {@link List} arayüzünü uygulamıştır.</p>
 *
 * <ul>
 *     <li>ArrayList'e çok benzer, ancak <b>bütün metotları {@code synchronized}</b>'dır.</li>
 *     <li>Dolduğunda varsayılan olarak kapasitesini <b>iki katına</b> çıkarır
 *         (ArrayList ~%50 artırır). {@code capacityIncrement} ile artış miktarı verilebilir.</li>
 *     <li>Tek thread'li kodda gereksiz kilit maliyeti vardır.</li>
 * </ul>
 *
 * <p><b>Modern alternatifler:</b> Tek thread için {@link java.util.ArrayList}; çok thread için
 * {@link Collections#synchronizedList(List)} veya okuma ağırlıklı senaryolarda
 * {@link CopyOnWriteArrayList}. Yeni kodda Vector kullanılması önerilmez.</p>
 *
 * @author cemdirman
 */
public class VectorOrnegi {

    public static void main(String[] args) {
        baslik("Vector Örneği");

        bolum("Kapasite ve boyut farkı");
        // Eski örnekte 'List vector = new Vector()' ile ham tip (raw type) kullanılıp
        // içine hem String hem Integer konuyordu. Generic'ler sayesinde (Java 5+)
        // tip güvenliği derleme zamanında sağlanır; ham tip kullanmayın.
        Vector<String> takimlar = new Vector<>(10); // başlangıç kapasitesi 10
        yaz("capacity()", takimlar.capacity());
        yaz("size()", takimlar.size());

        // Not: size() 0 olduğu için 'i < takimlar.size()' koşuluyla kurulan bir döngü
        // hiç çalışmaz. Kapasite, eleman sayısı değildir!
        for (int i = 0; i < 12; i++) {
            takimlar.add(i + ". takım");
        }
        yaz("12 eleman sonrası size()", takimlar.size());
        yaz("12 eleman sonrası capacity() (2 kat)", takimlar.capacity());

        bolum("capacityIncrement");
        Vector<Character> harfler = new Vector<>(3, 2); // kapasite 3, her dolduğunda +2
        for (char c = 'A'; c <= 'D'; c++) {
            harfler.add(c);
        }
        yaz("harfler", harfler);
        yaz("capacity() (3 + 2)", harfler.capacity());

        bolum("setSize ile boyut değiştirme");
        Vector<String> kucuk = new Vector<>(List.of("x", "y"));
        kucuk.setSize(4); // eksik kalan yerler null ile doldurulur
        yaz("setSize(4)", kucuk);

        bolum("Legacy API: Enumeration ve elementAt");
        Vector<String> renkler = new Vector<>(List.of("sarı", "lacivert"));
        renkler.addElement("beyaz");
        yaz("elementAt(0)", renkler.elementAt(0));
        Enumeration<String> en = renkler.elements();
        var sb = new StringBuilder();
        while (en.hasMoreElements()) {
            sb.append(en.nextElement()).append(' ');
        }
        yaz("Enumeration ile", sb.toString().trim());
        // Enumeration -> Iterator dönüşümü (Java 9+)
        yaz("asIterator().next()", renkler.elements().asIterator().next());

        bolum("Modern alternatif");
        List<String> senkronListe = Collections.synchronizedList(new java.util.ArrayList<>());
        senkronListe.add("thread-safe");
        yaz("Collections.synchronizedList", senkronListe);
    }
}
