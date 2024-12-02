package model.value

import model.exprc.ExprC

class ClosV implements Value {
    List<String> arguments;

    ExprC body;

    Map<String, Value> env

    ClosV(arguments, body, env) {
        this.arguments = arguments;
        this.body = body;
        this.env = env;
    }
}
