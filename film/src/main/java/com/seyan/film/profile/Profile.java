package com.seyan.film.profile;

import com.seyan.film.film.Film;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "profiles")
@DynamicUpdate
/*@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")*/
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String biography;

    @ManyToMany(mappedBy = "cast", fetch = FetchType.LAZY)
    private List<Film> starringFilms;

    @OneToMany(mappedBy = "director", fetch = FetchType.LAZY) //, cascade = CascadeType.ALL, orphanRemoval = true
    private List<Film> directedFilms;

    @Column(unique = true)
    private String url;

    /*@PreRemove
    private void removeDirectedFilms() {
        directedFilms.clear();
    }*/
}
