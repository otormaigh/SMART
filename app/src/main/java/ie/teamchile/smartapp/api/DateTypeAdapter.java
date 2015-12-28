package ie.teamchile.smartapp.api;


import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.Date;

import ie.teamchile.smartapp.util.Constants;

public class DateTypeAdapter implements JsonSerializer<Date>, JsonDeserializer<Date> {
    @Override
    public Date deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        try {
            Date date;
            try {
                date = Constants.DF_SERVER_DATE_FULL.parse(jsonElement.getAsString());
            } catch (Exception ignored) {
                date = Constants.DF_SERVER_DATE_SMALL.parse(jsonElement.getAsString());
            }
            return date;
        } catch (ParseException e) {
            throw new JsonParseException(e);
        }
    }

    @Override
    public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(Constants.DF_SERVER_DATE_FULL.format(src));
    }
}