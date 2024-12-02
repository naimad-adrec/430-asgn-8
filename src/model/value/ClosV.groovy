package model.value

import model.exprc.ExprC

class ClosV implements Value {
    private List<String> arguments;

    private ExprC body;

    private List<Binding> env;

    ClosV(arguments, body, env) {
        this.arguments = arguments;
        this.body = body;
        this.env = env;
    }
}
