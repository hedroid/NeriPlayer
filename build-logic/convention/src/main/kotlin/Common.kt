import org.gradle.api.Project

object Common {
    private const val RELEASE_VERSION_CODE = 10001
    private const val RELEASE_VERSION_NAME = "1.0.1"

    fun getBuildVersionCode(): Int {
        return RELEASE_VERSION_CODE
    }

    fun getBuildVersionName(project: Project): String {
        return RELEASE_VERSION_NAME
    }
}
