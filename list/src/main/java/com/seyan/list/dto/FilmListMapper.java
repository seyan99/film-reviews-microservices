package com.seyan.list.dto;


import com.seyan.list.entry.ListEntry;
import com.seyan.list.entry.ListEntryId;
import com.seyan.list.filmlist.FilmList;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Component;

import java.beans.PropertyDescriptor;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class FilmListMapper {
    public FilmList mapFilmListCreationDTOToFilmList(FilmListCreationDTO dto) {
        FilmList filmList = new FilmList();
        BeanUtils.copyProperties(dto, filmList, getNullFieldNames(dto));
        return filmList;
    }

    public Map<Integer, ListEntry> mapListEntriesToOrderedMap(List<ListEntry> entries) {
        return entries.stream().collect(Collectors.toMap(entries::indexOf, it -> it));
    }

    public List<Long> mapListEntriesToFilmIds(List<ListEntry> filmEntries) {
        if (filmEntries == null) {
            return null;
        }

        return filmEntries.stream()
                .map(ListEntry::getFilmId)
                .toList();
    }

    public List<ListEntry> mapFilmIdsToListEntries(Long listId, List<Long> filmIds, LocalDateTime date) {
        if (filmIds == null) {
            return null;
        }

        List<ListEntryId> listEntryIds = mapFilmIdToListEntryId(listId, filmIds);

        return listEntryIds.stream()
                .map(it -> new ListEntry(it.getListId(), it.getFilmId(), null, date))
                .toList();
    }

    private List<ListEntryId> mapFilmIdToListEntryId(Long listId, List<Long> filmIds) {
        if (filmIds == null) {
            return null;
        }

        return filmIds.stream()
                .map(it -> new ListEntryId(listId, it))
                .toList();
    }

    public FilmList mapFlmListUpdateDTOToFilmList(FilmListUpdateDTO source, FilmList destination) {
        BeanUtils.copyProperties(source, destination, getNullFieldNames(source));
        return destination;
    }

    public FilmListResponseDTO mapFilmListToFilmListResponseDTO(FilmList filmList) {
        return new FilmListResponseDTO(
                filmList.getId(),
                filmList.getUserId(),
                filmList.getTitle(),
                filmList.getDescription(),
                filmList.getPrivacy(),
                filmList.getLikedUsersIds().size(),
                Collections.emptyList(),
                filmList.getCommentIds().size(),
                Collections.emptyList(),
                filmList.getFilmIds().size()
        );
    }

    public List<FilmListResponseDTO> mapFilmListToFilmListResponseDTO(List<FilmList> filmLists) {
        if (filmLists == null) {
            return null;
        }

        return filmLists.stream()
                .map(this::mapFilmListToFilmListResponseDTO)
                .toList();
    }

    private String[] getNullFieldNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> fieldNames = new HashSet<>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null)
                fieldNames.add(pd.getName());
        }

        String[] result = new String[fieldNames.size()];
        return fieldNames.toArray(result);
    }
}
