package com.infinityworks.webapp.clients.gmaps;

import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.common.LambdaLogger;
import com.infinityworks.webapp.error.MapsApiFailure;
import com.infinityworks.webapp.error.MapsApiNotFoundFailure;
import retrofit2.Call;
import retrofit2.Response;

public class MapsRequestExecutor {
    private static final LambdaLogger log = LambdaLogger.getLogger(MapsRequestExecutor.class);
    private static final String FORMAT = "Error calling Maps API %s. Response contained status code=%d, status text=%s";
    private static final String FORMAT_EXC = "IO Exception calling Maps API %s";

    public <T> Try<T> execute(Call<T> call) {
        try {
            Response<T> response = call.execute();
            if (response.isSuccessful()) {
                T body = response.body();
                return Try.success(body);
            } else {
                return handleUnsuccessful(call, response);
            }
        } catch (Exception e) {
            String msg = String.format(FORMAT_EXC, call.request().toString());
            return Try.failure(new MapsApiFailure(msg, e));
        }
    }

    private <T> Try<T> handleUnsuccessful(Call<T> call, Response<T> response) {
        String msg = String.format(FORMAT, call.request().toString(), response.code(), response.message());
        if (response.code() == 404) {
            return Try.failure(new MapsApiNotFoundFailure(msg));
        } else {
            MapsApiFailure failure = new MapsApiFailure(msg);
            return Try.failure(failure);
        }
    }
}
