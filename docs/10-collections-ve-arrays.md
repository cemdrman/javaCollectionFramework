# Collections ve Arrays Yardımcı Sınıfları

> `Collection` (tekil) bir **arayüzdür**; `Collections` (çoğul) ise koleksiyonlar üzerinde çalışan
> static metotlardan oluşan bir **yardımcı sınıftır**. Aynı ilişki `Array` / `Arrays` için de
> geçerlidir.

## java.util.Collections

| Grup | Metotlar |
|---|---|
| Sıralama | `sort(list)`, `sort(list, cmp)`, `reverseOrder()`, `reverseOrder(cmp)` |
| Arama | `binarySearch(list, key)` (liste sıralı olmalı), `min`, `max`, `frequency` |
| Düzenleme | `reverse`, `shuffle`, `swap`, `rotate`, `fill`, `copy`, `addAll`, `replaceAll` |
| Sorgulama | `disjoint`, `indexOfSubList`, `lastIndexOfSubList` |
| Hazır koleksiyonlar | `emptyList/Set/Map`, `singleton`, `singletonList`, `singletonMap`, `nCopies` |
| Salt-okunur görünüm | `unmodifiableList/Set/Map/Collection/SequencedCollection...` |
| Senkronize sarmalayıcı | `synchronizedList/Set/Map...` |
| Tip kontrollü | `checkedList/Set/Map...` |
| Dönüşüm | `list(enumeration)`, `enumeration(collection)`, `asLifoQueue(deque)`, `newSetFromMap(map)` |

### synchronized* sarmalayıcılarında dolaşma

Her metot çağrısı tek başına senkronizedir; ancak **dolaşma birden fazla çağrıdan oluşur** ve
elle kilitlenmelidir:

```java
List<String> liste = Collections.synchronizedList(new ArrayList<>());
synchronized (liste) {
    for (String s : liste) { ... }
}
```

Çok thread'li kodda genellikle `java.util.concurrent` koleksiyonları daha iyi bir seçimdir
(bkz. [11-concurrent.md](11-concurrent.md)).

Örnek: [`CollectionsSinifiOrnegi.java`](../src/main/java/javacollectionframework/yardimcilar/CollectionsSinifiOrnegi.java)

## java.util.Arrays

| Grup | Metotlar |
|---|---|
| Yazdırma | `toString`, `deepToString` |
| Sıralama | `sort` (ilkel: Dual-Pivot Quicksort, nesne: TimSort), `parallelSort` |
| Arama | `binarySearch` |
| Doldurma | `fill`, `setAll`, `parallelSetAll`, `parallelPrefix` |
| Kopyalama | `copyOf`, `copyOfRange` |
| Karşılaştırma | `equals`, `deepEquals`, `hashCode`, `deepHashCode`, `compare`, `mismatch` |
| Dönüşüm | `asList`, `stream`, `spliterator` |

### Sık yapılan hatalar

```java
int[] a = {1, 2, 3}, b = {1, 2, 3};
a.equals(b);               // false! diziler equals'ı override etmez
Arrays.equals(a, b);       // true

System.out.println(a);     // [I@1b6d3586
Arrays.toString(a);        // [1, 2, 3]

Arrays.asList(a);          // List<int[]>, tek elemanlı!
Arrays.stream(a).boxed().toList();  // List<Integer> [1, 2, 3]

List<String> l = Arrays.asList("x", "y");
l.add("z");                // UnsupportedOperationException (sabit boyut)
```

### Koleksiyon ↔ dizi

```java
String[] dizi = liste.toArray(String[]::new);       // Java 11+
List<String> kopya = List.of(dizi);                  // değiştirilemez kopya
List<String> degistirilebilir = new ArrayList<>(Arrays.asList(dizi));
```

Örnek: [`ArraysSinifiOrnegi.java`](../src/main/java/javacollectionframework/yardimcilar/ArraysSinifiOrnegi.java)
