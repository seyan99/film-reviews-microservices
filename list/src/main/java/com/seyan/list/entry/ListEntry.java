package com.seyan.list.entry;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "entries")
@IdClass(ListEntryId.class)
@DynamicUpdate
public class ListEntry {
    @Id
    @Column(name = "list_id")
    private Long listId;

    @Id
    @Column(name = "film_id")
    private Long filmId;

    private Long entryOrder;

    private LocalDateTime whenAdded;
}
