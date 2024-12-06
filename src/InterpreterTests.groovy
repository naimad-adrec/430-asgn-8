import org.junit.jupiter.api.Test
import static org.junit.jupiter.api.Assertions.*
import model.exprc.*

class InterpreterTest {

    @Test
    void testAddition() {
        def expr = new AppC(
                new IdC("+"),
                [new NumC(3), new NumC(4)]
        )

        def result = Interpreter.topInterp(expr)

        assertEquals("7", result)
    }
}