import model.exprc.*
import model.value.*

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
                final Value testResult = interp(exprC.test, env)
                switch (testResult) {
                    case BoolV:
                        if (testResult.bool == true) {
                            return interp(exprC.then, env)
                        }

                        return interp(exprC.elseExpr, env)
                    default:
                        throw new RuntimeException(String.format("AAQZ expected BoolV but received: %s",
                                serialize(testResult)))
                }
            case LamC:
                return new ClosV(exprC.args, exprC.body, env)
            case AppC:
                final Value function = interp(exprC.function, env)
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
                                    return primopReadNum()
                                case "read-str":
                                    return primopReadString()
                            }
                        } else if (exprC.arguments.size() == 1) {
                            final Value firstArg = interp(exprC.arguments.get(0), env)
                            switch (function.op) {
                                case "print-ln":
                                    return primopPrintLn(firstArg)
                                case "error":
                                    return primopError(firstArg)
                            }
                        } else if (exprC.arguments.size() == 2) {
                            final Value firstArg = interp(exprC.arguments.get(0), env)
                            final Value secondArg = interp(exprC.arguments.get(1), env)

                            switch (function.op) {
                                case "+":
                                case "-":
                                case "*":
                                case "/":
                                case "<=":
                                    return primopArith(function.op, firstArg, secondArg)
                                case "equal?":
                                    return primopEqual(firstArg, secondArg)
                            }
                        } else {
                            switch (function.op) {
                                case "seq":
                                    return primopSeq(exprC, env)
                                case "++":
                                    return primopPlusPlus(exprC, env)
                            }
                        }
                        throw new RuntimeException(String.format("AAQZ incorrect function: %s", serialize(function)))
                    default:
                        throw new RuntimeException(String.format("AAQZ value is not callable: %s", serialize(function)))
                }
            default:
                throw new RuntimeException(String.format("AAQZ Expected ExprC but received: %s", exprC))
        }
    }

     static String serialize(final Value value) {
        switch (value) {
            case NumV:
                return value.number.toString()
            case StrV:
                return value.string.toString()
            case BoolV:
                return value.bool.toString()
            case ClosV:
                return "#<procedure>"
            case PrimOpV:
                return "#<primop>"
        }

        throw new RuntimeException(String.format("AAQZ expected Value but received: %s", value))
    }

    private static Value lookup(final String name, final Map<String, Value> env) {
        final Value val = env.get(name)

        if (val == null) {
            throw new RuntimeException(String.format("AAQZ name not found in env: %s", name))
        }

        return val
    }

    private static NumV primopReadNum() {
        final String numAsStr = System.in.newReader().readLine()

        if (numAsStr.isNumber()) {
            return new NumV(numAsStr.toInteger())
        }

        throw new RuntimeException(String.format("AAQZ expected number but received: %s", numAsStr))
    }

    private static StrV primopReadString() {
        final String str = System.in.newReader().readLine()
        return new StrV(str)
    }

    private static BoolV primopPrintLn(Value firstVal) {
        if (!(firstVal instanceof StrV))
            throw new RuntimeException(String.format("AAQZ expected string but received: %s", serialize(firstVal)))
        System.out.newWriter().write((firstVal as StrV).string)
        return new BoolV(true)
    }

    private static Value primopError(Value firstVal) {
        throw new RuntimeException(String.format("AAQZ user-error: %s", serialize(firstVal)))
    }

    private static Value primopArith(String op, Value firstArg, Value secondArg) {
        if (!(firstArg instanceof NumV))
            throw new RuntimeException(String.format("AAQZ not a number in %s: %s", op, serialize(firstArg)))


        if (!(secondArg instanceof NumV))
            throw new RuntimeException(String.format("AAQZ not a number in %s: %s", op, serialize(secondArg)))

        final NumV secondNum = secondArg as NumV

        if(op.equals("/") and secondNum.number == 0)
            throw new RuntimeException(String.format("AAQZ divide by 0"))

        final NumV firstNum = firstArg as NumV

        switch(op) {
            case "-":
                return new NumV(firstNum.number - secondNum.number)
            case "*":
                return new NumV(firstNum.number * secondNum.number)
            case "/":
                return new NumV(firstNum.number / secondNum.number)
            case "<=":
                return new BoolV(firstNum.number <= secondNum.number)
            default: // +
                return new NumV(firstNum.number + secondNum.number)
        }
    }

    private static primopEqual(Value firstArg, Value secondArg) {
        if (firstArg.class == ClosV || secondArg.class == ClosV ||
                firstArg.class == PrimOpV || secondArg.class == PrimOpV ||
                firstArg.class != secondArg.class) {
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

    private static Value primopSeq(AppC exprC, Map<String, Value> env) {
        Value lastVal = new BoolV(false) // this will never be returned
        for (final ExprC arg : exprC.arguments) {
            lastVal = interp(arg, env)
        }

        return lastVal
    }

    private static StrV primopPlusPlus(AppC exprC, Map<String, Value> env) {
        String concatenatedStr = ""
        for (final ExprC arg : exprC.arguments) {
            concatenatedStr.concat(serialize(interp(arg, env)))
        }

        return new StrV(concatenatedStr)
    }
}
