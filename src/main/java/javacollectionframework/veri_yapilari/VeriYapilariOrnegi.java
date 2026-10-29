package javacollectionframework.veri_yapilari;

import java.util.Objects;

import static javacollectionframework.ortak.Konsol.baslik;
import static javacollectionframework.ortak.Konsol.bolum;
import static javacollectionframework.ortak.Konsol.yaz;

/**
 * Kendi yazdığımız veri yapılarının ({@link BenimArrayList}, {@link BenimLinkedList},
 * {@link BenimHashMap}, {@link BenimStack}) iç davranışlarını gösteren örnek.
 *
 * @author cemdirman
 */
public class VeriYapilariOrnegi {

    /** hashCode'u kasıtlı olarak kötü yazılmış anahtar: hepsi aynı kovaya düşer. */
    record KotuAnahtar(String ad) {
        @Override
        public int hashCode() {
            return 42;
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof KotuAnahtar(String digerAd) && Objects.equals(ad, digerAd); // Java 21 record pattern
        }
    }

    public static void main(String[] args) {
        baslik("Kendi Veri Yapılarımız");

        bolum("BenimArrayList: kapasite artışı (x1.5)");
        BenimArrayList<Integer> liste = new BenimArrayList<>(4);
        var kapasiteler = new StringBuilder();
        for (int i = 1; i <= 20; i++) {
            liste.add(i);
            kapasiteler.append(liste.kapasite()).append(' ');
        }
        yaz("her add sonrası kapasite", kapasiteler.toString().trim());
        liste.add(0, 0);
        liste.remove(Integer.valueOf(10));
        yaz("add(0, 0), remove(10)", liste);

        bolum("BenimLinkedList: düğümlerle çalışma");
        BenimLinkedList<String> bagli = new BenimLinkedList<>();
        bagli.add("B");
        bagli.add("C");
        bagli.addFirst("A");
        bagli.add(3, "D");
        yaz("liste", bagli);
        bagli.tersCevir();
        yaz("tersCevir()", bagli);
        yaz("removeFirst() / removeLast()", bagli.removeFirst() + " / " + bagli.removeLast());
        yaz("kalan", bagli);

        bolum("BenimHashMap: rehash");
        BenimHashMap<String, Integer> map = new BenimHashMap<>();
        var gecis = new StringBuilder();
        for (int i = 0; i < 30; i++) {
            int onceki = map.kapasite();
            map.put("anahtar" + i, i);
            if (map.kapasite() != onceki) {
                gecis.append(map.size()).append(". elemanda ").append(onceki).append("->")
                        .append(map.kapasite()).append("; ");
            }
        }
        yaz("rehash anları", gecis.toString().trim());
        yaz("get(\"anahtar7\")", map.get("anahtar7"));

        bolum("BenimHashMap: kötü hashCode ile çakışma");
        BenimHashMap<KotuAnahtar, String> kotu = new BenimHashMap<>();
        kotu.put(new KotuAnahtar("x"), "1");
        kotu.put(new KotuAnahtar("y"), "2");
        kotu.put(new KotuAnahtar("z"), "3");
        // Hepsi aynı kovada: get artık zinciri dolaşır (O(n)) ama yine de DOĞRU çalışır.
        yaz("get(y) (aynı kovada zincir)", kotu.get(new KotuAnahtar("y")));
        yaz("remove(x), size()", kotu.remove(new KotuAnahtar("x")) + ", " + kotu.size());

        bolum("BenimStack");
        BenimStack<Character> yigin = new BenimStack<>();
        for (char c : "yığın".toCharArray()) {
            yigin.push(c);
        }
        var ters = new StringBuilder();
        while (!yigin.isEmpty()) {
            ters.append(yigin.pop());
        }
        yaz("\"yığın\" tersten", ters);
    }
}
