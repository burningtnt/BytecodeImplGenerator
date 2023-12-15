package net.burningtn.bcidemos;

import net.burningtnt.bcidemos.demo0.Demo0;
import net.burningtnt.bcidemos.demo1.Demo1;
import net.burningtnt.bcidemos.demo2.Demo2;
import net.burningtnt.bcidemos.demo3.Demo3;
import org.junit.jupiter.api.Test;

public final class DemoTest {
    @Test
    public void demo0() {
        Demo0.main(new String[0]);
    }

    @Test
    public void demo1() {
        Demo1.main(new String[0]);
    }

    @Test
    public void demo2() {
        Demo2.main(new String[0]);
    }

    @Test
    public void demo3() {
        Demo3.main(new String[0]);
    }
}
