package io.nubank.github.authorizer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

class AuthorizerApplicationTests {

    private static final String[] APPLICATION_ARGS = new String[0];
    private static final String TEST_RESOURCES_FOLDER = "src/test/resources/";

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        outContent.reset();

        System.setOut(new PrintStream(outContent));
    }

    @Test
    void testExample() throws IOException {

        String testCase = "example";
        String expectedResult = configureInputAndOutput(testCase);

        AuthorizerApplication.main(APPLICATION_ARGS);

        assertThat(outContent.toString().trim()).isEqualTo(expectedResult.trim());
    }

    private String configureInputAndOutput(String testCase) throws IOException {
        configureInput(testCase);
        return extractExpectedResult(testCase);
    }

    private void configureInput(String testCase) throws FileNotFoundException {
        FileInputStream input = extractInput(testCase);
        System.setIn(input);
    }

    private String extractExpectedResult(String testCase) throws IOException {
        return Files.readString(Paths.get(TEST_RESOURCES_FOLDER + testCase + "/output"), StandardCharsets.UTF_8);
    }

    private FileInputStream extractInput(String testCase) throws FileNotFoundException {
        return new FileInputStream(TEST_RESOURCES_FOLDER + testCase + "/input");
    }

}
