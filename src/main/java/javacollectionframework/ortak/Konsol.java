package javacollectionframework.ortak;

/**
 * Örneklerde çıktıyı bölümlere ayırmak için kullanılan küçük yardımcı sınıf.
 */
public final class Konsol {

    private Konsol() {
    }

    /** Bir örneğin başlığını belirgin şekilde yazdırır. */
    public static void baslik(String metin) {
        System.out.println();
        System.out.println("=".repeat(60));
        System.out.println(" " + metin);
        System.out.println("=".repeat(60));
    }

    /** Bir örnek içindeki alt bölümü yazdırır. */
    public static void bolum(String metin) {
        System.out.println();
        System.out.println("--- " + metin + " ---");
    }

    /** "etiket : değer" biçiminde hizalı çıktı üretir. */
    public static void yaz(String etiket, Object deger) {
        System.out.printf("%-38s: %s%n", etiket, deger);
    }
}
