package javacollectionframework.stream;

import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static javacollectionframework.ortak.Konsol.baslik;
import static javacollectionframework.ortak.Konsol.bolum;
import static javacollectionframework.ortak.Konsol.yaz;

/**
 * <h2>Stream API ve Collectors</h2>
 *
 * <p>Stream API (Java 8+), koleksiyonlar üzerinde <b>bildirimsel</b> (declarative) veri işleme
 * sağlar. Bir stream hattı (pipeline) üç bölümden oluşur:</p>
 * <ol>
 *     <li><b>Kaynak:</b> {@code collection.stream()}, {@code Stream.of}, {@code Arrays.stream}...</li>
 *     <li><b>Ara işlemler</b> (lazy, yeni stream döner): {@code filter, map, flatMap, distinct,
 *         sorted, limit, skip, peek, takeWhile, dropWhile, mapMulti, gather}</li>
 *     <li><b>Uç işlem</b> (hattı çalıştırır): {@code collect, toList, forEach, reduce, count,
 *         min, max, anyMatch, findFirst}</li>
 * </ol>
 *
 * <p>Stream'ler kaynağı değiştirmez ve yalnızca <b>bir kez</b> kullanılabilir.</p>
 *
 * @author cemdirman
 */
public class StreamVeCollectorsOrnegi {

    enum Departman { YAZILIM, PAZARLAMA, INSAN_KAYNAKLARI }

    record Calisan(String ad, Departman departman, int maas, int yas, List<String> yetenekler) {
    }

    static final List<Calisan> CALISANLAR = List.of(
            new Calisan("Cem", Departman.YAZILIM, 95_000, 34, List.of("java", "spring", "sql")),
            new Calisan("Ayşe", Departman.YAZILIM, 110_000, 41, List.of("java", "kotlin")),
            new Calisan("Mehmet", Departman.PAZARLAMA, 60_000, 29, List.of("seo", "sql")),
            new Calisan("Zeynep", Departman.YAZILIM, 70_000, 25, List.of("python", "sql")),
            new Calisan("Elif", Departman.INSAN_KAYNAKLARI, 55_000, 38, List.of("iletişim")),
            new Calisan("Can", Departman.PAZARLAMA, 72_000, 45, List.of("seo", "analitik")));

