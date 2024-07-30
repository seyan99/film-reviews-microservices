package com.seyan.film.profile;

import com.seyan.reviewmonolith.profile.dto.ProfileCreationDTO;
import com.seyan.reviewmonolith.profile.dto.ProfileMapper;
import com.seyan.reviewmonolith.profile.dto.ProfileResponseDTO;
import com.seyan.reviewmonolith.profile.dto.ProfileUpdateDTO;
import com.seyan.reviewmonolith.responseWrapper.CustomResponseWrapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/profiles")
@RestController
public class ProfileController {
    private final ProfileService profileService;
    private final ProfileMapper profileMapper;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CustomResponseWrapper<ProfileResponseDTO>> createProfile(@RequestBody @Valid ProfileCreationDTO dto) {

        Profile profile = profileService.createProfile(dto);
        ProfileResponseDTO response = profileMapper.mapProfileToProfileResponseDTO(profile);

        CustomResponseWrapper<ProfileResponseDTO> wrapper = CustomResponseWrapper.<ProfileResponseDTO>builder()
                .status(HttpStatus.CREATED.value())
                .message("Profile has been successfully created")
                .data(response)
                .build();

        return new ResponseEntity<>(wrapper, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/update")
    public ResponseEntity<CustomResponseWrapper<ProfileResponseDTO>> updateProfile(
            @PathVariable("id") Long id, @RequestBody @Valid ProfileUpdateDTO dto) {

        Profile profile = profileService.updateProfile(dto, id);
        ProfileResponseDTO response = profileMapper.mapProfileToProfileResponseDTO(profile);

        CustomResponseWrapper<ProfileResponseDTO> wrapper = CustomResponseWrapper.<ProfileResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Profile has been updated")
                .data(response)
                .build();

        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<CustomResponseWrapper<ProfileResponseDTO>> deleteProfile(@PathVariable("id") Long profileId) {

        profileService.deleteProfile(profileId);

        CustomResponseWrapper<ProfileResponseDTO> wrapper = CustomResponseWrapper.<ProfileResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Profile has been deleted")
                .data(null)
                .build();

        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    // todo actor/profile-url
    // todo director/profile-url

    //todo executive-producer/profile-url
    //todo writer/profile-url

    @GetMapping("/{id}/details")
    public ResponseEntity<CustomResponseWrapper<ProfileResponseDTO>> profileDetailsById(@PathVariable("id") Long profileId) {

        Profile profile = profileService.getProfileById(profileId);
        ProfileResponseDTO response = profileMapper.mapProfileToProfileResponseDTO(profile);

        CustomResponseWrapper<ProfileResponseDTO> wrapper = CustomResponseWrapper.<ProfileResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Profile details")
                .data(response)
                .build();

        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @GetMapping("/{profileUrl}")
    public ResponseEntity<CustomResponseWrapper<ProfileResponseDTO>> profileDetailsByUrl(@PathVariable(value = "profileUrl") String profileUrl) {

        Profile profile = profileService.getProfileByUrl(profileUrl);
        ProfileResponseDTO response = profileMapper.mapProfileToProfileResponseDTO(profile);

        CustomResponseWrapper<ProfileResponseDTO> wrapper = CustomResponseWrapper.<ProfileResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Profile details")
                .data(response)
                .build();

        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<CustomResponseWrapper<List<ProfileResponseDTO>>> getAllProfiles() {

        List<Profile> allProfiles = profileService.getAllProfiles();
        List<ProfileResponseDTO> response = profileMapper.mapProfileToProfileResponseDTO(allProfiles);

        CustomResponseWrapper<List<ProfileResponseDTO>> wrapper = CustomResponseWrapper.<List<ProfileResponseDTO>>builder()
                .status(HttpStatus.OK.value())
                .message("List of all profiles")
                .data(response)
                .build();

        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @PatchMapping("/{id}/update/add-starring")
    public ResponseEntity<CustomResponseWrapper<ProfileResponseDTO>> addStarringFilm(
            @PathVariable(value = "id") Long profileId, @RequestBody List<Long> filmIdList) {

        Profile profile = profileService.addStarringFilm(profileId, filmIdList);
        ProfileResponseDTO response = profileMapper.mapProfileToProfileResponseDTO(profile);

        CustomResponseWrapper<ProfileResponseDTO> wrapper = CustomResponseWrapper.<ProfileResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Profile with updated starring films")
                .data(response)
                .build();

        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @PatchMapping("/{id}/update/remove-starring")
    public ResponseEntity<CustomResponseWrapper<ProfileResponseDTO>> removeStarringFilm(
            @PathVariable(value = "id") Long profileId, @RequestBody List<Long> filmIdList) {

        Profile profile = profileService.removeStarringFilm(profileId, filmIdList);
        ProfileResponseDTO response = profileMapper.mapProfileToProfileResponseDTO(profile);

        CustomResponseWrapper<ProfileResponseDTO> wrapper = CustomResponseWrapper.<ProfileResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Profile with updated starring films")
                .data(response)
                .build();

        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @PatchMapping("/{id}/update/add-directed")
    public ResponseEntity<CustomResponseWrapper<ProfileResponseDTO>> addDirectedFilm(
            @PathVariable(value = "id") Long profileId, @RequestBody List<Long> filmIdList) {

        Profile profile = profileService.addDirectedFilm(profileId, filmIdList);
        ProfileResponseDTO response = profileMapper.mapProfileToProfileResponseDTO(profile);

        CustomResponseWrapper<ProfileResponseDTO> wrapper = CustomResponseWrapper.<ProfileResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Profile with updated directed films")
                .data(response)
                .build();

        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @PatchMapping("/{id}/update/remove-directed")
    public ResponseEntity<CustomResponseWrapper<ProfileResponseDTO>> removeDirectedFilm(
            @PathVariable(value = "id") Long profileId, @RequestBody List<Long> filmIdList) {

        Profile profile = profileService.removeDirectedFilm(profileId, filmIdList);
        ProfileResponseDTO response = profileMapper.mapProfileToProfileResponseDTO(profile);

        CustomResponseWrapper<ProfileResponseDTO> wrapper = CustomResponseWrapper.<ProfileResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Profile with updated directed films")
                .data(response)
                .build();

        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    /*@PatchMapping("/{id}/update/add-starring")
    public ResponseEntity<CustomResponseWrapper<ProfileResponseDTO>> addStarringFilm(
            @PathVariable(value = "id") Long profileId, @RequestParam(required = false) Long filmId,
            @RequestBody(required = false) List<Long> filmIdList) {

        Profile profile = new Profile();

        if ((filmId == null & filmIdList == null)
                || (filmId != null & filmIdList != null)) {
            CustomResponseWrapper<ProfileResponseDTO> wrapper = CustomResponseWrapper.<ProfileResponseDTO>builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("Request should contain film id OR film id list")
                    .data(null)
                    .build();
            return new ResponseEntity<>(wrapper, HttpStatus.BAD_REQUEST);
        } else if (filmId != null) {
            profile = profileService.addStarringFilm(profileId, filmId);
        } else {
            profile = profileService.addStarringFilm(profileId, filmIdList);
        }

        ProfileResponseDTO response = profileMapper.mapProfileToProfileResponseDTO(profile);
        CustomResponseWrapper<ProfileResponseDTO> wrapper = CustomResponseWrapper.<ProfileResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Profile with updated starring films")
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @PatchMapping("/{id}/update/remove-starring")
    public ResponseEntity<CustomResponseWrapper<ProfileResponseDTO>> removeStarringFilm(
            @PathVariable(value = "id") Long profileId, @RequestParam(required = false) Long filmId,
            @RequestBody(required = false) List<Long> filmIdList) {

        Profile profile = new Profile();

        if ((filmId == null & filmIdList == null)
                || (filmId != null & filmIdList != null)) {
            CustomResponseWrapper<ProfileResponseDTO> wrapper = CustomResponseWrapper.<ProfileResponseDTO>builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("Request should contain film id OR film id list")
                    .data(null)
                    .build();
            return new ResponseEntity<>(wrapper, HttpStatus.BAD_REQUEST);
        } else if (filmId != null) {
            profile = profileService.removeStarringFilm(profileId, filmId);
        } else {
            profile = profileService.removeStarringFilm(profileId, filmIdList);
        }

        ProfileResponseDTO response = profileMapper.mapProfileToProfileResponseDTO(profile);
        CustomResponseWrapper<ProfileResponseDTO> wrapper = CustomResponseWrapper.<ProfileResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Profile with updated starring films")
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }*/

    /*@PatchMapping("/{id}/update/add-directed")
    public ResponseEntity<CustomResponseWrapper<ProfileResponseDTO>> addDirectedFilm(
            @PathVariable(value = "id") Long profileId, @RequestParam(required = false) Long filmId,
            @RequestBody(required = false) List<Long> filmIdList) {

        Profile profile = new Profile();

        if ((filmId == null & filmIdList == null)
                || (filmId != null & filmIdList != null)) {
            CustomResponseWrapper<ProfileResponseDTO> wrapper = CustomResponseWrapper.<ProfileResponseDTO>builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("Request should contain film id OR film id list")
                    .data(null)
                    .build();
            return new ResponseEntity<>(wrapper, HttpStatus.BAD_REQUEST);
        } else if (filmId != null) {
            profile = profileService.addDirectedFilm(profileId, filmId);
        } else {
            profile = profileService.addDirectedFilm(profileId, filmIdList);
        }

        ProfileResponseDTO response = profileMapper.mapProfileToProfileResponseDTO(profile);
        CustomResponseWrapper<ProfileResponseDTO> wrapper = CustomResponseWrapper.<ProfileResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Profile with updated directed films")
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @PatchMapping("/{id}/update/remove-directed")
    public ResponseEntity<CustomResponseWrapper<ProfileResponseDTO>> removeDirectedFilm(
            @PathVariable(value = "id") Long profileId, @RequestParam(required = false) Long filmId,
            @RequestBody(required = false) List<Long> filmIdList) {

        Profile profile = new Profile();

        if ((filmId == null & filmIdList == null)
                || (filmId != null & filmIdList != null)) {
            CustomResponseWrapper<ProfileResponseDTO> wrapper = CustomResponseWrapper.<ProfileResponseDTO>builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("Request should contain film id OR film id list")
                    .data(null)
                    .build();
            return new ResponseEntity<>(wrapper, HttpStatus.BAD_REQUEST);
        } else if (filmId != null) {
            profile = profileService.removeDirectedFilm(profileId, filmId);
        } else {
            profile = profileService.removeDirectedFilm(profileId, filmIdList);
        }

        ProfileResponseDTO response = profileMapper.mapProfileToProfileResponseDTO(profile);
        CustomResponseWrapper<ProfileResponseDTO> wrapper = CustomResponseWrapper.<ProfileResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Profile with updated directed films")
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }*/
}
