package cz.cvut.fel.util.predicate;

import cz.cvut.fel.metamodel.TObject;
import cz.cvut.fel.metamodel.TPackage;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PackageNamePredicateTest {

    private PackageNamePredicate packageNamePredicate;

    @DisplayName("Compare two equal Package names.")
    @Test
    public void evaluate_SameObjects_ReturnsTrue() {
//        ARRANGE
        packageNamePredicate = new PackageNamePredicate("MyModels", false);
        TPackage tPackage = mock(TPackage.class);

        when(tPackage.getName()).thenReturn("MyModels");

//        ACT
        boolean result = packageNamePredicate.evaluate(tPackage);

//        ASSERT
        assertTrue(result);

    }

    @DisplayName("Compare two different Package names.")
    @Test
    public void evaluate_DifferentObjects_ReturnsFalse() {
//        ARRANGE
        packageNamePredicate = new PackageNamePredicate("MyModelsPackage", false);
        TPackage tPackage = mock(TPackage.class);

        when(tPackage.getName()).thenReturn("MyModels");

//        ACT
        boolean result = packageNamePredicate.evaluate(tPackage);

//        ASSERT
        assertFalse(result);

    }

    @DisplayName("Compare two same Package names - ignore cases.")
    @Test
    public void evaluate_IgnoreCasesSamePackages_ReturnsTrue() {
//        ARRANGE
        packageNamePredicate = new PackageNamePredicate("MyModels", true);
        TPackage tPackage = mock(TPackage.class);

        when(tPackage.getName()).thenReturn("mymodels");

//        ACT
        boolean result = packageNamePredicate.evaluate(tPackage);

//        ASSERT
        assertTrue(result);

    }

    @DisplayName("Not a TPackage object")
    @Test
    public void evaluate_NotTPackage_ThrowsException() {
//        ARRANGE
        packageNamePredicate = new PackageNamePredicate("MyModels", true);

//        ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> packageNamePredicate.evaluate(mock(TObject.class)));


    }
}
