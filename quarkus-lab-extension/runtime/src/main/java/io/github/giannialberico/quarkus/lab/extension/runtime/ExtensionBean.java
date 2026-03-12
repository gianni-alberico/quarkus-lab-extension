package io.github.giannialberico.quarkus.lab.extension.runtime;

public class ExtensionBean {

    private final String name;

    public ExtensionBean(String name) {
        this.name = name;
    }

    public String greet() {
        return "Hello, " + name + "!";
    }
}
