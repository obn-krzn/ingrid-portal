/*
 * Copyright (c) 2006 wemove digital solutions. All rights reserved.
 */
package de.ingrid.portal.config;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Provides access to the ingrid portal preferences.
 * 
 * @author joachim@wemove.com
 */
public class PortalConfig extends PropertiesConfiguration {

    /** how old can rss news be, before the are deleted from news history in days */
    public final static String RSS_HISTORY_DAYS = "rss.history.days";

    /**
     * timout for queries in ms should be larger than query.timout.ranked and
     * query.timout.unranked because ranked and unranked query is encapsulated
     * inside a threaded query
     */
    public final static String QUERY_TIMEOUT_THREADED = "query.timeout.threaded";

    /** timout for ranked queries in ms */
    public final static String QUERY_TIMEOUT_RANKED = "query.timeout.ranked";

    /** timout for unranked queries in ms */
    public final static String QUERY_TIMEOUT_UNRANKED = "query.timeout.unranked";

    /** default timeout for sns queries in ms */
    public final static String SNS_TIMEOUT_DEFAULT = "sns.timeout.default";

    /**
     * always read values from database or only once and then from cache, true
     * or false
     */
    public final static String ALWAYS_REREAD_DB_VALUES = "db.reread";

    public final static String EMAIL_REGISTRATION_CONFIRMATION_SENDER = "email.registration.confirmation.sender";

    public static final String EMAIL_SMTP_SERVER = "email.smtp.server";

    public static final String EMAIL_WEBMASTER = "email.webmaster";

    public static final String EMAIL_CONTACT_FORM_RECEIVER = "email.contact.form.receiver";

    public static final String QUERY_HISTORY_DISPLAY_SIZE = "query.history.display.size";

    public static final String DETAILS_GENERIC_UCFIRST_STOPWORDS = "detail.generic.ucfirst.stopwords";

    public static final String SAVE_ENTRIES_MAX_NUMBER = "save.entries.max.number";

    public static final String WMS_MAPBENDER_ADMIN_URL = "wms.mapbender.admin.url";

    public static final String WMS_MAPLAB_ADMIN_URL = "wms.maplab.admin.url";

    public static final String APACHE_STATISTICS_URL = "apache.statistics.url";
    
    public static final String UDK_FIELDS_DATE = "udk.fields.date";

    public static final String UDK_FIELDS_TRANSLATE = "udk.fields.translate";
    
    public static final String PORTAL_PROFILES = "portal.profiles";

    public static final String PORTAL_PROFILE = "portal.profile";

    public static final String PORTAL_ENABLE_SEARCH_TOPICS_SEARCHTERM = "portal.enable.search.topics.searchterm";
    
    public static final String PORTAL_ENABLE_SEARCH_CATALOG = "portal.enable.search.catalog";

    public static final String PORTAL_ENABLE_SEARCH_CATALOG_THESAURUS = "portal.enable.search.catalog.thesaurus";

    public static final String PORTAL_ENABLE_DATASOURCE_RESEARCH = "portal.enable.datasource.research";

    public static final String PORTAL_ENABLE_DATASOURCE_ADDRESSES = "portal.enable.datasource.addresses";

	public static final String PORTAL_ENABLE_DATASOURCE_LAWS = "portal.enable.datasource.laws";
    
    public static final String PORTAL_SEARCH_RESTRICT_PARTNER = "portal.search.restrict.partner";

    public static final String PORTAL_SEARCH_DISPLAY_PROVIDERS = "portal.search.display.providers";
    public static final String PORTAL_SEARCH_DISPLAY_PROVIDERS_ADDRESS = "portal.search.display.providers.address";
    public static final String PORTAL_SEARCH_DISPLAY_PROVIDERS_ENVINFO = "portal.search.display.providers.default";
    public static final String PORTAL_SEARCH_DISPLAY_PROVIDERS_LAW = "portal.search.display.providers.law";
    public static final String PORTAL_SEARCH_DISPLAY_PROVIDERS_RESEARCH = "portal.search.display.providers.research";
    
    public static final String PORTAL_LOGGER_RESOURCE = "portal.logger.resource";

    public static final String PORTAL_ENABLE_MEASURE = "portal.enable.measure";

    public static final String PORTAL_ENABLE_SERVICE = "portal.enable.service";
    
    public static final String PORTAL_ENABLE_ENVIROMENT = "portal.enable.enviroment";

	public static final String PORTAL_ENABLE_CHRONICLE = "portal.enable.chronicle";

	public static final String PORTAL_ENABLE_MAPS = "portal.enable.maps";

	public static final String PORTAL_ENABLE_DEFAULT_GROUPING_DOMAIN = "portal.enable.default.grouping.domain";
	
	public static final String PORTAL_ENABLE_SEARCH_RESULTS_UNRANKED = "portal.enable.search.results.unranked";

	public static final String PORTAL_ENABLE_SEARCH_RESULTS_UNRANKED_ALLIPLUGS = "portal.enable.search.results.unranked.alliplugs";
	
	public static final String PORTAL_ENABLE_NEWSLETTER = "enable.newsletter.registration";

	public static final String PORTAL_ENABLE_SNS_LOGO = "portal.enable.sns.logo";

	public static final String COMPONENT_MONITOR_ALERT_EMAIL_SUBJECT = "component.monitor.alert.email.subject";
    
	public static final String COMPONENT_MONITOR_ALERT_EMAIL_SENDER = "component.monitor.alert.email.sender";

	public static final String COMPONENT_MONITOR_DEFAULT_EMAIL = "component.monitor.default.email";

	public static final String COMPONENT_MONITOR_SNS_LOGIN = "component.monitor.sns.login";

	public static final String COMPONENT_MONITOR_SNS_PASSWORD = "component.monitor.sns.password";
	
	public static final String COMPONENT_MONITOR_UPDATE_ALERT_EMAIL_SUBJECT = "component.monitor.update.alert.email.subject";
	
	// contains the short version of all supported languages
	public static final String LANGUAGES_SHORT = "languages.short";
	
	// the specific language is added after this variable (e.g. languages.names.de) 
	public static final String LANGUAGES_NAMES = "languages.names.";
	
	// the url to the upgrade server
    public static final String UPGRADE_SERVER_URL = "upgrade.server.url";
	
    // disable button and textfield for edit partner/provider 
    public static final String DISABLE_PARTNER_PROVIDER_EDIT = "portal.disable.partner.provider.edit";
    
    // disable piwik
    public static final String ENABLE_PIWIK = "portal.enable.piwik";
    
	// private stuff
    private static PortalConfig instance = null;

    private final static Log log = LogFactory.getLog(PortalConfig.class);

	
    public static synchronized PortalConfig getInstance() {
        if (instance == null) {
            try {
                instance = new PortalConfig();
            } catch (Exception e) {
                if (log.isFatalEnabled()) {
                    log.fatal(
                            "Error loading the portal config application config file. (ingrid-portal-apps.properties)",
                            e);
                }
            }
        }
        return instance;
    }

    private PortalConfig() throws Exception {
        super("ingrid-portal-apps.properties");
    }
}
