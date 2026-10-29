package javacollectionframework.eszamanli;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

import static javacollectionframework.ortak.Konsol.baslik;
import static javacollectionframework.ortak.Konsol.bolum;
import static javacollectionframework.ortak.Konsol.yaz;

/**
 * <h2>BlockingQueue</h2>
 *
 * <p>{@link BlockingQueue}, kuyruk boşken alma işleminin, doluyken ekleme işleminin
 * <b>beklediği</b> (bloklandığı) thread-safe kuyruktur. <b>Üretici-tüketici</b>
 * (producer-consumer) probleminin standart çözümüdür.</p>
 *
 * <pre>
 *              Exception   Özel değer   Bloklar   Zaman aşımı
 *   Ekleme     add(e)      offer(e)     put(e)    offer(e, süre, birim)
 *   Çıkarma    remove()    poll()       take()    poll(süre, birim)
 *   Bakma      element()   peek()       -         -
 * </pre>
 *
 * <ul>
 *     <li>{@link ArrayBlockingQueue}: sabit kapasiteli, dizi tabanlı.</li>
 *     <li>{@link LinkedBlockingQueue}: isteğe bağlı kapasiteli (varsayılan Integer.MAX_VALUE),
 *         ekleme ve çıkarma için ayrı kilitler.</li>
 *     <li>{@link PriorityBlockingQueue}: sınırsız, önceliğe göre çıkarır.</li>
 *     <li>{@link DelayQueue}: elemanlar süreleri dolduğunda alınabilir.</li>
 *     <li>{@link SynchronousQueue}: kapasitesi sıfırdır; her {@code put} bir {@code take} bekler.</li>
 * </ul>
 *
 * @author cemdirman
 */
public class BlockingQueueOrnegi {

    private static final String BITTI = "<<BİTTİ>>"; // "zehirli hap" (poison pill)

    record Hatirlatma(String mesaj, long zamanNanos) implements Delayed {

        static Hatirlatma sonra(String mesaj, long milisaniye) {
            return new Hatirlatma(mesaj, System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(milisaniye));
        }

        @Override
        public long getDelay(TimeUnit birim) {
            return birim.convert(zamanNanos - System.nanoTime(), TimeUnit.NANOSECONDS);
        }

        @Override
        public int compareTo(Delayed diger) {
            return Long.compare(getDelay(TimeUnit.NANOSECONDS), diger.getDelay(TimeUnit.NANOSECONDS));
        }
    }

    public static void main(String[] args) throws InterruptedException {
        baslik("BlockingQueue Örnekleri");

        bolum("Üretici-tüketici (ArrayBlockingQueue, kapasite 3)");
        BlockingQueue<String> kuyruk = new ArrayBlockingQueue<>(3);
        List<String> tuketilen = Collections.synchronizedList(new ArrayList<>());
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            executor.submit(() -> {                  // üretici
                for (int i = 1; i <= 10; i++) {
                    kuyruk.put("sipariş-" + i);    // kuyruk doluysa bekler
                }
                kuyruk.put(BITTI);
                return null;
            });
            executor.submit(() -> {                  // tüketici
                while (true) {
                    String siparis = kuyruk.take(); // kuyruk boşsa bekler
                    if (siparis.equals(BITTI)) {
                        break;
                    }
                    tuketilen.add(siparis);
                }
                return null;
            });
        }
        yaz("tüketilen (FIFO sırasıyla)", tuketilen);

        bolum("offer / poll ile zaman aşımı");
        BlockingQueue<Integer> dolu = new ArrayBlockingQueue<>(1);
        yaz("offer(1)", dolu.offer(1));
        yaz("offer(2) (dolu)", dolu.offer(2));
        yaz("offer(2, 50ms) (bekle, sonra vazgeç)", dolu.offer(2, 50, TimeUnit.MILLISECONDS));
        yaz("remainingCapacity()", dolu.remainingCapacity());
        dolu.clear();
        yaz("poll(50ms) (boş)", dolu.poll(50, TimeUnit.MILLISECONDS));

        bolum("drainTo: kuyruğu toplu boşaltma");
        BlockingQueue<Integer> linked = new LinkedBlockingQueue<>(List.of(1, 2, 3, 4, 5));
        List<Integer> hedef = new ArrayList<>();
        int aktarilan = linked.drainTo(hedef, 3);
        yaz("drainTo(hedef, 3)", aktarilan + " eleman -> " + hedef + ", kalan " + linked);

        bolum("PriorityBlockingQueue");
        BlockingQueue<Integer> oncelikli = new PriorityBlockingQueue<>(List.of(5, 1, 4, 2, 3));
        var sira = new ArrayList<Integer>();
        oncelikli.drainTo(sira);
        yaz("drainTo (öncelik sırası)", sira);

        bolum("DelayQueue: süresi dolan eleman alınabilir");
        DelayQueue<Hatirlatma> gecikmeli = new DelayQueue<>();
        gecikmeli.put(Hatirlatma.sonra("ikinci (120ms)", 120));
        gecikmeli.put(Hatirlatma.sonra("birinci (60ms)", 60));
        yaz("hemen poll() (süre dolmadı)", gecikmeli.poll());
        yaz("take()", gecikmeli.take().mesaj());
        yaz("take()", gecikmeli.take().mesaj());

        bolum("SynchronousQueue: doğrudan el değiştirme");
        SynchronousQueue<String> elden = new SynchronousQueue<>();
        yaz("offer() (alan yok)", elden.offer("paket"));
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            executor.submit(() -> {
                elden.put("paket");           // bir tüketici alana kadar bekler
                return null;
            });
            yaz("take()", elden.take());
        }
        yaz("size() (her zaman 0)", elden.size());
    }
}
