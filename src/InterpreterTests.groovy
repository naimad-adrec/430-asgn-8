import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import static org.junit.jupiter.api.Assertions.*
import model.exprc.*

class InterpreterTest {

    @Nested
    @DisplayName("Primops Tests")
    class testPrimops {
        @Test
        void testAdd() {
            def expr = new AppC(
                    new IdC("+"),
                    [new NumC(3), new NumC(4)]
            )

            def result = Interpreter.topInterp(expr)

            assertEquals("7", result)
        }

        @Test
        void testSub() {
            def expr = new AppC(
                    new IdC("-"),
                    [new NumC(3), new NumC(4)]
            )

            def result = Interpreter.topInterp(expr)

            assertEquals("-1", result)
        }

        @Test
        void testMult() {
            def expr = new AppC(
                    new IdC("*"),
                    [new NumC(3), new NumC(4)]
            )

            def result = Interpreter.topInterp(expr)

            assertEquals("12", result)
        }

        @Test
        void testDiv() {
            def expr = new AppC(
                    new IdC("/"),
                    [new NumC(3), new NumC(4)]
            )

            def result = Interpreter.topInterp(expr)

            assertEquals("0.75", result)
        }

        @Test
        void testDivByZero() {
            def expr = new AppC(
                    new IdC("/"),
                    [new NumC(3), new NumC(0)]
            )

            RuntimeException exception = assertThrows(RuntimeException.class,
                    {Interpreter.topInterp(expr)})

            assertEquals("AAQZ divide by 0", exception.getMessage())
        }
    }

}