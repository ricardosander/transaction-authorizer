package io.nubank.github.authorizer;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import java.io.IOException;

class OperationResultJsonSerializer extends JsonSerializer<OperationResult> {

    @Override
    public void serialize(OperationResult operationResult, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("account");
        jsonGenerator.writeObject(operationResult.getAccount() == null ? new EmptyObject() : operationResult.getAccount());
        jsonGenerator.writeFieldName("violations");
        jsonGenerator.writeObject(operationResult.getViolations());
        jsonGenerator.writeEndObject();
    }
}
