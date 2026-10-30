# Java Collection Framework'e Genel Bakış

**Collection Framework**, bir grup nesneyi saklamak, düzenlemek ve işlemek için Java 1.2 ile
gelen; **arayüzler**, bu arayüzlerin **gerçekleştirimleri** (implementation) ve **algoritmalardan**
oluşan birleşik bir mimaridir.

Neden önemli?

- **Ortak dil:** `List`, `Set`, `Map` gibi arayüzler sayesinde kodunuzu gerçekleştirimden
  bağımsız yazabilirsiniz (`List<String> l = new ArrayList<>();`).
- **Hazır, test edilmiş, hızlı** veri yapıları ve algoritmalar (`sort`, `binarySearch`...).
- **Birlikte çalışabilirlik:** bütün koleksiyonlar birbirine dönüştürülebilir, stream'e
  çevrilebilir.

## Hiyerarşi

```text
Iterable<E>
└── Collection<E>
    ├── List<E>                      (SequencedCollection, Java 21+)
    │   ├── ArrayList
    │   ├── LinkedList               (aynı zamanda Deque)
    │   └── Vector                   (legacy)
    │       └── Stack                (legacy)
    ├── Set<E>
    │   ├── HashSet
    │   │   └── LinkedHashSet        (SequencedSet, Java 21+)
    │   ├── SortedSet<E>
    │   │   └── NavigableSet<E>
    │   │       └── TreeSet
    │   └── EnumSet
    └── Queue<E>
        ├── PriorityQueue
        └── Deque<E>                 (SequencedCollection, Java 21+)
            ├── ArrayDeque
            └── LinkedList

Map<K,V>                             (Collection'dan türemez)
├── HashMap
│   └── LinkedHashMap                (SequencedMap, Java 21+)
├── SortedMap<K,V>
│   └── NavigableMap<K,V>
│       └── TreeMap
├── EnumMap
├── IdentityHashMap
├── WeakHashMap
└── Hashtable                        (legacy)
    └── Properties
```

`java.util.concurrent` paketi bunlara thread-safe karşılıklar ekler: `ConcurrentHashMap`,
`CopyOnWriteArrayList`, `ConcurrentSkipListMap`, `BlockingQueue` ailesi... (bkz.
[11-concurrent.md](11-concurrent.md)).

## Temel arayüzler

| Arayüz | Sıra | Tekrar | Erişim | Tipik gerçekleştirim |
|---|---|---|---|---|
| `List` | Ekleme sırası | ✅ | İndeks | `ArrayList` |
| `Set` | Yok / ekleme / sıralı | ❌ | `contains` | `HashSet` |
| `Queue` | FIFO / öncelik | ✅ | Baş | `ArrayDeque`, `PriorityQueue` |
| `Deque` | İki uç | ✅ | Baş ve son | `ArrayDeque` |
| `Map` | Yok / ekleme / sıralı | Anahtar ❌ | Anahtar | `HashMap` |

## Collection arayüzünün ortak metotları

```java
boolean add(E e);              boolean remove(Object o);
boolean addAll(Collection c);  boolean removeAll(Collection c);
boolean retainAll(Collection c); boolean removeIf(Predicate p);   // Java 8+
boolean contains(Object o);    boolean containsAll(Collection c);
int size();  boolean isEmpty();  void clear();
Iterator<E> iterator();        Stream<E> stream();               // Java 8+
Object[] toArray();            <T> T[] toArray(IntFunction<T[]> f); // Java 11+
```

## Hangi koleksiyonu seçmeliyim?

```text
Anahtar → değer eşlemesi mi?
├── Evet → Map
│     ├── Sıra önemsiz ............................ HashMap
│     ├── Ekleme sırası / LRU ..................... LinkedHashMap
│     ├── Sıralı anahtar / aralık sorgusu ......... TreeMap
│     ├── Anahtarlar enum ......................... EnumMap
│     └── Çok thread .............................. ConcurrentHashMap
└── Hayır
      ├── Tekrarlar olmamalı mı? → Set
      │     ├── Sıra önemsiz ...................... HashSet
      │     ├── Ekleme sırası ..................... LinkedHashSet
      │     ├── Sıralı ............................ TreeSet
      │     └── Enum değerleri .................... EnumSet
      ├── Baştan/sondan işleme mi? → Queue / Deque
      │     ├── FIFO kuyruk veya yığın ............ ArrayDeque
      │     ├── Önceliğe göre ..................... PriorityQueue
      │     └── Üretici-tüketici .................. ArrayBlockingQueue / LinkedBlockingQueue
      └── İndeksli, sıralı liste → List
            ├── Genel kullanım .................... ArrayList
            ├── Okuma çok, yazma az, çok thread ... CopyOnWriteArrayList
            └── Değişmeyecek ...................... List.of(...)
```

## Karmaşıklık özeti

| | get / erişim | add | remove | contains |
|---|---|---|---|---|
| `ArrayList` | O(1) | O(1)* | O(n) | O(n) |
| `LinkedList` | O(n) | O(1) | O(1)** | O(n) |
| `ArrayDeque` | O(1) uçlar | O(1)* | O(1) uçlar | O(n) |
| `PriorityQueue` | O(1) peek | O(log n) | O(log n) poll | O(n) |
| `HashSet` / `HashMap` | O(1) | O(1) | O(1) | O(1) |
| `LinkedHashSet` / `LinkedHashMap` | O(1) | O(1) | O(1) | O(1) |
| `TreeSet` / `TreeMap` | O(log n) | O(log n) | O(log n) | O(log n) |
| `EnumSet` / `EnumMap` | O(1) | O(1) | O(1) | O(1) |

\* amortize &nbsp;&nbsp; \*\* düğüm/iterator konumu biliniyorsa; indeksle silme O(n)

Bu farkları kendi makinenizde gözlemlemek için:
[`PerformansKarsilastirmaOrnegi.java`](../src/main/java/javacollectionframework/performans/PerformansKarsilastirmaOrnegi.java)
(ör. 20.000 elemanlı listede rastgele `get(i)`: ArrayList ~0,1 ms, LinkedList ~165 ms).

## Sürümlere göre önemli yenilikler

| Sürüm | Yenilik |
|---|---|
| Java 1.2 | Collection Framework |
| Java 5 | Generics, for-each, `java.util.concurrent`, `Queue`, `EnumSet/EnumMap` |
| Java 6 | `Deque`, `ArrayDeque`, `NavigableSet/Map` |
| Java 7 | Diamond operatörü `<>` |
| Java 8 | Stream API, lambda, `forEach`, `removeIf`, `Map.merge/compute`, `Spliterator`, HashMap ağaç kovaları |
| Java 9 | `List.of`, `Set.of`, `Map.of`, `takeWhile/dropWhile` |
| Java 10 | `List.copyOf`, `Collectors.toUnmodifiableList`, `var` |
| Java 11 | `Collection.toArray(IntFunction)` |
| Java 12 | `Collectors.teeing` |
| Java 16 | `Stream.toList()`, `mapMulti`, record'lar |
| Java 19 | `HashMap.newHashMap(n)`, `HashSet.newHashSet(n)` |
| Java 21 | **Sequenced Collections**, sanal thread'ler, record pattern'ları |
| Java 22 | İsimsiz değişkenler `_` |
| Java 24 | **Stream Gatherers** |
| Java 25 (LTS) | Kompakt kaynak dosyaları ve instance `main` metotları, `java.lang.IO` |
