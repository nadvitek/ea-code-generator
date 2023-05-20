package cz.cvut.fel.generator;

import cz.cvut.fel.configuration.LDMConfiguration;
import cz.cvut.fel.configuration.OpenApiConfiguration;
import cz.cvut.fel.metamodel.TPackage;
import cz.cvut.fel.repository.TObjectRepository;
import cz.cvut.fel.repository.TPackageRepository;
import cz.cvut.fel.service.YamlToStringService;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SwaggerGeneratorTest {

    private final SwaggerGenerator swaggerGenerator = new SwaggerGenerator(mock(TPackageRepository.class),
        mock(YamlToStringService.class),
        mock(TObjectRepository.class),
            "false");

    @DisplayName("LDM generation fails due to missing package")
    @Test
    public void generate_LDMConfigPackageNotFound_ThrowsException() {
//        ARRANGE
        LDMConfiguration ldm = new LDMConfiguration()
                .basePackage("package")
                .ignoredPackage("ignore")
                .mainVersion("main")
                .minorVersion("minor")
                .description("desc");

//        ACT
        assertThrows(IllegalArgumentException.class, () -> swaggerGenerator.generate(ldm));

//        ASSERT

    }

    @DisplayName("API generation fails due to missing Interface")
    @Test
    public void generate_APIConfigInterfaceNotFound_ThrowsException() {
//        ARRANGE
        OpenApiConfiguration openApiConfiguration =
                new OpenApiConfiguration("a", "1.0");


//        ACT
        assertThrows(IllegalArgumentException.class,
                () -> swaggerGenerator.generate(openApiConfiguration));

//        ASSERT

    }
}
