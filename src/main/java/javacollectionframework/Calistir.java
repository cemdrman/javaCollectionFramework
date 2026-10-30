package javacollectionframework;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Consumer;

import javacollectionframework.collections.degistirilemez.DegistirilemezKoleksiyonOrnegi;
import javacollectionframework.collections.list.ArrayListOrnegi;
import javacollectionframework.collections.list.LinkedListOrnegi;
import javacollectionframework.collections.list.StackOrnegi;
import javacollectionframework.collections.list.VectorOrnegi;
import javacollectionframework.collections.map.EnumMapOrnegi;
import javacollectionframework.collections.map.HashMapOrnegi;
import javacollectionframework.collections.map.LinkedHashMapOrnegi;
import javacollectionframework.collections.map.OzelMaplerOrnegi;
import javacollectionframework.collections.map.TreeMapOrnegi;
import javacollectionframework.collections.queue.ArrayDequeOrnegi;
import javacollectionframework.collections.queue.PriorityQueueOrnegi;
import javacollectionframework.collections.queue.QueueOrnegi;
import javacollectionframework.collections.sequenced.SequencedCollectionOrnegi;
import javacollectionframework.collections.set.EnumSetOrnegi;
import javacollectionframework.collections.set.HashSetOrnegi;
import javacollectionframework.collections.set.LinkedHashSetOrnegi;
import javacollectionframework.collections.set.TreeSetOrnegi;
import javacollectionframework.eszamanli.BlockingQueueOrnegi;
import javacollectionframework.eszamanli.ConcurrentHashMapOrnegi;
import javacollectionframework.eszamanli.DigerEsZamanliKoleksiyonlarOrnegi;
import javacollectionframework.iterasyon.IteratorOrnegi;
import javacollectionframework.performans.PerformansKarsilastirmaOrnegi;
import javacollectionframework.siralama.ComparableComparatorOrnegi;
import javacollectionframework.stream.GatherersOrnegi;
import javacollectionframework.stream.StreamVeCollectorsOrnegi;
import javacollectionframework.veri_yapilari.VeriYapilariOrnegi;
import javacollectionframework.yardimcilar.ArraysSinifiOrnegi;
import javacollectionframework.yardimcilar.CollectionsSinifiOrnegi;

/**
 * Bütün örnekleri listeleyen ve seçileni çalıştıran giriş noktası.
 *
 * <pre>
 * ./mvnw -q compile exec:java                          # listeyi gösterir
 * ./mvnw -q compile exec:java -Dexec.args="5"          # 5 numaralı örneği çalıştırır
 * ./mvnw -q compile exec:java -Dexec.args="treeset"    # adında "treeset" geçenleri çalıştırır
 * ./mvnw -q compile exec:java -Dexec.args="hepsi"      # hepsini sırayla çalıştırır
 * </pre>
 *
 * @author cemdirman
 */
public final class Calistir {

    /** Örneğin konusu, sınıf adı ve main metodu. */
    record Ornek(String konu, String ad, Consumer<String[]> main) {
    }

    @FunctionalInterface
    interface MainMetodu {
        void calistir(String[] args) throws Exception;
    }

    private static Ornek ornek(String konu, Class<?> sinif, MainMetodu main) {
        return new Ornek(konu, sinif.getSimpleName(), args -> {
            try {
                main.calistir(args);
            } catch (Exception e) {
                throw new IllegalStateException(sinif.getSimpleName() + " çalıştırılamadı", e);
            }
        });
    }

