package model.exprc

class LamC implements ExprC {
    private List<String> args;

    private ExprC body;

    LamC(args, body) {
        this.args = args;
        this.body = body;
    }
}
