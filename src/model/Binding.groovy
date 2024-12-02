package model

import model.value.Value

class Binding {
    private String name;

    private Value val;

    Binding(name, val) {
        this.name = name;
        this.val = val;
    }
}
