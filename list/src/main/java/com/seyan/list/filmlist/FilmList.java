package com.seyan.list.filmlist;

import com.seyan.list.entry.ListEntry;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ToString
@Data
@AllArgsConstructor
//@NoArgsConstructor
//@Builder
@Entity
@Table(name = "lists")
@DynamicUpdate
public class FilmList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String title;
    private String description;
    @Enumerated(EnumType.STRING)
    private Privacy privacy;

   // private Long likeCount;
    private Set<Long> likedUsersIds;

    //private Long commentCount;
    private Set<Long> commentIds;

    @OneToMany(cascade=CascadeType.ALL)
    @JoinColumn(name = "list_id", referencedColumnName = "id")
    private List<ListEntry> filmIds = new ArrayList<>();

    private LocalDateTime creationDate;

    private LocalDateTime lastUpdateDate;
    //todo you watched method

    public FilmList() {
        this.likedUsersIds = new HashSet<>();
        this.commentIds = new HashSet<>();
    }
}
