package today.also.hyuil;

import org.assertj.core.api.Assertions;

import java.util.Date;

public class Test {

    @org.junit.jupiter.api.Test
    void test() {

        Assertions.assertThat(new Date(20220310)).isAfter(new Date());
    }

}
