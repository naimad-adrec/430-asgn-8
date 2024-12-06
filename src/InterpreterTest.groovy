import org.junit.jupiter.api.Test
import static org.junit.jupiter.api.Assertions.*
import model.exprc.*
import model.value.*

class InterpreterTest {

    @Test
    void testAddition() {
        def expr = new AppC(
                new PrimOpV("+"),
                [new NumC(3), new NumC(4)]
        )

        def result = Interpreter.interp(expr, [:])

        assertEquals("7", Interpreter.serialize(result))
    }
}
