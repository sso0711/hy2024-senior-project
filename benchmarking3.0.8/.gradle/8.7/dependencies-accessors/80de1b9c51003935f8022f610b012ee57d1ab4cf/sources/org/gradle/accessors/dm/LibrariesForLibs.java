package org.gradle.accessors.dm;

import org.gradle.api.NonNullApi;
import org.gradle.api.artifacts.MinimalExternalModuleDependency;
import org.gradle.plugin.use.PluginDependency;
import org.gradle.api.artifacts.ExternalModuleDependencyBundle;
import org.gradle.api.artifacts.MutableVersionConstraint;
import org.gradle.api.provider.Provider;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.ProviderFactory;
import org.gradle.api.internal.catalog.AbstractExternalDependencyFactory;
import org.gradle.api.internal.catalog.DefaultVersionCatalog;
import java.util.Map;
import org.gradle.api.internal.attributes.ImmutableAttributesFactory;
import org.gradle.api.internal.artifacts.dsl.CapabilityNotationParser;
import javax.inject.Inject;

/**
 * A catalog of dependencies accessible via the {@code libs} extension.
 */
@NonNullApi
public class LibrariesForLibs extends AbstractExternalDependencyFactory {

    private final AbstractExternalDependencyFactory owner = this;
    private final ComLibraryAccessors laccForComLibraryAccessors = new ComLibraryAccessors(owner);
    private final DeLibraryAccessors laccForDeLibraryAccessors = new DeLibraryAccessors(owner);
    private final IoLibraryAccessors laccForIoLibraryAccessors = new IoLibraryAccessors(owner);
    private final JavaxLibraryAccessors laccForJavaxLibraryAccessors = new JavaxLibraryAccessors(owner);
    private final JunitLibraryAccessors laccForJunitLibraryAccessors = new JunitLibraryAccessors(owner);
    private final OrgLibraryAccessors laccForOrgLibraryAccessors = new OrgLibraryAccessors(owner);
    private final VersionAccessors vaccForVersionAccessors = new VersionAccessors(providers, config);
    private final BundleAccessors baccForBundleAccessors = new BundleAccessors(objects, providers, config, attributesFactory, capabilityNotationParser);
    private final PluginAccessors paccForPluginAccessors = new PluginAccessors(providers, config);

    @Inject
    public LibrariesForLibs(DefaultVersionCatalog config, ProviderFactory providers, ObjectFactory objects, ImmutableAttributesFactory attributesFactory, CapabilityNotationParser capabilityNotationParser) {
        super(config, providers, objects, attributesFactory, capabilityNotationParser);
    }

    /**
     * Group of libraries at <b>com</b>
     */
    public ComLibraryAccessors getCom() {
        return laccForComLibraryAccessors;
    }

    /**
     * Group of libraries at <b>de</b>
     */
    public DeLibraryAccessors getDe() {
        return laccForDeLibraryAccessors;
    }

    /**
     * Group of libraries at <b>io</b>
     */
    public IoLibraryAccessors getIo() {
        return laccForIoLibraryAccessors;
    }

    /**
     * Group of libraries at <b>javax</b>
     */
    public JavaxLibraryAccessors getJavax() {
        return laccForJavaxLibraryAccessors;
    }

    /**
     * Group of libraries at <b>junit</b>
     */
    public JunitLibraryAccessors getJunit() {
        return laccForJunitLibraryAccessors;
    }

    /**
     * Group of libraries at <b>org</b>
     */
    public OrgLibraryAccessors getOrg() {
        return laccForOrgLibraryAccessors;
    }

    /**
     * Group of versions at <b>versions</b>
     */
    public VersionAccessors getVersions() {
        return vaccForVersionAccessors;
    }

    /**
     * Group of bundles at <b>bundles</b>
     */
    public BundleAccessors getBundles() {
        return baccForBundleAccessors;
    }

    /**
     * Group of plugins at <b>plugins</b>
     */
    public PluginAccessors getPlugins() {
        return paccForPluginAccessors;
    }

    public static class ComLibraryAccessors extends SubDependencyFactory {
        private final ComJgoodiesLibraryAccessors laccForComJgoodiesLibraryAccessors = new ComJgoodiesLibraryAccessors(owner);
        private final ComSunLibraryAccessors laccForComSunLibraryAccessors = new ComSunLibraryAccessors(owner);

