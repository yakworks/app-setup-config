package org.grails.plugins.appsetupconfig

import grails.plugins.*
import org.grails.plugins.appsetupconfig.AppSetupService

class AppSetupConfigGrailsPlugin extends Plugin {

    def grailsVersion = "3.3.9 > *"
    def pluginExcludes = [
        "grails-app/views/error.gsp"
    ]

    // TODO Fill in these fields
    def title = "App Setup Config" // Headline display name of the plugin
    def author = "Sudhir Nimavat"
    def authorEmail = ""
    def description = "Brief summary/description of the plugin"
    def profiles = ['web']

    def documentation = "http://grails.org/yakworks/app-setup-config"


    void doWithApplicationContext() {
        AppSetupService appSetupService = (AppSetupService) applicationContext.getBean("appSetupService")
        appSetupService.enhanceGrailsApplication()

    }

    void onConfigChange(Map<String, Object> event) {
        //this will reload appsetup config
        grailsApplication.getSetupConfig(true)

    }
}
