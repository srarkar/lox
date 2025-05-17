
package com.craftinginterpreters.lox;

import java.util.List;

import static com.craftinginterpreters.lox.TokenType.*;

/*
Lox Grammar
expression     → equality ;
equality       → comparison ( ( "!=" | "==" ) comparison )* ;
comparison     → term ( ( ">" | ">=" | "<" | "<=" ) term )* ;
term           → factor ( ( "-" | "+" ) factor )* ;
factor         → unary ( ( "/" | "*" ) unary )* ;
unary          → ( "!" | "-" ) unary
               | primary ;
primary        → NUMBER | STRING | "true" | "false" | "nil"
               | "(" expression ")" ;
 */


// Parsing involves breaking down a string and choosing which productions to follow
// Recursive descent
class Parser {
  private final List<Token> tokens;
  private int current = 0;

  Parser(List<Token> tokens) {
    this.tokens = tokens;
  }

  //equality → comparison ( ( "!=" | "==" ) comparison )* ;
  private Expr expression() {
    return equality();
  }

  private Expr equality() {
    Expr expr = comparison();

    while (match(BANG_EQUAL, EQUAL_EQUAL)) {
      Token operator = previous();
      Expr right = comparison();
      expr = new Expr.Binary(expr, operator, right);
    }

    return expr;
  }

  // helpful to check if there is a sequence of equality operators (!=, ==) or not
  private boolean match(TokenType... types) {
    for (TokenType type : types) {
      if (check(type)) {
        advance();
        return true;
      }
    }
    return false;
  }
  // does not consume token
  private boolean check(TokenType type) {
    if (isAtEnd()) return false;
    return peek().type == type;
  }

  // primitive operations
  private Token advance() {
    if (!isAtEnd()) current++;
    return previous();
  }

  // ran out of tokens to parse
  private boolean isAtEnd() {
    return peek().type == EOF;
  }

  // current token about to be consumed
  private Token peek() {
    return tokens.get(current);
  }

  // most recently consumed token
  private Token previous() {
    return tokens.get(current - 1);
  }

}