        public ComLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>com.jgoodies</b>
         */
        public ComJgoodiesLibraryAccessors getJgoodies() {
            return laccForComJgoodiesLibraryAccessors;
        }

        /**
         * Group of libraries at <b>com.sun</b>
         */
        public ComSunLibraryAccessors getSun() {
            return laccForComSunLibraryAccessors;
        }

    }

    public static class ComJgoodiesLibraryAccessors extends SubDependencyFactory {
        private final ComJgoodiesJgoodiesLibraryAccessors laccForComJgoodiesJgoodiesLibraryAccessors = new ComJgoodiesJgoodiesLibraryAccessors(owner);

        public ComJgoodiesLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>com.jgoodies.jgoodies</b>
         */
        public ComJgoodiesJgoodiesLibraryAccessors getJgoodies() {
            return laccForComJgoodiesJgoodiesLibraryAccessors;
        }

    }

    public static class ComJgoodiesJgoodiesLibraryAccessors extends SubDependencyFactory {

        public ComJgoodiesJgoodiesLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>forms</b> with <b>com.jgoodies:jgoodies-forms</b> coordinates and
         * with version reference <b>com.jgoodies.jgoodies.forms</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getForms() {
            return create("com.jgoodies.jgoodies.forms");
        }

    }

    public static class ComSunLibraryAccessors extends SubDependencyFactory {
        private final ComSunXmlLibraryAccessors laccForComSunXmlLibraryAccessors = new ComSunXmlLibraryAccessors(owner);

        public ComSunLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>com.sun.xml</b>
         */
        public ComSunXmlLibraryAccessors getXml() {
            return laccForComSunXmlLibraryAccessors;
        }

    }

    public static class ComSunXmlLibraryAccessors extends SubDependencyFactory {
        private final ComSunXmlBindLibraryAccessors laccForComSunXmlBindLibraryAccessors = new ComSunXmlBindLibraryAccessors(owner);

        public ComSunXmlLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>com.sun.xml.bind</b>
         */
        public ComSunXmlBindLibraryAccessors getBind() {
            return laccForComSunXmlBindLibraryAccessors;
        }

    }

    public static class ComSunXmlBindLibraryAccessors extends SubDependencyFactory {
        private final ComSunXmlBindJaxbLibraryAccessors laccForComSunXmlBindJaxbLibraryAccessors = new ComSunXmlBindJaxbLibraryAccessors(owner);

        public ComSunXmlBindLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>com.sun.xml.bind.jaxb</b>
         */
        public ComSunXmlBindJaxbLibraryAccessors getJaxb() {
            return laccForComSunXmlBindJaxbLibraryAccessors;
        }

    }

    public static class ComSunXmlBindJaxbLibraryAccessors extends SubDependencyFactory {

        public ComSunXmlBindJaxbLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>core</b> with <b>com.sun.xml.bind:jaxb-core</b> coordinates and
         * with version reference <b>com.sun.xml.bind.jaxb.core</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getCore() {
            return create("com.sun.xml.bind.jaxb.core");
        }

        /**
         * Dependency provider for <b>impl</b> with <b>com.sun.xml.bind:jaxb-impl</b> coordinates and
         * with version reference <b>com.sun.xml.bind.jaxb.impl</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getImpl() {
            return create("com.sun.xml.bind.jaxb.impl");
        }

    }

    public static class DeLibraryAccessors extends SubDependencyFactory {
        private final DeScissLibraryAccessors laccForDeScissLibraryAccessors = new DeScissLibraryAccessors(owner);

        public DeLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>de.sciss</b>
         */
        public DeScissLibraryAccessors getSciss() {
            return laccForDeScissLibraryAccessors;
        }

    }

    public static class DeScissLibraryAccessors extends SubDependencyFactory {

        public DeScissLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>weblaf</b> with <b>de.sciss:weblaf</b> coordinates and
         * with version reference <b>de.sciss.weblaf</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getWeblaf() {
            return create("de.sciss.weblaf");
        }

    }

    public static class IoLibraryAccessors extends SubDependencyFactory {
        private final IoNettyLibraryAccessors laccForIoNettyLibraryAccessors = new IoNettyLibraryAccessors(owner);

        public IoLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>io.netty</b>
         */
        public IoNettyLibraryAccessors getNetty() {
            return laccForIoNettyLibraryAccessors;
        }

    }

