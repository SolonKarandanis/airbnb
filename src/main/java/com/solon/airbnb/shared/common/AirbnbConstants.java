package com.solon.airbnb.shared.common;

import java.util.HashSet;
import java.util.Set;

public class AirbnbConstants {
	
	private AirbnbConstants() {
		
	}
	
	public static final String UTF_8 = "UTF-8";
	public static final String HEADER_NAME_LANGUAGE_ISO = "Lang-ISO";

    public static final String APPLICATION_BUNDLE = "application_messages";
    public static final String VALIDATION_BUNDLE = "messages";

    public static final String JMX_JNDI_PREFIX = "java:/jms/queue/";

    /**
     * EMail JMX Queue name
     */
    public static final String EMAIL_QUEUE_NAME = "ccm_mailsQueue";
    /**
     * Audit JMX Queue name
     */
    public static final String AUDIT_QUEUE_NAME = "ccm_auditsQueue"; 

	public static final String ACTIVE_LOCALE = "activeLocale";


	
	/**
	 * User statuses
	 */
	public static final String USER_STATUS_ACTIVE = "account.status.active";
	public static final String USER_STATUS_DEACTIVATED = "account.status.deactivated";
	public static final String USER_STATUS_LOCKED = "account.status.locked";
	
	 /*
     * SEARCH TYPES
     */
    public static final String SEARCH_TYPE_USERS = "search.type.users";
    public static final String SEARCH_TYPE_PRODUCT_LISTS = "search.type.productists";
    public static final String SEARCH_TYPE_ATC_VERSIONS = "search.type.atcversion";

	public static final String PRODUCT_SERVICE_NAME = "productService";
	public static final String FILE_SERVICE_NAME = "fileService";

	public static final String SCHEDULER_JOB_FULL_ACTIVE_ITEM_REINDEXING = "FULL_ACTIVE_ITEM_REINDEXING";

	public static final String SCHEDULER_TOP_MODULE_NAME_ADMIN = "ADMIN";

	public static final String SCHEDULER_MODULE_PRODUCT = "PRODUCT";
	public static final String SCHEDULER_MODULE_FILESYSTEM = "FILESYSTEM";

	public static final String SCHEDULER_CRON_JOB_FULL_ACTIVE_ITEM_REINDEXING = "CRONFULLACTIVEITEMREINDEXING";
	public static final String SCHEDULER_JOB_PARTIAL_ACTIVE_ITEM_REINDEXING = "PARTIAL_ACTIVE_ITEM_REINDEXING";
	public static final String SCHEDULER_CRON_JOB_PARTIAL_ACTIVE_ITEM_REINDEXING = "CRONPARTIALACTIVEITEMREINDEXING";
	
	public static final String SCHEDULER_JOB_FILE_SYSTEM_CLEAN_UP = "FILE_SYSTEM_CLEAN_UP";
	public static final String SCHEDULER_CRON_JOB_FILESYSTEM = "CRONFILESYSTEMCLEANUP";

	public static final Integer START_FULL_REINDEX_CRON_HOUR=23;
	public static final Integer START_FULL_REINDEX_CRON_MINUTE=59;
	public static final Integer PARTIAL_REINDEX_INTERVAL_MINUTES=10;
	
	public static final Integer START_FILESYSTEM_CLEANUP_HOUR=23;
	public static final Integer START_FILESYSTEM_CLEANUP_MIN=59;


	public static final String PRODUCT_LIST_STATUS_COMPLETED = "COMPLETE";
	public static final String PRODUCT_LIST_STATUS_PENDING = "PENDING";
	public static final String PRODUCT_LIST_STATUS_ERROR = "ERROR";
	public static final String PRODUCT_LIST_STATUS_ALL = "ALL";
	private static final Set<String> permittedSearchProductListStatusValues = new HashSet();
    static {
    	permittedSearchProductListStatusValues.add(AirbnbConstants.PRODUCT_LIST_STATUS_COMPLETED);
    	permittedSearchProductListStatusValues.add(AirbnbConstants.PRODUCT_LIST_STATUS_PENDING);
    	permittedSearchProductListStatusValues.add(AirbnbConstants.PRODUCT_LIST_STATUS_ERROR);
    	permittedSearchProductListStatusValues.add(AirbnbConstants.PRODUCT_LIST_STATUS_ALL);
    }
    
    public static Set<String> getPermittedSearchProductListStatusValues() {
        return Set.copyOf(permittedSearchProductListStatusValues);
    }
	
	public static final String ATC_VER_STATUS_DRAFT = "DRAFT";
	public static final String ATC_VER_STATUS_COMPLETE = "COMPLETE";
	public static final String ATC_VER_STATUS_ALL = "ALL";
	
	private static final Set<String> permittedSearchAtcVersionStatusValues = new HashSet();
    static {
    	permittedSearchAtcVersionStatusValues.add(AirbnbConstants.ATC_VER_STATUS_DRAFT);
    	permittedSearchAtcVersionStatusValues.add(AirbnbConstants.ATC_VER_STATUS_COMPLETE);
    	permittedSearchAtcVersionStatusValues.add(AirbnbConstants.ATC_VER_STATUS_ALL);
    }
    
    public static Set<String> getPermittedSearchAtcVersionStatusValues() {
        return Set.copyOf(permittedSearchAtcVersionStatusValues);
    }

	public static final String SEARCH_TYPE_AND = "search.type.and";
	public static final String SEARCH_TYPE_OR = "search.type.or";


	public static final String EMAIL_REGEX                    = ".*@.+\\..+";
	public static final String PASSWORD_REGEX                 = ".*\\d+.*";
	public static final String TOKEN_REGEX                    = "^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$";
	public static final int    DEFAULT_PAGE_SIZE              = 25;
	public static final int    COUNTRY_PROFILE_MINIMUN_YEAR   = 2012;
	public static final String ELEMENT_STRUCTURE_DUMMY        = "N/A";
	public static final String LANG_ISO_EN = "en";
	public static final String DOCUMENT_INDEX_NAME = "gppd";

}
