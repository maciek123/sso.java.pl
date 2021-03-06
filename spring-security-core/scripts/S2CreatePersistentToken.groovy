/* Copyright 2006-2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import groovy.text.SimpleTemplateEngine

includeTargets << new File("$springSecurityCorePluginDir/scripts/_S2Common.groovy")

fullClassName = null

USAGE = """
	Usage: grails s2-create-persistent-token <domain-class-name>

	Creates a persistent token domain class

	Example: grails s2-create-persistent-token com.yourapp.PersistentLogin
"""

target(s2CreatePersistentToken: 'Creates the persistent token domain class for the Spring Security Core plugin') {
	depends(checkVersion, configureProxy, packageApp, classpath)

	configure()
	createDomainClass()
	updateConfig()
}

private void configure() {

	fullClassName = parseArgs()
	String packageName
	String className
	(packageName, className) = splitClassName(fullClassName)
	
	String packageDeclaration = ''
	if (packageName) {
		packageDeclaration = "package $packageName"
	}

	templateAttributes = [packageName: packageName,
	                      packageDeclaration: packageDeclaration,
	                      className: className]
}

private void createDomainClass() {
	String dir = packageToDir(templateAttributes.packageName)
	generateFile "$templateDir/PersistentLogin.groovy.template",
		"$appDir/domain/${dir}${templateAttributes.className}.groovy"
}

private void updateConfig() {
	def configFile = new File(appDir, 'conf/Config.groovy')
	if (configFile.exists()) {
		configFile.withWriterAppend {
			it.writeLine "grails.plugins.springsecurity.rememberMe.persistent = true"
			it.writeLine "grails.plugins.springsecurity.rememberMe.persistentToken.domainClassName = '$fullClassName'"
		}
	}
}

private parseArgs() {
	args = args ? args.split('\n') : []
	switch (args.size()) {
		case 1:
			ant.echo message: "Creating persistent token class ${args[0]}"
			return args[0]
		default:
			ant.echo message: USAGE
			System.exit 1
			break
	}
}

setDefaultTarget 's2CreatePersistentToken'
