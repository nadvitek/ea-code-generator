package cz.cvut.fel.util.predicate;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

public class EqualStringPredicateTest {

    private EqualStringPredicate equalStringPredicate;

    @DisplayName("Compare two equal Strings.")
    @Test
    public void evaluate_SameObjects_ReturnsTrue() {
//        ARRANGE
        equalStringPredicate = new EqualStringPredicate("HiWorld", false);

//        ACT
        boolean result = equalStringPredicate.evaluate("HiWorld");

//        ASSERT
        assertTrue(result);

    }

    @DisplayName("Compare two different Strings.")
    @Test
    public void evaluate_DifferentObjects_ReturnsFalse() {
//        ARRANGE
        equalStringPredicate = new EqualStringPredicate("HiWorld", false);

//        ACT
        boolean result = equalStringPredicate.evaluate("HiWoooorld");

//        ASSERT
        assertFalse(result);

    }

    @DisplayName("Compare two different Strings.")
    @Test
    public void evaluate_IgnoreCaseSameStrings_ReturnsTrue() {
//        ARRANGE
        equalStringPredicate = new EqualStringPredicate("HiWorld", true);

//        ACT
        boolean result = equalStringPredicate.evaluate("hiworld");

//        ASSERT
        assertTrue(result);

    }

    @DisplayName("Compare two different Strings.")
    @Test
    public void evaluate_NotString_ThrowsException() {
//        ARRANGE
        equalStringPredicate = new EqualStringPredicate("HiWorld", true);

//        ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                ()-> equalStringPredicate.evaluate(12));

    }
}
