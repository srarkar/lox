package com.craftinginterpreters.lox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.craftinginterpreters.lox.TokenType.*; 

class Scanner {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();

    // offsets that index into the source string
    private int start = 0;
    private int current = 0;
    // source line of current so location is known
    private int line = 1;

    Scanner(String source) {
    this.source = source;
  }

// list we are going to fill with generated tokens
List<Token> scanTokens() {
    while (!isAtEnd()) {
      // We are at the beginning of the next lexeme.
      start = current;
      scanToken();
    }

    tokens.add(new Token(EOF, "", null, line));
    return tokens;
  }

  private void scanToken() {
    // single char lexemes
    char c = advance();
    switch (c) {
      case '(': addToken(LEFT_PAREN); break;
      case ')': addToken(RIGHT_PAREN); break;
      case '{': addToken(LEFT_BRACE); break;
      case '}': addToken(RIGHT_BRACE); break;
      case ',': addToken(COMMA); break;
      case '.': addToken(DOT); break;
      case '-': addToken(MINUS); break;
      case '+': addToken(PLUS); break;
      case ';': addToken(SEMICOLON); break;
      case '*': addToken(STAR); break; 
    }
  }

  // Helpers

  // All characters are consumed
  private boolean isAtEnd() {
    return current >= source.length();
  }

  // Consume and return next character
  private char advance() {
    return source.charAt(current++);
  }

  // Grab text of current lexeme
  private void addToken(TokenType type) {
    addToken(type, null);
  }

  // what advance() is for input, addToken is for output
  private void addToken(TokenType type, Object literal) {
    String text = source.substring(start, current);
    tokens.add(new Token(type, text, literal, line));
  }

}



