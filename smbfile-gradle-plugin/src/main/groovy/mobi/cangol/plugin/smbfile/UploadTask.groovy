package mobi.cangol.plugin.smbfile

import com.android.build.gradle.api.ApkVariantOutput
import com.android.build.gradle.api.ApplicationVariant
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class UploadTask extends DefaultTask {
    UploadPluginExtension extension
    UploadClient client
    ApplicationVariant variant

    @TaskAction
    upload() {
        def log = project.logger
        if (client == null) {
            client = UploadClient.init(extension)
        }

        def apkOutput = variant.outputs.find { variantOutput -> variantOutput instanceof ApkVariantOutput }

        String apkPath = apkOutput.outputFile.getAbsolutePath()
        log.error("apkPath ===> " + apkPath)

        def destDirPath = extension.getProperty(variant.buildType.name + "Dir");
        if (destDirPath == null) {
            destDirPath = ""
        }

        def flag=""
        if (!extension.flag.equalsIgnoreCase("")) {
            flag="_"+extension.flag
        }
        log.error("flag ===> " + flag)

        def fileDir = "V" + variant.versionName+ "." + variant.versionCode+ "_" + new Date().format("yyyy-MM-dd_HH-mm-ss", TimeZone.getTimeZone("GMT+8"))+flag
        def destDir = destDirPath + "/" + fileDir
        log.error("destDir ===> " + destDir)

        def path = client.upload(destDir, apkOutput.outputFile.name, apkPath)
        log.info("upload ===> " + path)

    }

}
