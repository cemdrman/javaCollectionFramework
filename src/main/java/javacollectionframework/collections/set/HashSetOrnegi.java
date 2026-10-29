package javacollectionframework.collections.set;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static javacollectionframework.ortak.Konsol.baslik;
import static javacollectionframework.ortak.Konsol.bolum;
import static javacollectionframework.ortak.Konsol.yaz;

/**
 * <h2>HashSet</h2>
 *
 * <p>{@link Set}, matematikteki <b>küme</b> kavramına karşılık gelir: aynı eleman birden fazla
 * bulunamaz. {@link HashSet} ise arka planda bir {@link java.util.HashMap} kullanır; elemanlar
 * map'in anahtarı (key) olarak saklanır.</p>
 *
 * <ul>
 *     <li>{@code add / remove / contains}: ortalama <b>O(1)</b></li>
 *     <li>Eleman sırası <b>garanti edilmez</b> (hash değerine göre dağılır).</li>
 *     <li>Bir adet {@code null} elemana izin verir.</li>
 *     <li>Benzersizlik {@code hashCode()} ve {@code equals()} ile belirlenir. Bu iki metot
 *         birlikte ve tutarlı şekilde override edilmelidir.</li>
 * </ul>
 *
 * @author cemdirman
 */
public class HashSetOrnegi {

    /** equals/hashCode'u override ETMEYEN sınıf: her nesne farklı kabul edilir. */
    static final class OgrenciHatali {
        final String numara;

        OgrenciHatali(String numara) {
            this.numara = numara;
        }
    }

    /** equals/hashCode'u doğru override eden klasik sınıf. */
    static final class Ogrenci {
        final String numara;
        final String ad;

        Ogrenci(String numara, String ad) {
            this.numara = numara;
            this.ad = ad;
        }

        @Override
        public boolean equals(Object o) {
            // Java 16+ pattern matching for instanceof
            return o instanceof Ogrenci diger && numara.equals(diger.numara);
        }

        @Override
        public int hashCode() {
            return Objects.hash(numara);
        }
    }

    /** Java 16+ record: equals, hashCode ve toString otomatik üretilir. */
    record Sehir(String ad, int plaka) {
    }

    public static void main(String[] args) {
        baslik("HashSet Örneği");

        bolum("Temel kullanım - tekrar eden elemanlar eklenmez");
        Set<String> isimler = new HashSet<>();
        yaz("add(\"cem\")", isimler.add("cem"));
        yaz("add(\"emir\")", isimler.add("emir"));
        yaz("add(\"cem\") tekrar", isimler.add("cem")); // false döner
        isimler.add("asya");
        isimler.add(null);
        yaz("isimler (sıra garanti değil)", isimler);
        yaz("contains(\"emir\")", isimler.contains("emir"));
        yaz("size()", isimler.size());

        bolum("Listeden tekrarları temizleme");
        List<Integer> tekrarli = List.of(3, 1, 3, 2, 1, 3);
        Set<Integer> benzersiz = new HashSet<>(tekrarli);
        yaz("liste", tekrarli);
        yaz("new HashSet<>(liste)", benzersiz);

        bolum("Küme işlemleri");
        Set<Integer> a = new HashSet<>(Set.of(1, 2, 3, 4));
        Set<Integer> b = Set.of(3, 4, 5, 6);

        Set<Integer> birlesim = new HashSet<>(a);
        birlesim.addAll(b);            // A ∪ B
        Set<Integer> kesisim = new HashSet<>(a);
        kesisim.retainAll(b);          // A ∩ B
        Set<Integer> fark = new HashSet<>(a);
        fark.removeAll(b);             // A \ B
        yaz("A", a);
        yaz("B", b);
        yaz("birleşim (addAll)", birlesim);
        yaz("kesişim (retainAll)", kesisim);
        yaz("fark A\\B (removeAll)", fark);
        yaz("A, {1,2}'yi kapsıyor mu (containsAll)", a.containsAll(Set.of(1, 2)));

        bolum("equals ve hashCode'un önemi");
        Set<OgrenciHatali> hatali = new HashSet<>();
        hatali.add(new OgrenciHatali("101"));
        hatali.add(new OgrenciHatali("101"));
        yaz("override YOK -> size()", hatali.size() + " (aynı öğrenci iki kez!)");

        Set<Ogrenci> dogru = new HashSet<>();
        dogru.add(new Ogrenci("101", "Cem"));
        dogru.add(new Ogrenci("101", "Cem D."));
        yaz("override VAR -> size()", dogru.size());

        Set<Sehir> sehirler = new HashSet<>();
        sehirler.add(new Sehir("İstanbul", 34));
        sehirler.add(new Sehir("İstanbul", 34));
        yaz("record -> size()", sehirler.size());
        yaz("record contains", sehirler.contains(new Sehir("İstanbul", 34)));

        bolum("Kapasite önceden biliniyorsa (Java 19+)");
        // new HashSet<>(100) yük faktörü nedeniyle 100 elemanda yeniden boyutlanabilir.
        // newHashSet(100) ise 100 elemanı yeniden boyutlanmadan alacak kapasiteyi hesaplar.
        Set<Integer> hazir = HashSet.newHashSet(100);
        for (int i = 0; i < 100; i++) {
            hazir.add(i);
        }
        yaz("HashSet.newHashSet(100).size()", hazir.size());
    }
}
