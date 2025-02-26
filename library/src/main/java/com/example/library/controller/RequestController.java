package com.example.library.controller;

import com.example.library.dto.general.EntityIdDto;
import com.example.library.dto.request.RequestDto;
import com.example.library.dto.request.SimpleRequestDto;
import com.example.library.entity.Request;
import com.example.library.service.RequestService;
import com.example.library.util.constants.RestConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(RestConstants.RequestController.BASE)
public class RequestController {

    private final RequestService requestService;

    @PreAuthorize("hasAnyAuthority('USER')")
    @PostMapping(RestConstants.RequestController.CREATE)
    public ResponseEntity<EntityIdDto> postRequest(@RequestBody RequestDto requestDto) {
        Request request = requestService.addRequest(requestDto);
        return new ResponseEntity<>(EntityIdDto.of(request.getId()), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PutMapping(RestConstants.RequestController.COMPLETE)
    public ResponseEntity<EntityIdDto> adminActivateUser(@PathVariable(value = RestConstants.ID) Long id) {
        Request request = requestService.completeRequest(id);
        return new ResponseEntity<>(EntityIdDto.of(request.getId()), HttpStatus.CREATED);
    }

    @GetMapping
    public Page<SimpleRequestDto> getRequests(
            @RequestParam(defaultValue = RestConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(defaultValue = RestConstants.DEFAULT_PAGE_SIZE) int size) {
        return requestService.getRequests(page, size);
    }
}
