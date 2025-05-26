package com.craftinginterpreters.lox;

class Interpreter implements Expr.Visitor<Object> {
    
    // grouping via parenthesis
    // recursively evaluate sub-expression
    @Override
    public Object visitGroupingExpr(Expr.Grouping expr) {
      return evaluate(expr.expression);
    }

    private Object evaluate(Expr expr) {
        return expr.accept(this);
    }

    // convert tree node into runtime value
    @Override
    public Object visitLiteralExpr(Expr.Literal expr) {
      return expr.value;
    }
    
    // note how we cast since we do not statically know if the subexpression is a number
    // the cast occurs at runtime when the - is evaluated, which is what it means to be dynamically typed
    @Override
    public Object visitUnaryExpr(Expr.Unary expr) {
      Object right = evaluate(expr.right);
  
      switch (expr.operator.type) {
        case MINUS:
          return -(double)right;
      }
  
      // Unreachable.
      return null;
    }
}