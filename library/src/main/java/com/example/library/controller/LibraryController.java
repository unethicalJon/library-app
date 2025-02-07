package com.example.library.controller;

import com.example.library.dto.general.EntityIdDto;
import com.example.library.dto.library.LibraryDto;
import com.example.library.entity.Library;
import com.example.library.service.LibraryService;
import com.example.library.util.constants.RestConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(RestConstants.LibraryController.BASE)
public class LibraryController {

    private final LibraryService libraryService;

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping(RestConstants.LibraryController.ADD)
    public ResponseEntity<EntityIdDto> registerLibrary(@RequestBody LibraryDto addLibraryRequest) {
        Library library = libraryService.registerLibrary(addLibraryRequest);
        return new ResponseEntity<>(EntityIdDto.of(library.getId()), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PutMapping(RestConstants.LibraryController.UPDATE + RestConstants.ID_PATH)
    public ResponseEntity<Library> updateLibrary(@PathVariable(value = RestConstants.ID) Long id, @RequestBody LibraryDto updatedLibrary) {
        Library library = libraryService.updateLibrary(id, updatedLibrary);
        return new ResponseEntity(EntityIdDto.of(library.getId()), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @DeleteMapping(RestConstants.LibraryController.DELETE + RestConstants.ID_PATH)
    public String deleteLibrary(@PathVariable(value = RestConstants.ID) Long id) {
        return libraryService.deleteLibrary(id);
    }

}
