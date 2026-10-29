package javacollectionframework.collections.list;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Stack;

import static javacollectionframework.ortak.Konsol.baslik;
import static javacollectionframework.ortak.Konsol.bolum;
import static javacollectionframework.ortak.Konsol.yaz;

/**
 * <h2>Stack</h2>
 *
 * <p>{@link Stack}, <b>LIFO</b> (Last In First Out - son giren ilk çıkar) mantığıyla çalışan,
 * {@link java.util.Vector}'dan türeyen legacy bir sınıftır.</p>
 *
 * <ul>
 *     <li>{@code push} - en üste ekler</li>
 *     <li>{@code pop} - en üstteki elemanı çıkarır (boşsa {@code EmptyStackException})</li>
 *     <li>{@code peek} - en üstteki elemana bakar, çıkarmaz</li>
 *     <li>{@code search} - elemanın üstten kaçıncı sırada olduğunu döner (1 tabanlı)</li>
 * </ul>
 *
 * <p><b>Önemli:</b> Stack, Vector'dan türediği için senkronizedir ve {@code get(i)},
 * {@code add(i, e)} gibi yığın mantığını bozan metotları da sunar. Java dokümantasyonu yeni
 * kodda {@link Deque} (özellikle {@link ArrayDeque}) kullanılmasını önerir.</p>
 *
 * @author cemdirman
 */
public class StackOrnegi {

    public static void main(String[] args) {
        baslik("Stack Örneği");

        bolum("Legacy Stack");
        Stack<String> tabaklar = new Stack<>();
        tabaklar.push("1. tabak");
        tabaklar.push("2. tabak");
        tabaklar.push("3. tabak");
        yaz("tabaklar", tabaklar);
        yaz("peek()", tabaklar.peek());
        yaz("search(\"1. tabak\")", tabaklar.search("1. tabak"));
        yaz("pop()", tabaklar.pop());
        yaz("pop sonrası", tabaklar);
        yaz("empty()", tabaklar.empty());

        bolum("Önerilen: ArrayDeque ile yığın");
        Deque<String> yigin = new ArrayDeque<>();
        yigin.push("1. tabak");
        yigin.push("2. tabak");
        yigin.push("3. tabak");
        // Deque'nin toString'i üstten alta doğru yazar
        yaz("yigin (üstten alta)", yigin);
        yaz("peek()", yigin.peek());
        yaz("pop()", yigin.pop());

        bolum("Uygulama: parantez dengesi kontrolü");
        for (String ifade : new String[]{"{[()()]}", "([)]", "((())", "a(b)c[d]{e}"}) {
            yaz(ifade, dengeliMi(ifade) ? "dengeli" : "dengesiz");
        }
    }

    /** Her açılan parantez yığına konur, kapanan parantez yığının tepesiyle eşleşmelidir. */
    static boolean dengeliMi(String ifade) {
        Deque<Character> yigin = new ArrayDeque<>();
        for (char c : ifade.toCharArray()) {
            switch (c) {
                case '(', '[', '{' -> yigin.push(c);
                case ')', ']', '}' -> {
                    if (yigin.isEmpty()) {
                        return false;
                    }
                    char acilan = yigin.pop();
                    boolean eslesti = switch (c) {
                        case ')' -> acilan == '(';
                        case ']' -> acilan == '[';
                        default -> acilan == '{';
                    };
                    if (!eslesti) {
                        return false;
                    }
                }
                default -> { /* parantez olmayan karakterler yok sayılır */ }
            }
        }
        return yigin.isEmpty();
    }
}
