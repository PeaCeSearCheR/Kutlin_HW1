package service

import api.GitHubApi
import model.GitHubUser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class GitHubService(private val api: GitHubApi) {
    private val usersCache = mutableMapOf<String, GitHubUser>()

    fun fetchUserData(username: String): GitHubUser? {
        if (usersCache.containsKey(username)) {
            println("داده‌های کاربر از حافظه کش خوانده شد.")
            return usersCache[username]
        }

        try {
            val userResponse = api.getUserData(username).execute()
            if (userResponse.isSuccessful) {
                val user = userResponse.body()
                if (user != null) {
                    val reposResponse = api.getUserRepos(username).execute()
                    val repos = if (reposResponse.isSuccessful) {
                        reposResponse.body()?.map { it.name } ?: emptyList()
                    } else {
                        emptyList()
                    }

                    val completeUser = user.copy(repos = repos)
                    usersCache[username] = completeUser
                    println("داده‌های کاربر و ریپوزیتوری‌ها ذخیره شد.")
                    return completeUser
                }
            }
        } catch (e: Exception) {
            println("خطا در دریافت اطلاعات کاربر: ${e.message}")
        }

        return null

}

    fun displayUsers() {
        if (usersCache.isEmpty()) {
            println("هیچ کاربری در حافظه کش ذخیره نشده است.")
        } else {
            println("لیست کاربران ذخیره شده:")
            usersCache.forEach { (username, user) ->
                println("نام کاربری: $username")
            }
        }
    }

    fun searchUserByUsername(username: String) {
        val user = usersCache[username]
        if (user != null) {
            println("یافت شد: ${user.username}")
        } else {
            println("کاربر با نام کاربری $username در حافظه کش یافت نشد.")
        }
    }

    fun searchRepoByName(repoName: String) {
        val foundRepos = usersCache.values.flatMap { it.repos }.filter { it.contains(repoName, true) }
        if (foundRepos.isNotEmpty()) {
            println("ریپوزیتوری‌های یافت شده:")
            foundRepos.forEach { println(it) }
        } else {
            println("ریپوزیتوری با نام $repoName یافت نشد.")
        }
    }
}
