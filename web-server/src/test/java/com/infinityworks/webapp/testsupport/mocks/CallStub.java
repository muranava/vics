package com.infinityworks.webapp.testsupport.mocks;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;

/**
 * Stubs the retrofit {@link Call} - to be used in unit tests
 */
public class CallStub<T> implements Call<T> {

    private final Request request;
    private final Response<T> response;

    private CallStub(Request request, Response<T> response) {
        this.request = request;
        this.response = response;
    }

    public static <T> CallStub<T> success(T data) {
        return new CallStub<>(new Request.Builder().url("http://localhost:9002").build(), Response.success(data));
    }

    public static <T> CallStub<T> serverError(Request.Builder reqBuilder, T data) {
        return new CallStub<>(reqBuilder.build(), Response.success(data));
    }

    @Override
    public Response<T> execute() throws IOException {
        return response;
    }

    @Override
    public void enqueue(Callback<T> callback) {

    }

    @Override
    public boolean isExecuted() {
        return false;
    }

    @Override
    public void cancel() {

    }

    @Override
    public boolean isCanceled() {
        return false;
    }

    @Override
    public Call<T> clone() {
        return null;
    }

    @Override
    public Request request() {
        return request;
    }
}
