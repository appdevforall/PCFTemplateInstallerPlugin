package com.example.pcfinstaller 

import com.itsaky.androidide.plugins.IPlugin
import com.itsaky.androidide.plugins.PluginContext
import com.itsaky.androidide.plugins.services.ArchiveFormat
import com.itsaky.androidide.plugins.services.ExtractResult
import com.itsaky.androidide.plugins.services.IdeArchiveService
import com.itsaky.androidide.plugins.services.IdeEnvironmentService
import com.itsaky.androidide.plugins.services.IdeFileService
import com.itsaky.androidide.plugins.services.IdeTemplateService

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import android.util.Log
import java.io.File
import java.io.FileNotFoundException

class PCFInstaller : IPlugin {

    private lateinit var context: PluginContext
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var templateService: IdeTemplateService? = null
    private lateinit var PCFBuilderDir: File
    private lateinit var PCFExampleDir: File
    private lateinit var environmentService: IdeEnvironmentService
    private lateinit var fileService: IdeFileService
     
    override fun initialize(context: PluginContext): Boolean {
        return try {
            this.context = context
            templateService = context.services.get(IdeTemplateService::class.java)
            context.logger.info("PCFInstaller initialized successfully")
            environmentService = context.services.get(IdeEnvironmentService::class.java) ?: return false
            fileService = context.services.get(IdeFileService::class.java) ?: return false 
            PCFBuilderDir = File(environmentService.getAndroidHomeDirectory(), PCFBUILDER_SUBDIR)
            PCFExampleDir = File(environmentService.getAndroidHomeDirectory(), PCFEXAMPLE_SUBDIR)

            true
        } catch (e: Exception) {
            context.logger.error("PCFInstaller initialization failed", e)
            false
        }
    }

    override fun activate(): Boolean {
        registerPCFBuilderTemplate()
        registerPCFExampleTemplate()        
        context.logger.info("PCFInstaller: Activating plugin")
        return true
    }

    override fun deactivate(): Boolean {
        removePCFInstaller()
        context.logger.info("PCFInstaller: Deactivating plugin")
        return true
    }

    override fun dispose() {
        scope.cancel()
        templateService = null
        context.logger.info("PCFInstaller: Disposing plugin")
    }


    private fun removePCFInstaller() {
        if (PCFBuilderDir.exists()) {
            val deleted = fileService.delete(PCFBuilderDir)
            context.logger.info("PCFBuilder removal from ${PCFBuilderDir.absolutePath}: $deleted")
        }
        if (PCFExampleDir.exists()) {
            val deleted = fileService.delete(PCFExampleDir)
            context.logger.info("PCFExample removal from ${PCFExampleDir.absolutePath}: $deleted")
        }
    }

    private fun registerPCFBuilderTemplate() {

        val ASSETS_PCFBUILDER = "templates/PCFBuilder"
        val rootDirectory: File = File("/sdcard/CodeOnTheGoProjects/PCFInstaller/src/main/assets/templates/PCFBuilder")

        runCatching {
            var cgtBuilder = templateService!!.createTemplateBuilder("PCFBuilder")
                .description("Build extensions.jar to support Pebble Custom Functions")
                .showPackageNameOption()
                .showLanguageOption()
                .showMinSdkOption()
                .thumbnailFromAssets("$ASSETS_PCFBUILDER/template/thumb.png", context)
                .addTextParameter("Author", "PLUGIN_AUTHOR", default = "Author")
                .addTextParameter("Description", "PLUGIN_DESC", default = "Plugin description")
                .addCheckboxParameter("Include Sample Functions", "EXAMPLE_CODE", default = true)
    
            Log.i(TAG, "***rootDirectory = $rootDirectory")
                rootDirectory.walk()
                .filter { it.isFile } 
                .forEach { file ->
                    try {
                        val assetFile = file.toString().removePrefix("$rootDirectory/")
                        if(!assetFile.startsWith("template/")) {                    
                            Log.i(TAG, "***Adding ${assetFile}, $ASSETS_PCFBUILDER/${assetFile}")
                            cgtBuilder = cgtBuilder.addStaticFromAssets("${assetFile}", "$ASSETS_PCFBUILDER/${assetFile}", context)
                        } else {
                            Log.i(TAG, "***Ignoring $assetFile")
                        }
                    } catch (e: FileNotFoundException) {
                        return@forEach
                    }
                }

            val cgtBuilderFile = cgtBuilder.build(context.resources.getPluginDirectory())

            templateService!!.registerTemplate(cgtBuilderFile)
            Log.i(TAG, "PCFBuilder template registered")
        }.onFailure {
            Log.e(TAG, "***Failed to register PCFBuilder template", it)
        }
    }


    private fun registerPCFExampleTemplate() {

        val ASSETS_PCFEXAMPLE = "templates/PCFExample"
        val rootDirectory: File = File("/sdcard/CodeOnTheGoProjects/PCFInstaller/src/main/assets/templates/PCFExample")

        runCatching {
            var cgtExample = templateService!!.createTemplateBuilder("PCFExample")
                .description("Demonstrate Pebble Custom Functions")
                .showPackageNameOption()
                .showLanguageOption()
                .showMinSdkOption()
                .thumbnailFromAssets("$ASSETS_PCFEXAMPLE/template/thumb.png", context)
                .addTextParameter("Author", "PLUGIN_AUTHOR", default = "Author")
                .addTextParameter("Description", "PLUGIN_DESC", default = "Plugin description")
                .addCheckboxParameter("Include Sample Functions", "EXAMPLE_CODE", default = true)
    
            Log.i(TAG, "***rootDirectory = $rootDirectory")
                rootDirectory.walk()
                .filter { it.isFile } 
                .forEach { file ->
                    try {
                        val assetFile = file.toString().removePrefix("$rootDirectory/")
                        if(!assetFile.startsWith("template/")) {                    
                            Log.i(TAG, "***Adding ${assetFile}, $ASSETS_PCFEXAMPLE/${assetFile}")
                            cgtExample = cgtExample.addStaticFromAssets("${assetFile}", 
                                "$ASSETS_PCFEXAMPLE/${assetFile}", context)
                        } else {
                            Log.i(TAG, "***Ignoring $assetFile")
                        }
                    } catch (e: FileNotFoundException) {
                        return@forEach
                    }
                }

            val cgtExampleFile = cgtExample.build(context.resources.getPluginDirectory())

            templateService!!.registerTemplate(cgtExampleFile)
            Log.i(TAG, "PCFExample template registered")

        }.onFailure {
            Log.e(TAG, "Failed to register PCFExample template", it)
        }
    }

    private fun fail(message: String) {
        context.logger.error("PCFBuilder installer: $message")
    }

    private companion object {
        const val PCFBUILDER_SUBDIR = "PCFBuilder"
        const val PCFEXAMPLE_SUBDIR = "PCFExample"
        const val TAG = "PCFInstallerPlugin"
    }

}
