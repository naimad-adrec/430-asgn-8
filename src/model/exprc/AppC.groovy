package model.exprc

class AppC implements ExprC {
    ExprC function;

    List<ExprC> arguments;

    AppC(function, arguments) {
        this.function = function;
        this.arguments = arguments;
    }
}
