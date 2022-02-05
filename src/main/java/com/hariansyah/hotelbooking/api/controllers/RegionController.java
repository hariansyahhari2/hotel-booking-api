package com.hariansyah.hotelbooking.api.controllers;

import com.hariansyah.hotelbooking.api.configs.jwt.JwtToken;
import com.hariansyah.hotelbooking.api.entities.Region;
import com.hariansyah.hotelbooking.api.exceptions.EntityNotFoundException;
import com.hariansyah.hotelbooking.api.exceptions.InvalidPermissionsException;
import com.hariansyah.hotelbooking.api.models.ResponseMessage;
import com.hariansyah.hotelbooking.api.models.entitymodels.requests.RegionRequest;
import com.hariansyah.hotelbooking.api.models.entitymodels.responses.RegionResponse;
import com.hariansyah.hotelbooking.api.repositories.AccountRepository;
import com.hariansyah.hotelbooking.api.services.RegionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static com.hariansyah.hotelbooking.api.models.validations.RoleValidation.validateRoleAdmin;

@RequestMapping("/region")
@RestController
public class RegionController {

    @Autowired
    private RegionService service;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private JwtToken jwtTokenUtil;

    private void validateAdmin(HttpServletRequest request) {
        validateRoleAdmin(request, jwtTokenUtil, accountRepository);
    }

    @GetMapping("/{id}")
    public ResponseMessage<RegionResponse> findById(
            @PathVariable Integer id
    ) {
        Region entity = service.findById(id);

        RegionResponse data = modelMapper.map(entity, RegionResponse.class);
        return ResponseMessage.success(data);
    }

    @PostMapping
    public ResponseMessage<Boolean> add(
            @RequestBody @Valid RegionRequest model,
            HttpServletRequest request
    ) {
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) throw new InvalidPermissionsException();
        validateAdmin(request);

        Region entity = modelMapper.map(model, Region.class);
        return ResponseMessage.success(service.save(entity));
    }

    @PutMapping("{id}")
    public ResponseMessage<Boolean> edit(
            @PathVariable Integer id,
            @RequestBody @Valid RegionRequest model,
            HttpServletRequest request
    ) {
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) throw new InvalidPermissionsException();
        validateAdmin(request);

        Region entity = service.findById(id);

        modelMapper.map(model, entity);
        return ResponseMessage.success(service.edit(entity));
    }

    @DeleteMapping("/{id}")
    public ResponseMessage<Boolean> delete(
            @PathVariable Integer id,
            HttpServletRequest request
    ) {
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) throw new InvalidPermissionsException();
        validateAdmin(request);

        Region entity = service.findById(id);
        if (entity == null) throw new EntityNotFoundException();

        return ResponseMessage.success(service.removeById(id));
    }

    @GetMapping
    public ResponseMessage<List<RegionResponse>> findAll() {
        List<Region> entities = service.findAll();
        List<RegionResponse> data = entities.stream()
                .map(e -> modelMapper.map(e, RegionResponse.class))
                .collect(Collectors.toList());
        return ResponseMessage.success(data);
    }
}
