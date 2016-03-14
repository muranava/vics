package com.infinityworks.webapp.paf.client;

import com.infinityworks.webapp.paf.dto.*;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;
import java.util.Map;

/**
 * Client for core API (PAF) containing voter and address data
 */
public interface PafClient {

    /**
     * Gets the streets in the given ward
     *
     * @param wardCode the code of the ward to get the streets for
     * @return a collection of all streets in the given ward.
     */
    @GET("wards/{wardCode}/streets")
    Call<StreetsResponse> streetsByWardCode(@Path("wardCode") String wardCode);

    /**
     * Gets the voters grouped by the given streets
     *
     * @param wardCode the code of the electoral ward to restrict the search by
     * @param streets  the streets to get the voters from
     * @return a collection of voters grouped by street
     */
    @POST("wards/{wardCode}/streets")
    Call<PropertyResponse> votersByStreets(@Path("wardCode") String wardCode,
                                           @Body List<PafStreet> streets);

    /**
     * Searches a voter by attributes
     *
     * @param parameters voter attributes to search
     * @return the voters matching the criteria
     */
    @GET("voter")
    Call<SearchVoterResponse> voterSearch(@QueryMap Map<String, String> parameters);

    /**
     * Records that a voter has been contacted
     *
     * @param ern            the voter ID
     * @param contactRequest the information recorded about the voter
     * @return the contact data
     */
    @POST("voter/{ern}/contact")
    Call<RecordContactRequest> recordContact(@Path("ern") String ern,
                                             @Body RecordContactRequest contactRequest);

    /**
     * Records that a voter has voted
     *
     * @param ern the voter ID
     * @return ??? TODO
     */
    @POST("voter/{ern}/voted")
    Call<RecordVotedResponse> recordVote(@Path("ern") String ern);
}
