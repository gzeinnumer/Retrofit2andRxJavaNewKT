package com.gzeinnumer.retrofit2andrxjavanewkt

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gzeinnumer.retrofit2andrxjavanewkt.adapter.AdapterRX
import com.gzeinnumer.retrofit2andrxjavanewkt.model.ResponseNews
import com.gzeinnumer.retrofit2andrxjavanewkt.network.RetroServer
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    var recyclerView: RecyclerView? = null
    var adapterRX: AdapterRX? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    @SuppressLint("CheckResult")
    private fun typeFlowable() {
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
    }

    @SuppressLint("CheckResult")
    private fun typeObservable() {
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
    }

    private fun sentDataToAdapter(responseNews: ResponseNews?) {
        adapterRX = AdapterRX(applicationContext, responseNews?.articles)
        recyclerView!!.adapter = adapterRX
        recyclerView!!.layoutManager = LinearLayoutManager(this)
        recyclerView!!.setHasFixedSize(true)
    }

    override fun onStop() {
        super.onStop()
    }
}