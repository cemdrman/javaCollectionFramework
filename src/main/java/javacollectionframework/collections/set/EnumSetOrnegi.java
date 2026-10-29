package javacollectionframework.collections.set;

import java.util.EnumSet;
import java.util.Set;

import static javacollectionframework.ortak.Konsol.baslik;
import static javacollectionframework.ortak.Konsol.bolum;
import static javacollectionframework.ortak.Konsol.yaz;

/**
 * <h2>EnumSet</h2>
 *
 * <p>{@link EnumSet}, yalnızca tek bir enum tipinin sabitlerini tutan, son derece hızlı ve
 * kompakt bir {@link Set} gerçekleştirimidir. İçeride bir <b>bit vektörü</b> kullanır:
 * 64 veya daha az sabitli enum'lar için tek bir {@code long} yeterlidir.</p>
 *
 * <ul>
 *     <li>Bütün temel işlemler <b>O(1)</b> ve bit işlemleri kadar hızlıdır.</li>
 *     <li>Elemanlar enum'daki tanım sırasına ({@code ordinal}) göre dolaşılır.</li>
 *     <li>{@code null} kabul etmez. Constructor yerine static fabrika metotlarıyla oluşturulur.</li>
 *     <li>Eski "bit flag" ({@code int} sabitleri ile {@code |}, {@code &}) kullanımının tip güvenli
 *         karşılığıdır.</li>
 * </ul>
 *
 * @author cemdirman
 */
public class EnumSetOrnegi {

    enum Gun { PAZARTESI, SALI, CARSAMBA, PERSEMBE, CUMA, CUMARTESI, PAZAR }

    enum Yetki { OKU, YAZ, SIL, YONET }

    public static void main(String[] args) {
        baslik("EnumSet Örneği");

        bolum("Fabrika metotları");
        yaz("allOf", EnumSet.allOf(Gun.class));
        yaz("noneOf", EnumSet.noneOf(Gun.class));
        yaz("of(PAZAR, SALI) (sıra: ordinal)", EnumSet.of(Gun.PAZAR, Gun.SALI));
        EnumSet<Gun> haftaIci = EnumSet.range(Gun.PAZARTESI, Gun.CUMA);
        yaz("range(PAZARTESI, CUMA)", haftaIci);
        yaz("complementOf(haftaIci)", EnumSet.complementOf(haftaIci));

        bolum("Bit flag yerine EnumSet");
        Set<Yetki> editor = EnumSet.of(Yetki.OKU, Yetki.YAZ);
        Set<Yetki> admin = EnumSet.allOf(Yetki.class);
        yaz("editor", editor);
        yaz("editor SIL yetkisine sahip mi", editor.contains(Yetki.SIL));
        yaz("admin editor'ün tüm yetkilerine sahip mi", admin.containsAll(editor));

        EnumSet<Yetki> eksik = EnumSet.copyOf(admin);
        eksik.removeAll(editor);
        yaz("editor'ün eksik yetkileri", eksik);

        bolum("switch ile birlikte kullanım");
        for (Gun gun : EnumSet.of(Gun.CUMA, Gun.CUMARTESI)) {
            String tur = switch (gun) {
                case CUMARTESI, PAZAR -> "hafta sonu";
                default -> "hafta içi";
            };
            yaz(gun.name(), tur);
        }
    }
}
