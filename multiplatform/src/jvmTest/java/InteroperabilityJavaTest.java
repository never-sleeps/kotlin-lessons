import jvm.InteroperabilityJava;
import kotlin.test.UtilsKt;
import org.junit.jupiter.api.Test;

public class InteroperabilityJavaTest {

    @Test
    void test1() {
        InteroperabilityJava.Companion.functionOne(); // без @JvmStatic возможен только такой вариант
        InteroperabilityJava.functionOne(); // с @JvmStatic
    }

    @Test
    void test2() {
        new InteroperabilityJava().defaults("123", 123, true);
        System.out.println(new InteroperabilityJava().defaults("p1"));
    }

    @Test
    void test3() {
        System.out.println(new InteroperabilityJava().customName());
    }

    @Test
    void test4() {
        DateUtils.getDate(); // вызов kotlin-метода из UtilDate.kt
        DateUtils.getName(); // вызов kotlin-метода из UtilName.kt
    }
}
