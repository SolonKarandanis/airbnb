package com.solon.airbnb.user.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.Objects;

@NamedQueries({
        @NamedQuery(name = Authority.FIND_BY_NAME,
                query = "SELECT a FROM Authority a "
                        + "WHERE a.name = :name ")
})
@Entity
@Table(name = "authority")
public class Authority implements Serializable {

    public static final String FIND_BY_NAME= "Authority.findByName";

    @NotNull
    @Size(max=50)
    @Id
    @Column(length = 50)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Authority authority = (Authority) o;
        return Objects.equals(name, authority.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public String toString() {
        return "Authority{" +
                "name='" + name + '\'' +
                '}';
    }
}
