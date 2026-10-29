# Set Arayüzü

`java.util.Set`, matematikteki **küme** kavramının karşılığıdır: **aynı eleman birden fazla
bulunamaz**. "Aynı" olmanın anlamı gerçekleştirime göre değişir:

- `HashSet` / `LinkedHashSet` → `hashCode()` + `equals()`
- `TreeSet` → `compareTo()` / `compare()` sonucu `0` ise
- `EnumSet` → enum sabitinin kendisi

```text
Collection
  └── Set
        ├── HashSet
        │     └── LinkedHashSet          (SequencedSet, Java 21+)
        ├── SortedSet
        │     └── NavigableSet
        │           ├── TreeSet
        │           └── ConcurrentSkipListSet (java.util.concurrent)
        ├── EnumSet
        └── CopyOnWriteArraySet          (java.util.concurrent)
```

## HashSet

Arka planda `HashMap` kullanır; elemanlar map'in anahtarıdır, değer olarak sabit bir nesne
saklanır.

- `add`, `remove`, `contains` ortalama **O(1)**.
- Dolaşma sırası **tanımsızdır** ve zamanla değişebilir. Sıraya güvenmeyin.
- Bir tane `null` elemana izin verir.

### equals ve hashCode sözleşmesi

Kendi sınıflarınızı `HashSet`'e (veya `HashMap` anahtarına) koyacaksanız:

1. `a.equals(b)` ise `a.hashCode() == b.hashCode()` **olmak zorundadır**.
2. Eşit hashCode, eşitlik anlamına gelmez (çakışma olabilir).
3. Set içindeki bir nesnenin `equals/hashCode`'unu etkileyen alanlarını **değiştirmeyin**;
   nesne "kaybolur" (`contains` false döner ama eleman hâlâ oradadır).

Java 16+ **record**'lar `equals`, `hashCode` ve `toString`'i tüm bileşenlere göre otomatik
üretir ve değiştirilemez olduğu için set elemanı/map anahtarı olarak idealdir.

```java
record Sehir(String ad, int plaka) {}
Set<Sehir> s = new HashSet<>();
s.add(new Sehir("İstanbul", 34));
s.contains(new Sehir("İstanbul", 34)); // true
```

Örnek: [`HashSetOrnegi.java`](../src/main/java/javacollectionframework/collections/set/HashSetOrnegi.java)

## LinkedHashSet

HashSet + elemanları birbirine bağlayan çift yönlü liste. **Ekleme sırasını korur.**
Java 21 ile `SequencedSet` oldu: `getFirst()`, `getLast()`, `addFirst()`, `addLast()`,
`removeFirst()`, `removeLast()`, `reversed()`.

Listeden sırayı bozmadan tekrarları silmenin en kısa yolu:

```java
List<String> benzersiz = List.copyOf(new LinkedHashSet<>(liste));
// veya: liste.stream().distinct().toList();
```

Örnek: [`LinkedHashSetOrnegi.java`](../src/main/java/javacollectionframework/collections/set/LinkedHashSetOrnegi.java)

## TreeSet

Kırmızı-siyah ağaç (red-black tree) ile gerçekleştirilmiş, **her zaman sıralı** settir.

- `add`, `remove`, `contains` **O(log n)**.
- Elemanlar ya `Comparable` olmalı ya da constructor'a `Comparator` verilmelidir.
- `null` kabul etmez.
- Comparator `0` döndürüyorsa iki eleman **aynı** kabul edilir. Örneğin
  `String.CASE_INSENSITIVE_ORDER` ile `"cem"` ve `"Cem"` tek eleman olur.
- Türkçe karakterleri doğru sıralamak için `Collator.getInstance(Locale.of("tr", "TR"))`
  kullanın; `String`'in doğal sıralaması Unicode değerine göredir (`Ç`, `İ`, `Ş` en sona düşer).

`NavigableSet` metotları ile aralık sorguları:

| Metot | Anlamı |
|---|---|
| `first()` / `last()` | En küçük / en büyük |
| `floor(e)` / `ceiling(e)` | `<= e` en büyük / `>= e` en küçük |
| `lower(e)` / `higher(e)` | `< e` en büyük / `> e` en küçük |
| `headSet(e)` / `tailSet(e)` / `subSet(a, b)` | Aralık görünümleri |
| `pollFirst()` / `pollLast()` | En küçüğü / en büyüğü çıkarır |
| `descendingSet()` | Ters sıralı görünüm |

Örnek: [`TreeSetOrnegi.java`](../src/main/java/javacollectionframework/collections/set/TreeSetOrnegi.java)

## EnumSet

Tek bir enum tipinin sabitlerine özel, **bit vektörü** tabanlı set. Bütün işlemler O(1)'dir ve
diğer setlerden çok daha hızlı ve hafiftir. Eski `int` bit flag'lerinin (`OKU | YAZ`) tip güvenli
karşılığıdır.

```java
EnumSet.allOf(Gun.class);
EnumSet.of(Gun.PAZAR, Gun.SALI);
EnumSet.range(Gun.PAZARTESI, Gun.CUMA);
EnumSet.complementOf(haftaIci);
```

Örnek: [`EnumSetOrnegi.java`](../src/main/java/javacollectionframework/collections/set/EnumSetOrnegi.java)

## Küme işlemleri

| Matematik | Java (yerinde değiştirir) |
|---|---|
| A ∪ B | `a.addAll(b)` |
| A ∩ B | `a.retainAll(b)` |
| A \ B | `a.removeAll(b)` |
| B ⊆ A | `a.containsAll(b)` |

## Karşılaştırma

| | HashSet | LinkedHashSet | TreeSet | EnumSet |
|---|---|---|---|---|
| Sıra | Yok | Ekleme sırası | Sıralı | `ordinal` sırası |
| add/contains | O(1) | O(1) | O(log n) | O(1) |
| `null` | 1 adet | 1 adet | Hayır | Hayır |
| Ne zaman? | Varsayılan | Sıra önemliyse | Sıralı/aralık sorgusu | Enum değerleri |
