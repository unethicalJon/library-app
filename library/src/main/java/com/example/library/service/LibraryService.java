package com.example.library.service;

import com.example.library.dto.library.LibraryDto;
import com.example.library.entity.Library;
import com.example.library.entity.User;
import com.example.library.exceptions.BadRequestException;
import com.example.library.repository.LibraryRepository;
import com.example.library.repository.UserRepository;
import com.example.library.security.CustomUserDetails;
import com.example.library.security.UserUtil;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LibraryService {

    private final LibraryRepository libraryRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

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

     /*public Page<LibraryDto> getLibraries(Pageable pageable) {
        User user = loggedInUser();

        switch (user.getRole()) {
            case ADMIN:
                return libraryRepository.findAll(pageable).map(this::mapToDto);
            case USER:
                if (user.getLibrary() != null) {
                    return new PageImpl<>(List.of(mapToDto(user.getLibrary())), Pageable pageable, 1); //return page content as a list
                } else {
                    return Page.empty(pageable); // creates new empty page for given pageable
                }
                default:
                throw new IllegalStateException("Unexpected role: " + user.getRole());
        }
    }*/

/*    public List<LibraryDto> getLibraries(String name, int page, int size) {
        User user = loggedInUser();

        switch (user.getRole()) {
            case ADMIN:
             //return libraryRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
             return libraryRepository.findAllBy(name, PageRequest.of(page, size)).stream().map(this::mapToDto));
            case USER:
                Library library = findById(user.getLibrary().getId());
                return List.of(mapToDto(library));

            default:
                throw new IllegalStateException("Unexpected role: " + user.getRole());
        }
    }*/

    private User loggedInUser() {
        CustomUserDetails loggedInUser = UserUtil.getLoggedInUser();
        return userRepository.findById(loggedInUser.getId())
                .orElseThrow(() -> new BadRequestException("User not found"));
    }

    private LibraryDto mapToDto(Library library) {
        return modelMapper.map(library, LibraryDto.class);
    }

    public Page<LibraryDto> getLibrariesRoleAdmin(String name, String address, int page, int size) {
        return libraryRepository.findAllByNameContainingOrAddressContaining(name, address, PageRequest.of(page, size)).map(this::mapToDto);
    }

    public LibraryDto getLibraryRoleUser() {
        Library library = findById(loggedInUser().getLibrary().getId());
        return mapToDto(library);
    }

    public Page<LibraryDto> searchByName(String name, Pageable pageable) {
        return libraryRepository.findByNameContaining(name, pageable).map(this::mapToDto);
    }
}

