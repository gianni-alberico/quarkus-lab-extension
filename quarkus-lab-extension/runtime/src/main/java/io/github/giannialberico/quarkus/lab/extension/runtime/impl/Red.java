package io.github.giannialberico.quarkus.lab.extension.runtime.impl;

import io.github.giannialberico.quarkus.lab.extension.runtime.Colored;
import jakarta.inject.Singleton;

@Singleton
public class Red implements Colored {
    @Override
    public String getColor() {
        return "red";
    }
}
