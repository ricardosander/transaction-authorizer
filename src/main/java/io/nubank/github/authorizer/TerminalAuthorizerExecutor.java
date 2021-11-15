package io.nubank.github.authorizer;

import io.nubank.github.authorizer.account.AccountRepositoryFactory;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TerminalAuthorizerExecutor {

    private final Scanner input;
    private final PrintStream output;
    private final ObjectMapper jsonMapper;
    private final AuthorizerProcessor authorizerProcessor;

    public TerminalAuthorizerExecutor(Scanner input, PrintStream output, ObjectMapper jsonMapper) {
        this.input = input;
        this.output = output;
        this.jsonMapper = jsonMapper;
        this.authorizerProcessor = new AuthorizerProcessor(AccountRepositoryFactory.create());
    }

    public void execute() throws IOException {

        List<OperationRequest> requests = new ArrayList<>();
        while (input.hasNext()) {
            String line = input.nextLine();
            Operation operation = jsonMapper.readValue(line, Operation.class);
            requests.add(operation.toDomain());
        }

        List<OperationResult> results = authorizerProcessor.execute(requests);

        for (OperationResult result : results) {
            output.println(jsonMapper.writeValueAsString(result));
        }
    }
}
