package com.solon.airbnb.listing.domain;

import java.util.Arrays;
import java.util.Objects;

import com.solon.airbnb.shared.domain.AbstractAuditingEntity;

import com.solon.airbnb.shared.domain.DomainConstants;
import jakarta.persistence.*;

@NamedQueries({
        @NamedQuery(name = ListingPicture.DELETE_BY_LISTING_PUBLIC_ID_AND_LANDLORD_PUBLIC_ID,
                query = "DELETE FROM ListingPicture lp "
                        + "WHERE lp.id = (SELECT l.id FROM Listing l WHERE l.publicId = :publicId AND l.landlordPublicId = :landlordPublicId) "),
})
@Entity
@Table(name = "listing_picture")
public class ListingPicture extends AbstractAuditingEntity<Long> {

    public static final String DELETE_BY_LISTING_PUBLIC_ID_AND_LANDLORD_PUBLIC_ID= "ListingPicture.deleteByListingPublicIdAndLandlordPublicId";

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = DomainConstants.LISTING_PICTURE_GEN
    )
    @SequenceGenerator(
            name = DomainConstants.LISTING_PICTURE_GEN,
            sequenceName = DomainConstants.LISTING_PICTURE_SQ,
            allocationSize = 1
    )
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "listing_fk", referencedColumnName = "id")
    private Listing listing;

    @Lob
    @Column(name = "file", nullable = false)
    @Basic(fetch = FetchType.EAGER)
    private byte[] file;

    @Column(name = "file_content_type")
    private String fileContentType;

    @Column(name = "is_cover")
    private Boolean isCover;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Listing getListing() {
        return listing;
    }

    public void setListing(Listing listing) {
        this.listing = listing;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public String getFileContentType() {
        return fileContentType;
    }

    public void setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
    }

    public Boolean isCover() {
        return isCover;
    }

    public void setCover(Boolean cover) {
        isCover = cover;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ListingPicture that = (ListingPicture) o;
        return isCover == that.isCover && Objects.deepEquals(file, that.file) && Objects.equals(fileContentType, that.fileContentType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Arrays.hashCode(file), fileContentType, isCover);
    }

    @Override
    public String toString() {
        return "ListingPicture{" +
                "file=" + Arrays.toString(file) +
                ", fileContentType='" + fileContentType + '\'' +
                ", isCover=" + isCover +
                '}';
    }

}
