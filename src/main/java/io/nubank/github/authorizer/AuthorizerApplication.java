package io.nubank.github.authorizer;

import io.nubank.github.authorizer.account.AccountResult;
import io.nubank.github.authorizer.account.AccountResultJsonSerializer;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.module.SimpleModule;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

public class AuthorizerApplication {

    public static void main(String[] args) throws IOException {

        Scanner input = new Scanner(System.in);
        PrintStream output = System.out;
        ObjectMapper jsonMapper = configureJsonMapper();

        TerminalAuthorizerExecutor authorizer = new TerminalAuthorizerExecutor(input, output, jsonMapper);
        authorizer.execute();
    }

    private static ObjectMapper configureJsonMapper() {

        ObjectMapper objectMapper = new ObjectMapper();

        SimpleModule simpleModule = createJacksonModule();

        simpleModule.addDeserializer(Operation.class, new OperationJsonDeserializer());
        simpleModule.addSerializer(OperationResult.class, new OperationResultJsonSerializer());
        simpleModule.addSerializer(AccountResult.class, new AccountResultJsonSerializer());
        simpleModule.addSerializer(EmptyObject.class, new EmptyJsonSerializer());

        objectMapper.registerModule(simpleModule);

        return objectMapper;
    }

    private static SimpleModule createJacksonModule() {
        return new SimpleModule("AuthorizerApplicationModule", new Version(1, 0, 0, null));
    }

}
