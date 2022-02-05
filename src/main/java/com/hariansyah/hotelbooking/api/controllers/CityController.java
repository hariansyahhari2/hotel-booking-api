package com.hariansyah.hotelbooking.api.controllers;

import com.hariansyah.hotelbooking.api.configs.jwt.JwtToken;
import com.hariansyah.hotelbooking.api.entities.City;
import com.hariansyah.hotelbooking.api.entities.Region;
import com.hariansyah.hotelbooking.api.exceptions.ForeignKeyNotFoundException;
import com.hariansyah.hotelbooking.api.exceptions.InvalidPermissionsException;
import com.hariansyah.hotelbooking.api.models.ResponseMessage;
import com.hariansyah.hotelbooking.api.models.entitymodels.requests.CityRequest;
import com.hariansyah.hotelbooking.api.models.entitymodels.responses.CityResponse;
import com.hariansyah.hotelbooking.api.models.fileupload.ImageUploadRequest;
import com.hariansyah.hotelbooking.api.repositories.AccountRepository;
import com.hariansyah.hotelbooking.api.services.CityService;
import com.hariansyah.hotelbooking.api.services.FileService;
import com.hariansyah.hotelbooking.api.services.RegionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityExistsException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.hariansyah.hotelbooking.api.models.validations.RoleValidation.validateRoleAdmin;

@RequestMapping("/city")
@RestController
public class CityController {

    @Autowired
    private CityService service;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private RegionService regionService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private JwtToken jwtTokenUtil;

    @Autowired
    private FileService fileService;

    private void validateAdmin(HttpServletRequest request) {
        validateRoleAdmin(request, jwtTokenUtil, accountRepository);
    }

    @GetMapping("/{id}")
    public ResponseMessage<CityResponse> findById(
            @PathVariable Integer id
    ) {
        City entity = service.findById(id);

        CityResponse data = modelMapper.map(entity, CityResponse.class);
        return ResponseMessage.success(data);
    }

    @PostMapping
    public ResponseMessage<Boolean> add(
            @RequestBody @Valid CityRequest model,
            HttpServletRequest request
    ) {
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) throw new InvalidPermissionsException();
        validateAdmin(request);
        City entity = modelMapper.map(model, City.class);

        Region customerIdentity = regionService.findById(model.getRegionId());

        if (customerIdentity == null) {
            throw new ForeignKeyNotFoundException();
        }

        entity.setRegion(customerIdentity);
        return ResponseMessage.success(service.save(entity));
    }

    @PutMapping("/{id}")
    public ResponseMessage<Boolean> edit(
            @PathVariable Integer id,
            @RequestBody @Valid CityRequest model,
            HttpServletRequest request
    ) {
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) throw new InvalidPermissionsException();
        validateAdmin(request);

        City entity = service.findById(id);

        Region customerIdentity = regionService.findById(model.getRegionId());
        entity.setRegion(customerIdentity);

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

        service.findById(id);

        return ResponseMessage.success(service.removeById(id));
    }

    @GetMapping
    public ResponseMessage<List<CityResponse>> findAll() {
        List<City> entities = service.findAll();
        List<CityResponse> data = entities.stream()
                .map(e -> modelMapper.map(e, CityResponse.class))
                .collect(Collectors.toList());
        return ResponseMessage.success(data);
    }

    @PostMapping(value = "/upload/{id}", consumes = {"multipart/form-data"})
    public ResponseMessage<Object> upload(
            @PathVariable Integer id,
            ImageUploadRequest model,
            HttpServletRequest request
    ) throws IOException {
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) throw new InvalidPermissionsException();
        validateAdmin(request);

        City entity = service.findById(id);

        fileService.upload(entity, model.getFile().getInputStream());

        return ResponseMessage.success(true);
    }

    @GetMapping("/download/{id}.png")
    public void download(
            @PathVariable Integer id,
            HttpServletResponse response
    ) throws IOException {
        City entity = service.findById(id);
        if (entity == null) throw new EntityExistsException();

        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "filename=\"" + entity.getId() + "\"" );
        fileService.download(entity, response.getOutputStream());
    }
}