    public static class IoNettyLibraryAccessors extends SubDependencyFactory {
        private final IoNettyNettyLibraryAccessors laccForIoNettyNettyLibraryAccessors = new IoNettyNettyLibraryAccessors(owner);

        public IoNettyLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>io.netty.netty</b>
         */
        public IoNettyNettyLibraryAccessors getNetty() {
            return laccForIoNettyNettyLibraryAccessors;
        }

    }

    public static class IoNettyNettyLibraryAccessors extends SubDependencyFactory {

        public IoNettyNettyLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>all</b> with <b>io.netty:netty-all</b> coordinates and
         * with version reference <b>io.netty.netty.all</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getAll() {
            return create("io.netty.netty.all");
        }

    }

    public static class JavaxLibraryAccessors extends SubDependencyFactory {
        private final JavaxXmlLibraryAccessors laccForJavaxXmlLibraryAccessors = new JavaxXmlLibraryAccessors(owner);

        public JavaxLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>javax.xml</b>
         */
        public JavaxXmlLibraryAccessors getXml() {
            return laccForJavaxXmlLibraryAccessors;
        }

    }

    public static class JavaxXmlLibraryAccessors extends SubDependencyFactory {
        private final JavaxXmlBindLibraryAccessors laccForJavaxXmlBindLibraryAccessors = new JavaxXmlBindLibraryAccessors(owner);

        public JavaxXmlLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>javax.xml.bind</b>
         */
        public JavaxXmlBindLibraryAccessors getBind() {
            return laccForJavaxXmlBindLibraryAccessors;
        }

    }

    public static class JavaxXmlBindLibraryAccessors extends SubDependencyFactory {
        private final JavaxXmlBindJaxbLibraryAccessors laccForJavaxXmlBindJaxbLibraryAccessors = new JavaxXmlBindJaxbLibraryAccessors(owner);

        public JavaxXmlBindLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>javax.xml.bind.jaxb</b>
         */
        public JavaxXmlBindJaxbLibraryAccessors getJaxb() {
            return laccForJavaxXmlBindJaxbLibraryAccessors;
        }

    }

    public static class JavaxXmlBindJaxbLibraryAccessors extends SubDependencyFactory {

        public JavaxXmlBindJaxbLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>api</b> with <b>javax.xml.bind:jaxb-api</b> coordinates and
         * with version reference <b>javax.xml.bind.jaxb.api</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getApi() {
            return create("javax.xml.bind.jaxb.api");
        }

    }

    public static class JunitLibraryAccessors extends SubDependencyFactory {

        public JunitLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>junit</b> with <b>junit:junit</b> coordinates and
         * with version reference <b>junit.junit</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getJunit() {
            return create("junit.junit");
        }

    }

    public static class OrgLibraryAccessors extends SubDependencyFactory {
        private final OrgApacheLibraryAccessors laccForOrgApacheLibraryAccessors = new OrgApacheLibraryAccessors(owner);
        private final OrgAsynchttpclientLibraryAccessors laccForOrgAsynchttpclientLibraryAccessors = new OrgAsynchttpclientLibraryAccessors(owner);
        private final OrgJsonLibraryAccessors laccForOrgJsonLibraryAccessors = new OrgJsonLibraryAccessors(owner);
        private final OrgOnosprojectLibraryAccessors laccForOrgOnosprojectLibraryAccessors = new OrgOnosprojectLibraryAccessors(owner);
        private final OrgSlf4jLibraryAccessors laccForOrgSlf4jLibraryAccessors = new OrgSlf4jLibraryAccessors(owner);

        public OrgLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>org.apache</b>
         */
        public OrgApacheLibraryAccessors getApache() {
            return laccForOrgApacheLibraryAccessors;
        }

        /**
         * Group of libraries at <b>org.asynchttpclient</b>
         */
        public OrgAsynchttpclientLibraryAccessors getAsynchttpclient() {
            return laccForOrgAsynchttpclientLibraryAccessors;
        }

        /**
         * Group of libraries at <b>org.json</b>
         */
        public OrgJsonLibraryAccessors getJson() {
            return laccForOrgJsonLibraryAccessors;
        }

        /**
         * Group of libraries at <b>org.onosproject</b>
         */
        public OrgOnosprojectLibraryAccessors getOnosproject() {
            return laccForOrgOnosprojectLibraryAccessors;
        }

        /**
         * Group of libraries at <b>org.slf4j</b>
         */
        public OrgSlf4jLibraryAccessors getSlf4j() {
            return laccForOrgSlf4jLibraryAccessors;
        }

    }

