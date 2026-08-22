package moe.ouom.neriplayer.core.update

import com.google.gson.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import moe.ouom.neriplayer.core.di.AppContainer
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

internal const val NERIPLAYER_RELEASES_URL = "https://github.com/hedroid/NeriPlayer/releases"
internal const val NERIPLAYER_LATEST_RELEASE_API =
    "https://api.github.com/repos/hedroid/NeriPlayer/releases/latest"

internal sealed interface AppUpdateCheckResult {
    data class UpdateAvailable(
        val tagName: String,
        val releaseUrl: String
    ) : AppUpdateCheckResult

    data class UpToDate(val tagName: String) : AppUpdateCheckResult

    data object NoRelease : AppUpdateCheckResult

    data object Failed : AppUpdateCheckResult
}

internal object AppUpdateChecker {
    suspend fun check(
        currentVersion: String,
        client: OkHttpClient = AppContainer.sharedOkHttpClient
    ): AppUpdateCheckResult = withContext(Dispatchers.IO) {
        runCatching {
            val request = Request.Builder()
                .url(NERIPLAYER_LATEST_RELEASE_API)
                .header("Accept", "application/vnd.github+json")
                .header("X-GitHub-Api-Version", "2022-11-28")
                .header("User-Agent", "NeriPlayer-Android")
                .build()

            client.newCall(request).execute().use { response ->
                if (response.code == 404) {
                    return@withContext AppUpdateCheckResult.NoRelease
                }
                if (!response.isSuccessful) {
                    throw IOException("GitHub release request failed with HTTP ${response.code}")
                }
                parseLatestRelease(
                    currentVersion = currentVersion,
                    responseBody = response.body.string()
                )
            }
        }.getOrElse { AppUpdateCheckResult.Failed }
    }
}

internal fun parseLatestRelease(
    currentVersion: String,
    responseBody: String
): AppUpdateCheckResult {
    val release = JsonParser.parseString(responseBody).asJsonObject
    val tagName = release.get("tag_name")?.asString?.trim().orEmpty()
    if (tagName.isEmpty()) {
        return AppUpdateCheckResult.Failed
    }
    val releaseUrl = release.get("html_url")?.asString?.trim()
        ?.takeIf { it.startsWith("https://github.com/hedroid/NeriPlayer/releases/") }
        ?: NERIPLAYER_RELEASES_URL

    return if (normalizeReleaseVersion(tagName) == normalizeReleaseVersion(currentVersion)) {
        AppUpdateCheckResult.UpToDate(tagName)
    } else {
        AppUpdateCheckResult.UpdateAvailable(tagName, releaseUrl)
    }
}

internal fun normalizeReleaseVersion(value: String): String {
    return value.trim()
        .removePrefix("refs/tags/")
        .removePrefix("NeriPlayer-")
        .removePrefix("v")
        .trim()
        .lowercase()
}
