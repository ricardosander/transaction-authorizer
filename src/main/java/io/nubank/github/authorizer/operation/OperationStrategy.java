package io.nubank.github.authorizer.operation;

public interface OperationStrategy {
    OperationResult execute(OperationRequest request);
}
