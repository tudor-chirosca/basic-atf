package org.static_examples.static_initialization;

public class TryIt {
    public static void main(String[] args) {
        System.out.println(x + ", " + y + ", " + z);
    }

    static int x;

    static {
        System.out.println(x + ", " + TryIt.y);
    }

    static int y = 100;

    static {
        System.out.println(x + ", " + TryIt.y);
    }

    static {
        x = y + 10;
    }

    static int z = -1;
}
