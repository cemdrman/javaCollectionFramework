package javacollectionframework.collections.list;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import static javacollectionframework.ortak.Konsol.baslik;
import static javacollectionframework.ortak.Konsol.bolum;
import static javacollectionframework.ortak.Konsol.yaz;

/**
 * <h2>LinkedList</h2>
 *
 * <p>{@link LinkedList}, <b>çift yönlü bağlı liste</b> (doubly linked list) yapısıdır. Her düğüm
 * (node) elemanı ve bir önceki/sonraki düğümün referansını tutar.</p>
 *
 * <p>Hem {@link List} hem de {@link java.util.Deque} arayüzlerini uygular; bu yüzden liste, kuyruk
 * (queue) ve yığın (stack) olarak kullanılabilir.</p>
 *
 * <ul>
 *     <li>Baştan / sondan ekleme-silme: <b>O(1)</b></li>
 *     <li>İndeks ile erişim: <b>O(n)</b> (düğümler tek tek dolaşılır)</li>
 *     <li>Iterator konumunda ekleme-silme: <b>O(1)</b></li>
 *     <li>Her eleman için ekstra düğüm nesnesi tuttuğundan bellek kullanımı ArrayList'ten fazladır.</li>
 * </ul>
 *
 * <p><b>Pratik not:</b> Kuyruk/yığın ihtiyacı için çoğunlukla {@link java.util.ArrayDeque},
 * liste ihtiyacı için {@link java.util.ArrayList} daha hızlıdır. LinkedList; iterator ile
 * sık sık ortadan ekleme/silme yapılan nadir senaryolarda avantajlıdır.</p>
 *
 * @author cemdirman
 */
public class LinkedListOrnegi {

    public static void main(String[] args) {
        baslik("LinkedList Örneği");

        bolum("List olarak kullanım");
        LinkedList<String> sehirler = new LinkedList<>();
        sehirler.add("İstanbul");
        sehirler.add("Sinop");
        sehirler.add("Ordu");
        yaz("sehirler", sehirler);
        yaz("getFirst()", sehirler.getFirst());

        sehirler.addFirst("Antalya");
        yaz("addFirst(Antalya) sonrası ilk", sehirler.getFirst());

        sehirler.addLast("Çanakkale");
        yaz("addLast(Çanakkale) sonrası", sehirler);
        yaz("size()", sehirler.size());
        yaz("element() (ilk eleman)", sehirler.element());
        yaz("get(2) -> O(n) erişim", sehirler.get(2));

        bolum("Deque (çift uçlu kuyruk) olarak kullanım");
        LinkedList<Integer> kuyruk = new LinkedList<>();
        kuyruk.offer(1);        // sona ekle
        kuyruk.offer(2);
        kuyruk.offerFirst(0);   // başa ekle
        yaz("kuyruk", kuyruk);
        yaz("peek() (silmeden bak)", kuyruk.peek());
        yaz("poll() (baştan al)", kuyruk.poll());
        yaz("pollLast() (sondan al)", kuyruk.pollLast());
        yaz("kalan", kuyruk);

        bolum("Stack (yığın) olarak kullanım - LIFO");
        LinkedList<String> yigin = new LinkedList<>();
        yigin.push("birinci");
        yigin.push("ikinci");
        yigin.push("üçüncü");
        yaz("yigin", yigin);
        yaz("pop()", yigin.pop());
        yaz("kalan", yigin);

        bolum("ListIterator ile çift yönlü dolaşma ve yerinde değişiklik");
        List<String> harfler = new LinkedList<>(List.of("a", "b", "c", "d"));
        ListIterator<String> it = harfler.listIterator();
        while (it.hasNext()) {
            String harf = it.next();
            if (harf.equals("b")) {
                it.add("b2");               // iterator konumuna O(1) ekleme
            } else if (harf.equals("c")) {
                it.set(harf.toUpperCase()); // son dönen elemanı değiştirme
            }
        }
        yaz("ileri dolaşma sonrası", harfler);

        var geri = new StringBuilder();
        while (it.hasPrevious()) {
            geri.append(it.previous()).append(' ');
        }
        yaz("geriye doğru", geri.toString().trim());

        bolum("Java 21+ SequencedCollection metotları");
        yaz("reversed()", sehirler.reversed());
        yaz("removeLast()", sehirler.removeLast());
        yaz("son durum", sehirler);
    }
}
