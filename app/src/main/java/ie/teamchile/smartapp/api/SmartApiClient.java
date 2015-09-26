package ie.teamchile.smartapp.api;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ie.teamchile.smartapp.BuildConfig;
import ie.teamchile.smartapp.model.BaseModel;
import ie.teamchile.smartapp.util.NotKeys;
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
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .setPrettyPrinting()
                .create();
    }

    public static synchronized SmartApi getUnAuthorizedApiClient() {
        if (unAuthorizedSmartApi == null) {

            unAuthorizedSmartApi = new RestAdapter.Builder()
                    .setEndpoint(NotKeys.BASE_URL)
                    .setLogLevel(BuildConfig.RETROFIT_LOG_LEVEL)
                    .setClient(new OkClient())
                    .setConverter(new GsonConverter(gson))
                    .build().create(SmartApi.class);
        }
        return unAuthorizedSmartApi;
    }

    public static synchronized SmartApi getAuthorizedApiClient() {
        if (authorizedSmartApi == null) {
            RequestInterceptor requestInterceptor = new RequestInterceptor() {
                @Override
                public void intercept(RequestFacade request) {
                    String authToken = BaseModel.getInstance().getLogin().getToken();
                    request.addHeader(AUTH_TOKEN, authToken);
                    request.addHeader(API_KEY, NotKeys.API_KEY);
                }
            };

            authorizedSmartApi = new RestAdapter.Builder()
                    .setEndpoint(NotKeys.BASE_URL)
                    .setLogLevel(BuildConfig.RETROFIT_LOG_LEVEL)
                    .setClient(new OkClient())
                    .setRequestInterceptor(requestInterceptor)
                    .setConverter(new GsonConverter(gson))
                    .build().create(SmartApi.class);
        }
        return authorizedSmartApi;
    }
}
