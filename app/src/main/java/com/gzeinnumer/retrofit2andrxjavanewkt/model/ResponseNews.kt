package com.gzeinnumer.retrofit2andrxjavanewkt.model

import com.google.gson.annotations.SerializedName

class ResponseNews {
    @SerializedName("totalResults")
    var totalResults = 0

    @SerializedName("articles")
    var articles: List<ArticlesItem>? = null

    @SerializedName("status")
    var status: String? = null

}