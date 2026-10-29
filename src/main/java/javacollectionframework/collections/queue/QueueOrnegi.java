package javacollectionframework.collections.queue;

import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

import static javacollectionframework.ortak.Konsol.baslik;
import static javacollectionframework.ortak.Konsol.bolum;
import static javacollectionframework.ortak.Konsol.yaz;

/**
 * <h2>Queue</h2>
 *
 * <p>{@link Queue}, elemanların işlenmeyi beklediği <b>kuyruk</b> yapısıdır. Genellikle
 * <b>FIFO</b> (First In First Out - ilk giren ilk çıkar) mantığıyla çalışır.</p>
 *
 * <p>Queue her işlem için iki metot sunar. Biri başarısız olduğunda <b>exception fırlatır</b>,
 * diğeri <b>özel bir değer</b> ({@code false} ya da {@code null}) döner:</p>
 *
 * <pre>
 *              Exception fırlatır   Özel değer döner
 *   Ekleme     add(e)               offer(e)
 *   Çıkarma    remove()             poll()
 *   Bakma      element()            peek()
 * </pre>
 *
 * @author cemdirman
 */
public class QueueOrnegi {

    public static void main(String[] args) {
        baslik("Queue Örneği");

        bolum("FIFO - bankadaki sıra");
        // Queue bir arayüzdür; ArrayDeque en hızlı genel amaçlı gerçekleştirimdir.
        Queue<String> sira = new ArrayDeque<>();
        sira.offer("1. müşteri");
        sira.offer("2. müşteri");
        sira.offer("3. müşteri");
        yaz("sıra", sira);
        yaz("peek() (sıradaki)", sira.peek());
        while (!sira.isEmpty()) {
            yaz("poll() ile işlenen", sira.poll());
        }

        bolum("Boş kuyrukta iki metot ailesinin farkı");
        yaz("poll() boş kuyrukta", sira.poll());
        yaz("peek() boş kuyrukta", sira.peek());
        try {
            sira.remove();
        } catch (NoSuchElementException e) {
            yaz("remove() boş kuyrukta", "NoSuchElementException");
        }
        try {
            sira.element();
        } catch (NoSuchElementException e) {
            yaz("element() boş kuyrukta", "NoSuchElementException");
        }

        bolum("null elemanlar");
        // ArrayDeque null kabul etmez; çünkü poll()'un 'kuyruk boş' anlamında null döndürmesiyle
        // karışırdı. LinkedList ise (tarihsel nedenlerle) null kabul eder; bu önerilmez.
        try {
            new ArrayDeque<String>().offer(null);
        } catch (NullPointerException e) {
            yaz("ArrayDeque.offer(null)", "NullPointerException");
        }
        Queue<String> bagli = new LinkedList<>();
        bagli.offer(null);
        yaz("LinkedList.offer(null) -> size()", bagli.size());
    }
}
