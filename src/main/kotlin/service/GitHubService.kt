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

        val call = api.getUserData(username)
        return try {
            val response = call.execute()
            if (response.isSuccessful) {
                val user = response.body()
                user?.let {
                    usersCache[username] = it
                    println("داده‌های کاربر ذخیره شد.")
                }
                user
            } else {
                println("پاسخ ناموفق از سرور: ${response.code()}")
                null
            }
        } catch (e: Exception) {
            println("خطا در ارتباط با سرور: ${e.message}")
            null
        }
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