    public static class OrgApacheLibraryAccessors extends SubDependencyFactory {
        private final OrgApacheHttpcomponentsLibraryAccessors laccForOrgApacheHttpcomponentsLibraryAccessors = new OrgApacheHttpcomponentsLibraryAccessors(owner);
        private final OrgApacheLoggingLibraryAccessors laccForOrgApacheLoggingLibraryAccessors = new OrgApacheLoggingLibraryAccessors(owner);

        public OrgApacheLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>org.apache.httpcomponents</b>
         */
        public OrgApacheHttpcomponentsLibraryAccessors getHttpcomponents() {
            return laccForOrgApacheHttpcomponentsLibraryAccessors;
        }

        /**
         * Group of libraries at <b>org.apache.logging</b>
         */
        public OrgApacheLoggingLibraryAccessors getLogging() {
            return laccForOrgApacheLoggingLibraryAccessors;
        }

    }

    public static class OrgApacheHttpcomponentsLibraryAccessors extends SubDependencyFactory {

        public OrgApacheHttpcomponentsLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>httpasyncclient</b> with <b>org.apache.httpcomponents:httpasyncclient</b> coordinates and
         * with version reference <b>org.apache.httpcomponents.httpasyncclient</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getHttpasyncclient() {
            return create("org.apache.httpcomponents.httpasyncclient");
        }

        /**
         * Dependency provider for <b>httpclient</b> with <b>org.apache.httpcomponents:httpclient</b> coordinates and
         * with version reference <b>org.apache.httpcomponents.httpclient</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getHttpclient() {
            return create("org.apache.httpcomponents.httpclient");
        }

    }

    public static class OrgApacheLoggingLibraryAccessors extends SubDependencyFactory {
        private final OrgApacheLoggingLog4jLibraryAccessors laccForOrgApacheLoggingLog4jLibraryAccessors = new OrgApacheLoggingLog4jLibraryAccessors(owner);

        public OrgApacheLoggingLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>org.apache.logging.log4j</b>
         */
        public OrgApacheLoggingLog4jLibraryAccessors getLog4j() {
            return laccForOrgApacheLoggingLog4jLibraryAccessors;
        }

    }

    public static class OrgApacheLoggingLog4jLibraryAccessors extends SubDependencyFactory {
        private final OrgApacheLoggingLog4jLog4jLibraryAccessors laccForOrgApacheLoggingLog4jLog4jLibraryAccessors = new OrgApacheLoggingLog4jLog4jLibraryAccessors(owner);

        public OrgApacheLoggingLog4jLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>org.apache.logging.log4j.log4j</b>
         */
        public OrgApacheLoggingLog4jLog4jLibraryAccessors getLog4j() {
            return laccForOrgApacheLoggingLog4jLog4jLibraryAccessors;
        }

    }

    public static class OrgApacheLoggingLog4jLog4jLibraryAccessors extends SubDependencyFactory {

        public OrgApacheLoggingLog4jLog4jLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>core</b> with <b>org.apache.logging.log4j:log4j-core</b> coordinates and
         * with version reference <b>org.apache.logging.log4j.log4j.core</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getCore() {
            return create("org.apache.logging.log4j.log4j.core");
        }

    }

    public static class OrgAsynchttpclientLibraryAccessors extends SubDependencyFactory {
        private final OrgAsynchttpclientAsyncLibraryAccessors laccForOrgAsynchttpclientAsyncLibraryAccessors = new OrgAsynchttpclientAsyncLibraryAccessors(owner);

        public OrgAsynchttpclientLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>org.asynchttpclient.async</b>
         */
        public OrgAsynchttpclientAsyncLibraryAccessors getAsync() {
            return laccForOrgAsynchttpclientAsyncLibraryAccessors;
        }

    }

    public static class OrgAsynchttpclientAsyncLibraryAccessors extends SubDependencyFactory {
        private final OrgAsynchttpclientAsyncHttpLibraryAccessors laccForOrgAsynchttpclientAsyncHttpLibraryAccessors = new OrgAsynchttpclientAsyncHttpLibraryAccessors(owner);

        public OrgAsynchttpclientAsyncLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>org.asynchttpclient.async.http</b>
         */
        public OrgAsynchttpclientAsyncHttpLibraryAccessors getHttp() {
            return laccForOrgAsynchttpclientAsyncHttpLibraryAccessors;
        }

    }

