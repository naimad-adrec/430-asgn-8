import model.exprc.*
import model.value.*

static void main(String[] args) {

    HashMap<String, Value> topEnv = new HashMap<String, Value>()
    topEnv.put("true", new BoolV(true))
    topEnv.put("false", new BoolV(false))
    topEnv.put("+", new PrimOpV("+"))
    topEnv.put("-", new PrimOpV("-"))
    topEnv.put("*", new PrimOpV("*"))
    topEnv.put("/", new PrimOpV("/"))
    topEnv.put("<=", new PrimOpV("<="))
    topEnv.put("equal?", new PrimOpV("equal?"))
    topEnv.put("error", new PrimOpV("error"))
    topEnv.put("println", new PrimOpV("println"))
    topEnv.put("read-num", new PrimOpV("read-num"))
    topEnv.put("read-str", new PrimOpV("read-str"))
    topEnv.put("seq", new PrimOpV("seq"))
    topEnv.put("++", new PrimOpV("++"))

    println serialize(Interpreter.interp(new IdC("true"), topEnv))
}

private static String serialize(final Value value) {
    switch (value) {
        case NumV:
            return value.number.toString()
        case StrV:
            return value.string.toString()
        case BoolV:
            return value.bool.toString()
        case ClosV:
            return "#<procedure>"
        case PrimOpV:
            return "#<primop>"
    }

    throw new RuntimeException(String.format("AAQZ expected Value but received: %s", value))
}
