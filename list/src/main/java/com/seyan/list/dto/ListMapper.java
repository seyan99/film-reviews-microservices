package com.seyan.list.dto;


import com.seyan.list.entry.ListEntry;
import com.seyan.list.entry.ListEntryId;
import com.seyan.list.external.film.FilmPreviewResponseDTO;
import com.seyan.list.external.film.PageableFilmPreviewResponseDTO;
import com.seyan.list.list.List;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.beans.PropertyDescriptor;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ListMapper {
    public PageableFilmPreviewResponseDTO mapFilmPreviewToPageableFilmReviewDTO(
            Optional<Integer> pageNo,
            int pageSize,
            int totalElements,
            java.util.List<FilmPreviewResponseDTO> content
            ) {
        int page = pageNo.orElse(0);
        int partialPages = totalElements % 5 > 0 ? 1 : 0;
        int fullPages = totalElements / 5;
        return PageableFilmPreviewResponseDTO.builder()
                .pageNo(page)
                .pageSize(pageSize)
                .totalElements(totalElements)
                .totalPages(fullPages + partialPages)
                .last(page == fullPages + partialPages)
                .content(content)
                .build();
    }

    public PageableListResponseDTO mapListsPageToPageableListResponseDTO(Page<List> comments) {
        java.util.List<ListResponseDTO> mapped = mapListToListResponseDTO(comments.getContent());

        return PageableListResponseDTO.builder()
                .content(mapped)
                .pageNo(comments.getNumber())
                .pageSize(comments.getSize())
                .totalPages(comments.getTotalPages())
                .last(comments.isLast())
                .build();
    }

    public List mapListCreationDTOToList(ListCreationDTO dto) {
        List list = new List();
        BeanUtils.copyProperties(dto, list, getNullFieldNames(dto));
        list.setLastUpdateDate(dto.creationDate());
        return list;
    }

    public Map<Integer, ListEntry> mapListEntriesToOrderedMap(java.util.List<ListEntry> entries) {
        return entries.stream().collect(Collectors.toMap(entries::indexOf, it -> it));
    }

    public java.util.List<Long> mapListEntriesToFilmIds(java.util.List<ListEntry> filmEntries) {
        if (filmEntries == null) {
            return null;
        }

        return filmEntries.stream()
                .map(ListEntry::getFilmId)
                .toList();
    }

    public java.util.List<ListEntry> mapFilmIdsToListEntries(Long listId, java.util.List<Long> filmIds, LocalDateTime date) {
        if (filmIds == null) {
            return null;
        }

        java.util.List<ListEntryId> listEntryIds = mapFilmIdToListEntryId(listId, filmIds);

        return listEntryIds.stream()
                .map(it -> new ListEntry(it.getListId(), it.getFilmId(), null, date))
                .toList();
    }

    private java.util.List<ListEntryId> mapFilmIdToListEntryId(Long listId, java.util.List<Long> filmIds) {
        if (filmIds == null) {
            return null;
        }

        return filmIds.stream()
                .map(it -> new ListEntryId(listId, it))
                .toList();
    }

    public List mapListUpdateDTOToList(ListUpdateDTO source, List destination) {
        BeanUtils.copyProperties(source, destination, getNullFieldNames(source));
        return destination;
    }

    public ListResponseDTO mapListToListResponseDTO(List list) {
        return new ListResponseDTO(
                list.getId(),
                list.getUserId(),
                list.getUsername(),
                list.getTitle(),
                list.getDescription(),
                list.getPrivacy(),
                list.getLikedUsersIds().size(),
                //Collections.emptyList(),
                list.getCommentIds().size(),
                //Collections.emptyList(),
                list.getFilmIds().size(),
                list.getCreationDate(),
                list.getLastUpdateDate()
        );
    }

    public java.util.List<ListResponseDTO> mapListToListResponseDTO(java.util.List<List> lists) {
        if (lists == null) {
            return null;
        }

        return lists.stream()
                .map(this::mapListToListResponseDTO)
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
