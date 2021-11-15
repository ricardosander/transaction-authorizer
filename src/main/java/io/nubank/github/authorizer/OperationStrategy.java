package io.nubank.github.authorizer;

public interface OperationStrategy {
    OperationResult execute(OperationRequest request);
}
