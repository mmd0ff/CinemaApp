package com.example.cinemaatl

import android.accounts.NetworkErrorException
import com.example.cinemaatl.model.ErrorModel
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.Response
import java.text.ParseException

suspend fun <T> apiCall(call: suspend () -> Response<T>): ApiResult<T> {
    return try {
        val result = withContext(Dispatchers.IO) {
            call.invoke()
        }
        if (result.isSuccessful) {
            ApiResult.Success(result.body())
        } else {
            val gson = Gson()
            val jsonObject = JSONObject(result.errorBody()?.charStream()?.readText().toString())
            val error = gson.fromJson(jsonObject.toString(), ErrorModel::class.java)
            ApiResult.Error(error)
        }
    } catch (e: NetworkErrorException) {
        e.printStackTrace()
        ApiResult.Error(
            ErrorModel(
                errorCode = 505,
                errorMessage = "Internet Xetasi",
                "Zehmet olmasa internet sebekesini yoxlayin"
            )
        )
    } catch (e: ParseException) {
        ApiResult.Error(
            ErrorModel(
                errorCode = 505,
                errorMessage = "Sistem Xetasi",
                "Zehmet olmasa bir qeder sonra yoxlayin"
            )
        )
    } catch (e: Exception) {
        e.printStackTrace()
        ApiResult.Error(
            ErrorModel(
                errorCode = 505,
                errorMessage = "Sistem Xetasi",
                "Zehmet olmasa bir qeder sonra yoxlayin"
            )
        )
    }
}