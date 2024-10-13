package com.solon.airbnb.listing.domain;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import com.solon.airbnb.shared.domain.DomainConstants;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.UuidGenerator;

import com.solon.airbnb.shared.domain.AbstractAuditingEntity;
import com.solon.airbnb.shared.domain.UuidEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "listing")
public class Listing extends AbstractAuditingEntity<Long> implements UuidEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = DomainConstants.LISTING_GEN)
    @SequenceGenerator(name = DomainConstants.LISTING_GEN, sequenceName = DomainConstants.LISTING_SQ, allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @NaturalId
    @UuidGenerator
    @Column(name = "public_id", nullable = false)
    private UUID publicId;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "guests")
    private Integer guests;
    
    @Column(name = "bedrooms")
    private Integer bedrooms;
    
    @Column(name = "beds")
    private Integer beds;
    
    @Column(name = "bathrooms")
    private Integer bathrooms;

    @Column(name = "price")
    private Integer price;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private BookingCategory bookingCategory;

    @Column(name = "location")
    private String location;

    @Column(name = "landlord_public_id")
    private UUID landlordPublicId;

    @OneToMany(mappedBy = "listing", cascade = CascadeType.REMOVE)
    private Set<ListingPicture> pictures = new HashSet<>();

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getGuests() {
        return guests;
    }

    public void setGuests(Integer guests) {
        this.guests = guests;
    }

    public int getBedrooms() {
        return bedrooms;
    }

    public void setBedrooms(Integer bedrooms) {
        this.bedrooms = bedrooms;
    }

    public int getBeds() {
        return beds;
    }

    public void setBeds(Integer beds) {
        this.beds = beds;
    }

    public int getBathrooms() {
        return bathrooms;
    }

    public void setBathrooms(Integer bathrooms) {
        this.bathrooms = bathrooms;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public BookingCategory getBookingCategory() {
        return bookingCategory;
    }

    public void setBookingCategory(BookingCategory bookingCategory) {
        this.bookingCategory = bookingCategory;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public UUID getLandlordPublicId() {
        return landlordPublicId;
    }

    public void setLandlordPublicId(UUID landlordPublicId) {
        this.landlordPublicId = landlordPublicId;
    }

    public Set<ListingPicture> getPictures() {
        return pictures;
    }

    public void setPictures(Set<ListingPicture> pictures) {
        this.pictures = pictures;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Listing listing = (Listing) o;
        return guests == listing.guests && bedrooms == listing.bedrooms && beds == listing.beds && bathrooms == listing.bathrooms && price == listing.price && Objects.equals(title, listing.title) && Objects.equals(description, listing.description) && bookingCategory == listing.bookingCategory && Objects.equals(location, listing.location) && Objects.equals(landlordPublicId, listing.landlordPublicId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, guests, bedrooms, beds, bathrooms, price, bookingCategory, location, landlordPublicId);
    }

    @Override
    public String toString() {
        return "Listing{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", guests=" + guests +
                ", bedrooms=" + bedrooms +
                ", beds=" + beds +
                ", bathrooms=" + bathrooms +
                ", price=" + price +
                ", bookingCategory=" + bookingCategory +
                ", location='" + location + '\'' +
                ", landlordPublicId=" + landlordPublicId +
                '}';
    }
}
