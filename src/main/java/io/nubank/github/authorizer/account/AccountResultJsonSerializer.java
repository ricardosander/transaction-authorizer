package io.nubank.github.authorizer.account;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import java.io.IOException;

public class AccountResultJsonSerializer extends JsonSerializer<AccountResult> {

    @Override
    public void serialize(AccountResult accountResult, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("active-card");
        jsonGenerator.writeBoolean(accountResult.isActiveCard());
        jsonGenerator.writeFieldName("available-limit");
        jsonGenerator.writeNumber(accountResult.getAvailableLimit());
        jsonGenerator.writeEndObject();
    }
}
