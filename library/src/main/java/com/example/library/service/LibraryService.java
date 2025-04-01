package com.example.library.service;

import com.example.library.dto.library.LibraryDto;
import com.example.library.entity.Library;
import com.example.library.entity.User;
import com.example.library.exceptions.BadRequestException;
import com.example.library.repository.LibraryRepository;
import com.example.library.repository.UserRepository;
import com.example.library.security.CustomUserDetails;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import static com.example.library.security.UserUtil.getLoggedInUser;

@Service
@AllArgsConstructor
public class LibraryService {

    private final LibraryRepository libraryRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public Library save(Library library) { return libraryRepository.save(library);}

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
        return save(library);
    }

    public Library updateLibrary(Long id, LibraryDto editLibraryRequest) {
        Library library = findById(id);
        validateUpdatedName(library.getName(), editLibraryRequest.getName());
        addLibrary(library, editLibraryRequest);
        return save(library);
    }

    public String deleteLibrary(Long id) {
        findById(id);
        libraryRepository.deleteById(id);
        return "Library with id: " + id + " deleted succesfully";
    }

    private LibraryDto mapToDto(Library library) {
        return modelMapper.map(library, LibraryDto.class);
    }

    public Page<LibraryDto> getLibrariesRoleAdmin(String keyword, int page, int size) {
        return libraryRepository.findbyNameOrAddress(keyword, PageRequest.of(page, size)).map(this::mapToDto);
    }

    public LibraryDto getLibraryRoleUser() {
        Library library = findById(loggedInUser().getLibrary().getId());
        return mapToDto(library);
    }

    private User loggedInUser() {
        CustomUserDetails loggedInUser = getLoggedInUser();
        return userRepository.findById(loggedInUser.getId())
                .orElseThrow(() -> new BadRequestException("User not found"));
    }

}

