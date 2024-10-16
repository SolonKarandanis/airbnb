package com.solon.airbnb.user.domain;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import com.solon.airbnb.shared.domain.DomainConstants;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.jdbc.Expectation.RowCount;

import com.solon.airbnb.shared.domain.AbstractAuditingEntity;
import com.solon.airbnb.shared.domain.UuidEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;



@SQLDelete(
        sql = "UPDATE Users SET status='0' WHERE username=?",
        verify = RowCount.class
)
@NamedEntityGraph(name = User.GRAPH_USERS_AUTHORITIES,
	attributeNodes = @NamedAttributeNode("authorities")
)
@Entity
@Table(name = "airbnb_user")
public class User extends AbstractAuditingEntity<Long> implements UuidEntity{
	
	public static final String GRAPH_USERS_AUTHORITIES="graph.users.authorities";

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = DomainConstants.USER_GEN
    )
    @SequenceGenerator(
            name = DomainConstants.USER_GEN,
            sequenceName = DomainConstants.USER_SQ,
            allocationSize = 1
    )
    @Column(name = "id")
    private Long id;
    
    @NaturalId
    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "first_name")
    private String firstName;

    @NaturalId
    @Column(name = "email")
    private String email;

    @Column(name = "image_url")
    private String imageUrl;
    
    @Column(name = "status")
    private AccountStatus status;

    @NaturalId
    @UuidGenerator
    @Column(name = "public_id", nullable = false)
    private UUID publicId;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    },fetch = FetchType.LAZY)
    @JoinTable(name = "user_authority",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "name")})
    private Set<Authority> authorities = new HashSet<>();

    @Column(name = "is_verified")
    private Boolean isVerified = false;

    @Override
	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
	public UUID getPublicId() {
        return publicId;
    }

    public void setPublicId(UUID publicId) {
        this.publicId = publicId;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }


    public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public AccountStatus getStatus() {
		return status;
	}

	public void setStatus(AccountStatus status) {
		this.status = status;
	}

    public Boolean getVerified() {
        return isVerified;
    }

    public void setVerified(Boolean verified) {
        isVerified = verified;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(lastName, user.lastName) && Objects.equals(firstName, user.firstName) && Objects.equals(email, user.email) && Objects.equals(imageUrl, user.imageUrl) && Objects.equals(publicId, user.publicId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lastName, firstName, email, imageUrl, publicId);
    }

    @Override
    public String toString() {
        return "User{" +
                "lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", email='" + email + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", publicId=" + publicId +
                '}';
    }
}