    public static void main(String[] args) {
        baslik("Stream API ve Collectors");

        bolum("filter, map, sorted, toList");
        List<String> yuksekMaasli = CALISANLAR.stream()
                .filter(c -> c.maas() > 70_000)
                .sorted(Comparator.comparingInt(Calisan::maas).reversed())
                .map(Calisan::ad)
                .toList(); // Java 16+, değiştirilemez liste
        yaz("maaşı > 70.000 (azalan)", yuksekMaasli);

        bolum("flatMap ve toCollection");
        Set<String> yetenekler = CALISANLAR.stream()
                .flatMap(c -> c.yetenekler().stream())
                .collect(Collectors.toCollection(TreeSet::new));
        yaz("tüm yetenekler (sıralı, benzersiz)", yetenekler);

        bolum("reduce, sum, average, istatistik");
        int toplamMaas = CALISANLAR.stream().mapToInt(Calisan::maas).sum();
        double ortYas = CALISANLAR.stream().mapToInt(Calisan::yas).average().orElse(0);
        IntSummaryStatistics ist = CALISANLAR.stream().collect(Collectors.summarizingInt(Calisan::maas));
        Optional<Calisan> enGenc = CALISANLAR.stream().min(Comparator.comparingInt(Calisan::yas));
        yaz("toplam maaş", toplamMaas);
        yaz("ortalama yaş", ortYas);
        yaz("maaş istatistiği", ist);
        yaz("en genç", enGenc.map(Calisan::ad).orElse("-"));
        yaz("reduce ile çarpım 1..5", IntStream.rangeClosed(1, 5).reduce(1, (a, b) -> a * b));

        bolum("anyMatch, allMatch, noneMatch");
        yaz("python bilen var mı", CALISANLAR.stream().anyMatch(c -> c.yetenekler().contains("python")));
        yaz("herkes 18 yaş üstü mü", CALISANLAR.stream().allMatch(c -> c.yas() >= 18));
        yaz("maaşı 200.000 üstü yok mu", CALISANLAR.stream().noneMatch(c -> c.maas() > 200_000));

        bolum("groupingBy");
        Map<Departman, List<String>> departmanaGore = CALISANLAR.stream()
                .collect(Collectors.groupingBy(Calisan::departman,
                        () -> new java.util.EnumMap<>(Departman.class),
                        Collectors.mapping(Calisan::ad, Collectors.toList())));
        yaz("departmana göre adlar", departmanaGore);

        Map<Departman, Long> sayilar = CALISANLAR.stream()
                .collect(Collectors.groupingBy(Calisan::departman, Collectors.counting()));
        yaz("departman başına kişi", new TreeMap<>(sayilar));

        Map<Departman, Double> ortMaas = CALISANLAR.stream()
                .collect(Collectors.groupingBy(Calisan::departman, TreeMap::new,
                        Collectors.averagingInt(Calisan::maas)));
        yaz("departman ortalama maaş", ortMaas);

        Map<Departman, Optional<Calisan>> enYuksek = CALISANLAR.stream()
                .collect(Collectors.groupingBy(Calisan::departman, TreeMap::new,
                        Collectors.maxBy(Comparator.comparingInt(Calisan::maas))));
        enYuksek.forEach((d, c) -> yaz("  en yüksek maaş - " + d, c.map(Calisan::ad).orElseThrow()));

        Map<Departman, Set<String>> departmanYetenek = CALISANLAR.stream()
                .collect(Collectors.groupingBy(Calisan::departman, TreeMap::new,
                        Collectors.flatMapping(c -> c.yetenekler().stream(),
                                Collectors.toCollection(TreeSet::new)))); // Java 9+
        yaz("departman yetenekleri (flatMapping)", departmanYetenek);

        bolum("partitioningBy");
        Map<Boolean, List<String>> otuzBes = CALISANLAR.stream()
                .collect(Collectors.partitioningBy(c -> c.yas() >= 35,
                        Collectors.mapping(Calisan::ad, Collectors.toList())));
        yaz("35 yaş ve üstü", otuzBes.get(true));
        yaz("35 yaş altı", otuzBes.get(false));

        bolum("toMap");
        Map<String, Integer> adMaas = CALISANLAR.stream()
                .collect(Collectors.toMap(Calisan::ad, Calisan::maas, (a, b) -> a, TreeMap::new));
        yaz("ad -> maaş (TreeMap)", adMaas);
        Map<String, Calisan> adaGore = CALISANLAR.stream()
                .collect(Collectors.toMap(Calisan::ad, Function.identity()));
        yaz("toMap(ad, identity).get(\"Elif\")", adaGore.get("Elif").departman());
        try {
            // Aynı anahtar iki kez gelirse ve birleştirme fonksiyonu verilmezse hata alınır.
            Stream.of("a", "b", "a").collect(Collectors.toMap(s -> s, s -> 1));
        } catch (IllegalStateException e) {
            yaz("tekrarlı anahtarla toMap", "IllegalStateException (Duplicate key)");
        }

        bolum("joining, teeing, collectingAndThen");
        yaz("joining", CALISANLAR.stream().map(Calisan::ad)
                .collect(Collectors.joining(", ", "[", "]")));
        // teeing (Java 12+): iki collector'ı paralel uygulayıp sonuçları birleştirir
        String aralik = CALISANLAR.stream().collect(Collectors.teeing(
                Collectors.minBy(Comparator.comparingInt(Calisan::maas)),
                Collectors.maxBy(Comparator.comparingInt(Calisan::maas)),
                (min, max) -> min.orElseThrow().maas() + " - " + max.orElseThrow().maas()));
        yaz("maaş aralığı (teeing)", aralik);
        int benzersizYetenek = CALISANLAR.stream().flatMap(c -> c.yetenekler().stream())
                .collect(Collectors.collectingAndThen(Collectors.toSet(), Set::size));
        yaz("benzersiz yetenek sayısı", benzersizYetenek);

        bolum("Java 9+ / 16+ stream eklemeleri");
        yaz("takeWhile(< 5)", Stream.of(1, 3, 4, 7, 2, 1).takeWhile(x -> x < 5).toList());
        yaz("dropWhile(< 5)", Stream.of(1, 3, 4, 7, 2, 1).dropWhile(x -> x < 5).toList());
        yaz("iterate(1, < 100, * 3)", Stream.iterate(1, x -> x < 100, x -> x * 3).toList());
        yaz("ofNullable(null).count()", Stream.ofNullable(null).count());
        List<Object> karisik = List.of(1, List.of(2, 3), 4, List.of(5));
        List<Integer> duz = karisik.stream()
                .<Integer>mapMulti((eleman, asagi) -> {          // Java 16+
                    if (eleman instanceof List<?> liste) {
                        liste.forEach(x -> asagi.accept((Integer) x));
                    } else if (eleman instanceof Integer i) {
                        asagi.accept(i);
                    }
                })
                .toList();
        yaz("mapMulti ile düzleştirme", duz);

        bolum("Map'leri stream ile işlemek");
        Map<String, Integer> stok = Map.of("elma", 40, "armut", 0, "kiraz", 12, "muz", 7);
        List<String> azalan = stok.entrySet().stream()
                .filter(e -> e.getValue() > 0)
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .toList();
        yaz("stoğu olanlar (çoktan aza)", azalan);

        bolum("Paralel stream");
        long asalSayisi = IntStream.rangeClosed(2, 200_000).parallel()
                .filter(StreamVeCollectorsOrnegi::asalMi)
                .count();
        yaz("2..200000 arası asal sayısı", asalSayisi);
    }

    private static boolean asalMi(int n) {
        return IntStream.rangeClosed(2, (int) Math.sqrt(n)).noneMatch(b -> n % b == 0);
    }
}
