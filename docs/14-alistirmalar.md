# Alıştırmalar

Konuları pekiştirmek için koleksiyonlarla çözülen 8 problem. Her birinde **hangi koleksiyonun
neden** seçildiğini düşünmek asıl kazanımdır.

Önce kendiniz çözmeyi deneyin. Çözümler
[`Cozumler.java`](../src/main/java/javacollectionframework/alistirmalar/Cozumler.java) dosyasında,
testleri ise
[`CozumlerTest.java`](../src/test/java/javacollectionframework/alistirmalar/CozumlerTest.java)
dosyasındadır. Kendi çözümünüzü `Cozumler` sınıfına yazıp testlerle kontrol edebilirsiniz:

```bash
./mvnw test -Dtest=CozumlerTest
```

---

### 1. İlk tekrar eden eleman

Bir listede **ilk kez tekrar eden** elemanı bulun; yoksa `Optional.empty()` dönün.

```text
[5, 3, 8, 3, 5]  →  3      (3, 5'ten önce tekrar ediyor)
[1, 2, 3]        →  boş
```

<details><summary>İpucu</summary>

`Set.add` eleman zaten varsa `false` döner. Hedef: O(n).
</details>

### 2. İki toplam

Toplamı `hedef` olan iki elemanın indekslerini bulun.

```text
[2, 7, 11, 15], hedef 9  →  [0, 1]
[3, 2, 4],      hedef 6  →  [1, 2]
```

<details><summary>İpucu</summary>

İç içe döngü O(n²)'dir. Her eleman için `hedef - eleman` değerini daha önce gördünüz mü?
Değer → indeks eşlemesi için `HashMap` kullanın. Hedef: O(n).
</details>

### 3. Anagram grupları

Aynı harflerden oluşan kelimeleri, ilk görülme sırasını koruyarak gruplayın.

```text
[elma, kitap, emla, java, male, kapit]  →  [[elma, emla, male], [kitap, kapit], [java]]
```

<details><summary>İpucu</summary>

Anagramların harfleri sıralanınca aynı metin elde edilir. Sırayı korumak için
`LinkedHashMap` + `computeIfAbsent`.
</details>

### 4. İlk benzersiz karakter

Metinde yalnızca bir kez geçen **ilk** karakteri bulun.

```text
"koleksiyon"  →  'l'
"aabb"        →  boş
```

<details><summary>İpucu</summary>

Hem saymak hem de sırayı hatırlamak gerekiyor: `LinkedHashMap` + `merge`.
</details>

### 5. En sık geçen k kelime

Bir metinde en sık geçen `k` kelimeyi, çoktan aza doğru dönün. Büyük/küçük harf ayrımı
yapmayın, noktalama işaretlerini yok sayın. Frekansı eşit olan kelimelerde alfabetik sıra
geçerlidir.

```text
"c b a b a", k = 2  →  [a, b]
```

<details><summary>İpucu</summary>

Önce `HashMap` ile sayın. Hepsini sıralamak O(n log n)'dir; boyutu `k` olan bir min-heap
(`PriorityQueue`) ile O(n log k)'ya inebilirsiniz.
</details>

### 6. Tekrarsız en uzun alt metin

İçinde hiçbir karakterin tekrar etmediği en uzun **bitişik** alt metni bulun.

```text
"abcabcbb"    →  "abc"
"pwwkew"      →  "wke"
"koleksiyon"  →  "leksiyon"
```

<details><summary>İpucu</summary>

Kayan pencere: her karakterin son görüldüğü indeksi `HashMap`'te tutun. Pencere içindeki bir
karakter tekrar görülünce pencerenin başını ileri taşıyın. Hedef: O(n).
</details>

### 7. Sıralı listeleri birleştirme

`k` adet sıralı listeyi tek bir sıralı listede birleştirin.

```text
[[1, 4, 5], [1, 3, 4], [2, 6]]  →  [1, 1, 2, 3, 4, 4, 5, 6]
```

<details><summary>İpucu</summary>

Her listenin sıradaki elemanını bir `PriorityQueue`'da tutun; en küçüğü çekip aynı listenin
bir sonraki elemanını ekleyin. Hedef: O(N log k).
</details>

### 8. Uçuş ağında en kısa yol

Şehirler arası tek yönlü uçuşlar `Map<String, List<String>>` olarak veriliyor. Bir şehirden
diğerine **en az aktarmalı** rotayı bulun; yol yoksa boş liste dönün.

```text
Sinop → [Samsun, İstanbul]
Samsun → [Ankara]
İstanbul → [Ankara, İzmir]
Ankara → [Antalya]
İzmir → [Antalya]

Sinop → İzmir  =  [Sinop, İstanbul, İzmir]
```

<details><summary>İpucu</summary>

Genişlik öncelikli arama (BFS): kuyruk için `ArrayDeque`, ziyaret edilenler ve "nereden
gelindi" bilgisi için `HashMap`. Yolu hedeften geriye doğru izleyip `addFirst` ile oluşturun.
</details>
