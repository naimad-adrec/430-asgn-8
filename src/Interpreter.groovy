import model.exprc.AppC
import model.exprc.ExprC
import model.exprc.IdC
import model.exprc.IfC
import model.exprc.LamC
import model.exprc.NumC
import model.exprc.StrC
import model.value.BoolV
import model.value.ClosV
import model.value.NumV
import model.value.PrimOpV
import model.value.StrV
import model.value.Value

class Interpreter {

    static Value interp(final ExprC exprC, Map<String, Value> env) {
        switch (exprC) {
            case NumC:
                return new NumV(exprC.number)
            case StrC:
                return new StrV(exprC.string)
            case IdC:
                return lookup(exprC.symbol, env)
            case IfC:
                final testResult = interp(exprC.test, env)
                switch (testResult) {
                    case BoolV:
                        if (testResult.bool == true) {
                            return interp(exprC.then, env)
                        }

                        return interp(exprC.elseExpr, env)
                }

                throw new RuntimeException(String.format("AAQZ expected BoolV but received: %s", testResult))
            case LamC:
                return new ClosV(exprC.args, exprC.body, env)
            case AppC:
                final function = interp(exprC.function, env)
                switch (function) {
                    case ClosV:
                        if (exprC.arguments.size() != function.arguments.size()) {
                            throw new RuntimeException("AAQZ mismatch arguments")
                        }

                        for (int i = 0; i < exprC.arguments.size(); i++) {
                            function.env.put(function.arguments.get(i), interp(exprC.arguments.get(i), function.env))
                        }

                        return interp(function.body, function.env)
                    case PrimOpV:
                        if (exprC.arguments.size() == 0) {
                            switch (function.op) {
                                case "read-num":
                                    final String numAsStr = System.in.newReader().readLine()

                                    if (numAsStr.isNumber()) {
                                        return new NumV(numAsStr.toInteger())
                                    }

                                    throw new RuntimeException(String.format("AAQZ expected number but received: %s", numAsStr))
                                case "read-str":
                                    final String str = System.in.newReader().readLine()
                                    return new StrV(str)
                            }
                        } else if (exprC.arguments.size() == 1) {
                            final StrV firstStr = interp(exprC.arguments.get(0), env) as StrV
                            switch (function.op) {
                                case "print-ln":
                                    System.out.newWriter().write(firstStr.string)
                                    return new BoolV(true)
                                case "error":
                                    throw new RuntimeException(String.format("AAQZ user-error: %s", firstStr))
                            }
                        } else if (exprC.arguments.size() == 2) {
                            final Value firstArg = interp(exprC.arguments.get(0), env)
                            final Value secondArg = interp(exprC.arguments.get(1), env)

                            switch (function.op) {
                                case "+":
                                    final NumV firstNum = firstArg as NumV
                                    final NumV secondNum = secondArg as NumV
                                    return new NumV(firstNum.number + secondNum.number)
                                case "-":
                                    final NumV firstNum = firstArg as NumV
                                    final NumV secondNum = secondArg as NumV
                                    return new NumV(firstNum.number - secondNum.number)
                                case "*":
                                    final NumV firstNum = firstArg as NumV
                                    final NumV secondNum = secondArg as NumV
                                    return new NumV(firstNum.number * secondNum.number)
                                case "/":
                                    final NumV firstNum = firstArg as NumV
                                    final NumV secondNum = secondArg as NumV

                                    if (secondNum.number == 0) {
                                        throw new RuntimeException("AAQZ cannot divide by zero")
                                    }

                                    return new NumV(firstNum.number / secondNum.number)
                                case "<=":
                                    final NumV firstNum = firstArg as NumV
                                    final NumV secondNum = secondArg as NumV

                                    return new BoolV(firstNum.number <= secondNum.number)
                                case "equal?":
                                    if (firstArg.class == ClosV || secondArg.class == ClosV ||
                                            firstArg.class == PrimOpV || secondArg.class == PrimOpV) {
                                        throw new RuntimeException("AAQZ cannot compare ClosV or PrimOpV")
                                    }

                                    if (firstArg.class != secondArg.class) {
                                        return new BoolV(false)
                                    }

                                    switch (firstArg) {
                                        case NumV:
                                            final NumV secondNumber = secondArg as NumV
                                            return new BoolV(firstArg.number == secondNumber.number)
                                        case StrV:
                                            final StrV secondString = secondArg as StrV
                                            return new BoolV(firstArg.string == secondString.string)
                                        case BoolV:
                                            final BoolV secondBool = secondArg as BoolV
                                            return new BoolV(firstArg.bool == secondBool.bool)
                                    }

                                    return new BoolV(firstArg == secondArg)
                            }
                        } else {
                            switch (function.op) {
                                case "seq":
                                    Value lastVal
                                    for (final ExprC arg : exprC.arguments) {
                                        lastVal = interp(arg, env)
                                    }

                                    return lastVal
                                case "++":
                                    String concatenatedStr = ""
                                    for (final ExprC arg : exprC.arguments) {
                                        StrV newStr = interp(arg, env) as StrV
                                        concatenatedStr.concat(newStr.string)
                                    }

                                    return new StrV(concatenatedStr)
                            }
                        }
                        throw new RuntimeException(String.format("AAQZ incorrect function: %s", function))
                }
        }

        throw new RuntimeException(String.format("AAQZ Expected ExprC but received: %s", exprC))
    }

    private static Value lookup(final String name, final Map<String, Value> env) {
        final Value val = env.get(name)

        if (val == null) {
            throw new RuntimeException(String.format("AAQZ name not found in env: %s", name))
        }

        return val
    }
}
