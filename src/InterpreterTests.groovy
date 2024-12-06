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

        @Test
        void testEqualTrue() {
            def expr = new AppC(
                    new IdC("equal?"),
                    [new NumC(4), new NumC(4)]
            )

            def result = Interpreter.topInterp(expr)

            assertEquals("true", result)
        }

        @Test
        void testEqualFalse() {
            def expr = new AppC(
                    new IdC("equal?"),
                    [new NumC(1), new NumC(4)]
            )

            def result = Interpreter.topInterp(expr)

            assertEquals("false", result)
        }

        @Test
        void testLTEqual() {

            def expr = new AppC(
                    new IdC("<="),
                    [new NumC(1), new NumC(4)]
            )

            def result = Interpreter.topInterp(expr)

            assertEquals("true", result)
        }

    }

    @Test
    void testFuncAsArg() {
        def expr = new AppC(
                new LamC(["f", "x", "y"],
                        new AppC(new IdC("f"), [new IdC("x"), new IdC("y")])),
                [new LamC(["a", "b"],
                        new AppC(new IdC("+"), [new IdC("a"), new IdC("b")])),
                 new NumC(2),
                 new NumC(3)])

        def result = Interpreter.topInterp(expr)

        assertEquals("5", result)
    }

    @Test
    void testIfThen() {
        def expr = new IfC(
                new IdC("true"),
                new NumC(1),
                new NumC(0)
        )

        def result = Interpreter.topInterp(expr)

        assertEquals("1", result)
    }

    @Test
    void testIfElse() {
        def expr = new IfC(
                new IdC("false"),
                new NumC(1),
                new NumC(0)
        )

        def result = Interpreter.topInterp(expr)

        assertEquals("0", result)
    }

    @Test
    void testIfError() {
        def expr = new IfC(
                new IdC("+"),
                new NumC(1),
                new NumC(0)
        )

        RuntimeException exception = assertThrows(RuntimeException.class,
                {Interpreter.topInterp(expr)})

        assertEquals("AAQZ expected BoolV but received: #<primop>", exception.getMessage())
    }

    @Test
    void testStr() {
        def expr = new StrC("hi")

        def result = Interpreter.topInterp(expr)

        assertEquals("hi", result)
    }



}