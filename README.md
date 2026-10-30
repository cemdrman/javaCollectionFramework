# **Java Collection Framework**

## **Bu proje Java için Türkçe kaynak oluşturmak amacıyla oluşturulmuştur.**

Java Collection Framework'ün bütün temel arayüz ve sınıflarını; çalıştırılabilir örnekler,
kod içi açıklamalar ve konu anlatımlarıyla **Java 25 (LTS)** üzerinden anlatır.

- **collections** paketi altında Java'nın kendi veri yapılarının (List, Set, Queue, Map...)
  kullanımını anlatan örnekler bulunur.
- **docs** klasöründe her konunun geniş anlatımı bulunur.
- **veri_yapilari** paketi altında ise Java'nın içinde bulunan bu yapıların aslında nasıl
  çalıştığını anlayabileceğiniz, bizler tarafından kodlanmış örnekler ve testleri bulunur.

## Gereksinimler

- **JDK 25** veya üzeri ([Adoptium Temurin](https://adoptium.net), [Oracle JDK](https://www.oracle.com/java/technologies/downloads/) vb.)
- Maven kurmanıza gerek yoktur; proje Maven Wrapper (`mvnw`) ile gelir.

## Çalıştırma

```bash
git clone https://github.com/cemdrman/java-collection-framework.git
cd java-collection-framework
```

Derleme ve testler:

```bash
./mvnw verify
```

Örnek menüsü (Windows'ta `mvnw.cmd`):

```bash
./mvnw -q compile exec:java                        # bütün örnekleri numaralarıyla listeler
./mvnw -q compile exec:java -Dexec.args="7"        # 7 numaralı örneği çalıştırır
./mvnw -q compile exec:java -Dexec.args="treemap"  # adında "treemap" geçen örnekleri çalıştırır
./mvnw -q compile exec:java -Dexec.args="hepsi"    # hepsini sırayla çalıştırır
```

Hiç derleme yapmadan, tek bir dosyayla Java 25'in kompakt kaynak dosyalarını
(`void main()`, otomatik importlar, `IO.println`) denemek için:

```bash
java ornekler/HizliBaslangic.java
```

Belirli bir sınıfı doğrudan çalıştırmak için:

```bash
./mvnw -q compile exec:java -Dexec.mainClass=javacollectionframework.collections.list.ArrayListOrnegi
```

Örnekleri IntelliJ IDEA, Eclipse, VS Code veya NetBeans'te projeyi **Maven projesi olarak
açıp** ilgili sınıfın `main` metodunu çalıştırarak da deneyebilirsiniz.

## İçindekiler

| # | Konu | Anlatım | Örnekler |
|---|---|---|---|
| 1 | Genel bakış, hiyerarşi, seçim rehberi | [01-genel-bakis](docs/01-genel-bakis.md) | — |
| 2 | **List**: ArrayList, LinkedList, Vector, Stack | [02-list](docs/02-list.md) | [`collections/list`](src/main/java/javacollectionframework/collections/list) |
| 3 | **Set**: HashSet, LinkedHashSet, TreeSet, EnumSet | [03-set](docs/03-set.md) | [`collections/set`](src/main/java/javacollectionframework/collections/set) |
| 4 | **Queue / Deque**: ArrayDeque, PriorityQueue | [04-queue-deque](docs/04-queue-deque.md) | [`collections/queue`](src/main/java/javacollectionframework/collections/queue) |
| 5 | **Map**: HashMap, LinkedHashMap, TreeMap, EnumMap, IdentityHashMap, WeakHashMap, Hashtable | [05-map](docs/05-map.md) | [`collections/map`](src/main/java/javacollectionframework/collections/map) |
| 6 | **Sequenced Collections** (Java 21+) | [06-sequenced-collections](docs/06-sequenced-collections.md) | [`collections/sequenced`](src/main/java/javacollectionframework/collections/sequenced) |
| 7 | Değiştirilemez koleksiyonlar: `List.of`, `copyOf`, unmodifiable | [07-degistirilemez-koleksiyonlar](docs/07-degistirilemez-koleksiyonlar.md) | [`collections/degistirilemez`](src/main/java/javacollectionframework/collections/degistirilemez) |
| 8 | Comparable ve Comparator | [08-comparable-comparator](docs/08-comparable-comparator.md) | [`siralama`](src/main/java/javacollectionframework/siralama) |
| 9 | Iterator, ListIterator, fail-fast, Spliterator | [09-iterator](docs/09-iterator.md) | [`iterasyon`](src/main/java/javacollectionframework/iterasyon) |
| 10 | `Collections` ve `Arrays` yardımcı sınıfları | [10-collections-ve-arrays](docs/10-collections-ve-arrays.md) | [`yardimcilar`](src/main/java/javacollectionframework/yardimcilar) |
| 11 | Eşzamanlı koleksiyonlar: ConcurrentHashMap, BlockingQueue, CopyOnWrite... | [11-concurrent](docs/11-concurrent.md) | [`eszamanli`](src/main/java/javacollectionframework/eszamanli) |
| 12 | Stream API, Collectors ve **Gatherers** (Java 24+) | [12-stream-api](docs/12-stream-api.md) | [`stream`](src/main/java/javacollectionframework/stream) |
| 13 | Kendi ArrayList, LinkedList, HashMap ve Stack'imizi yazmak | [13-veri-yapilari](docs/13-veri-yapilari.md) | [`veri_yapilari`](src/main/java/javacollectionframework/veri_yapilari) |
| 14 | **Alıştırmalar** (çözümleri ve testleriyle) | [14-alistirmalar](docs/14-alistirmalar.md) | [`alistirmalar`](src/main/java/javacollectionframework/alistirmalar) |

Ek kaynak: [OOP Kavramları (PDF)](docs/oop/OOP_Kavramları.pdf)

## Proje yapısı

```text
java-collection-framework
├── docs/                         Konu anlatımları
├── ornekler/HizliBaslangic.java  Java 25 kompakt kaynak dosyası ile hızlı başlangıç
├── src/main/java/javacollectionframework
│   ├── collections/
│   │   ├── list/                 ArrayList, LinkedList, Vector, Stack
│   │   ├── set/                  HashSet, LinkedHashSet, TreeSet, EnumSet
│   │   ├── queue/                Queue, ArrayDeque, PriorityQueue
│   │   ├── map/                  HashMap, LinkedHashMap, TreeMap, EnumMap, özel map'ler
│   │   ├── sequenced/            SequencedCollection / SequencedMap
│   │   └── degistirilemez/       List.of, copyOf, unmodifiable
│   ├── siralama/                 Comparable, Comparator
│   ├── iterasyon/                Iterator, ListIterator, Spliterator
│   ├── yardimcilar/              Collections, Arrays
│   ├── eszamanli/                java.util.concurrent koleksiyonları
│   ├── stream/                   Stream API, Collectors, Gatherers
│   ├── veri_yapilari/            Kendi yazdığımız veri yapıları
│   ├── alistirmalar/             Alıştırma çözümleri
│   ├── performans/               Performans karşılaştırması
│   └── ortak/                    Örneklerin ortak yardımcı sınıfı
├── src/test/java/...             veri_yapilari ve alıştırma testleri (JUnit 5)
├── pom.xml
└── mvnw / mvnw.cmd               Maven Wrapper
```

## Örnek çıktı

```text
============================================================
 TreeSet Örneği
============================================================

--- Türkçe alfabeye göre sıralama (Collator) ---
Collator(tr)                          : [Ankara, Çorum, Iğdır, İzmir, Ordu, Sinop, Şırnak, Uşak]
Doğal sıralama (Unicode, yanlış)      : [Ankara, Iğdır, Ordu, Sinop, Uşak, Çorum, İzmir, Şırnak]
```

### **Projeye katkı sağlamak isteyenler iletişime geçebilir.**
