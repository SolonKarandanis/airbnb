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
    public static final String BOOKING_GEN = "bookingSequenceGenerator";
    public static final String EMAIL_GEN = "EMAILS_ID_GENERATOR";
    public static final String EMAIL_ATTACHMENT_GEN = "EMAIL_ATTACHMENTS_ID_GENERATOR";
    public static final String FILE_INFO_GEN = "FILIE_INFO_ID_GENERATOR";
    /*
     * DATABASE SEQUENCES
     */
    public static final String USER_SQ = "user_generator";
    public static final String TOKEN_SQ = "token_generator";
    public static final String LISTING_SQ = "listing_generator";
    public static final String LISTING_PICTURE_SQ = "listing_picture_generator";
    public static final String BOOKING_SQ = "booking_generator";
    public static final String EMAIL_SQ = "email_generator";
    public static final String EMAIL_ATTACHMENT_SQ = "email_attachments_generator";
    public static final String FILE_INFO_SQ = "file_info_generator";
}