    public static class OrgAsynchttpclientAsyncHttpLibraryAccessors extends SubDependencyFactory {

        public OrgAsynchttpclientAsyncHttpLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>client</b> with <b>org.asynchttpclient:async-http-client</b> coordinates and
         * with version reference <b>org.asynchttpclient.async.http.client</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getClient() {
            return create("org.asynchttpclient.async.http.client");
        }

    }

    public static class OrgJsonLibraryAccessors extends SubDependencyFactory {

        public OrgJsonLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>json</b> with <b>org.json:json</b> coordinates and
         * with version reference <b>org.json.json</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getJson() {
            return create("org.json.json");
        }

    }

    public static class OrgOnosprojectLibraryAccessors extends SubDependencyFactory {

        public OrgOnosprojectLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>openflowj</b> with <b>org.onosproject:openflowj</b> coordinates and
         * with version reference <b>org.onosproject.openflowj</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getOpenflowj() {
            return create("org.onosproject.openflowj");
        }

    }

    public static class OrgSlf4jLibraryAccessors extends SubDependencyFactory {
        private final OrgSlf4jSlf4jLibraryAccessors laccForOrgSlf4jSlf4jLibraryAccessors = new OrgSlf4jSlf4jLibraryAccessors(owner);

        public OrgSlf4jLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>org.slf4j.slf4j</b>
         */
        public OrgSlf4jSlf4jLibraryAccessors getSlf4j() {
            return laccForOrgSlf4jSlf4jLibraryAccessors;
        }

    }

    public static class OrgSlf4jSlf4jLibraryAccessors extends SubDependencyFactory {

        public OrgSlf4jSlf4jLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>api</b> with <b>org.slf4j:slf4j-api</b> coordinates and
         * with version reference <b>org.slf4j.slf4j.api</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getApi() {
            return create("org.slf4j.slf4j.api");
        }

    }

    public static class VersionAccessors extends VersionFactory  {

