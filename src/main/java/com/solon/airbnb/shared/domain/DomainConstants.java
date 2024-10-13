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
    public static final String LISTING_GEN = "listingSequenceGenerator";
    public static final String LISTING_PICTURE_GEN = "listingPictureSequenceGenerator";

    /*
     * DATABASE SEQUENCES
     */
    public static final String USER_SQ = "user_generator";
    public static final String TOKEN_SQ = "token_generator";
    public static final String LISTING_SQ = "listing_generator";
    public static final String LISTING_PICTURE_SQ = "listing_picture_generator";
}
