package com.seyan.list.list;

import com.seyan.list.entry.ListEntry;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@ToString
@Data
@AllArgsConstructor
@Builder
@Entity
@Table(name = "lists")
@DynamicUpdate
public class List {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String username;
    private String title;
    private String description;
    @Enumerated(EnumType.STRING)
    private Privacy privacy;
    private java.util.List<Long> likedUsersIds;
    private java.util.List<Long> commentIds;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "list_id", referencedColumnName = "id")
    private java.util.List<ListEntry> filmIds = new ArrayList<>();
    private LocalDateTime creationDate;
    private LocalDateTime lastUpdateDate;

    public List() {
        this.likedUsersIds = new ArrayList<>();
        this.commentIds = new ArrayList<>();
    }
}
