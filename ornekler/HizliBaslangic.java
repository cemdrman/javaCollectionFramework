/*
 * Java 25 kompakt kaynak dosyası (JEP 512) ile hızlı başlangıç.
 *
 * Bu dosyada sınıf tanımı, 'public static' ya da import satırı yok:
 *   - Kompakt kaynak dosyalarında sınıf derleyici tarafından örtük olarak oluşturulur.
 *   - main metodu 'void main()' kadar sade olabilir (instance main metodu).
 *   - java.base modülündeki bütün public sınıflar (List, Map, ArrayList, ...) otomatik
 *     import edilir.
 *   - IO.println, System.out.println'in kısa yoludur (java.lang.IO, Java 25).
 *
 * Derlemeden doğrudan çalıştırmak için (proje kök dizininde):
 *
 *   java ornekler/HizliBaslangic.java
 */

record Ogrenci(String ad, String sehir, int not) {
}

void main() {
    List<Ogrenci> ogrenciler = List.of(
            new Ogrenci("Cem", "Sinop", 85),
            new Ogrenci("Ayşe", "İzmir", 92),
            new Ogrenci("Mehmet", "Sinop", 67),
            new Ogrenci("Zeynep", "Ankara", 74),
            new Ogrenci("Elif", "İzmir", 58));

    IO.println("== List ==");
    var isimler = new ArrayList<String>();
    for (Ogrenci o : ogrenciler) {
        isimler.add(o.ad());
    }
    IO.println("isimler           : " + isimler);
    IO.println("ilk / son         : " + isimler.getFirst() + " / " + isimler.getLast());
    IO.println("ters              : " + isimler.reversed());

    IO.println("== Set ==");
    Set<String> sehirler = new TreeSet<>();
    ogrenciler.forEach(o -> sehirler.add(o.sehir()));
    IO.println("şehirler (sıralı) : " + sehirler);
    // Not: "İzmir" Unicode sırası nedeniyle sona düşer. Türkçe alfabetik sıra için
    // Collator kullanılır (bkz. collections/set/TreeSetOrnegi.java).

    IO.println("== Map ==");
    Map<String, List<String>> sehreGore = new TreeMap<>();
    for (Ogrenci o : ogrenciler) {
        sehreGore.computeIfAbsent(o.sehir(), _ -> new ArrayList<>()).add(o.ad()); // '_': isimsiz değişken
    }
    IO.println("şehre göre        : " + sehreGore);

    IO.println("== Queue ==");
    Queue<Ogrenci> notSirasi = new PriorityQueue<>(Comparator.comparingInt(Ogrenci::not).reversed());
    notSirasi.addAll(ogrenciler);
    IO.println("en yüksek not     : " + notSirasi.poll().ad());

    IO.println("== Stream ==");
    String gecenler = ogrenciler.stream()
            .filter(o -> o.not() >= 60)
            .map(Ogrenci::ad)
            .collect(Collectors.joining(", "));
    IO.println("geçenler (>= 60)  : " + gecenler);

    IO.println("== switch + pattern matching ==");
    for (Object deger : List.of(List.of(1, 2), Set.of("a"), Map.of("k", 1))) {
        String tur = switch (deger) {
            case List<?> l -> "List, " + l.size() + " eleman";
            case Set<?> s -> "Set, " + s.size() + " eleman";
            case Map<?, ?> m -> "Map, " + m.size() + " çift";
            default -> "bilinmiyor";
        };
        IO.println(deger + " -> " + tur);
    }
}
