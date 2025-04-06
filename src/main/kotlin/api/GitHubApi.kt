package api

import model.GitHubUser
import model.Repo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface GitHubApi {
    @GET("users/{username}")
    fun getUserData(@Path("username") username: String): Call<GitHubUser>

    @GET("users/{username}/repos")
    fun getUserRepos(@Path("username") username: String): Call<List<Repo>>
}