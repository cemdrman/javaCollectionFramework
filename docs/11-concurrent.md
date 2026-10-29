# Eşzamanlı (Concurrent) Koleksiyonlar

`ArrayList`, `HashMap`, `HashSet` gibi standart koleksiyonlar **thread-safe değildir**. Birden fazla
thread aynı anda yazarsa veri kaybolabilir, iç yapı bozulabilir ya da
`ConcurrentModificationException` oluşabilir.

Thread-safe koleksiyon elde etmenin üç yolu vardır:

| Yöntem | Örnek | Not |
|---|---|---|
| Legacy senkronize sınıflar | `Vector`, `Hashtable`, `Stack` | Tek kilit, yavaş. Kullanmayın. |
| Senkronize sarmalayıcılar | `Collections.synchronizedMap(m)` | Tek kilit; dolaşırken elle kilitleme gerekir |
| **`java.util.concurrent`** | `ConcurrentHashMap`, `CopyOnWriteArrayList`... | İnce taneli kilit / kilitsiz algoritmalar ✅ |

## java.util.concurrent koleksiyonları

| Arayüz | Gerçekleştirim | Özellik |
|---|---|---|
| `ConcurrentMap` | `ConcurrentHashMap` | Kova bazlı kilit, atomik `merge/compute` |
| `ConcurrentNavigableMap` | `ConcurrentSkipListMap` | Sıralı, skip list, O(log n) |
| `NavigableSet` | `ConcurrentSkipListSet` | Sıralı, skip list |
| `List` | `CopyOnWriteArrayList` | Her yazmada kopya; okuma ağırlıklı |
| `Set` | `CopyOnWriteArraySet` | Her yazmada kopya; küçük setler |
| `Queue` | `ConcurrentLinkedQueue` | Kilitsiz (CAS), sınırsız |
| `Deque` | `ConcurrentLinkedDeque` | Kilitsiz, iki uçlu |
| `BlockingQueue` | `ArrayBlockingQueue` | Sabit kapasite, tek kilit |
| | `LinkedBlockingQueue` | İsteğe bağlı kapasite, iki kilit |
| | `PriorityBlockingQueue` | Önceliğe göre, sınırsız |
| | `DelayQueue` | Süresi dolan eleman alınır |
| | `SynchronousQueue` | Kapasite 0, doğrudan el değiştirme |
| `BlockingDeque` | `LinkedBlockingDeque` | İki uçlu bloklayan kuyruk |
| `TransferQueue` | `LinkedTransferQueue` | Üretici, tüketici alana kadar bekleyebilir |

## ConcurrentHashMap

- Okumalar kilitsiz, yazmalar yalnızca ilgili kovayı kilitler.
- `putIfAbsent`, `merge`, `compute`, `computeIfAbsent`, `replace(k, eski, yeni)` **atomiktir**.
- `null` anahtar/değer kabul etmez.
- Paralel toplu işlemler: `forEach`, `search`, `reduce` (`parallelismThreshold` parametresiyle).
- Sayaç olarak `ConcurrentHashMap<K, LongAdder>` kullanmak yüksek çekişmede çok hızlıdır.

```java
// ❌ Atomik DEĞİL: iki işlem arasında başka thread araya girebilir
if (!map.containsKey(k)) map.put(k, v);
map.put(k, map.get(k) + 1);

// ✅ Atomik
map.putIfAbsent(k, v);
map.merge(k, 1, Integer::sum);
```

Örnek: [`ConcurrentHashMapOrnegi.java`](../src/main/java/javacollectionframework/eszamanli/ConcurrentHashMapOrnegi.java)
— 1000 sanal thread (Java 21+) ile `HashMap` ve `ConcurrentHashMap` karşılaştırması.

## BlockingQueue ve üretici-tüketici

| | Exception | Özel değer | **Bloklar** | Zaman aşımı |
|---|---|---|---|---|
| Ekleme | `add` | `offer` | `put` | `offer(e, t, birim)` |
| Çıkarma | `remove` | `poll` | `take` | `poll(t, birim)` |

```java
BlockingQueue<Siparis> kuyruk = new ArrayBlockingQueue<>(100);
// üretici thread
kuyruk.put(siparis);        // doluysa bekler
// tüketici thread
Siparis s = kuyruk.take();  // boşsa bekler
```

Tüketiciyi durdurmak için kuyruğa özel bir "**zehirli hap**" (poison pill) eleman konabilir.

Örnek: [`BlockingQueueOrnegi.java`](../src/main/java/javacollectionframework/eszamanli/BlockingQueueOrnegi.java)

## CopyOnWrite ve SkipList koleksiyonları

- **CopyOnWriteArrayList**: Her `add/set/remove` iç dizinin yeni bir kopyasını oluşturur
  (O(n)). Iterator anlık görüntüde çalışır, dolaşırken değişiklik serbesttir. Olay dinleyici
  listeleri gibi **çok okunan, nadiren değişen** veriler için idealdir.
- **ConcurrentSkipListMap/Set**: `TreeMap`/`TreeSet`'in eşzamanlı karşılığıdır.
- **ConcurrentLinkedQueue**: Kilitsiz kuyruk; `size()` O(n)'dir, `isEmpty()` tercih edin.

Örnek: [`DigerEsZamanliKoleksiyonlarOrnegi.java`](../src/main/java/javacollectionframework/eszamanli/DigerEsZamanliKoleksiyonlarOrnegi.java)

## Sanal thread'ler (Java 21+) ve koleksiyonlar

Örneklerde `Executors.newVirtualThreadPerTaskExecutor()` kullanılmıştır. Sanal thread'ler çok
ucuz olduğu için binlercesi kolayca oluşturulabilir; bu da yarış durumlarını (race condition)
göstermeyi kolaylaştırır. `try-with-resources` bloğu sonunda executor bütün görevlerin
bitmesini bekler.
