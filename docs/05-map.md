# Map Arayüzü

`java.util.Map<K, V>`, **anahtar → değer** eşlemesi tutar. Anahtarlar benzersizdir; aynı
anahtarla yapılan ikinci `put` eski değerin üzerine yazar ve eski değeri döner.

> Map, `Collection` arayüzünden **türemez**; ancak Collection Framework'ün bir parçasıdır ve
> `keySet()`, `values()`, `entrySet()` görünümleri aracılığıyla koleksiyonlarla birlikte çalışır.

```text
Map
 ├── HashMap
 │     └── LinkedHashMap            (SequencedMap, Java 21+)
 ├── SortedMap
 │     └── NavigableMap
 │           ├── TreeMap
 │           └── ConcurrentSkipListMap   (java.util.concurrent)
 ├── EnumMap
 ├── IdentityHashMap
 ├── WeakHashMap
 ├── Hashtable (legacy)
 │     └── Properties
 └── ConcurrentMap
       └── ConcurrentHashMap        (java.util.concurrent)
```

## Temel metotlar

| Metot | Açıklama |
|---|---|
| `put(k, v)` | Ekler/günceller, eski değeri döner |
| `get(k)` / `getOrDefault(k, d)` | Değeri okur; yoksa `null` / `d` |
| `containsKey(k)` / `containsValue(v)` | O(1) / O(n) |
| `remove(k)` | Siler, eski değeri döner |
| `keySet()` / `values()` / `entrySet()` | Canlı **görünümler** |
| `putIfAbsent(k, v)` | Yalnızca anahtar yoksa ekler |
| `computeIfAbsent(k, f)` | Yoksa hesaplar ve ekler — gruplama için ideal |
| `computeIfPresent(k, f)` | Varsa yeniden hesaplar; `null` dönerse siler |
| `compute(k, f)` | Her durumda yeniden hesaplar |
| `merge(k, v, f)` | Yoksa `v` ekler, varsa `f(eski, v)` — sayma için ideal |
| `replaceAll(f)` | Bütün değerleri dönüştürür |
| `Map.of(...)` / `Map.ofEntries(...)` / `Map.copyOf(m)` | Değiştirilemez map |
| `Map.entry(k, v)` | Değiştirilemez çift |

### Sık kullanılan kalıplar

```java
// Kelime sayma
frekans.merge(kelime, 1, Integer::sum);

// Gruplama (Map<K, List<V>>)
gruplar.computeIfAbsent(anahtar, k -> new ArrayList<>()).add(deger);

// Verimli dolaşma
for (var e : map.entrySet()) { e.getKey(); e.getValue(); }
map.forEach((k, v) -> ...);
```

## HashMap

Hash tablosu ile gerçekleştirilmiştir.

1. `hashCode()` değeri karıştırılır ve kova (bucket) indeksine dönüştürülür.
2. Aynı kovaya düşen girdiler bağlı listede tutulur; bir kovada **8**'den fazla girdi olursa
   (ve tablo en az 64 kova ise) liste **kırmızı-siyah ağaca** dönüşür (Java 8+). Böylece kötü
   hash fonksiyonlarında bile en kötü durum O(log n) olur.
3. Eleman sayısı `kapasite × loadFactor` (varsayılan `16 × 0.75 = 12`) değerini aşınca tablo
   iki katına çıkar ve girdiler yeniden dağıtılır (rehash).

- `get`/`put` ortalama **O(1)**.
- Bir `null` anahtara izin verir.
- **Değiştirilebilir nesneleri anahtar yapmayın.** Anahtar olarak `String`, `Integer`, enum veya
  **record** kullanın.
- Kaç eleman olacağı biliniyorsa `HashMap.newHashMap(n)` (Java 19+) ile rehash maliyetinden
  kaçının.

Örnek: [`HashMapOrnegi.java`](../src/main/java/javacollectionframework/collections/map/HashMapOrnegi.java)

## LinkedHashMap

HashMap + girdileri bağlayan çift yönlü liste.

- **Ekleme sırası** (varsayılan) ya da **erişim sırası** (`new LinkedHashMap<>(16, 0.75f, true)`).
- `removeEldestEntry` override edilerek birkaç satırda **LRU önbellek** yazılabilir.
- Java 21 ile `SequencedMap`: `firstEntry()`, `lastEntry()`, `putFirst()`, `putLast()`,
  `pollFirstEntry()`, `pollLastEntry()`, `reversed()`, `sequencedKeySet()`...

Örnek: [`LinkedHashMapOrnegi.java`](../src/main/java/javacollectionframework/collections/map/LinkedHashMapOrnegi.java)

## TreeMap

Kırmızı-siyah ağaç ile gerçekleştirilmiş, anahtarları **sıralı** tutan map.

- Bütün temel işlemler **O(log n)**.
- `null` anahtar kabul etmez.
- `NavigableMap` metotları: `floorKey/Entry`, `ceilingKey/Entry`, `lowerKey`, `higherKey`,
  `headMap`, `tailMap`, `subMap`, `descendingMap`, `pollFirstEntry`...
- Aralık tabanlı eşlemeler (not → harf notu, saat → etkinlik, IP aralığı → ülke) için idealdir.

Örnek: [`TreeMapOrnegi.java`](../src/main/java/javacollectionframework/collections/map/TreeMapOrnegi.java)

## EnumMap

Anahtarları enum olan map. Arka planda `ordinal()` ile indekslenen bir dizi tutar; hash
hesaplanmadığı için HashMap'ten hızlıdır. Anahtarlar tanım sırasıyla dolaşılır.

Örnek: [`EnumMapOrnegi.java`](../src/main/java/javacollectionframework/collections/map/EnumMapOrnegi.java)

## Özel amaçlı map'ler

| Sınıf | Özellik | Kullanım |
|---|---|---|
| `IdentityHashMap` | Anahtarları `==` ile karşılaştırır | Nesne grafı dolaşma, proxy'ler |
| `WeakHashMap` | Anahtarlar zayıf referanslı; GC silebilir | Nesnelere metadata iliştirme |
| `Hashtable` | Legacy, senkronize, `null` yok | Yeni kodda `ConcurrentHashMap` kullanın |
| `Properties` | `String` → `String` yapılandırma | `.properties` dosyaları |

Örnek: [`OzelMaplerOrnegi.java`](../src/main/java/javacollectionframework/collections/map/OzelMaplerOrnegi.java)

## Karşılaştırma

| | HashMap | LinkedHashMap | TreeMap | EnumMap | ConcurrentHashMap |
|---|---|---|---|---|---|
| Sıra | Yok | Ekleme / erişim | Sıralı anahtar | `ordinal` | Yok |
| get/put | O(1) | O(1) | O(log n) | O(1) | O(1) |
| `null` anahtar | 1 adet | 1 adet | Hayır | Hayır | Hayır |
| `null` değer | Evet | Evet | Evet | Evet | Hayır |
| Thread-safe | Hayır | Hayır | Hayır | Hayır | Evet |
