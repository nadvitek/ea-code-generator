package cz.cvut.fel;

import cz.cvut.fel.generator.SwaggerGenerator;
import cz.cvut.fel.metamodel.TPackage;
import cz.cvut.fel.util.predicate.PackageNamePredicate;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.util.FileSystemUtils;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EaCodeGeneratorApplicationTest {

    EaCodeGeneratorApplication eaCodeGeneratorApplication;
    @DisplayName("Testing correct init.")
    @Test
    public void init_NoException_LogsCorrectly() {
//        ARRANGE
        eaCodeGeneratorApplication = new EaCodeGeneratorApplication(mock(SwaggerGenerator.class));

//        ACT + ASSERT
        eaCodeGeneratorApplication.init();

    }

    @DisplayName("Testing correct API generation.")
    @Test
    public void run_GenerationDoesntFailAPIType_FilesGenerated() {
//        ARRANGE
        eaCodeGeneratorApplication = new EaCodeGeneratorApplication(mock(SwaggerGenerator.class));
        eaCodeGeneratorApplication.setInterfaceName("a");
        eaCodeGeneratorApplication.setInterfaceMainVersion("b");

//        ACT + ASSERT
        eaCodeGeneratorApplication.run();

    }

    @DisplayName("Testing correct LDM generation.")
    @Test
    public void run_GenerationDoesntFailLDMType_FilesGenerated() {
//        ARRANGE
        eaCodeGeneratorApplication = new EaCodeGeneratorApplication(mock(SwaggerGenerator.class));
        eaCodeGeneratorApplication.setLdmPackage("a");

//        ACT + ASSERT
        eaCodeGeneratorApplication.run();

    }

    @DisplayName("Testing generation failure.")
    @Test
    public void run_GenerationFails_ExceptionThrown() {
//        ARRANGE
        eaCodeGeneratorApplication = new EaCodeGeneratorApplication(mock(SwaggerGenerator.class));

//        ACT + ASSERT
        assertThrows(UnsupportedOperationException.class,
                () -> eaCodeGeneratorApplication.run());

    }

    @DisplayName("Testing that stub generation doesn't crash application.")
    @Test
    public void run_StubGenerationFailsCorrectly_NoExceptionThrown() {
//        ARRANGE
        eaCodeGeneratorApplication = new EaCodeGeneratorApplication(mock(SwaggerGenerator.class));
        eaCodeGeneratorApplication.setLdmPackage("a");
        eaCodeGeneratorApplication.setGenerationAllowed(true);

//        ACT + ASSERT
        eaCodeGeneratorApplication.run();

    }

    @DisplayName("Testing that stub generation generates stubs.")
    @Test
    public void run_StubGeneration_FilesGenerated() {
//        ARRANGE
        eaCodeGeneratorApplication = new EaCodeGeneratorApplication(mock(SwaggerGenerator.class));
        eaCodeGeneratorApplication.setLdmPackage("a");
        eaCodeGeneratorApplication.setGenerationAllowed(true);

        eaCodeGeneratorApplication.setGenerationInput("src/test/resources/petstore.yaml");
        eaCodeGeneratorApplication.setGenerationBackendLanguage("spring");
        eaCodeGeneratorApplication.setGenerationFrontendLanguage("typescript-angular");
        eaCodeGeneratorApplication.setGenerationCommand("openapi-generator generate -i %s -g %s -o %s");
        eaCodeGeneratorApplication.setGenerationOutput("gen/");

//        ACT
        eaCodeGeneratorApplication.run();

//        ASSERT
        File file = new File("gen/backend/pom.xml");
        assertTrue(file.exists());

        FileSystemUtils.deleteRecursively(new File("gen/backend"));
        FileSystemUtils.deleteRecursively(new File("gen/frontend"));
    }

}
