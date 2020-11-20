package com.gzeinnumer.retrofit2andrxjavanewkt.network

import com.gzeinnumer.retrofit2andrxjavanewkt.model.ResponseNews
import io.reactivex.Flowable
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    //rx-java-type-1
    //?country=us&apiKey=e5430ac2a413408aaafdf60bfa27a874
    @GET("top-headlines")
    fun getBeritaObservable(
            @Query("country") country: String,
            @Query("apiKey") apiKey: String
    ): Observable<Response<ResponseNews>>

    //rx-java-type-2
    //?country=us&apiKey=e5430ac2a413408aaafdf60bfa27a874
    @GET("top-headlines")
    fun getBeritaFlowable(
            @Query("country") country: String,
            @Query("apiKey") apiKey: String
    ): Flowable<Response<ResponseNews>>
}