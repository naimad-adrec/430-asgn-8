package model.exprc

class LamC implements ExprC {
    List<String> args;

    ExprC body;

    LamC(args, body) {
        this.args = args;
        this.body = body;
    }
}
