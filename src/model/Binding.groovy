package model

import model.value.Value

class Binding {
    String name;

    Value val;

    Binding(name, val) {
        this.name = name;
        this.val = val;
    }
}
