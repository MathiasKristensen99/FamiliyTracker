package easv.familiytracker.models

import java.io.Serializable
import org.json.JSONObject

data class BEFMember(val id: String, val name: String, val phone: String, val picture: String, val location: String): Serializable {
    constructor(jsonObject: JSONObject) :
            this(jsonObject["id"] as String, jsonObject["name"] as String, jsonObject["phone"] as String, jsonObject["picture"] as String, jsonObject["location"] as String)
}