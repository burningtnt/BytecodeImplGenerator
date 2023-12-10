package net.burningtn.bcidemos;

import net.burningtnt.bcidemos.demo0.Demo0;
import net.burningtnt.bcidemos.demo1.Demo1;
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
}
