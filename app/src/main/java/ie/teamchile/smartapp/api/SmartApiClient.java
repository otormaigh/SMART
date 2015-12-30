package ie.teamchile.smartapp.api;

import android.content.Context;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.Date;

import ie.teamchile.smartapp.BuildConfig;
import ie.teamchile.smartapp.model.Login;
import ie.teamchile.smartapp.model.RealmInteger;
import ie.teamchile.smartapp.model.RealmString;
import ie.teamchile.smartapp.util.NotKeys;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

/**
 * Created by user on 8/1/15.
 */
public class SmartApiClient {
    private static final String AUTH_TOKEN = "Auth-Token";
    private static final String API_KEY = "Api-Key";
    private static SmartApi unAuthorizedSmartApi;
    private static SmartApi authorizedSmartApi;
    private static Gson gson;

    static {
        gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateTypeAdapter())
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .setPrettyPrinting()
                .setExclusionStrategies(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
                        return f.getDeclaringClass().equals(RealmObject.class);
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return false;
                    }
                })
                .registerTypeAdapter(new TypeToken<RealmList<RealmInteger>>() {
                }.getType(), new TypeAdapter<RealmList<RealmInteger>>() {

                    @Override
                    public void write(JsonWriter out, RealmList<RealmInteger> value) throws IOException {
                        // Ignore
                    }

                    @Override
                    public RealmList<RealmInteger> read(JsonReader in) throws IOException {
                        RealmList<RealmInteger> list = new RealmList<>();
                        in.beginArray();
                        while (in.hasNext()) {
                            list.add(new RealmInteger(in.nextInt()));
                        }
                        in.endArray();
                        return list;
                    }
                })
                .registerTypeAdapter(new TypeToken<RealmList<RealmString>>() {
                }.getType(), new TypeAdapter<RealmList<RealmString>>() {

                    @Override
                    public void write(JsonWriter out, RealmList<RealmString> value) throws IOException {
                        // Ignore
                    }

                    @Override
                    public RealmList<RealmString> read(JsonReader in) throws IOException {
                        RealmList<RealmString> list = new RealmList<>();
                        in.beginArray();
                        while (in.hasNext()) {
                            list.add(new RealmString(in.nextString()));
                        }
                        in.endArray();
                        return list;
                    }
                })
                .create();
    }

    public static synchronized SmartApi getUnAuthorizedApiClient() {
        if (unAuthorizedSmartApi == null) {

            unAuthorizedSmartApi = new RestAdapter.Builder()
                    .setEndpoint(NotKeys.BASE_URL + NotKeys.PORT)
                    .setLogLevel(BuildConfig.RETROFIT_LOG_LEVEL)
                    .setClient(new OkClient())
                    .setConverter(new GsonConverter(gson))
                    .build().create(SmartApi.class);
        }
        return unAuthorizedSmartApi;
    }

    public static synchronized SmartApi getAuthorizedApiClient(final Context context) {
        if (authorizedSmartApi == null) {
            RequestInterceptor requestInterceptor = new RequestInterceptor() {
                @Override
                public void intercept(RequestFacade request) {
                    String authToken = Realm.getInstance(context).where(Login.class).findFirst().getToken();
                    request.addHeader(AUTH_TOKEN, authToken);
                    request.addHeader(API_KEY, NotKeys.API_KEY);
                }
            };

            authorizedSmartApi = new RestAdapter.Builder()
                    .setEndpoint(NotKeys.BASE_URL + NotKeys.PORT)
                    .setLogLevel(BuildConfig.RETROFIT_LOG_LEVEL)
                    .setClient(new OkClient())
                    .setRequestInterceptor(requestInterceptor)
                    .setConverter(new GsonConverter(gson))
                    .build().create(SmartApi.class);
        }
        return authorizedSmartApi;
    }

    public static synchronized SmartApi getAuthorizedApiClient(final Realm realm) {
        if (authorizedSmartApi == null) {
            RequestInterceptor requestInterceptor = new RequestInterceptor() {
                @Override
                public void intercept(RequestFacade request) {
                    String authToken = realm.where(Login.class).findFirst().getToken();
                    request.addHeader(AUTH_TOKEN, authToken);
                    request.addHeader(API_KEY, NotKeys.API_KEY);
                }
            };

            authorizedSmartApi = new RestAdapter.Builder()
                    .setEndpoint(NotKeys.BASE_URL + NotKeys.PORT)
                    .setLogLevel(BuildConfig.RETROFIT_LOG_LEVEL)
                    .setClient(new OkClient())
                    .setRequestInterceptor(requestInterceptor)
                    .setConverter(new GsonConverter(gson))
                    .build().create(SmartApi.class);
        }
        return authorizedSmartApi;
    }
}
