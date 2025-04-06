package model

import com.google.gson.annotations.SerializedName

data class GitHubUser(
    @SerializedName("login")
    val username: String? = null,

    @SerializedName("followers")
    val followers: Int = 0,

    @SerializedName("following")
    val following: Int = 0,

    @SerializedName("created_at")
    val createdAt: String? = null,

    var repos: List<String> = listOf()
)

data class Repo(val name: String)

