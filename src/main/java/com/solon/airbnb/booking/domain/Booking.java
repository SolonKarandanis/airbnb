package com.solon.airbnb.booking.domain;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;
import com.solon.airbnb.shared.domain.DomainConstants;
import jakarta.persistence.*;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.UuidGenerator;

import com.solon.airbnb.shared.domain.AbstractAuditingEntity;
import com.solon.airbnb.shared.domain.UuidEntity;

@NamedQueries({
        @NamedQuery(name = Booking.BOOKING_EXISTS_AT_INTERVAL,
                query = "SELECT CASE WHEN COUNT(booking) > 0 THEN TRUE ELSE FALSE END " +
                        "FROM Booking  booking "+
                        "WHERE NOT (booking.endDate <= :startDate OR booking.startDate >= :endDate) " +
                        "AND booking.fkListing = :fkListing"),
})
@Entity
@Table(name = "booking")
public class Booking extends AbstractAuditingEntity<Long> implements UuidEntity{

    public static final String BOOKING_EXISTS_AT_INTERVAL= "Booking.bookingExistsAtInterval";
    public static final String FIND_ALL_BY_FK_LISTING= "Booking.findAllByFkListing";
    public static final String FIND_ALL_BY_FK_TENANT= "Booking.findAllByFkTenant";
    public static final String DELETE_BOOKING_BY_FK_TENANT_AND_PUBLIC_ID= "Booking.deleteBookingByFkTenantAndPublicId";
    public static final String DELETE_BOOKING_BY_PUBLIC_ID_AND_FK_LISTING= "Booking.deleteBookingByPublicIdAndFkListing";
    public static final String FIND_ALL_BY_FK_LISTING_IN= "Booking.findAllByFkListingIn";
    public static final String FIND_ALL_MATCH_WITH_DATE= "Booking.findAllMatchWithDate";

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = DomainConstants.BOOKING_GEN
    )
    @SequenceGenerator(
            name = DomainConstants.BOOKING_GEN,
            sequenceName = DomainConstants.BOOKING_SQ,
            allocationSize = 1
    )
    @Column(name = "id")
    private Long id;

    @NaturalId
    @UuidGenerator
    @Column(name = "public_id", nullable = false)
    private UUID publicId;

    @Column(name = "start_date", nullable = false)
    private OffsetDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private OffsetDateTime endDate;

    @Column(name = "total_price", nullable = false)
    private Integer totalPrice;

    @Column(name = "nb_of_travelers", nullable = false)
    private Integer numberOfTravelers;

    @Column(name = "fk_tenant", nullable = false)
    private UUID fkTenant;

    @Column(name = "fk_listing", nullable = false)
    private UUID fkListing;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
	public UUID getPublicId() {
        return publicId;
    }

    public void setPublicId(UUID publicId) {
        this.publicId = publicId;
    }

    public OffsetDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(OffsetDateTime startDate) {
        this.startDate = startDate;
    }

    public OffsetDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(OffsetDateTime endDate) {
        this.endDate = endDate;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getNumberOfTravelers() {
        return numberOfTravelers;
    }

    public void setNumberOfTravelers(Integer numberOfTravelers) {
        this.numberOfTravelers = numberOfTravelers;
    }

    public UUID getFkTenant() {
        return fkTenant;
    }

    public void setFkTenant(UUID fkTenant) {
        this.fkTenant = fkTenant;
    }

    public UUID getFkListing() {
        return fkListing;
    }

    public void setFkListing(UUID fkListing) {
        this.fkListing = fkListing;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return totalPrice == booking.totalPrice && numberOfTravelers == booking.numberOfTravelers && Objects.equals(startDate, booking.startDate) && Objects.equals(endDate, booking.endDate) && Objects.equals(fkTenant, booking.fkTenant) && Objects.equals(fkListing, booking.fkListing);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startDate, endDate, totalPrice, numberOfTravelers, fkTenant, fkListing);
    }

    @Override
    public String toString() {
        return "Booking{" +
                "startDate=" + startDate +
                ", endDate=" + endDate +
                ", totalPrice=" + totalPrice +
                ", numberOfTravelers=" + numberOfTravelers +
                ", fkTenant=" + fkTenant +
                ", fkListing=" + fkListing +
                '}';
    }

}
