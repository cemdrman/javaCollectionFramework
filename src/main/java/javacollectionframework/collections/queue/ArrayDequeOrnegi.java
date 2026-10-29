package javacollectionframework.collections.queue;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

import static javacollectionframework.ortak.Konsol.baslik;
import static javacollectionframework.ortak.Konsol.bolum;
import static javacollectionframework.ortak.Konsol.yaz;

/**
 * <h2>Deque ve ArrayDeque</h2>
 *
 * <p>{@link Deque} (double ended queue - "dek" diye okunur), iki ucundan da ekleme ve çıkarma
 * yapılabilen kuyruktur. Bu sayede hem <b>kuyruk (FIFO)</b> hem de <b>yığın (LIFO)</b> olarak
 * kullanılabilir.</p>
 *
 * <p>{@link ArrayDeque}, Deque'nin <b>dairesel dizi</b> (circular array) ile gerçekleştirimidir.</p>
 * <ul>
 *     <li>İki uçta ekleme/çıkarma: amortize <b>O(1)</b></li>
 *     <li>{@link java.util.Stack}'ten ve kuyruk olarak {@link java.util.LinkedList}'ten daha hızlıdır.</li>
 *     <li>{@code null} kabul etmez, thread-safe değildir.</li>
 * </ul>
 *
 * <pre>
 *               Baş (first)                  Son (last)
 *   Ekleme      addFirst / offerFirst / push addLast / offerLast / add / offer
 *   Çıkarma     removeFirst / pollFirst / pop removeLast / pollLast
 *   Bakma       getFirst / peekFirst / peek  getLast / peekLast
 * </pre>
 *
 * @author cemdirman
 */
public class ArrayDequeOrnegi {

    public static void main(String[] args) {
        baslik("Deque / ArrayDeque Örneği");

        bolum("İki uçlu kullanım");
        Deque<Integer> deque = new ArrayDeque<>();
        deque.offerLast(2);
        deque.offerLast(3);
        deque.offerFirst(1);
        deque.offerFirst(0);
        yaz("deque", deque);
        yaz("peekFirst() / peekLast()", deque.peekFirst() + " / " + deque.peekLast());
        yaz("pollFirst()", deque.pollFirst());
        yaz("pollLast()", deque.pollLast());
        yaz("kalan", deque);

        bolum("Kuyruk (FIFO) olarak: offer + poll");
        Deque<String> kuyruk = new ArrayDeque<>();
        kuyruk.offer("a");
        kuyruk.offer("b");
        kuyruk.offer("c");
        yaz("poll() sırası", kuyruk.poll() + " " + kuyruk.poll() + " " + kuyruk.poll());

        bolum("Yığın (LIFO) olarak: push + pop");
        Deque<String> yigin = new ArrayDeque<>();
        yigin.push("a");
        yigin.push("b");
        yigin.push("c");
        yaz("pop() sırası", yigin.pop() + " " + yigin.pop() + " " + yigin.pop());

        bolum("Ters yönde dolaşma");
        Deque<Character> harfler = new ArrayDeque<>();
        for (char c : "JAVA".toCharArray()) {
            harfler.add(c);
        }
        var sb = new StringBuilder();
        Iterator<Character> it = harfler.descendingIterator();
        it.forEachRemaining(sb::append);
        yaz("descendingIterator()", sb);
        yaz("reversed() (Java 21+)", harfler.reversed());

        bolum("Uygulama: palindrom kontrolü");
        for (String kelime : new String[]{"kabak", "ey edip adanada pide ye", "java"}) {
            yaz(kelime, palindromMu(kelime));
        }

        bolum("Uygulama: kayan pencere (sliding window) maksimumu");
        int[] sicakliklar = {12, 15, 11, 9, 18, 21, 17, 14, 16};
        yaz("sıcaklıklar", java.util.Arrays.toString(sicakliklar));
        yaz("3 günlük pencere maksimumları", java.util.Arrays.toString(pencereMaksimumlari(sicakliklar, 3)));
    }

    static boolean palindromMu(String metin) {
        Deque<Character> d = new ArrayDeque<>();
        for (char c : metin.toCharArray()) {
            if (Character.isLetterOrDigit(c)) {
                d.addLast(Character.toLowerCase(c));
            }
        }
        while (d.size() > 1) {
            if (!d.pollFirst().equals(d.pollLast())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Deque içinde indeksleri, değerleri azalan sırada olacak şekilde tutar. Böylece her
     * pencerenin maksimumu deque'nin başındadır. Toplam karmaşıklık O(n)'dir.
     */
    static int[] pencereMaksimumlari(int[] dizi, int k) {
        int[] sonuc = new int[dizi.length - k + 1];
        Deque<Integer> indeksler = new ArrayDeque<>();
        for (int i = 0; i < dizi.length; i++) {
            if (!indeksler.isEmpty() && indeksler.peekFirst() <= i - k) {
                indeksler.pollFirst();                 // pencereden çıkan indeks
            }
            while (!indeksler.isEmpty() && dizi[indeksler.peekLast()] <= dizi[i]) {
                indeksler.pollLast();                  // artık maksimum olamayacaklar
            }
            indeksler.offerLast(i);
            if (i >= k - 1) {
                sonuc[i - k + 1] = dizi[indeksler.peekFirst()];
            }
        }
        return sonuc;
    }
}
