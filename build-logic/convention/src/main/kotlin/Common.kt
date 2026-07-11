import org.gradle.api.Project

object Common {
    private const val RELEASE_VERSION_CODE = 10001
    private const val RELEASE_VERSION_NAME = "1.0.1"

    fun getBuildVersionCode(project: Project): Int {
        val override = (project.findProperty("buildVersionCode") as String?)?.trim()
        return override?.toIntOrNull() ?: RELEASE_VERSION_CODE
    }

    fun getBuildVersionName(project: Project): String {
        val override = (project.findProperty("buildVersionName") as String?)?.trim()
        return if (override.isNullOrEmpty()) RELEASE_VERSION_NAME else override
    }
}
