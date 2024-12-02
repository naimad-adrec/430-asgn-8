package model.value

import model.exprc.ExprC

class ClosV implements Value {
    List<String> arguments;

    ExprC body;

    List<Binding> env;

    ClosV(arguments, body, env) {
        this.arguments = arguments;
        this.body = body;
        this.env = env;
    }
}
