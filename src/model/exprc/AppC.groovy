package model.exprc

class AppC implements ExprC {
    private ExprC function;

    private List<ExprC> arguments;

    AppC(function, arguments) {
        this.function = function;
        this.arguments = arguments;
    }
}
