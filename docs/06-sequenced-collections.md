# Sequenced Collections (Java 21+)

[JEP 431](https://openjdk.org/jeps/431) ile Java 21'de, **elemanlarının sırası tanımlı** olan
koleksiyonlar için ortak bir API eklendi.

## Sorun

Java 21'den önce ilk ve son elemana erişmenin tek tip bir yolu yoktu:

| | İlk eleman | Son eleman |
|---|---|---|
| `List` | `list.get(0)` | `list.get(list.size() - 1)` |
| `Deque` | `deque.getFirst()` | `deque.getLast()` |
| `SortedSet` | `set.first()` | `set.last()` |
| `LinkedHashSet` | `set.iterator().next()` | ❌ (sonuna kadar dolaşmak gerekir) |

Ters sırada dolaşmak için de her yapının farklı bir yöntemi vardı (`descendingIterator`,
`descendingSet`, `ListIterator`...).

## Çözüm: üç yeni arayüz

```text
           SequencedCollection            SequencedMap
            /        |       \                 |
         List      Deque   SequencedSet   LinkedHashMap
                           /         \    SortedMap → NavigableMap → TreeMap
                 LinkedHashSet    SortedSet → NavigableSet → TreeSet
```

### SequencedCollection

```java
E getFirst();          E getLast();
void addFirst(E e);    void addLast(E e);
E removeFirst();       E removeLast();
SequencedCollection<E> reversed();
```

### SequencedMap

```java
Map.Entry<K,V> firstEntry();      Map.Entry<K,V> lastEntry();
Map.Entry<K,V> pollFirstEntry();  Map.Entry<K,V> pollLastEntry();
V putFirst(K k, V v);             V putLast(K k, V v);
SequencedMap<K,V> reversed();
SequencedSet<K> sequencedKeySet();
SequencedCollection<V> sequencedValues();
SequencedSet<Map.Entry<K,V>> sequencedEntrySet();
```

## Dikkat edilmesi gerekenler

- `reversed()` bir **kopya değil görünümdür**. Görünüme yapılan ekleme/silme asıl koleksiyonu
  değiştirir.
- Sıranın kullanıcı tarafından değil, karşılaştırıcı tarafından belirlendiği `TreeSet`/`TreeMap`
  gibi yapılarda `addFirst`, `addLast`, `putFirst`, `putLast` → `UnsupportedOperationException`.
- `LinkedHashSet.addLast(e)` ve `LinkedHashMap.putLast(k, v)` var olan elemanı **sona taşır**;
  normal `add`/`put` ise konumunu değiştirmez.
- `HashSet` ve `HashMap` sırası tanımsız olduğu için bu arayüzleri **uygulamaz**.
- Değiştirilemez görünümler için: `Collections.unmodifiableSequencedCollection`,
  `unmodifiableSequencedSet`, `unmodifiableSequencedMap`.

Örnek: [`SequencedCollectionOrnegi.java`](../src/main/java/javacollectionframework/collections/sequenced/SequencedCollectionOrnegi.java)
