package me.anwarshahriar;

import java.util.*;

public class Assembler {
    private Set<Provider> providers;

    public Assembler() {
        providers = new HashSet<>();
    }

    public static Assembler newInstance() {
        return new Assembler();
    }

    public Collection<Provider> getProviders() {
        return providers;
    }

    public void add(Provider provider) {
        providers.add(provider);
    }
}
