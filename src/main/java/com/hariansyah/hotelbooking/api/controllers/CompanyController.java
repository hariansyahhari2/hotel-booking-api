package com.hariansyah.hotelbooking.api.controllers;

import com.hariansyah.hotelbooking.api.configs.jwt.JwtToken;
import com.hariansyah.hotelbooking.api.entities.City;
import com.hariansyah.hotelbooking.api.entities.Company;
import com.hariansyah.hotelbooking.api.exceptions.InvalidPermissionsException;
import com.hariansyah.hotelbooking.api.models.ResponseMessage;
import com.hariansyah.hotelbooking.api.models.entitymodels.requests.CompanyRequest;
import com.hariansyah.hotelbooking.api.models.entitymodels.responses.CompanyResponse;
import com.hariansyah.hotelbooking.api.models.fileupload.ImageUploadRequest;
import com.hariansyah.hotelbooking.api.repositories.AccountRepository;
import com.hariansyah.hotelbooking.api.services.CityService;
import com.hariansyah.hotelbooking.api.services.CompanyService;
import com.hariansyah.hotelbooking.api.services.FileService;
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

import static com.hariansyah.hotelbooking.api.models.validations.RoleValidation.validateRoleHotelManager;

@RequestMapping("/company")
@RestController
public class CompanyController {

    @Autowired
    private CompanyService service;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CityService cityService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private JwtToken jwtTokenUtil;

    @Autowired
    private FileService fileService;

    private void validateManager(HttpServletRequest request) {
        validateRoleHotelManager(request, jwtTokenUtil, accountRepository);
    }

    @GetMapping("/{id}")
    public ResponseMessage<CompanyResponse> findById(
            @PathVariable Integer id
    ) {
        Company entity = service.findById(id);

        CompanyResponse data = modelMapper.map(entity, CompanyResponse.class);
        return ResponseMessage.success(data);
    }

    @PostMapping
    public ResponseMessage<Boolean> add(
            @RequestBody @Valid CompanyRequest model,
            HttpServletRequest request
    ) {
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) throw new InvalidPermissionsException();
        validateManager(request);
        Company entity = modelMapper.map(model, Company.class);

        City city = cityService.findById(model.getCityId());

        entity.setCity(city);

        return ResponseMessage.success(service.save(entity));
    }

    @PutMapping("/{id}")
    public ResponseMessage<Boolean> edit(
            @PathVariable Integer id,
            @RequestBody @Valid CompanyRequest model,
            HttpServletRequest request
    ) {
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) throw new InvalidPermissionsException();
        validateManager(request);
        Company entity = service.findById(id);

        City city = cityService.findById(model.getCityId());
        entity.setCity(city);

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
        validateManager(request);
        service.findById(id);

        return ResponseMessage.success(service.removeById(id));
    }

    @GetMapping
    public ResponseMessage<List<CompanyResponse>> findAll() {
        List<Company> entities = service.findAll();
        List<CompanyResponse> data = entities.stream()
                .map(e -> modelMapper.map(e, CompanyResponse.class))
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
        validateManager(request);
        Company entity = service.findById(id);

        fileService.upload(entity, model.getFile().getInputStream());

        return ResponseMessage.success(true);
    }

    @GetMapping("/download/{id}.png")
    public void download(
            @PathVariable Integer id,
            HttpServletResponse response
    ) throws IOException {
        Company entity = service.findById(id);
        if (entity == null) {
            throw new EntityExistsException();
        }

        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "filename=\"" + entity.getId() + "\"" );
        fileService.download(entity, response.getOutputStream());
    }
}
