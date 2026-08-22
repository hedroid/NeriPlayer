package moe.ouom.neriplayer.core.update

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AppUpdateCheckerTest {
    @Test
    fun `matching NeriPlayer tag is up to date`() {
        val result = parseLatestRelease(
            currentVersion = "abc12345.08221234",
            responseBody = releaseJson("NeriPlayer-abc12345.08221234")
        )

        assertEquals(
            AppUpdateCheckResult.UpToDate("NeriPlayer-abc12345.08221234"),
            result
        )
    }

    @Test
    fun `different release tag reports update from fork`() {
        val result = parseLatestRelease(
            currentVersion = "abc12345.08221234",
            responseBody = releaseJson("NeriPlayer-def67890.08231234")
        )

        assertEquals(
            AppUpdateCheckResult.UpdateAvailable(
                tagName = "NeriPlayer-def67890.08231234",
                releaseUrl = "https://github.com/hedroid/NeriPlayer/releases/tag/NeriPlayer-def67890.08231234"
            ),
            result
        )
    }

    @Test
    fun `untrusted release URL falls back to fork releases page`() {
        val result = parseLatestRelease(
            currentVersion = "1.0.0",
            responseBody = """{"tag_name":"v1.1.0","html_url":"https://example.com/release"}"""
        )

        assertTrue(result is AppUpdateCheckResult.UpdateAvailable)
        assertEquals(
            NERIPLAYER_RELEASES_URL,
            (result as AppUpdateCheckResult.UpdateAvailable).releaseUrl
        )
    }

    private fun releaseJson(tagName: String): String {
        return """{"tag_name":"$tagName","html_url":"https://github.com/hedroid/NeriPlayer/releases/tag/$tagName"}"""
    }
}
