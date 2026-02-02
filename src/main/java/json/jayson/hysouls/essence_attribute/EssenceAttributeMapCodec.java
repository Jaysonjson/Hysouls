package json.jayson.hysouls.essence_attribute;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.ExtraInfo;
import com.hypixel.hytale.codec.PrimitiveCodec;
import com.hypixel.hytale.codec.RawJsonCodec;
import com.hypixel.hytale.codec.codecs.simple.IntegerCodec;
import com.hypixel.hytale.codec.schema.SchemaContext;
import com.hypixel.hytale.codec.schema.config.IntegerSchema;
import com.hypixel.hytale.codec.schema.config.Schema;
import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.bson.BsonNull;
import org.bson.BsonValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class EssenceAttributeMapCodec implements Codec<HashMap<String, Integer>>, RawJsonCodec<HashMap<String, Integer>>, PrimitiveCodec {

    @Override
    public @Nullable HashMap<String, Integer> decode(BsonValue bsonValue, ExtraInfo extraInfo) {
        if(!bsonValue.isDocument()) return null;
        BsonDocument bsonDocument = bsonValue.asDocument();
        HashMap<String, Integer> map = new HashMap<>();
        for (Map.Entry<String, BsonValue> entry : bsonDocument.entrySet()) {
            BsonValue value = entry.getValue();
            if (value.isInt32()) {
                map.put(entry.getKey(), value.asInt32().getValue());
            } else if (value.isInt64()) {
                map.put(entry.getKey(), (int) value.asInt64().getValue());
            }
        }
        return map;
    }

    @Override
    public BsonValue encode(HashMap<String, Integer> map, ExtraInfo extraInfo) {
        if (map == null) return BsonNull.VALUE;

        BsonDocument doc = new BsonDocument();

        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            doc.put(entry.getKey(), new BsonInt32(entry.getValue()));
        }

        return doc;
    }

    //TODO NOT INTEGER SCHEMA
    @Override
    public @NotNull Schema toSchema(@NotNull SchemaContext schemaContext) {
        return new IntegerSchema();
    }

}

