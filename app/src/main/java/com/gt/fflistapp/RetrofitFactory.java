package com.gt.fflistapp;

import java.io.IOException;
import java.net.ProtocolException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.BufferedSource;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by hitesh on 02/12/17.
 */
public class RetrofitFactory {

    private static Retrofit m_retrofit = null;
    private static OkHttpClient client = null;

    public static Retrofit getInstnace() {
        if (m_retrofit == null) {

            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            client = new OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .addNetworkInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Response response;
                            try {
                                response = chain.proceed(chain.request());
                            } catch (ProtocolException e) {
                                response = new Response.Builder()
                                        .request(chain.request())
                                        .code(204)
                                        .protocol(Protocol.HTTP_1_1)
                                        .body(new ResponseBody() {
                                            @Override
                                            public MediaType contentType() {
                                                return MediaType.parse("application/json");
                                            }

                                            @Override
                                            public long contentLength() {
                                                return 12;
                                            }

                                            @Override
                                            public BufferedSource source() {
                                                return null;
                                            }
                                        })
                                        .build();
                            }
                            return response;
                        }
                    }).build();


            m_retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.instagram.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();


        }
        return m_retrofit;
    }


    public static <T> T getServiceInstance(final Class<T> service) {
        return getInstnace().create(service);
    }

}