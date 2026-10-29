package javacollectionframework.collections.map;

import java.util.EnumMap;
import java.util.Map;

import static javacollectionframework.ortak.Konsol.baslik;
import static javacollectionframework.ortak.Konsol.bolum;
import static javacollectionframework.ortak.Konsol.yaz;

/**
 * <h2>EnumMap</h2>
 *
 * <p>{@link EnumMap}, anahtarları tek bir enum tipinden olan özel bir Map'tir. Arka planda, enum
 * sabitinin {@code ordinal()} değerini indeks olarak kullanan <b>basit bir dizi</b> tutar.</p>
 *
 * <ul>
 *     <li>Hash hesabı yoktur; bütün işlemler <b>O(1)</b> ve HashMap'ten daha hızlıdır.</li>
 *     <li>Anahtarlar enum'daki tanım sırasına göre dolaşılır.</li>
 *     <li>{@code null} anahtar kabul etmez.</li>
 * </ul>
 *
 * @author cemdirman
 */
public class EnumMapOrnegi {

    enum SiparisDurumu { ALINDI, HAZIRLANIYOR, KARGODA, TESLIM_EDILDI, IPTAL }

    record Siparis(int no, SiparisDurumu durum) {
    }

    public static void main(String[] args) {
        baslik("EnumMap Örneği");

        bolum("Durum açıklamaları");
        Map<SiparisDurumu, String> aciklama = new EnumMap<>(SiparisDurumu.class);
        aciklama.put(SiparisDurumu.KARGODA, "Siparişiniz yolda");
        aciklama.put(SiparisDurumu.ALINDI, "Siparişiniz alındı");
        aciklama.put(SiparisDurumu.TESLIM_EDILDI, "Teslim edildi");
        yaz("aciklama (ordinal sırası)", aciklama);

        bolum("Duruma göre sayma");
        Siparis[] siparisler = {
                new Siparis(1, SiparisDurumu.KARGODA),
                new Siparis(2, SiparisDurumu.ALINDI),
                new Siparis(3, SiparisDurumu.KARGODA),
                new Siparis(4, SiparisDurumu.IPTAL),
                new Siparis(5, SiparisDurumu.TESLIM_EDILDI),
                new Siparis(6, SiparisDurumu.KARGODA),
        };
        Map<SiparisDurumu, Integer> sayac = new EnumMap<>(SiparisDurumu.class);
        for (SiparisDurumu d : SiparisDurumu.values()) {
            sayac.put(d, 0);
        }
        for (Siparis s : siparisler) {
            sayac.merge(s.durum(), 1, Integer::sum);
        }
        sayac.forEach((durum, adet) -> yaz("  " + durum, adet));
    }
}
