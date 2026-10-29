# Değiştirilemez Koleksiyonlar

Değiştirilemez (immutable) koleksiyonlar thread'ler arasında güvenle paylaşılabilir, savunmacı
kopyalama ihtiyacını azaltır ve hatalı değişiklikleri derleme sonrası değil, ilk denemede
(`UnsupportedOperationException`) ortaya çıkarır.

## Üç farklı kavram

| | Nasıl | Kaynak değişirse? | `null` |
|---|---|---|---|
| **Salt-okunur görünüm** | `Collections.unmodifiableList(l)` | Görünüm de değişir | Kaynağa bağlı |
| **Değiştirilemez kopya** | `List.copyOf(l)`, `stream.toList()` | Etkilenmez | `copyOf`: hayır, `toList`: evet |
| **Fabrika** | `List.of(...)`, `Set.of(...)`, `Map.of(...)` | — | Hayır |

```java
List<String> kaynak  = new ArrayList<>(List.of("a", "b"));
List<String> gorunum = Collections.unmodifiableList(kaynak);
List<String> kopya   = List.copyOf(kaynak);
kaynak.add("c");
// gorunum → [a, b, c]   kopya → [a, b]
```

## Fabrika metotları (Java 9+)

```java
List.of("a", "b", "c");
Set.of(1, 2, 3);
Map.of("bir", 1, "iki", 2);                     // en fazla 10 çift
Map.ofEntries(Map.entry("bir", 1), Map.entry("iki", 2));
List.copyOf(koleksiyon);  Set.copyOf(koleksiyon);  Map.copyOf(map);   // Java 10+
```

Kurallar:

- `null` → `NullPointerException` (hatta `contains(null)` bile).
- `Set.of`/`Map.of` içinde tekrar → `IllegalArgumentException`.
- `Set.of`/`Map.of` dolaşma sırası JVM her başladığında değişebilir; **sıraya güvenmeyin**.
- `add`, `remove`, `set`, `put`, `clear`, `sort`... → `UnsupportedOperationException`.

## Stream sonuçları

| | Değiştirilebilir mi? | `null` |
|---|---|---|
| `stream.toList()` (Java 16+) | Hayır | Evet |
| `Collectors.toList()` | Belirtilmemiş (pratikte `ArrayList`) | Evet |
| `Collectors.toUnmodifiableList()` (Java 10+) | Hayır | Hayır |

## Arrays.asList tuzağı

`Arrays.asList(dizi)` **sabit boyutlu** ve diziye **bağlı** bir liste döner:

- `set` çalışır ve diziyi değiştirir.
- `add`/`remove` → `UnsupportedOperationException`.

## Yüzeysel değiştirilemezlik

`List.of(new StringBuilder("x"))` listesinin elemanları değiştirilemez ama elemanın **içi**
değiştirilebilir. Tam değiştirilemezlik için elemanların da değiştirilemez olması gerekir
(`String`, `Integer`, `record`, `LocalDate`...).

Örnek: [`DegistirilemezKoleksiyonOrnegi.java`](../src/main/java/javacollectionframework/collections/degistirilemez/DegistirilemezKoleksiyonOrnegi.java)
