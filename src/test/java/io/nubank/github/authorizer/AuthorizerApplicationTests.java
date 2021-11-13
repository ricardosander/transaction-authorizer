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

        String testCase = "simple-example";
        String expectedResult = configureInputAndOutput(testCase);

        AuthorizerApplication.main(APPLICATION_ARGS);

        assertThat(outContent.toString().trim()).isEqualTo(expectedResult.trim());
    }

    @Test
    void testAccountCreation() throws IOException {

        String testCase = "account-creation";
        String expectedResult = configureInputAndOutput(testCase);

        AuthorizerApplication.main(APPLICATION_ARGS);

        assertThat(outContent.toString().trim()).isEqualTo(expectedResult.trim());
    }

    @Test
    void testAccountCreationWithViolation() throws IOException {

        String testCase = "account-creation-with-violation";
        String expectedResult = configureInputAndOutput(testCase);

        AuthorizerApplication.main(APPLICATION_ARGS);

        assertThat(outContent.toString().trim()).isEqualTo(expectedResult.trim());
    }

    @Test
    void testTransactionCreation() throws IOException {

        String testCase = "transaction-creation";
        String expectedResult = configureInputAndOutput(testCase);

        AuthorizerApplication.main(APPLICATION_ARGS);

        assertThat(outContent.toString().trim()).isEqualTo(expectedResult.trim());
    }

    @Test
    void testTransactionCreationAccountNotInitialized() throws IOException {

        String testCase = "transaction-creation-account-not-initialized";
        String expectedResult = configureInputAndOutput(testCase);

        AuthorizerApplication.main(APPLICATION_ARGS);

        assertThat(outContent.toString().trim()).isEqualTo(expectedResult.trim());
    }

    @Test
    void testTransactionCreationCardNotActive() throws IOException {

        String testCase = "transaction-creation-card-not-active";
        String expectedResult = configureInputAndOutput(testCase);

        AuthorizerApplication.main(APPLICATION_ARGS);

        assertThat(outContent.toString().trim()).isEqualTo(expectedResult.trim());
    }

    @Test
    void testTransactionCreationInsufficientLimit() throws IOException {

        String testCase = "transaction-creation-insufficient-limit";
        String expectedResult = configureInputAndOutput(testCase);

        AuthorizerApplication.main(APPLICATION_ARGS);

        assertThat(outContent.toString().trim()).isEqualTo(expectedResult.trim());
    }

    @Test
    void testTransactionCreationHighFrequencySmallInterval() throws IOException {

        String testCase = "transaction-creation-high-frequency-small-interval";
        String expectedResult = configureInputAndOutput(testCase);

        AuthorizerApplication.main(APPLICATION_ARGS);

        assertThat(outContent.toString().trim()).isEqualTo(expectedResult.trim());
    }

    @Test
    void testTransactionCreationDoubledTransaction() throws IOException {

        String testCase = "transaction-creation-doubled-transaction";
        String expectedResult = configureInputAndOutput(testCase);

        AuthorizerApplication.main(APPLICATION_ARGS);

        assertThat(outContent.toString().trim()).isEqualTo(expectedResult.trim());
    }

    @Test
    void testTransactionCreationMultipleViolations() throws IOException {

        String testCase = "transaction-creation-multiple-violations";
        String expectedResult = configureInputAndOutput(testCase);

        AuthorizerApplication.main(APPLICATION_ARGS);

        assertThat(outContent.toString().trim()).isEqualTo(expectedResult.trim());
    }

    @Test
    void testComplexExample() throws IOException {

        String testCase = "complex-example";
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
