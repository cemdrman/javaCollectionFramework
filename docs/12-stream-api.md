# Stream API, Collectors ve Gatherers

Stream API (Java 8+), koleksiyonlardaki veriyi **ne yapılacağını** söyleyerek (bildirimsel)
işlemeyi sağlar. Koleksiyonlar veriyi **saklar**, stream'ler veriyi **işler**.

```java
List<String> sonuc = calisanlar.stream()            // 1. kaynak
        .filter(c -> c.maas() > 70_000)             // 2. ara işlemler (lazy)
        .sorted(Comparator.comparingInt(Calisan::maas).reversed())
        .map(Calisan::ad)
        .toList();                                  // 3. uç işlem
```

## Temel kurallar

- Ara işlemler **tembeldir** (lazy); uç işlem çağrılmadan hiçbir şey çalışmaz.
- Stream kaynağı **değiştirmez**.
- Bir stream **yalnızca bir kez** tüketilebilir.
- Lambda'lar yan etkisiz (stateless, side-effect free) olmalıdır; özellikle paralel stream'lerde.

## Sık kullanılan işlemler

| Ara işlemler | Uç işlemler |
|---|---|
| `filter`, `map`, `mapToInt`, `flatMap` | `toList()` (Java 16+), `collect(...)` |
| `distinct`, `sorted`, `limit`, `skip` | `forEach`, `reduce`, `count` |
| `peek` | `min`, `max`, `findFirst`, `findAny` |
| `takeWhile`, `dropWhile` (Java 9+) | `anyMatch`, `allMatch`, `noneMatch` |
| `mapMulti` (Java 16+) | `toArray`, `sum`, `average`, `summaryStatistics` |
| `gather` (Java 24+) | |

## Collectors

| Collector | Sonuç |
|---|---|
| `toList()`, `toSet()`, `toCollection(TreeSet::new)` | Koleksiyon |
| `toUnmodifiableList/Set/Map()` | Değiştirilemez koleksiyon |
| `toMap(k, v)`, `toMap(k, v, birlestir, MapSupplier)` | Map (tekrarlı anahtarda birleştirici şart!) |
| `groupingBy(f)`, `groupingBy(f, downstream)`, `groupingBy(f, MapSupplier, downstream)` | `Map<K, List<T>>` / özelleştirilmiş |
| `partitioningBy(predicate)` | `Map<Boolean, List<T>>` |
| `counting()`, `summingInt`, `averagingInt`, `summarizingInt` | Sayısal özet |
| `minBy`, `maxBy` | `Optional<T>` |
| `mapping`, `filtering`, `flatMapping` (Java 9+) | Downstream dönüşümleri |
| `joining(ayrac, onek, sonek)` | `String` |
| `teeing(c1, c2, birlestir)` (Java 12+) | İki collector'ın birleşik sonucu |
| `collectingAndThen(c, f)` | Sonuca son bir dönüşüm uygular |

```java
// Departmana göre ortalama maaş, departman sıralı
Map<Departman, Double> ort = calisanlar.stream()
        .collect(Collectors.groupingBy(Calisan::departman, TreeMap::new,
                 Collectors.averagingInt(Calisan::maas)));
```

Örnek: [`StreamVeCollectorsOrnegi.java`](../src/main/java/javacollectionframework/stream/StreamVeCollectorsOrnegi.java)

## Stream Gatherers (Java 24+)

[JEP 485](https://openjdk.org/jeps/485) ile gelen `Stream.gather(Gatherer)` metodu, **özel ara
işlemler** yazmayı sağlar (Collector'ların ara işlem karşılığı).

| Hazır gatherer | Ne yapar? | Örnek |
|---|---|---|
| `Gatherers.windowFixed(n)` | Sabit boyutlu gruplar | `[1..7]` → `[[1,2,3],[4,5,6],[7]]` |
| `Gatherers.windowSliding(n)` | Kayan pencere | `[1,2,3,4]` → `[[1,2,3],[2,3,4]]` |
| `Gatherers.scan(init, f)` | Kümülatif sonuçlar | `[5,3,8]` → `[5,8,16]` |
| `Gatherers.fold(init, f)` | Tek sonuca indirger | `["j","a","v","a"]` → `"java"` |
| `Gatherers.mapConcurrent(n, f)` | En fazla `n` sanal thread ile eşzamanlı map, sıra korunur | Paralel HTTP çağrıları |

### Kendi gatherer'ınızı yazmak

```java
static <T, K> Gatherer<T, ?, T> anahtaraGoreBenzersiz(Function<? super T, ? extends K> anahtar) {
    return Gatherer.<T, HashSet<K>, T>ofSequential(
            HashSet::new,                                        // durum (state)
            Gatherer.Integrator.ofGreedy((gorulen, eleman, asagi) ->
                    !gorulen.add(anahtar.apply(eleman)) || asagi.push(eleman)));
}
```

Bir gatherer dört parçadan oluşur: **initializer** (durum oluşturur), **integrator** (her eleman
için çalışır, `push` ile aşağı akışa eleman gönderir), **combiner** (paralel çalışmada durumları
birleştirir) ve **finisher** (sonda kalan durumu boşaltır).

Örnek: [`GatherersOrnegi.java`](../src/main/java/javacollectionframework/stream/GatherersOrnegi.java)

## Paralel stream'ler

`stream().parallel()` / `parallelStream()` işi Fork/Join havuzuna dağıtır.

- CPU yoğun, büyük veri setlerinde fayda sağlar; küçük veride genellikle **daha yavaştır**.
- `ArrayList`, diziler ve `IntStream.range` iyi bölünür; `LinkedList` ve `Stream.iterate` kötü bölünür.
- Paylaşılan değiştirilebilir duruma yazmayın; `collect` kullanın.
