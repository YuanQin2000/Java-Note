package test.ant;

import com.foo.Bar;

public class HelloAnt {
    public static void main(String[] args) {
        Bar bar = new Bar("BuildTool");
        bar.DoSomething("Hello Ant...");
    }
}
