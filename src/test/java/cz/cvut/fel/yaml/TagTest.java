package cz.cvut.fel.yaml;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import static cz.cvut.fel.yaml.Tag.makeTag;
import static junit.framework.TestCase.assertEquals;

public class TagTest {

    @DisplayName("Makes normal tag from classic path.")
    @Test
    public void makeTag_PathComesNormal_ReturnsRightTag() {
//        ARRANGE
        String path = "/animals";

//        ACT
        String tag = makeTag(path);

//        ASSERT
        assertEquals("animals", tag);

    }

    @DisplayName("Makes normal tag from short path.")
    @Test
    public void makeTag_PathComesShort_ReturnsRightTag() {
//        ARRANGE
        String path = "/A";

//        ACT
        String tag = makeTag(path);

//        ASSERT
        assertEquals("A", tag);

    }

    @DisplayName("Makes normal tag from long path.")
    @Test
    public void makeTag_PathComesLonger_ReturnsRightTag() {
//        ARRANGE
        String path = "/people/men";

//        ACT
        String tag = makeTag(path);

//        ASSERT
        assertEquals("people", tag);

    }

    @DisplayName("Makes normal tag from longer path.")
    @Test
    public void makeTag_PathComesVeryLonger_ReturnsRightTag() {
//        ARRANGE
        String path = "/athens/{houseId}/people";

//        ACT
        String tag = makeTag(path);

//        ASSERT
        assertEquals("athens", tag);

    }
}
