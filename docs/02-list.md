# List Arayüzü

`java.util.List`, **sıralı** (ordered) ve **indekslenebilir** bir koleksiyondur. Elemanlar
eklendikleri sırayı korur, aynı eleman birden fazla kez bulunabilir ve `null` eklenebilir
(gerçekleştirime bağlı).

```text
Iterable
  └── Collection
        └── SequencedCollection (Java 21+)
              └── List
                    ├── ArrayList
                    ├── LinkedList   (aynı zamanda Deque)
                    ├── Vector       (legacy)
                    │     └── Stack  (legacy)
                    └── CopyOnWriteArrayList (java.util.concurrent)
```

## Temel metotlar

| Metot | Açıklama |
|---|---|
| `add(e)` / `add(i, e)` | Sona / verilen indekse ekler |
| `get(i)` / `set(i, e)` | İndeksteki elemanı okur / değiştirir |
| `remove(i)` / `remove(o)` | İndekse / değere göre siler |
| `indexOf(o)` / `lastIndexOf(o)` | Elemanın ilk / son indeksini döner, yoksa `-1` |
| `contains(o)` | Eleman var mı? (`equals` ile karşılaştırır) |
| `subList(from, to)` | `[from, to)` aralığının **görünümünü** döner |
| `sort(comparator)` | Listeyi yerinde sıralar (Java 8+) |
| `removeIf(predicate)` | Koşulu sağlayanları siler (Java 8+) |
| `replaceAll(operator)` | Her elemanı dönüştürür (Java 8+) |
| `getFirst()` / `getLast()` | İlk / son eleman (Java 21+) |
| `addFirst(e)` / `addLast(e)` | Başa / sona ekler (Java 21+) |
| `reversed()` | Ters sıralı **görünüm** (Java 21+) |
| `List.of(...)` / `List.copyOf(c)` | Değiştirilemez liste oluşturur (Java 9+ / 10+) |

> ⚠️ `List<Integer>` üzerinde `remove(1)` çağrısı **1. indeksi** siler. Değeri silmek için
> `remove(Integer.valueOf(1))` kullanın.

## ArrayList

Arka planda **dinamik bir dizi** (`Object[]`) kullanır. Dizi dolunca yaklaşık %50 büyüyen yeni bir
dizi oluşturulur ve elemanlar kopyalanır.

- İndeks ile erişim **O(1)** olduğu için okuma ağırlıklı işlerde çok hızlıdır.
- Sona ekleme amortize **O(1)**, araya ekleme/silme **O(n)**'dir.
- Belleği verimli kullanır, CPU önbelleğiyle (cache locality) uyumludur.
- **Varsayılan `List` tercihiniz ArrayList olmalıdır.**

Örnek: [`ArrayListOrnegi.java`](../src/main/java/javacollectionframework/collections/list/ArrayListOrnegi.java)

## LinkedList

**Çift yönlü bağlı liste**dir. Her eleman bir düğüm (node) içinde, önceki ve sonraki düğümün
referansıyla birlikte saklanır. `List` ve `Deque` arayüzlerini birlikte uygular.

- Baş ve sona ekleme/silme **O(1)**.
- İndeks ile erişim **O(n)** — `get(i)` çağrısı düğümleri tek tek dolaşır.
- Her düğüm ekstra nesne olduğundan bellek kullanımı yüksektir.
- Kuyruk/yığın için `ArrayDeque`, liste için `ArrayList` genellikle daha hızlıdır.

Örnek: [`LinkedListOrnegi.java`](../src/main/java/javacollectionframework/collections/list/LinkedListOrnegi.java)

## Vector ve Stack (legacy)

`Vector`, Java 1.0'dan kalma senkronize dinamik dizidir; kapasitesi dolduğunda **iki katına**
çıkar. `Stack` ise Vector'dan türeyen LIFO yığınıdır.

- Bütün metotlar `synchronized` olduğundan tek thread'li kodda gereksiz yavaştır.
- `Stack`, Vector'dan miras aldığı `get(i)`, `add(i, e)` gibi metotlar yüzünden yığın
  soyutlamasını bozar.
- Yeni kodda: `Vector` yerine `ArrayList` (gerekirse `Collections.synchronizedList` ya da
  `CopyOnWriteArrayList`), `Stack` yerine `ArrayDeque` kullanın.

Örnekler: [`VectorOrnegi.java`](../src/main/java/javacollectionframework/collections/list/VectorOrnegi.java),
[`StackOrnegi.java`](../src/main/java/javacollectionframework/collections/list/StackOrnegi.java)

## Karşılaştırma

| İşlem | ArrayList | LinkedList | Vector |
|---|---|---|---|
| `get(i)` | O(1) | O(n) | O(1) |
| Sona ekleme | amortize O(1) | O(1) | amortize O(1) |
| Başa ekleme | O(n) | O(1) | O(n) |
| Ortadan silme | O(n) | O(n) arama + O(1) silme | O(n) |
| Thread-safe | Hayır | Hayır | Evet (kaba kilit) |
| Büyüme | ~%50 | düğüm bazlı | %100 |

## Liste dolaşma yolları

```java
for (int i = 0; i < liste.size(); i++) { ... }   // indeks gerekiyorsa (LinkedList'te yavaş!)
for (String s : liste) { ... }                     // en okunaklı
Iterator<String> it = liste.iterator();            // dolaşırken güvenli silme
while (it.hasNext()) { if (kosul(it.next())) it.remove(); }
liste.forEach(System.out::println);                // Java 8+
liste.removeIf(s -> s.isBlank());                  // silme için en kısa yol
```

Dolaşırken listeyi `liste.remove(...)` ile değiştirmek `ConcurrentModificationException`
fırlatır. Ayrıntılar için [Iterator](09-iterator.md) bölümüne bakın.
