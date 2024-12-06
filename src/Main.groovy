import model.exprc.*

static void main(String[] args) {
    ExprC prog = new AppC(new IdC("+"), Arrays.asList(new NumC(2), new NumC(3)))
    println Interpreter.topInterp(prog)
}
