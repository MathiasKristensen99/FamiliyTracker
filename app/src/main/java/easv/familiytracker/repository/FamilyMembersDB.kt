package easv.familiytracker.repository

import android.util.Log
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.httpDelete
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.fuel.httpPut
import com.google.gson.Gson
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import easv.familiytracker.MainActivity
import easv.familiytracker.models.BEFMember
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class FamilyMembersDB {

    private val url = "https://familytracker.azurewebsites.net/api/FamilyMembers"

    private val httpClient : AsyncHttpClient = AsyncHttpClient()

    data class FamilyMember(var id: String, var name: String, var phone: String, var picture: String, var location: String)

    fun getAll(callback: ICallback) {
        httpClient.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                val familyMembers = getMembersFromString(String(responseBody!!))
                callback.familyMembers(familyMembers);
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                Log.d(MainActivity.TAG, "failure in getAll statusCode = $statusCode")
            }
        })
    }


    fun deleteMember(id: String, name: String, phone: String, picture: String, location: String){
        val familyMember = FamilyMember(id, name, phone, picture, location)
        val (_, _, result) = "https://familytracker.azurewebsites.net/api/FamilyMembers/$id".httpDelete().jsonBody(Gson().toJson(familyMember).toString()).responseString()
        println(result)
    }



    fun createMember(name: String, phone: String, picture: String, location: String) {
        val familyMember = FamilyMember("", name, phone, picture, location)

        val (_, _, result) = url.httpPost().jsonBody(Gson().toJson(familyMember).toString()).responseString()
        println(result)
    }



    fun updateMember(id: String, name: String, phone: String, picture: String, location: String) {
        val familyMember = FamilyMember(id, name, phone, picture, location)

        val (_, _, result) = "https://familytracker.azurewebsites.net/api/FamilyMembers/$id".httpPut().jsonBody(Gson().toJson(familyMember).toString()).responseString()
        println(result)
    }

    private fun getMembersFromString(jsonString: String?): List<BEFMember> {
        val result = ArrayList<BEFMember>()

        if (jsonString!!.startsWith("error")) {
            Log.d(MainActivity.TAG, "Error: $jsonString")
            return result
        }
        if (jsonString == null) {
            Log.d(MainActivity.TAG, "Error: NO RESULT")
            return result
        }
        var array: JSONArray?
        try {
            array = JSONObject(jsonString).getJSONArray("list")
            for (i in 0 until array.length()) {
                result.add(BEFMember(array.getJSONObject(i)))
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return result
    }
}

