package com.craftinginterpreters.lox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class Lox {
    static boolean hadError = false;
    public static void main(String[] args) throws IOException {
        if (args.length > 1) {
          System.out.println("Usage: jlox [script]");
          System.exit(64); 
        } else if (args.length == 1) {
          runFile(args[0]);
        } else {
          runPrompt();
        }
      }

      // Allow jlox to execute a file, given a path to it
      private static void runFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()));
        if (hadError) System.exit(65);
      }

      // Let the user execute code directly, one line at a time
      // Control + D to signal EOF, which breaks out of the loop
      private static void runPrompt() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);
    
        for (;;) { 
          System.out.print("> ");
          String line = reader.readLine();
          if (line == null) break;
          run(line);
          hadError = false; // Reset error flag
        }
      }
      private static void run(String source) {
        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();

        // parse to a syntax tree then use AstPrinter
        Parser parser = new Parser(tokens);
        Expr expression = parser.parse();

        // Stop if there was a syntax error.
        if (hadError) return;

        System.out.println(new AstPrinter().print(expression));
      }

      // Basic error handling
      // Necessary to include line number!
      // Not implemented, but can also specify exact location of error via string manipulation
      static void error(int line, String message) {
        report(line, "", message);
      }
    
      private static void report(int line, String where,
                                 String message) {
        System.err.println(
            "[line " + line + "] Error" + where + ": " + message);
        hadError = true;
      }

      // show parsing error to user
      static void error(Token token, String message) {
        if (token.type == TokenType.EOF) {
          report(token.line, " at end", message);
        } else {
          report(token.line, " at '" + token.lexeme + "'", message);
        }
      }

    


    }

