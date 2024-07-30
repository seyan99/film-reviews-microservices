package com.seyan.list.filmList.entry;

import jakarta.persistence.Embeddable;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;

@ToString
@Getter
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@DynamicUpdate
public class ListEntryId implements Serializable {
    private Long listId;
    private Long filmId;

    //todo delete class

}
