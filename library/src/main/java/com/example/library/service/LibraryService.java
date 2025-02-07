package com.example.library.service;

import com.example.library.dto.library.LibraryDto;
import com.example.library.entity.Library;
import com.example.library.exceptions.BadRequestException;
import com.example.library.repository.LibraryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LibraryService {

    private final LibraryRepository libraryRepository;

    public Library save(Library library) {return libraryRepository.save(library);}

    public Library findById(Long id) {
        return libraryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Library with ID " + id + " is not found"));
    }

    private void validateNewName(String newName) {
        if (libraryRepository.existsByName(newName)) {
            throw new BadRequestException("Name is already taken");
        }
    }

    private void validateUpdatedName(String currentName, String newName) {
        if (!currentName.equals(newName) && libraryRepository.existsByName(newName)) {
            throw new BadRequestException("Name is already taken");
        }
    }
    public void addLibrary(Library library, LibraryDto libraryDto) {
        library.setName(libraryDto.getName());
        library.setAddress(libraryDto.getAddress());

    }
    public Library registerLibrary(LibraryDto addLibraryRequest) {
        Library library = new Library();
        validateNewName(addLibraryRequest.getName());
        addLibrary(library, addLibraryRequest);
        save(library);
        return library;
    }

    public Library updateLibrary(Long id, LibraryDto editLibraryRequest) {
        Library library = findById(id);
        validateUpdatedName(library.getName(), editLibraryRequest.getName());
        addLibrary(library, editLibraryRequest);
        save(library);
        return library;
    }

    public String deleteLibrary(Long id) {
        findById(id);
        libraryRepository.deleteById(id);
        return "Library with id: " + id + " deleted succesfully";
    }
}
