package com.sourcegraph.semanticdb_javac;

import com.sun.tools.javac.code.Symbol;

import java.util.IdentityHashMap;

/** LocalSymbolsCache holds */
public final class LocalSymbolsCache {
  private final IdentityHashMap<Symbol, String> symbols = new IdentityHashMap<>();
  private int localsCounter = -1;

  public String get(Symbol sym) {
    return symbols.get(sym);
  }

  public String put(Symbol sym) {
    localsCounter++;
    String result = Symbols.local(String.valueOf(localsCounter));
    symbols.put(sym, result);
    return result;
  }
}
