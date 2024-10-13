package com.solon.airbnb.shared.domain;

public class DomainConstants {

    private DomainConstants() {
    }

    /*
     * HIBERNATE CONSTANTS
     */
    public static final String HIBERNATE_GEN_STRATEGY = "org.hibernate.id.enhanced.SequenceStyleGenerator";

    /*
     * SEQUENCE GENERATOR NAMES
     */
    public static final String USER_GEN = "userSequenceGenerator";
    public static final String TOKEN_GEN = "tokenSequenceGenerator";
    public static final String ATCDDD_ACTIVES_GEN = "atcdddActivesGen";
    public static final String ATCDDD_UD_GEN = "atcdddUdGen";

    /*
     * DATABASE SEQUENCES
     */
    public static final String USER_SQ = "user_generator";
    public static final String TOKEN_SQ = "token_generator";
    public static final String ATCDDD_ACTIVES_SQ = "atcddd_actives_id";
    public static final String ATCDDD_UD_SQ = "atcddd_ud_id";
}
