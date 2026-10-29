package javacollectionframework.iterasyon;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.StreamSupport;

import static javacollectionframework.ortak.Konsol.baslik;
import static javacollectionframework.ortak.Konsol.bolum;
import static javacollectionframework.ortak.Konsol.yaz;

/**
 * <h2>Iterator, ListIterator, Iterable ve Spliterator</h2>
 *
 * <ul>
 *     <li>{@link Iterator}: Bir koleksiyonu baştan sona dolaşmak için ortak arayüz:
 *         {@code hasNext()}, {@code next()}, {@code remove()}.</li>
 *     <li>{@link ListIterator}: List'e özel, <b>iki yönlü</b> dolaşma ve dolaşırken
 *         {@code set/add} desteği.</li>
 *     <li>{@link Iterable}: {@code iterator()} metodu olan her sınıf for-each döngüsünde
 *         kullanılabilir.</li>
 *     <li>{@link Spliterator}: Paralel stream'ler için "bölünebilir" iterator (Java 8+).</li>
 * </ul>
 *
 * <p><b>Fail-fast:</b> {@code java.util} koleksiyonlarının iterator'ları, dolaşma sırasında
 * koleksiyon iterator dışından değiştirilirse {@link ConcurrentModificationException} fırlatır.
 * <b>Fail-safe / weakly consistent:</b> {@code java.util.concurrent} koleksiyonları ise
 * exception fırlatmaz; bir anlık görüntü ya da güncel durum üzerinde dolaşır.</p>
 *
 * @author cemdirman
 */
public class IteratorOrnegi {

    /**
     * Kendi Iterable sınıfımız: [baslangic, bitis) aralığındaki sayıları adım adım üretir.
     * Iterable olduğu için for-each döngüsünde kullanılabilir.
     */
    record Aralik(int baslangic, int bitis, int adim) implements Iterable<Integer> {

        @Override
        public Iterator<Integer> iterator() {
            return new Iterator<>() {
                private int siradaki = baslangic;

                @Override
                public boolean hasNext() {
                    return siradaki < bitis;
                }

                @Override
                public Integer next() {
                    if (!hasNext()) {
                        throw new NoSuchElementException();
                    }
                    int deger = siradaki;
                    siradaki += adim;
                    return deger;
                }
            };
        }
    }

    public static void main(String[] args) {
        baslik("Iterator Örnekleri");

        bolum("Iterator ile dolaşma ve güvenli silme");
        List<Integer> sayilar = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6, 7, 8));
        Iterator<Integer> it = sayilar.iterator();
        while (it.hasNext()) {
            if (it.next() % 2 == 0) {
                it.remove(); // dolaşırken silmenin doğru yolu
            }
        }
        yaz("çiftler silindi", sayilar);

        bolum("Fail-fast: ConcurrentModificationException");
        List<String> isimler = new ArrayList<>(List.of("ali", "veli", "ayşe", "fatma"));
        try {
            for (String isim : isimler) {        // for-each arka planda Iterator kullanır
                if (isim.startsWith("a")) {
                    isimler.remove(isim);          // YANLIŞ: koleksiyonu doğrudan değiştirmek
                }
            }
        } catch (ConcurrentModificationException e) {
            yaz("for-each içinde remove", "ConcurrentModificationException");
        }
        isimler.removeIf(isim -> isim.startsWith("a")); // DOĞRU (Java 8+)
        yaz("removeIf sonrası", isimler);

        bolum("Weakly consistent: CopyOnWriteArrayList");
        List<String> cow = new CopyOnWriteArrayList<>(List.of("a", "b", "c"));
        for (String s : cow) {                  // iterator bir anlık görüntü (snapshot) üzerinde
            if (s.equals("a")) {
                cow.add("d");                     // exception yok
            }
        }
        yaz("CopyOnWriteArrayList", cow);

        bolum("ListIterator: iki yönlü dolaşma, set ve add");
        List<String> harfler = new ArrayList<>(List.of("a", "b", "c"));
        ListIterator<String> lit = harfler.listIterator();
        while (lit.hasNext()) {
            int indeks = lit.nextIndex();
            String harf = lit.next();
            lit.set(harf.toUpperCase() + indeks);
            if (harf.equals("b")) {
                lit.add("ara");
            }
        }
        yaz("ileri: set + add", harfler);
        var geri = new StringBuilder();
        while (lit.hasPrevious()) {
            geri.append(lit.previous()).append(' ');
        }
        yaz("geri", geri.toString().trim());

        bolum("Kendi Iterable sınıfımız");
        var sb = new StringBuilder();
        for (int sayi : new Aralik(0, 20, 3)) {
            sb.append(sayi).append(' ');
        }
        yaz("new Aralik(0, 20, 3)", sb.toString().trim());
        // Iterable'dan stream oluşturma
        int toplam = StreamSupport.stream(new Aralik(1, 11, 1).spliterator(), false)
                .mapToInt(Integer::intValue).sum();
        yaz("1..10 toplamı (stream)", toplam);

        bolum("Iterator.forEachRemaining (Java 8+)");
        Iterator<String> kalan = List.of("x", "y", "z").iterator();
        kalan.next(); // ilk elemanı tükettik
        var kalanlar = new StringBuilder();
        kalan.forEachRemaining(kalanlar::append);
        yaz("ilk elemandan sonra kalanlar", kalanlar);

        bolum("Spliterator: paralel işleme için bölme");
        List<Integer> buyuk = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            buyuk.add(i);
        }
        Spliterator<Integer> sp1 = buyuk.spliterator();
        Spliterator<Integer> sp2 = sp1.trySplit(); // yarısını yeni bir spliterator'a ayırır
        yaz("sp2 (ilk yarı) tahmini boyut", sp2.estimateSize());
        yaz("sp1 (ikinci yarı) tahmini boyut", sp1.estimateSize());
        yaz("özellikler SIZED | ORDERED",
                sp1.hasCharacteristics(Spliterator.SIZED) && sp1.hasCharacteristics(Spliterator.ORDERED));
        var ilkYari = new StringBuilder();
        sp2.forEachRemaining(i -> ilkYari.append(i).append(' '));
        yaz("sp2 elemanları", ilkYari.toString().trim());
    }
}
