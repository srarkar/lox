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

    // binary operators
    @Override
    public Object visitBinaryExpr(Expr.Binary expr) {
      Object left = evaluate(expr.left);
      Object right = evaluate(expr.right); 
  
      switch (expr.operator.type) {
        case GREATER:
          return (double)left > (double)right;
        case GREATER_EQUAL:
          return (double)left >= (double)right;
        case LESS:
          return (double)left < (double)right;
        case LESS_EQUAL:
          return (double)left <= (double)right;
        case MINUS:
          return (double)left - (double)right;
        case PLUS:
          if (left instanceof Double && right instanceof Double) {
            return (double)left + (double)right;
          } 

          if (left instanceof String && right instanceof String) {
            return (String)left + (String)right;
          }
          break;
        case BANG_EQUAL: return !isEqual(left, right);
        case EQUAL_EQUAL: return isEqual(left, right);
        case SLASH:
          return (double)left / (double)right;
        case STAR:
          return (double)left * (double)right;
      }
  
      // Unreachable.
      return null;
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
        case BANG:
            return !isTruthy(right);
        case MINUS:
          checkNumberOperand(expr.operator, right); // verify object type
          return -(double)right;
      }
  
      // Unreachable.
      return null;
    }

    private void checkNumberOperand(Token operator, Object operand) {
      if (operand instanceof Double) return;
      throw new RuntimeError(operator, "Operand must be a number.");
    }
    
    // only false and nil are falsey
    private boolean isTruthy(Object object) {
        if (object == null) return false;
        if (object instanceof Boolean) return (boolean)object;
        return true;
      }
    
    // it is important to consider what "equality" means in the context of a language
    // prevent null pointer exception
    private boolean isEqual(Object a, Object b) {
      if (a == null && b == null) return true;
      if (a == null) return false;

      return a.equals(b);
    }
}