        private final ComVersionAccessors vaccForComVersionAccessors = new ComVersionAccessors(providers, config);
        private final DeVersionAccessors vaccForDeVersionAccessors = new DeVersionAccessors(providers, config);
        private final IoVersionAccessors vaccForIoVersionAccessors = new IoVersionAccessors(providers, config);
        private final JavaxVersionAccessors vaccForJavaxVersionAccessors = new JavaxVersionAccessors(providers, config);
        private final JunitVersionAccessors vaccForJunitVersionAccessors = new JunitVersionAccessors(providers, config);
        private final OrgVersionAccessors vaccForOrgVersionAccessors = new OrgVersionAccessors(providers, config);
        public VersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.com</b>
         */
        public ComVersionAccessors getCom() {
            return vaccForComVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.de</b>
         */
        public DeVersionAccessors getDe() {
            return vaccForDeVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.io</b>
         */
        public IoVersionAccessors getIo() {
            return vaccForIoVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.javax</b>
         */
        public JavaxVersionAccessors getJavax() {
            return vaccForJavaxVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.junit</b>
         */
        public JunitVersionAccessors getJunit() {
            return vaccForJunitVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.org</b>
         */
        public OrgVersionAccessors getOrg() {
            return vaccForOrgVersionAccessors;
        }

    }

    public static class ComVersionAccessors extends VersionFactory  {

        private final ComJgoodiesVersionAccessors vaccForComJgoodiesVersionAccessors = new ComJgoodiesVersionAccessors(providers, config);
        private final ComSunVersionAccessors vaccForComSunVersionAccessors = new ComSunVersionAccessors(providers, config);
        public ComVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.com.jgoodies</b>
         */
        public ComJgoodiesVersionAccessors getJgoodies() {
            return vaccForComJgoodiesVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.com.sun</b>
         */
        public ComSunVersionAccessors getSun() {
            return vaccForComSunVersionAccessors;
        }

    }

    public static class ComJgoodiesVersionAccessors extends VersionFactory  {

        private final ComJgoodiesJgoodiesVersionAccessors vaccForComJgoodiesJgoodiesVersionAccessors = new ComJgoodiesJgoodiesVersionAccessors(providers, config);
        public ComJgoodiesVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.com.jgoodies.jgoodies</b>
         */
        public ComJgoodiesJgoodiesVersionAccessors getJgoodies() {
            return vaccForComJgoodiesJgoodiesVersionAccessors;
        }

    }

    public static class ComJgoodiesJgoodiesVersionAccessors extends VersionFactory  {

        public ComJgoodiesJgoodiesVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>com.jgoodies.jgoodies.forms</b> with value <b>1.9.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getForms() { return getVersion("com.jgoodies.jgoodies.forms"); }

    }

    public static class ComSunVersionAccessors extends VersionFactory  {

        private final ComSunXmlVersionAccessors vaccForComSunXmlVersionAccessors = new ComSunXmlVersionAccessors(providers, config);
        public ComSunVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.com.sun.xml</b>
         */
        public ComSunXmlVersionAccessors getXml() {
            return vaccForComSunXmlVersionAccessors;
        }

    }

    public static class ComSunXmlVersionAccessors extends VersionFactory  {

        private final ComSunXmlBindVersionAccessors vaccForComSunXmlBindVersionAccessors = new ComSunXmlBindVersionAccessors(providers, config);
        public ComSunXmlVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.com.sun.xml.bind</b>
         */
        public ComSunXmlBindVersionAccessors getBind() {
            return vaccForComSunXmlBindVersionAccessors;
        }

    }

    public static class ComSunXmlBindVersionAccessors extends VersionFactory  {

        private final ComSunXmlBindJaxbVersionAccessors vaccForComSunXmlBindJaxbVersionAccessors = new ComSunXmlBindJaxbVersionAccessors(providers, config);
        public ComSunXmlBindVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.com.sun.xml.bind.jaxb</b>
         */
        public ComSunXmlBindJaxbVersionAccessors getJaxb() {
            return vaccForComSunXmlBindJaxbVersionAccessors;
        }

    }

    public static class ComSunXmlBindJaxbVersionAccessors extends VersionFactory  {

        public ComSunXmlBindJaxbVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>com.sun.xml.bind.jaxb.core</b> with value <b>2.3.0.1</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getCore() { return getVersion("com.sun.xml.bind.jaxb.core"); }

        /**
         * Version alias <b>com.sun.xml.bind.jaxb.impl</b> with value <b>2.3.1</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getImpl() { return getVersion("com.sun.xml.bind.jaxb.impl"); }

    }

    public static class DeVersionAccessors extends VersionFactory  {

        private final DeScissVersionAccessors vaccForDeScissVersionAccessors = new DeScissVersionAccessors(providers, config);
        public DeVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.de.sciss</b>
         */
        public DeScissVersionAccessors getSciss() {
            return vaccForDeScissVersionAccessors;
        }

    }

    public static class DeScissVersionAccessors extends VersionFactory  {

        public DeScissVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>de.sciss.weblaf</b> with value <b>2.2.1</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getWeblaf() { return getVersion("de.sciss.weblaf"); }

    }

    public static class IoVersionAccessors extends VersionFactory  {

        private final IoNettyVersionAccessors vaccForIoNettyVersionAccessors = new IoNettyVersionAccessors(providers, config);
        public IoVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.io.netty</b>
         */
        public IoNettyVersionAccessors getNetty() {
            return vaccForIoNettyVersionAccessors;
        }

    }

    public static class IoNettyVersionAccessors extends VersionFactory  {

        private final IoNettyNettyVersionAccessors vaccForIoNettyNettyVersionAccessors = new IoNettyNettyVersionAccessors(providers, config);
        public IoNettyVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.io.netty.netty</b>
         */
        public IoNettyNettyVersionAccessors getNetty() {
            return vaccForIoNettyNettyVersionAccessors;
        }

    }

    public static class IoNettyNettyVersionAccessors extends VersionFactory  {

        public IoNettyNettyVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>io.netty.netty.all</b> with value <b>4.1.109.Final</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getAll() { return getVersion("io.netty.netty.all"); }

    }

    public static class JavaxVersionAccessors extends VersionFactory  {

        private final JavaxXmlVersionAccessors vaccForJavaxXmlVersionAccessors = new JavaxXmlVersionAccessors(providers, config);
        public JavaxVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.javax.xml</b>
         */
        public JavaxXmlVersionAccessors getXml() {
            return vaccForJavaxXmlVersionAccessors;
        }

    }

    public static class JavaxXmlVersionAccessors extends VersionFactory  {

        private final JavaxXmlBindVersionAccessors vaccForJavaxXmlBindVersionAccessors = new JavaxXmlBindVersionAccessors(providers, config);
        public JavaxXmlVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.javax.xml.bind</b>
         */
        public JavaxXmlBindVersionAccessors getBind() {
            return vaccForJavaxXmlBindVersionAccessors;
        }

    }

    public static class JavaxXmlBindVersionAccessors extends VersionFactory  {

        private final JavaxXmlBindJaxbVersionAccessors vaccForJavaxXmlBindJaxbVersionAccessors = new JavaxXmlBindJaxbVersionAccessors(providers, config);
        public JavaxXmlBindVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.javax.xml.bind.jaxb</b>
         */
        public JavaxXmlBindJaxbVersionAccessors getJaxb() {
            return vaccForJavaxXmlBindJaxbVersionAccessors;
        }

    }

    public static class JavaxXmlBindJaxbVersionAccessors extends VersionFactory  {

        public JavaxXmlBindJaxbVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>javax.xml.bind.jaxb.api</b> with value <b>2.3.1</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getApi() { return getVersion("javax.xml.bind.jaxb.api"); }

    }

    public static class JunitVersionAccessors extends VersionFactory  {

        public JunitVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>junit.junit</b> with value <b>4.13.2</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getJunit() { return getVersion("junit.junit"); }

    }

    public static class OrgVersionAccessors extends VersionFactory  {

        private final OrgApacheVersionAccessors vaccForOrgApacheVersionAccessors = new OrgApacheVersionAccessors(providers, config);
        private final OrgAsynchttpclientVersionAccessors vaccForOrgAsynchttpclientVersionAccessors = new OrgAsynchttpclientVersionAccessors(providers, config);
        private final OrgJsonVersionAccessors vaccForOrgJsonVersionAccessors = new OrgJsonVersionAccessors(providers, config);
        private final OrgOnosprojectVersionAccessors vaccForOrgOnosprojectVersionAccessors = new OrgOnosprojectVersionAccessors(providers, config);
        private final OrgSlf4jVersionAccessors vaccForOrgSlf4jVersionAccessors = new OrgSlf4jVersionAccessors(providers, config);
        public OrgVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.org.apache</b>
         */
        public OrgApacheVersionAccessors getApache() {
            return vaccForOrgApacheVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.org.asynchttpclient</b>
         */
        public OrgAsynchttpclientVersionAccessors getAsynchttpclient() {
            return vaccForOrgAsynchttpclientVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.org.json</b>
         */
        public OrgJsonVersionAccessors getJson() {
            return vaccForOrgJsonVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.org.onosproject</b>
         */
        public OrgOnosprojectVersionAccessors getOnosproject() {
            return vaccForOrgOnosprojectVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.org.slf4j</b>
         */
        public OrgSlf4jVersionAccessors getSlf4j() {
            return vaccForOrgSlf4jVersionAccessors;
        }

    }

    public static class OrgApacheVersionAccessors extends VersionFactory  {

        private final OrgApacheHttpcomponentsVersionAccessors vaccForOrgApacheHttpcomponentsVersionAccessors = new OrgApacheHttpcomponentsVersionAccessors(providers, config);
        private final OrgApacheLoggingVersionAccessors vaccForOrgApacheLoggingVersionAccessors = new OrgApacheLoggingVersionAccessors(providers, config);
        public OrgApacheVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.org.apache.httpcomponents</b>
         */
        public OrgApacheHttpcomponentsVersionAccessors getHttpcomponents() {
            return vaccForOrgApacheHttpcomponentsVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.org.apache.logging</b>
         */
        public OrgApacheLoggingVersionAccessors getLogging() {
            return vaccForOrgApacheLoggingVersionAccessors;
        }

    }

    public static class OrgApacheHttpcomponentsVersionAccessors extends VersionFactory  {

        public OrgApacheHttpcomponentsVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>org.apache.httpcomponents.httpasyncclient</b> with value <b>4.1.4</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getHttpasyncclient() { return getVersion("org.apache.httpcomponents.httpasyncclient"); }

        /**
         * Version alias <b>org.apache.httpcomponents.httpclient</b> with value <b>4.5.2</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getHttpclient() { return getVersion("org.apache.httpcomponents.httpclient"); }

    }

    public static class OrgApacheLoggingVersionAccessors extends VersionFactory  {

        private final OrgApacheLoggingLog4jVersionAccessors vaccForOrgApacheLoggingLog4jVersionAccessors = new OrgApacheLoggingLog4jVersionAccessors(providers, config);
        public OrgApacheLoggingVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.org.apache.logging.log4j</b>
         */
        public OrgApacheLoggingLog4jVersionAccessors getLog4j() {
            return vaccForOrgApacheLoggingLog4jVersionAccessors;
        }

    }

    public static class OrgApacheLoggingLog4jVersionAccessors extends VersionFactory  {

        private final OrgApacheLoggingLog4jLog4jVersionAccessors vaccForOrgApacheLoggingLog4jLog4jVersionAccessors = new OrgApacheLoggingLog4jLog4jVersionAccessors(providers, config);
        public OrgApacheLoggingLog4jVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.org.apache.logging.log4j.log4j</b>
         */
        public OrgApacheLoggingLog4jLog4jVersionAccessors getLog4j() {
            return vaccForOrgApacheLoggingLog4jLog4jVersionAccessors;
        }

    }

    public static class OrgApacheLoggingLog4jLog4jVersionAccessors extends VersionFactory  {

        public OrgApacheLoggingLog4jLog4jVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>org.apache.logging.log4j.log4j.core</b> with value <b>2.20.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getCore() { return getVersion("org.apache.logging.log4j.log4j.core"); }

    }

    public static class OrgAsynchttpclientVersionAccessors extends VersionFactory  {

        private final OrgAsynchttpclientAsyncVersionAccessors vaccForOrgAsynchttpclientAsyncVersionAccessors = new OrgAsynchttpclientAsyncVersionAccessors(providers, config);
        public OrgAsynchttpclientVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.org.asynchttpclient.async</b>
         */
        public OrgAsynchttpclientAsyncVersionAccessors getAsync() {
            return vaccForOrgAsynchttpclientAsyncVersionAccessors;
        }

    }

    public static class OrgAsynchttpclientAsyncVersionAccessors extends VersionFactory  {

        private final OrgAsynchttpclientAsyncHttpVersionAccessors vaccForOrgAsynchttpclientAsyncHttpVersionAccessors = new OrgAsynchttpclientAsyncHttpVersionAccessors(providers, config);
        public OrgAsynchttpclientAsyncVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.org.asynchttpclient.async.http</b>
         */
        public OrgAsynchttpclientAsyncHttpVersionAccessors getHttp() {
            return vaccForOrgAsynchttpclientAsyncHttpVersionAccessors;
        }

    }

    public static class OrgAsynchttpclientAsyncHttpVersionAccessors extends VersionFactory  {

        public OrgAsynchttpclientAsyncHttpVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>org.asynchttpclient.async.http.client</b> with value <b>2.12.3</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getClient() { return getVersion("org.asynchttpclient.async.http.client"); }

    }

    public static class OrgJsonVersionAccessors extends VersionFactory  {

        public OrgJsonVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>org.json.json</b> with value <b>20231013</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getJson() { return getVersion("org.json.json"); }

    }

    public static class OrgOnosprojectVersionAccessors extends VersionFactory  {

        public OrgOnosprojectVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>org.onosproject.openflowj</b> with value <b>0.9.7.onos</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getOpenflowj() { return getVersion("org.onosproject.openflowj"); }

    }

    public static class OrgSlf4jVersionAccessors extends VersionFactory  {

        private final OrgSlf4jSlf4jVersionAccessors vaccForOrgSlf4jSlf4jVersionAccessors = new OrgSlf4jSlf4jVersionAccessors(providers, config);
        public OrgSlf4jVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.org.slf4j.slf4j</b>
         */
        public OrgSlf4jSlf4jVersionAccessors getSlf4j() {
            return vaccForOrgSlf4jSlf4jVersionAccessors;
        }

    }

    public static class OrgSlf4jSlf4jVersionAccessors extends VersionFactory  {

        public OrgSlf4jSlf4jVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>org.slf4j.slf4j.api</b> with value <b>2.0.9</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getApi() { return getVersion("org.slf4j.slf4j.api"); }

    }

    public static class BundleAccessors extends BundleFactory {

        public BundleAccessors(ObjectFactory objects, ProviderFactory providers, DefaultVersionCatalog config, ImmutableAttributesFactory attributesFactory, CapabilityNotationParser capabilityNotationParser) { super(objects, providers, config, attributesFactory, capabilityNotationParser); }

    }

    public static class PluginAccessors extends PluginFactory {

        public PluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

    }

}
