package org.grails.plugins.appsetup

import grails.core.GrailsApplication
import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import spock.lang.Shared
import spock.lang.Specification

import java.nio.file.Files

@Integration
@Rollback
class AppSetupServiceSpec extends Specification {

    GrailsApplication grailsApplication
    @Shared String setupLocationStr = "target/virgin-2/setup"
    ConfigObject config

    void setup() {
        if(!config) {
            config = grailsApplication.getSetupConfig(true)
        }
    }

    def setupSpec() {
        File setupLocation = new File(setupLocationStr)
        if(!setupLocation.exists()) {
            setupLocation.mkdirs()
        }

        File source = new File("src/integration-test/resources/TestAppSetupConfig.groovy")
        File dest = new File(setupLocation, "TestAppSetupConfig.groovy")
        Files.copy(source.toPath(), dest.toPath())
        assert dest.exists()
    }

    def cleanupSpec() {
        File setupLocation = new File(setupLocationStr)
        File dest = new File(setupLocation, "TestAppSetupConfig.groovy")
        if(dest.exists()) {
            dest.delete()
        }
    }

    def "test default config is loaded"() {
        expect:
        config != null
        config.menus != null
    }

    def "test screen specific menu config is merged to default config"() {
        when:
        ConfigObject arTranConfig = config.screens.arTran.menus

        then:
        arTranConfig != null
        arTranConfig.size() == 9 //verfiy that default is merged to screen level
        !arTranConfig.open.row.enabled
        arTranConfig.podRequest.list.enabled
        arTranConfig.podRequest.row.enabled
        arTranConfig.podRequest.show.enabled
    }

    void "test block level options override default options"() {
        when:
        ConfigObject arTranConfig = config.screens.arTran.menus

        then:
        arTranConfig != null
        arTranConfig.open.size() == 1 //just row block
        arTranConfig.delete.size() == 3

        arTranConfig.delete.list.enabled == false
        arTranConfig.delete.list.label == "Delete"
        arTranConfig.delete.show.enabled == true
        arTranConfig.delete.show.label == "Delete"
        arTranConfig.delete.row.enabled == true
        arTranConfig.delete.row.label == "Delete"

        arTranConfig.update.list.ngClick == "massUpdate()"
        //arTranConfig.update.row.ngClick == "updateRow()"

    }

}
