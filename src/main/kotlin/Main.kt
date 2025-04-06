import service.GitHubService
import api.GitHubApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Scanner

fun main() {
    val baseUrl = "https://api.github.com/"
    val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api = retrofit.create(GitHubApi::class.java)
    val service = GitHubService(api)

    val scanner = Scanner(System.`in`)

    while (true) {
        println("1. دریافت اطلاعات کاربر")
        println("2. نمایش لیست کاربران")
        println("3. جستجو بر اساس نام کاربری")
        println("4. جستجو بر اساس نام ریپوزیتوری")
        println("5. خروج")
        print("گزینه را وارد کنید: ")

        val input = scanner.nextLine()
        when (input.toIntOrNull()) {
            1 -> {
                print("لطفاً نام کاربری گیت‌هاب را وارد کنید: ")
                val username = scanner.nextLine()
                val user = service.fetchUserData(username)
                if (user != null) {
                    println("نام کاربری: ${user.username}")
                    println("تعداد فالوورها: ${user.followers}")
                    println("تعداد دنبال‌شوندگان: ${user.following}")
                    println("تاریخ ایجاد حساب: ${user.createdAt}")
                    println("ریپوزیتوری‌ها:${user.repos}")
                    user.repos.forEach { println("- $it") }
                } else {
                    println("کاربری با این نام یافت نشد.")
                }
            }
            2 -> service.displayUsers()
            3 -> {
                print("نام کاربری مورد نظر را وارد کنید: ")
                val username = scanner.nextLine()
                service.searchUserByUsername(username)
            }
            4 -> {
                print("نام ریپوزیتوری مورد نظر را وارد کنید: ")
                val repoName = scanner.nextLine()
                service.searchRepoByName(repoName)
            }
            5 -> {
                println("خروج از برنامه.")
                break
            }
            else -> println("گزینه نامعتبر است.")
        }
        println()
    }
}
