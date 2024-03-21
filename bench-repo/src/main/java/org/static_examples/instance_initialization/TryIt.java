package org.static_examples.instance_initialization;

class TryIt {
    public static void main(String[] args) {
        new TryIt().go();
    }

    public void go() {
        System.out.println(x + ", " + y + ", " + z);
    }

    int x;

    {
        System.out.println(x + ", " + this.y);
    }

    int y = 100;

    {
        System.out.println(x + ", " + this.y);
    }

    {
        x = y + 10;
    }

    int z = -1;
}