    static final List<Ornek> ORNEKLER = List.of(
            ornek("List", ArrayListOrnegi.class, ArrayListOrnegi::main),
            ornek("List", LinkedListOrnegi.class, LinkedListOrnegi::main),
            ornek("List", VectorOrnegi.class, VectorOrnegi::main),
            ornek("List", StackOrnegi.class, StackOrnegi::main),
            ornek("Set", HashSetOrnegi.class, HashSetOrnegi::main),
            ornek("Set", LinkedHashSetOrnegi.class, LinkedHashSetOrnegi::main),
            ornek("Set", TreeSetOrnegi.class, TreeSetOrnegi::main),
            ornek("Set", EnumSetOrnegi.class, EnumSetOrnegi::main),
            ornek("Queue", QueueOrnegi.class, QueueOrnegi::main),
            ornek("Queue", ArrayDequeOrnegi.class, ArrayDequeOrnegi::main),
            ornek("Queue", PriorityQueueOrnegi.class, PriorityQueueOrnegi::main),
            ornek("Map", HashMapOrnegi.class, HashMapOrnegi::main),
            ornek("Map", LinkedHashMapOrnegi.class, LinkedHashMapOrnegi::main),
            ornek("Map", TreeMapOrnegi.class, TreeMapOrnegi::main),
            ornek("Map", EnumMapOrnegi.class, EnumMapOrnegi::main),
            ornek("Map", OzelMaplerOrnegi.class, OzelMaplerOrnegi::main),
            ornek("Sequenced", SequencedCollectionOrnegi.class, SequencedCollectionOrnegi::main),
            ornek("Değiştirilemez", DegistirilemezKoleksiyonOrnegi.class, DegistirilemezKoleksiyonOrnegi::main),
            ornek("Sıralama", ComparableComparatorOrnegi.class, ComparableComparatorOrnegi::main),
            ornek("Iterator", IteratorOrnegi.class, IteratorOrnegi::main),
            ornek("Yardımcılar", CollectionsSinifiOrnegi.class, CollectionsSinifiOrnegi::main),
            ornek("Yardımcılar", ArraysSinifiOrnegi.class, ArraysSinifiOrnegi::main),
            ornek("Eşzamanlı", ConcurrentHashMapOrnegi.class, ConcurrentHashMapOrnegi::main),
            ornek("Eşzamanlı", BlockingQueueOrnegi.class, BlockingQueueOrnegi::main),
            ornek("Eşzamanlı", DigerEsZamanliKoleksiyonlarOrnegi.class, DigerEsZamanliKoleksiyonlarOrnegi::main),
            ornek("Stream", StreamVeCollectorsOrnegi.class, StreamVeCollectorsOrnegi::main),
            ornek("Stream", GatherersOrnegi.class, GatherersOrnegi::main),
            ornek("Veri yapıları", VeriYapilariOrnegi.class, VeriYapilariOrnegi::main),
            ornek("Performans", PerformansKarsilastirmaOrnegi.class, PerformansKarsilastirmaOrnegi::main));

    private Calistir() {
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            listele();
            return;
        }
        String secim = args[0].toLowerCase(Locale.ROOT);
        if (secim.equals("hepsi")) {
            ORNEKLER.forEach(o -> o.main().accept(new String[0]));
            return;
        }
        List<Ornek> secilenler = numarayaGore(secim)
                .map(List::of)
                .orElseGet(() -> ORNEKLER.stream()
                        .filter(o -> o.ad().toLowerCase(Locale.ROOT).contains(secim))
                        .toList());
        if (secilenler.isEmpty()) {
            System.out.println("\"" + args[0] + "\" ile eşleşen örnek bulunamadı.");
            listele();
            return;
        }
        secilenler.forEach(o -> o.main().accept(new String[0]));
    }

    private static Optional<Ornek> numarayaGore(String secim) {
        try {
            int no = Integer.parseInt(secim);
            return no >= 1 && no <= ORNEKLER.size() ? Optional.of(ORNEKLER.get(no - 1)) : Optional.empty();
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    private static void listele() {
        System.out.println("Java Collection Framework örnekleri");
        System.out.println();
        for (int i = 0; i < ORNEKLER.size(); i++) {
            Ornek o = ORNEKLER.get(i);
            System.out.printf("  %2d. %-15s %s%n", i + 1, o.konu(), o.ad());
        }
        System.out.println();
        System.out.println("Çalıştırmak için: ./mvnw -q compile exec:java -Dexec.args=\"<numara | ad | hepsi>\"");
    }
}
