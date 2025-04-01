package com.example.library.controller;

import com.example.library.dto.general.EntityIdDto;
import com.example.library.dto.library.LibraryDto;
import com.example.library.entity.Library;
import com.example.library.service.LibraryService;
import com.example.library.util.apiDocs.LibraryControllerDoc;
import com.example.library.util.constants.RestConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(RestConstants.LibraryController.BASE)
public class LibraryController {

    private final LibraryService libraryService;

    @PreAuthorize("hasAnyAuthority(@Role.ADMIN_NAME)")
    @PostMapping(RestConstants.LibraryController.ADD)
    @LibraryControllerDoc.RegisterLibraryDoc
    public ResponseEntity<EntityIdDto> registerLibrary(@RequestBody LibraryDto addLibraryRequest) {
        Library library = libraryService.registerLibrary(addLibraryRequest);
        return new ResponseEntity<>(EntityIdDto.of(library.getId()), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority(@Role.ADMIN_NAME)")
    @PutMapping(RestConstants.LibraryController.UPDATE)
    @LibraryControllerDoc.UpdateLibraryDoc
    public ResponseEntity<EntityIdDto> updateLibrary(@PathVariable(value = RestConstants.ID) Long id,
                                                     @RequestBody LibraryDto updatedLibrary) {
        Library library = libraryService.updateLibrary(id, updatedLibrary);
        return new ResponseEntity<>(EntityIdDto.of(library.getId()), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority(@Role.ADMIN_NAME)")
    @DeleteMapping(RestConstants.LibraryController.DELETE)
    @LibraryControllerDoc.DeleteLibraryDoc
    public String deleteLibrary(@PathVariable(value = RestConstants.ID) Long id) {
        try {
            return libraryService.deleteLibrary(id);
        } catch (DataIntegrityViolationException ex) {
            throw new RuntimeException("Libraria me id: " + id + " eshte e lidhur me entitete te tjera!");
        }
    }

    @GetMapping()
    @PreAuthorize("hasAnyAuthority(@Role.ADMIN_NAME)")
    @LibraryControllerDoc.GetLibrariesAdminDoc
    public Page<LibraryDto> getLibrariesRoleAdmin(@RequestParam(required = false) String keyword,
                                                  @RequestParam(defaultValue = RestConstants.DEFAULT_PAGE_NUMBER) int page,
                                                  @RequestParam(defaultValue = RestConstants.DEFAULT_PAGE_SIZE) int size) {
        return libraryService.getLibrariesRoleAdmin(keyword, page, size);
    }

    @GetMapping(RestConstants.LibraryController.LIBRARIES_FOR_USER)
    @PreAuthorize("hasAnyAuthority(@Role.USER_NAME)")
    @LibraryControllerDoc.GetLibraryUserDoc
    public LibraryDto getLibraryRoleUser() {
        return libraryService.getLibraryRoleUser();
    }
}


