package com.example.library.service;

import com.example.library.datatype.RequestStatus;
import com.example.library.dto.request.RequestDto;
import com.example.library.dto.request.SimpleRequestDto;
import com.example.library.entity.Request;
import com.example.library.entity.User;
import com.example.library.exceptions.BadRequestException;
import com.example.library.repository.RequestRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RequestService {

    private final RequestRepository requestRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public Request save(Request request) {
        return requestRepository.save(request);
    }

    public Request findRequestById(Long id) {
        return requestRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Request not found: " + id));
    }

    public Request addRequest(RequestDto requestDto) {
        User user = userService.loggedInUser();
        Request request = new Request();

        request.setDescription(requestDto.getDescription());
        request.setUser(user);
        request.setRequestStatus(RequestStatus.CREATED);
        return save(request);
    }

    public Request completeRequest(Long id) {
        Request request = findRequestById(id);
        request.setRequestStatus(RequestStatus.COMPLETED);
        return save(request);
    }

    public Page<SimpleRequestDto> getRequests(int page, int size) {
        User user = userService.loggedInUser();
        return switch (user.getRole()) {
            case ADMIN -> requestRepository.findAll(PageRequest.of(page, size)).map(this::mapToDto);
            case USER -> requestRepository.findByUser(user, PageRequest.of(page, size)).map(this::mapToDto);
        };
    }

    private SimpleRequestDto mapToDto(Request request) {
        return modelMapper.map(request, SimpleRequestDto.class);
    }
}
