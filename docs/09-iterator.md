# Iterator, ListIterator, Iterable ve Spliterator

## Iterable ve Iterator

`Iterable<T>` arayüzünü uygulayan her sınıf (bütün `Collection`'lar dahil) **for-each**
döngüsünde kullanılabilir. Derleyici for-each döngüsünü bir `Iterator` döngüsüne çevirir:

```java
for (String s : liste) { ... }
// ↓ derleyici bunu üretir
for (Iterator<String> it = liste.iterator(); it.hasNext(); ) {
    String s = it.next();
    ...
}
```

| Metot | Açıklama |
|---|---|
| `hasNext()` | Sırada eleman var mı? |
| `next()` | Sıradaki elemanı döner; yoksa `NoSuchElementException` |
| `remove()` | Son dönen elemanı **güvenle** siler (isteğe bağlı) |
| `forEachRemaining(action)` | Kalan elemanlar için işlem yapar (Java 8+) |

Kendi sınıfınızı `Iterable` yaparak for-each'te kullanılabilir hale getirebilirsiniz
(bkz. `IteratorOrnegi.Aralik`).

## ListIterator

Yalnızca `List`'lerde bulunur ve **iki yönlü** dolaşma sağlar: `hasPrevious()`,
`previous()`, `nextIndex()`, `previousIndex()`, dolaşırken `set(e)` ve `add(e)`.

## Fail-fast ve ConcurrentModificationException

`ArrayList`, `HashMap` gibi `java.util` koleksiyonları içeride bir **değişiklik sayacı**
(`modCount`) tutar. Iterator her `next()` çağrısında bu sayacın değişip değişmediğine bakar;
koleksiyon iterator **dışından** değiştirildiyse `ConcurrentModificationException` fırlatır.

```java
for (String s : liste) {
    if (kosul(s)) liste.remove(s);   // ❌ ConcurrentModificationException
}

Iterator<String> it = liste.iterator();
while (it.hasNext()) {
    if (kosul(it.next())) it.remove(); // ✅
}

liste.removeIf(s -> kosul(s));         // ✅ en kısa yol
```

> Fail-fast davranışı **en iyi çaba** (best-effort) esasına dayanır. Örneğin sondan bir önceki
> elemanı silerseniz `hasNext()` false döndüğü için exception oluşmayabilir. Bu exception'a
> güvenerek program yazmayın; yalnızca hata tespiti içindir. Ayrıca adı yanıltıcıdır: tek
> thread'li kodda da oluşur.

## Weakly consistent iterator'lar

`java.util.concurrent` koleksiyonları (`ConcurrentHashMap`, `CopyOnWriteArrayList`,
`ConcurrentLinkedQueue`...) dolaşma sırasında değişikliğe izin verir ve
`ConcurrentModificationException` **fırlatmaz**.

- `CopyOnWriteArrayList`: iterator oluşturulduğu andaki **anlık görüntüyü** (snapshot) dolaşır.
- `ConcurrentHashMap`: dolaşma başladıktan sonraki değişiklikleri yansıtabilir ya da
  yansıtmayabilir.

## Spliterator (Java 8+)

"Splittable iterator": paralel stream'lerin veriyi parçalara bölmesini sağlar.

- `trySplit()` — elemanların bir kısmını yeni bir Spliterator'a ayırır.
- `tryAdvance(action)` / `forEachRemaining(action)` — eleman işler.
- `estimateSize()` — kalan eleman sayısı tahmini.
- `characteristics()` — `ORDERED`, `SIZED`, `SORTED`, `DISTINCT`, `NONNULL`, `IMMUTABLE`,
  `CONCURRENT`, `SUBSIZED`.

```java
Stream<Integer> s = StreamSupport.stream(iterable.spliterator(), false);
```

Örnek: [`IteratorOrnegi.java`](../src/main/java/javacollectionframework/iterasyon/IteratorOrnegi.java)
