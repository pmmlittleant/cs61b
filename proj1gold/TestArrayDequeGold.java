import static org.junit.Assert.*;
import org.junit.Test;

public class TestArrayDequeGold {


    @Test
    public void arrayTest() {
        StudentArrayDeque<Integer> sad1 = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> ads = new ArrayDequeSolution<>();
        String operation = "";

        for (int i = 0; i < 1000; i++) {
            Integer number = StdRandom.uniform(1000);
            if (sad1.size() < number) {
                sad1.addFirst(number);
                ads.addFirst(number);
                operation += "addFirst(" + number + ")\n";
            } else {
                sad1.addLast(number);
                ads.addLast(number);
                operation += "addLast(" + number + ")\n";
            }
            Integer x = StdRandom.uniform(0,4);
            Integer act = 1, exp = 1;
            if (x == 0) {
                act = sad1.removeFirst();
                exp = ads.removeFirst();
                operation += "removeFirst()\n";
            } else if (x == 1) {
                act = sad1.removeLast();
                exp = ads.removeLast();
                operation += "removeLast()\n";
            }  else if (x == 2) {
                sad1.addLast(number);
                ads.addLast(number);
                operation += "addLast(" + number + ")\n";
            }   else {
                sad1.addFirst(number);
                ads.addFirst(number);
                operation += "addFirst(" + number + ")\n";
            }
            assertEquals(operation, exp,act);
        }

    }
}