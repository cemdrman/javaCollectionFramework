# Queue ve Deque Arayüzleri

## Queue

`java.util.Queue`, işlenmeyi bekleyen elemanları tutan **kuyruk** yapısıdır. Genellikle **FIFO**
(ilk giren ilk çıkar) çalışır; `PriorityQueue` ise önceliğe göre çıkarır.

```text
Collection
  └── Queue
        ├── PriorityQueue
        ├── Deque
        │     ├── ArrayDeque
        │     ├── LinkedList
        │     └── BlockingDeque → LinkedBlockingDeque
        └── BlockingQueue  (java.util.concurrent, bkz. 11-concurrent.md)
              ├── ArrayBlockingQueue, LinkedBlockingQueue
              ├── PriorityBlockingQueue, DelayQueue
              └── SynchronousQueue, TransferQueue → LinkedTransferQueue
```

Her işlem için **iki metot ailesi** vardır:

| İşlem | Exception fırlatır | Özel değer döner |
|---|---|---|
| Ekleme | `add(e)` → `IllegalStateException` | `offer(e)` → `false` |
| Çıkarma | `remove()` → `NoSuchElementException` | `poll()` → `null` |
| Bakma | `element()` → `NoSuchElementException` | `peek()` → `null` |

Kapasite sınırlı kuyruklarda `offer`, kuyruğun boşalmasını beklenen bir durum olarak ele almak
için tercih edilir.

Örnek: [`QueueOrnegi.java`](../src/main/java/javacollectionframework/collections/queue/QueueOrnegi.java)

## Deque

`Deque` (double ended queue), **iki ucundan** ekleme/çıkarma yapılabilen kuyruktur. Bu sayede
hem kuyruk hem yığın olarak kullanılabilir.

| | Baş (first) | Son (last) |
|---|---|---|
| Ekleme | `addFirst`, `offerFirst`, `push` | `addLast`, `offerLast`, `add`, `offer` |
| Çıkarma | `removeFirst`, `pollFirst`, `pop`, `poll` | `removeLast`, `pollLast` |
| Bakma | `getFirst`, `peekFirst`, `peek` | `getLast`, `peekLast` |

- **Kuyruk olarak:** `offer` (sona) + `poll` (baştan)
- **Yığın olarak:** `push` (başa) + `pop` (baştan)

### ArrayDeque

Dairesel dizi (circular array) ile gerçekleştirilmiştir. İki uçta da amortize **O(1)**'dir.

> Java dokümantasyonu: *"Bu sınıf yığın olarak kullanıldığında `Stack`'ten, kuyruk olarak
> kullanıldığında `LinkedList`'ten muhtemelen daha hızlıdır."*

- `null` kabul etmez (çünkü `poll()`'un `null` dönmesi "boş" anlamına gelir).
- Thread-safe değildir.

Örnek: [`ArrayDequeOrnegi.java`](../src/main/java/javacollectionframework/collections/queue/ArrayDequeOrnegi.java)
— palindrom kontrolü ve kayan pencere maksimumu uygulamalarıyla.

## PriorityQueue

Dizi üzerinde tutulan bir **ikili yığın** (binary heap) ile gerçekleştirilmiştir.

- Varsayılan olarak **min-heap**: `poll()` en küçüğü verir.
- Max-heap için: `new PriorityQueue<>(Comparator.reverseOrder())`.
- `offer`/`poll` **O(log n)**, `peek` **O(1)**, `contains`/`remove(o)` **O(n)**.
- `toString()` ve iterator sırası **sıralı değildir**! Sıralı almak için `poll()` ile tek tek çekin.

```java
Queue<Hasta> acil = new PriorityQueue<>(
        Comparator.comparingInt(Hasta::aciliyet).reversed()
                  .thenComparingInt(Hasta::gelisSirasi));
```

Tipik kullanım alanları: görev zamanlayıcılar, Dijkstra / A* algoritmaları, "en büyük k eleman",
olay simülasyonları.

Örnek: [`PriorityQueueOrnegi.java`](../src/main/java/javacollectionframework/collections/queue/PriorityQueueOrnegi.java)

## Hangisini seçmeliyim?

| İhtiyaç | Seçim |
|---|---|
| Genel FIFO kuyruk | `ArrayDeque` |
| Yığın (stack) | `ArrayDeque` (`Stack` değil) |
| Önceliğe göre işleme | `PriorityQueue` |
| Üretici-tüketici, çok thread | `ArrayBlockingQueue` / `LinkedBlockingQueue` |
| Çok thread + öncelik | `PriorityBlockingQueue` |
| Kilitsiz, çok thread | `ConcurrentLinkedQueue` / `ConcurrentLinkedDeque` |
