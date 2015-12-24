package ie.teamchile.smartapp.api;


import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import ie.teamchile.smartapp.util.Constants;

public class DateTypeAdapter implements JsonSerializer<Date>, JsonDeserializer<Date> {
    @Override
    public Date deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        try {
            DateFormat df = new SimpleDateFormat(Constants.SERVER_DATE_FORMAT, Locale.getDefault());
            Date date;

            try {
                date = df.parse(jsonElement.getAsString());
            } catch (Exception ignored) {
                df = new SimpleDateFormat(Constants.SERVER_SMALL_DATE, Locale.getDefault());
                date = df.parse(jsonElement.getAsString());
            }
            return date;
        } catch (ParseException e) {
            throw new JsonParseException(e);
        }
    }

    @Override
    public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.SERVER_DATE_FORMAT, Locale.getDefault());
        Calendar cal = Calendar.getInstance();
        cal.setTime(src);
        return new JsonPrimitive(simpleDateFormat.format(cal.getTime()));
    }
}