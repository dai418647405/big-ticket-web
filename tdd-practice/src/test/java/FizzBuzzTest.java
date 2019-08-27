import org.daijing.tdd.practice.GameNumber;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;


public class FizzBuzzTest {

//    @Test
//    public void should_print_1_to_100() {
//        FizzBuzz fizzBuzz = new FizzBuzz(1);
//        Assert.assertEquals("1", fizzBuzz.print());
//    }
//
//    @Test
//    public void should_print_fizz_when_can_divided_by_3() {
//        FizzBuzz fizzBuzz = new FizzBuzz(3);
//        Assert.assertEquals("Fizz", fizzBuzz.print());
//    }
//
//    @Test
//    public void should_print_buzz_when_can_divided_by_5() {
//        FizzBuzz fizzBuzz = new FizzBuzz(5);
//        Assert.assertEquals("Buzz", fizzBuzz.print());
//    }
//
//    @Test
//    public void should_print_fizz_buzz_when_can_divided_by_3_and_5() {
//        FizzBuzz fizzBuzz = new FizzBuzz(15);
//        Assert.assertEquals("FizzBuzz", fizzBuzz.print());
//    }

    @Test
    public void should_output_rawnumber() {
        checkOutput(1, "1");
        checkOutput(2, "2");
    }

    @Test
    public void should_output_fizz_when_can_divided_by_3() {
        checkOutput(3, "fizz");
    }

    @Test
    public void should_output_buzz_when_can_divided_by_5() {
        checkOutput(5, "buzz");
    }

    @Test
    public void should_output_fizzbuzz_when_can_divided_by_3_and_5() {
        checkOutput(15, "fizzbuzz");
    }

    @Test
    public void should_output_fizz_when_contains_3() {
        checkOutput(13, "fizz");
    }

    @Test
    public void should_output_fizzbuzz_when_contains_3_and_5() {
        checkOutput(53, "fizzbuzz");
    }

    private void checkOutput(int i, String s) {
        assertThat(new GameNumber(i).toString(), is(s));
    }

}
