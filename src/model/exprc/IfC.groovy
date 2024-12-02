package model.exprc

class IfC implements ExprC {
    ExprC test;

    ExprC then;

    ExprC elseExpr;

    IdC(test, then, elseExpr) {
        this.test = test;
        this.then = then;
        this.elseExpr = elseExpr;
    }
}
