package io.nubank.github.authorizer;

import io.nubank.github.authorizer.account.AccountOperation;
import io.nubank.github.authorizer.transaction.TransactionOperation;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

import java.io.IOException;

class OperationJsonDeserializer extends JsonDeserializer<Operation> {

    @Override
    public Operation deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {

        JsonNode jsonNode = jsonParser.getCodec().readTree(jsonParser);

        JsonNode account = jsonNode.get("account");
        if (account != null) {
            return AccountOperation.create(
                    account.get("active-card").asBoolean(),
                    account.get("available-limit").asInt()
            );
        }

        JsonNode transaction = jsonNode.get("transaction");
        if (transaction != null) {
            return TransactionOperation.create(
                    transaction.get("merchant").asText(),
                    transaction.get("amount").asInt(),
                    transaction.get("time").asText()
            );
        }

        throw new IllegalArgumentException("Unexpected Operation type");
    }
}
