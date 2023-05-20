package cz.cvut.fel.service;

import cz.cvut.fel.yaml.*;
import cz.cvut.fel.yaml.enums.PrimitiveType;
import cz.cvut.fel.yaml.enums.Stereotype;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.*;

import static cz.cvut.fel.util.YamlCreator.TAB;
import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class YamlToStringServiceTest {

    private final YamlToStringService yamlToStringService = new YamlToStringService();

    @DisplayName("Creates String based on model.")
    @Test
    public void toStringModel_ModelOfNoParts_ReturnsCorrectString() {
//        ARRANGE
        Model model = mock(Model.class);

        when(model.getOpenApiVersion()).thenReturn("3.0.3");
        when(model.getDescription()).thenReturn("description");
        when(model.getTitle()).thenReturn("title");
        when(model.getVersion()).thenReturn("1.0");

        when(model.getTags()).thenReturn(Collections.emptyList());
        when(model.getMethods()).thenReturn(Collections.emptyMap());
        when(model.getSchemas()).thenReturn(Collections.emptyList());


        String expectedValue =
                "openapi: \"3.0.3\"\n" +
                        "info:\n" +
                        TAB + "description: \"description\"\n" +
                        TAB + "title: \"title\"\n" +
                        TAB + "version: \"1.0\"\n" +
                        "tags:\n" +
                        "paths:\n" +
                        "components:\n" +
                        TAB + "schemas:";

//        ACT
        String result = yamlToStringService.toString(model);

//        ASSERT
        assertEquals(expectedValue, result);
    }

    @DisplayName("Creates String based on model with Tags.")
    @Test
    public void toStringModel_ModelWithTags_ReturnsCorrectString() {
//        ARRANGE
        Model model = mock(Model.class);

        when(model.getOpenApiVersion()).thenReturn("3.0.3");
        when(model.getDescription()).thenReturn("description");
        when(model.getTitle()).thenReturn("title");
        when(model.getVersion()).thenReturn("1.0");


        List<Tag> tags = Arrays.asList(
                new Tag("pets"),
                new Tag("customers")
        );

        when(model.getTags()).thenReturn(tags);
        when(model.getMethods()).thenReturn(Collections.emptyMap());
        when(model.getSchemas()).thenReturn(Collections.emptyList());

        String tagsAsString =
                TAB + "-\n" +
                        TAB + TAB + "name: pets\n" +
                        TAB + TAB + "description: \"Operations with Pets.\"\n" +
                        TAB + "-\n" +
                        TAB + TAB + "name: customers\n" +
                        TAB + TAB + "description: \"Operations with Customers.\"\n";

        String expectedValue =
                "openapi: \"3.0.3\"\n" +
                        "info:\n" +
                        TAB + "description: \"description\"\n" +
                        TAB + "title: \"title\"\n" +
                        TAB + "version: \"1.0\"\n" +
                        "tags:\n" +
                        tagsAsString +
                        "paths:\n" +
                        "components:\n" +
                        TAB + "schemas:";

//        ACT
        String result = yamlToStringService.toString(model);

//        ASSERT
        assertEquals(expectedValue, result);
    }

    @DisplayName("Creates String based on model with path.")
    @Test
    public void toStringModel_ModelWithPath_ReturnsCorrectString() {
//        ARRANGE
        Model model = mock(Model.class);

        when(model.getOpenApiVersion()).thenReturn("3.0.3");
        when(model.getDescription()).thenReturn("description");
        when(model.getTitle()).thenReturn("title");
        when(model.getVersion()).thenReturn("1.0");

        when(model.matchingTag("/pets")).thenReturn("pets");

        Method method = mock(Method.class);
        when(method.getModel()).thenReturn(model);
        when(method.getType()).thenReturn(Stereotype.METHOD_GET);
        when(method.getPath()).thenReturn("/pets");
        when(method.getSummary()).thenReturn("Get Pets");

        List<Method> methods = Collections.singletonList(
                method
        );

        Map<String, List<Method>> methodMap = new HashMap<>();
        methodMap.put("/pets", methods);

        when(model.getTags()).thenReturn(Collections.emptyList());
        when(model.getMethods()).thenReturn(methodMap);
        when(model.getSchemas()).thenReturn(Collections.emptyList());

        String pathValue =
                TAB + "/pets:\n" +
                        TAB + TAB + "get:\n" +
                        TAB + TAB + TAB + "tags:\n" +
                        TAB + TAB + TAB + TAB + "- pets\n" +
                        TAB + TAB + TAB + "summary: Get Pets\n";

        String expectedValue =
                "openapi: \"3.0.3\"\n" +
                        "info:\n" +
                        TAB + "description: \"description\"\n" +
                        TAB + "title: \"title\"\n" +
                        TAB + "version: \"1.0\"\n" +
                        "tags:\n" +
                        "paths:\n" +
                        pathValue +
                        "components:\n" +
                        TAB + "schemas:";

//        ACT
        String result = yamlToStringService.toString(model);

//        ASSERT
        assertEquals(expectedValue, result);
    }

    @DisplayName("Creates String based on model with schema.")
    @Test
    public void toStringModel_ModelWithSchema_ReturnsCorrectString() {
//        ARRANGE
        Model model = mock(Model.class);

        when(model.getOpenApiVersion()).thenReturn("3.0.3");
        when(model.getDescription()).thenReturn("description");
        when(model.getTitle()).thenReturn("title");
        when(model.getVersion()).thenReturn("1.0");

        ComplexSchema sch1 = mock(ComplexSchema.class);
        ComplexSchema sch2 = mock(ComplexSchema.class);

        Property property = mock(Property.class);

        PrimitiveSchema primitiveSchema = mock(PrimitiveSchema.class);

        when(primitiveSchema.getType()).thenReturn(String.valueOf(PrimitiveType.INT));

        SrcCard srcCard = mock(SrcCard.class);
        when(srcCard.getName()).thenReturn("id");

        when(property.getSchema()).thenReturn(primitiveSchema);
        when(property.getSrcCard()).thenReturn(srcCard);

        List<Property> properties = Collections.singletonList(
                property
        );

        when(sch1.getSrcCard()).thenReturn(mock(SrcCard.class));
        when(sch1.getName()).thenReturn("Pet");
        when(sch1.getProperties()).thenReturn(properties);

        Property property2 = mock(Property.class);

        PrimitiveSchema primitiveSchema2 = mock(PrimitiveSchema.class);

        when(primitiveSchema2.getType()).thenReturn(String.valueOf(PrimitiveType.STRING));

        SrcCard srcCard2 = mock(SrcCard.class);
        when(srcCard2.getName()).thenReturn("name");

        when(property2.getSchema()).thenReturn(primitiveSchema2);
        when(property2.getSrcCard()).thenReturn(srcCard2);

        List<Property> properties2 = Collections.singletonList(
                property2
        );


        when(sch2.getSrcCard()).thenReturn(mock(SrcCard.class));
        when(sch2.getName()).thenReturn("Customer");
        when(sch2.getProperties()).thenReturn(properties2);

        List<Schema> schemas = Arrays.asList(
                sch1,
                sch2
        );

        when(model.getTags()).thenReturn(Collections.emptyList());
        when(model.getMethods()).thenReturn(Collections.emptyMap());
        when(model.getSchemas()).thenReturn(schemas);

        String schemaValue =
                TAB + TAB + "# null\n" +
                TAB + TAB + "Pet:\n" +
                TAB + TAB + TAB + "type: object\n" +
                        TAB + TAB + TAB + "properties:\n" +
                        TAB + TAB + TAB + TAB + "id:\n" +
                        TAB + TAB + TAB + TAB + TAB + "type: int\n" +
                TAB + TAB + "# null\n" +
                TAB + TAB + "Customer:\n" +
                TAB + TAB + TAB + "type: object\n" +
                        TAB + TAB + TAB + "properties:\n" +
                        TAB + TAB + TAB + TAB + "name:\n" +
                        TAB + TAB + TAB + TAB + TAB + "type: string";


        String expectedValue =
                "openapi: \"3.0.3\"\n" +
                        "info:\n" +
                        TAB + "description: \"description\"\n" +
                        TAB + "title: \"title\"\n" +
                        TAB + "version: \"1.0\"\n" +
                        "tags:\n" +
                        "paths:\n" +
                        "components:\n" +
                        TAB + "schemas:\n" +
                        schemaValue;

//        ACT
        String result = yamlToStringService.toString(model);

//        ASSERT
        assertEquals(expectedValue, result);
    }
}
