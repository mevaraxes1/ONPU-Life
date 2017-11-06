package com.visorien.coursach.data.retrofit;

import com.visorien.coursach.data.model.auth.AuthModel;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Visorien on 28.05.2017.
 */

public interface AuthApi {
    @GET("auth/role/{role}/login/{login}/password/{password}")
    Observable<AuthModel> getUser(@Path("role") String role, @Path("login") String login, @Path("password") String password);
}
