package javacollectionframework.veri_yapilari;

import java.util.EmptyStackException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BenimStackTest {

    @Test
    @DisplayName("LIFO sırası ve kapasite artışı")
    void lifo() {
        BenimStack<Integer> yigin = new BenimStack<>();
        for (int i = 0; i < 100; i++) {
            yigin.push(i);
        }
        assertEquals(100, yigin.size());
        assertEquals(99, yigin.peek());
        for (int i = 99; i >= 0; i--) {
            assertEquals(i, yigin.pop());
        }
        assertTrue(yigin.isEmpty());
    }

    @Test
    @DisplayName("Boş yığında pop ve peek EmptyStackException fırlatır")
    void bosYigin() {
        BenimStack<String> yigin = new BenimStack<>();
        assertThrows(EmptyStackException.class, yigin::pop);
        assertThrows(EmptyStackException.class, yigin::peek);
    }
}
