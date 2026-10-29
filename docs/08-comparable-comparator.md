# Comparable ve Comparator

Sıralı koleksiyonlar (`TreeSet`, `TreeMap`, `PriorityQueue`) ve sıralama metotları
(`List.sort`, `Collections.sort`, `Arrays.sort`, `stream.sorted`) elemanları nasıl
karşılaştıracaklarını bu iki arayüzden öğrenir.

| | `Comparable<T>` | `Comparator<T>` |
|---|---|---|
| Paket | `java.lang` | `java.util` |
| Metot | `int compareTo(T o)` | `int compare(T a, T b)` |
| Nerede tanımlanır? | Sınıfın içinde | Sınıfın dışında |
| Kaç tane olabilir? | Bir (doğal sıralama) | Sınırsız |
| Örnek | `String`, `Integer`, `LocalDate` | `String.CASE_INSENSITIVE_ORDER` |

Dönüş değeri: **negatif** → ilk önce, **0** → eşit, **pozitif** → ikinci önce.

## Comparable

```java
record Ogrenci(int numara, String ad) implements Comparable<Ogrenci> {
    @Override
    public int compareTo(Ogrenci diger) {
        return Integer.compare(numara, diger.numara);
    }
}
```

> ⚠️ `return this.numara - diger.numara;` yazmayın. `Integer.MIN_VALUE` gibi değerlerde
> **taşma** (overflow) yüzünden yanlış sonuç üretir. `Integer.compare` / `Long.compare` kullanın.

## Comparator (Java 8+ yardımcıları)

```java
Comparator.comparing(Ogrenci::ad)                       // anahtar seçici
Comparator.comparingInt(String::length)                 // ilkel tiplerde kutulama yok
Comparator.comparing(Ogrenci::bolum)
          .thenComparing(Ogrenci::ortalama, Comparator.reverseOrder())
          .thenComparing(Ogrenci::ad)                    // çok kriterli
comparator.reversed()                                   // ters çevirme
Comparator.naturalOrder() / Comparator.reverseOrder()
Comparator.nullsFirst(cmp) / Comparator.nullsLast(cmp)  // null güvenli
```

## Sözleşme

1. **İşaret simetrisi:** `sgn(compare(a, b)) == -sgn(compare(b, a))`
2. **Geçişlilik:** `a > b` ve `b > c` ise `a > c`
3. **Tutarlılık:** `compare(a, b) == 0` ise `a` ve `b` her `c` ile aynı sonucu vermeli
4. **Önerilen:** `compare(a, b) == 0` ⇔ `a.equals(b)` (equals ile tutarlılık)

4. madde sağlanmazsa `HashSet` ve `TreeSet` aynı veriyle **farklı** sonuç verir. Örneğin
`BigDecimal("2.0")` ve `BigDecimal("2.00")`: `compareTo` → eşit, `equals` → farklı;
`HashSet` 2, `TreeSet` 1 eleman tutar.

## Türkçe metin sıralama

`String`'in doğal sıralaması Unicode kod değerine göredir: `"Çorum"`, `"İzmir"`, `"Şırnak"`
listenin sonuna düşer. Doğru alfabetik sıra için:

```java
Collator tr = Collator.getInstance(Locale.of("tr", "TR"));
liste.sort(tr);
```

Örnek: [`ComparableComparatorOrnegi.java`](../src/main/java/javacollectionframework/siralama/ComparableComparatorOrnegi.java)
