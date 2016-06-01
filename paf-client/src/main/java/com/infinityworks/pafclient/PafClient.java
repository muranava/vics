package com.infinityworks.pafclient;

import com.infinityworks.pafclient.dto.*;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

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
                                           @Body List<PafStreetRequest> streets);

    /**
     * Gets the voters grouped by the given streets
     *
     * @param wardCode the code of the electoral ward to restrict the search by
     * @param streets  the streets to get the voters from
     * @return a collection of voters grouped by street
     */
    @POST("wards/{wardCode}/streets")
    Call<PropertyResponse> filteredVotersByStreets(@Path("wardCode") String wardCode,
                                                   @Body GotvVoterRequest streets);

    /**
     * Searches a voter by attributes
     *
     * @param parameters voter attributes to search
     * @return the voters matching the criteria
     */
    @GET("voters/search")
    Call<List<SearchVoterResponse>> voterSearch(@QueryMap Map<String, String> parameters);

    /**
     * Records that a voter has been contacted
     *
     * @param ern            the voter ID
     * @param contactRequest the information recorded about the voter
     * @return the contact data
     */
    @POST("voters/{ern}/contact")
    Call<RecordContactResponse> recordContact(@Path("ern") String ern,
                                              @Body RecordContactRequest contactRequest);

    /**
     * Deletes an existing contact record
     *
     * @param ern       the voter ID
     * @param contactId the contact record to delete
     * @return no content
     */
    @DELETE("voters/{ern}/contact/{contactId}")
    Call<DeleteContactResponse> deleteContact(@Path("ern") String ern,
                                              @Path("contactId") UUID contactId);

    @POST("voters/{ern}/voted")
    Call<RecordVotedResponse> recordVote(@Path("ern") String ern);

    @DELETE("voters/{ern}/voted")
    Call<RecordVotedResponse> undoVote(@Path("ern") String ern);

    @GET("wards/{wardCode}")
    Call<WardStats> wardStats(@Path("wardCode") String wardCode);

    @GET("constituencies/{constituencyCode}")
    Call<ConstituencyStats> constituencyStats(@Path("constituencyCode") String constituencyCode);
}
