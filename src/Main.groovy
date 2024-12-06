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

    ExprC prog = new AppC(new IdC("+"), Arrays.asList(new NumC(2), new NumC(3)))

    println Interpreter.serialize(Interpreter.interp(prog, topEnv))
}
