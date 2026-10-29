package javacollectionframework.collections.degistirilemez;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static javacollectionframework.ortak.Konsol.baslik;
import static javacollectionframework.ortak.Konsol.bolum;
import static javacollectionframework.ortak.Konsol.yaz;

/**
 * <h2>Değiştirilemez (immutable) ve salt-okunur (unmodifiable) koleksiyonlar</h2>
 *
 * <p>Java'da "değiştirilemez" denince birbirine karıştırılan üç farklı kavram vardır:</p>
 * <ol>
 *     <li><b>Unmodifiable view</b> ({@code Collections.unmodifiableList(liste)}): asıl listenin salt
 *         okunur <b>görünümüdür</b>. Görünüm üzerinden değişiklik yapılamaz ama asıl liste
 *         değişirse görünüm de değişir.</li>
 *     <li><b>Değiştirilemez kopya</b> ({@code List.copyOf}, {@code stream.toList()}): kaynaktan
 *         bağımsızdır, hiçbir şekilde değiştirilemez.</li>
 *     <li><b>Değiştirilemez fabrika</b> ({@code List.of}, {@code Set.of}, {@code Map.of}, Java 9+).</li>
 * </ol>
 *
 * <p>Fabrika metotlarının ({@code of/copyOf}) özellikleri:</p>
 * <ul>
 *     <li>{@code null} eleman/anahtar/değer kabul etmezler ({@code NullPointerException}).</li>
 *     <li>{@code Set.of} ve {@code Map.of} tekrar eden elemanda {@code IllegalArgumentException} fırlatır.</li>
 *     <li>{@code Set.of} ve {@code Map.of}'un dolaşma sırası <b>bilerek rastgeledir</b>; sıraya güvenmeyin.</li>
 *     <li>Değiştirme metotları {@code UnsupportedOperationException} fırlatır.</li>
 *     <li>İçindeki nesneler değiştirilebilirse, koleksiyon yalnızca <b>yüzeysel</b> olarak değiştirilemezdir.</li>
 * </ul>
 *
 * @author cemdirman
 */
public class DegistirilemezKoleksiyonOrnegi {

    public static void main(String[] args) {
        baslik("Değiştirilemez Koleksiyonlar");

        bolum("Fabrika metotları (Java 9+)");
        List<String> renkler = List.of("sarı", "lacivert");
        Set<Integer> asallar = Set.of(2, 3, 5, 7, 11);
        Map<String, Integer> plakalar = Map.of("İstanbul", 34, "Ankara", 6);
        Map<String, Integer> buyukMap = Map.ofEntries(
                Map.entry("Sinop", 57),
                Map.entry("Ordu", 52));
        yaz("List.of", renkler);
        yaz("Set.of (sıra rastgele)", asallar);
        yaz("Map.of", plakalar);
        yaz("Map.ofEntries", buyukMap);

        bolum("Değiştirme denemeleri");
        dene("List.of(...).add", () -> renkler.add("beyaz"));
        dene("List.of(...).set", () -> renkler.set(0, "kırmızı"));
        dene("Map.of(...).put", () -> plakalar.put("İzmir", 35));
        dene("List.of(null)", () -> List.of("a", null));
        dene("Set.of(1, 1)", () -> Set.of(1, 1));
        dene("List.of(...).contains(null)", () -> renkler.contains(null));

        bolum("Unmodifiable view vs copyOf");
        List<String> kaynak = new ArrayList<>(List.of("a", "b"));
        List<String> gorunum = Collections.unmodifiableList(kaynak);
        List<String> kopya = List.copyOf(kaynak);
        kaynak.add("c");
        yaz("kaynak", kaynak);
        yaz("unmodifiableList (değişikliği görür)", gorunum);
        yaz("List.copyOf (bağımsız)", kopya);
        // copyOf, zaten değiştirilemez bir listeye çağrılırsa kopyalamadan aynı nesneyi döner.
        yaz("List.copyOf(kopya) == kopya", List.copyOf(kopya) == kopya);

        bolum("Stream'den değiştirilemez liste");
        List<Integer> kareler = Stream.of(1, 2, 3).map(x -> x * x).toList(); // Java 16+
        dene("stream.toList().add", () -> kareler.add(16));
        List<Integer> degistirilebilir = Stream.of(1, 2, 3).collect(Collectors.toList());
        degistirilebilir.add(4);
        yaz("Collectors.toList() (değiştirilebilir*)", degistirilebilir);
        List<Integer> kesinDegistirilemez = Stream.of(1, 2, 3).collect(Collectors.toUnmodifiableList());
        dene("toUnmodifiableList().add", () -> kesinDegistirilemez.add(4));

        bolum("Arrays.asList: sabit boyutlu, diziye bağlı liste");
        String[] dizi = {"x", "y", "z"};
        List<String> diziListesi = Arrays.asList(dizi);
        diziListesi.set(0, "X");                   // set serbest, diziye yansır
        yaz("set(0, X) sonrası dizi", Arrays.toString(dizi));
        dene("Arrays.asList(...).add", () -> diziListesi.add("w")); // boyut değişemez

        bolum("Yüzeysel (shallow) değiştirilemezlik");
        List<StringBuilder> metinler = List.of(new StringBuilder("merhaba"));
        metinler.getFirst().append(" dünya"); // liste değişmedi ama içindeki nesne değişti
        yaz("List.of(StringBuilder) içeriği", metinler);
    }

    private static void dene(String etiket, Runnable islem) {
        try {
            islem.run();
            yaz(etiket, "başarılı");
        } catch (RuntimeException e) {
            yaz(etiket, e.getClass().getSimpleName());
        }
    }
}
