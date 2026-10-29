# Veri Yapıları: Koleksiyonlar İçeride Nasıl Çalışır?

`veri_yapilari` paketinde Java'nın kendi koleksiyonlarının **sadeleştirilmiş** halleri sıfırdan
yazılmıştır. Amaç, bir `ArrayList`'in neden indekse göre hızlı, bir `HashMap`'in neden
`hashCode`'a bu kadar bağımlı olduğunu kodu okuyarak anlamaktır.

Her sınıfın davranışı, `src/test` altındaki JUnit testleriyle Java'nın kendi sınıfıyla
**rastgele binlerce işlem** üzerinden karşılaştırılır.

```bash
./mvnw test
```

## BenimArrayList — dinamik dizi

```text
elemanlar: [ a | b | c | d | · | · ]     boyut = 4, kapasite = 6
                                ▲
                        add(e) buraya yazar (O(1))
```

- Dolu dizi `kapasite + kapasite/2` boyutunda yeni diziye kopyalanır (`Arrays.copyOf`).
  4 → 6 → 9 → 13 → 19 → 28...
- `add(i, e)` / `remove(i)`: `System.arraycopy` ile sağdaki elemanlar kaydırılır → **O(n)**.
- `remove` sonrası boşalan hücre `null` yapılır; aksi halde nesne GC tarafından toplanamaz
  (bellek sızıntısı).
- `modSayaci` her yapısal değişiklikte artar; iterator bunu kontrol ederek **fail-fast** olur.

Kod: [`BenimArrayList.java`](../src/main/java/javacollectionframework/veri_yapilari/BenimArrayList.java)

## BenimLinkedList — çift yönlü bağlı liste

```text
null ◄── [A] ◄──► [B] ◄──► [C] ──► null
          ▲                 ▲
         bas               son
```

- Baş/son ekleme ve silme yalnızca birkaç referans değiştirir → **O(1)**.
- `get(i)` düğümleri tek tek dolaşır → **O(n)**. İndeks listenin ikinci yarısındaysa
  dolaşmaya sondan başlanır.
- `tersCevir()` her düğümün `onceki`/`sonraki` referanslarını yer değiştirerek O(n)'de çalışır.

Kod: [`BenimLinkedList.java`](../src/main/java/javacollectionframework/veri_yapilari/BenimLinkedList.java)

## BenimHashMap — ayrı zincirlemeli hash tablosu

```text
put("elma", 3)
  1. h = "elma".hashCode() ^ (h >>> 16)      // üst bitleri karıştır
  2. i = h & (kapasite - 1)                   // kapasite 2'nin kuvveti → h % kapasite
  3. tablo[i] zincirinde "elma" equals ile aranır
  4. bulunursa değer güncellenir, yoksa zincirin başına eklenir
  5. boyut > kapasite * 0.75 ise tablo 2 katına çıkar (rehash)
```

- Kötü bir `hashCode` (ör. hep `42` dönen) bütün anahtarları **aynı kovaya** yığar; map yine
  doğru çalışır ama her işlem zinciri dolaşır ve **O(n)** olur. Java'nın asıl HashMap'i bu
  durumda zinciri kırmızı-siyah ağaca çevirerek O(log n)'e iner.
- 16 kova ile başlayan tablo 13. elemanda 32'ye, 25. elemanda 64'e büyür.

Kod: [`BenimHashMap.java`](../src/main/java/javacollectionframework/veri_yapilari/BenimHashMap.java)

## BenimStack — dizi tabanlı yığın

Tepe eleman dizinin sonundadır; `push`, `pop` ve `peek` **O(1)**'dir. Boş yığında
`EmptyStackException` fırlatılır.

Kod: [`BenimStack.java`](../src/main/java/javacollectionframework/veri_yapilari/BenimStack.java)

## Çalıştırma

Hepsinin iç davranışını gösteren örnek:
[`VeriYapilariOrnegi.java`](../src/main/java/javacollectionframework/veri_yapilari/VeriYapilariOrnegi.java)

```bash
./mvnw -q compile exec:java -Dexec.mainClass=javacollectionframework.veri_yapilari.VeriYapilariOrnegi
```
