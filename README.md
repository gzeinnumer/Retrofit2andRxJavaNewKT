# Retrofit2andRxJavaNew

- Implementation
```gradle
implementation 'com.squareup.retrofit2:adapter-rxjava2:2.5.0'
implementation 'com.squareup.retrofit2:converter-gson:2.5.0'
implementation 'io.reactivex.rxjava2:rxandroid:2.1.1'
implementation 'io.reactivex.rxjava2:rxjava:2.2.9'
implementation 'com.squareup.okhttp3:logging-interceptor:3.10.0'
```

- RetroServer
```kotlin
object RetroServer {
    private const val base_url = "https://newsapi.org/v2/"
    private fun setInit(): Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val httpClient = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor { chain ->
                    val request = chain.request().newBuilder()
                            .addHeader("Accept", "application/json")
                            .addHeader("Authorization", "Bearer Token")
                            .build()
                    chain.proceed(request)
                }
                .readTimeout(90, TimeUnit.SECONDS)
                .writeTimeout(90, TimeUnit.SECONDS)
                .connectTimeout(90, TimeUnit.SECONDS)
                .build()
        return Retrofit.Builder()
                .baseUrl(base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClient)
                .build()
    }

    val instance: ApiService
        get() = setInit().create(ApiService::class.java)
}
```

- Apiservice
```kotlin
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
```

- Rxjava Flowable
```kotlin
RetroServer.instance
    .getBeritaFlowable("us", "e5430ac2a413408aaafdf60bfa27a874")
    .subscribeOn(Schedulers.io())
    .observeOn(AndroidSchedulers.mainThread())
    .onErrorReturn(Function<Throwable, Response<ResponseNews>> { throwable ->
        Log.d(TAG, "apply: $throwable")
        null
    })
    .subscribe(Consumer<Response<ResponseNews>> { listResponse ->
        val data: ResponseNews? = listResponse.body() //json body
        val code = listResponse.code() //200
        val msg = listResponse.message() //SUCCESS
        Log.d(TAG, "accept: $code")
        sentDataToAdapter(data)
    })
```

- Rxjava Observable
```kotlin
RetroServer.instance
    .getBeritaObservable("us", "e5430ac2a413408aaafdf60bfa27a874")
    .subscribeOn(Schedulers.io())
    .observeOn(AndroidSchedulers.mainThread())
    .subscribe(object : Observer<Response<ResponseNews>> {
        override fun onComplete() {
            Log.d(TAG, "onComplete: Loading Dismis")
        }

        override fun onSubscribe(d: Disposable) {
            Log.d(TAG, "onSubscribe: Loading Show")
        }

        override fun onNext(listResponse: Response<ResponseNews>) {
            val data: ResponseNews? = listResponse.body() //json body
            val code = listResponse.code() //200
            val msg = listResponse.message() //SUCCESS
            Log.d(TAG, "onNext: $code")
            sentDataToAdapter(data)
        }

        override fun onError(e: Throwable) {
            Log.d(TAG, "onError: Error"+e.message)
        }
    })
```

---

**FullCode [AndroidManifest](https://github.com/gzeinnumer/Retrofit2andRxJavaNewKT/blob/master/app/src/main/AndroidManifest.xml) & [MainActivity](https://github.com/gzeinnumer/Retrofit2andRxJavaNewKT/blob/master/app/src/main/java/com/gzeinnumer/retrofit2andrxjavanewkt/MainActivity.java) & [RetroServer](https://github.com/gzeinnumer/Retrofit2andRxJavaNewKT/blob/master/app/src/main/java/com/gzeinnumer/retrofit2andrxjavanewkt/network/RetroServer.java) & [ApiService](https://github.com/gzeinnumer/Retrofit2andRxJavaNewKT/blob/master/app/src/main/java/com/gzeinnumer/retrofit2andrxjavanewkt/network/ApiService.java) & [AdapterRX](https://github.com/gzeinnumer/Retrofit2andRxJavaNewKT/blob/master/app/src/main/java/com/gzeinnumer/retrofit2andrxjavanewkt/adapter/AdapterRX.java) & [ResponseNews](https://github.com/gzeinnumer/Retrofit2andRxJavaNewKT/blob/master/app/src/main/java/com/gzeinnumer/retrofit2andrxjavanewkt/model/ResponseNews.java) & [ArticlesItem](https://github.com/gzeinnumer/Retrofit2andRxJavaNewKT/blob/master/app/src/main/java/com/gzeinnumer/retrofit2andrxjavanewkt/model/ArticlesItem.java) & [Source](https://github.com/gzeinnumer/Retrofit2andRxJavaNewKT/blob/master/app/src/main/java/com/gzeinnumer/retrofit2andrxjavanewkt/model/Source.java)**

---

```
Copyright 2020 M. Fadli Zein
```