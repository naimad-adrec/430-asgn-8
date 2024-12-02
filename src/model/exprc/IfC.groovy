package model.exprc

class IfC implements ExprC {
    private ExprC test;

    private ExprC then;

    private ExprC elseExpr;

    IdC(test, then, elseExpr) {
        this.test = test;
        this.then = then;
        this.elseExpr = elseExpr;
    }
}
