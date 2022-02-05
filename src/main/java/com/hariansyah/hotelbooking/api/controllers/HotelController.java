package com.hariansyah.hotelbooking.api.controllers;

import com.hariansyah.hotelbooking.api.configs.jwt.JwtToken;
import com.hariansyah.hotelbooking.api.entities.City;
import com.hariansyah.hotelbooking.api.entities.Company;
import com.hariansyah.hotelbooking.api.entities.Hotel;
import com.hariansyah.hotelbooking.api.exceptions.InvalidPermissionsException;
import com.hariansyah.hotelbooking.api.models.ResponseMessage;
import com.hariansyah.hotelbooking.api.models.entitymodels.requests.HotelRequest;
import com.hariansyah.hotelbooking.api.models.entitymodels.responses.HotelResponse;
import com.hariansyah.hotelbooking.api.models.fileupload.ImageUploadRequest;
import com.hariansyah.hotelbooking.api.repositories.AccountRepository;
import com.hariansyah.hotelbooking.api.services.CityService;
import com.hariansyah.hotelbooking.api.services.CompanyService;
import com.hariansyah.hotelbooking.api.services.FileService;
import com.hariansyah.hotelbooking.api.services.HotelService;
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

@RequestMapping("/hotel")
@RestController
public class HotelController {

    @Autowired
    private HotelService service;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CompanyService companyService;

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
    public ResponseMessage<HotelResponse> findById(
            @PathVariable Integer id
    ) {
        Hotel entity = service.findById(id);

        HotelResponse data = modelMapper.map(entity, HotelResponse.class);
        return ResponseMessage.success(data);
    }

    @PostMapping
    public ResponseMessage<Boolean> add(
            @RequestBody @Valid HotelRequest model,
            HttpServletRequest request
    ) {
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) throw new InvalidPermissionsException();
        validateManager(request);

        Hotel entity = modelMapper.map(model, Hotel.class);

        Company company = companyService.findById(model.getCompanyId());
        City city = cityService.findById(model.getCityId());

        entity.setCompany(company);
        entity.setCity(city);

        return ResponseMessage.success(service.save(entity));
    }

    @PutMapping("/{id}")
    public ResponseMessage<Boolean> edit(
            @PathVariable Integer id,
            @RequestBody @Valid HotelRequest model,
            HttpServletRequest request
    ) {
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) throw new InvalidPermissionsException();
        validateManager(request);
        Hotel entity = service.findById(id);

        Company company = companyService.findById(model.getCompanyId());
        City city = cityService.findById(model.getCityId());

        entity.setCompany(company);
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
    public ResponseMessage<List<HotelResponse>> findAll() {
        List<Hotel> entities = service.findAll();
        List<HotelResponse> data = entities.stream()
                .map(e -> modelMapper.map(e, HotelResponse.class))
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
        Hotel entity = service.findById(id);

        fileService.upload(entity, model.getFile().getInputStream());

        return ResponseMessage.success(true);
    }

    @GetMapping("/download/{id}.png")
    public void download(
            @PathVariable Integer id,
            HttpServletResponse response
    ) throws IOException {
        Hotel entity = service.findById(id);
        if (entity == null) {
            throw new EntityExistsException();
        }

        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "filename=\"" + entity.getId() + "\"" );
        fileService.download(entity, response.getOutputStream());
    }
}
