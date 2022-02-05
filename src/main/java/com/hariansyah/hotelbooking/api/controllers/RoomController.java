package com.hariansyah.hotelbooking.api.controllers;

import com.hariansyah.hotelbooking.api.configs.jwt.JwtToken;
import com.hariansyah.hotelbooking.api.entities.Hotel;
import com.hariansyah.hotelbooking.api.entities.Room;
import com.hariansyah.hotelbooking.api.exceptions.EntityNotFoundException;
import com.hariansyah.hotelbooking.api.exceptions.InvalidPermissionsException;
import com.hariansyah.hotelbooking.api.models.ResponseMessage;
import com.hariansyah.hotelbooking.api.models.entitymodels.requests.RoomRequest;
import com.hariansyah.hotelbooking.api.models.entitymodels.responses.RoomResponse;
import com.hariansyah.hotelbooking.api.models.fileupload.ImageUploadRequest;
import com.hariansyah.hotelbooking.api.repositories.AccountRepository;
import com.hariansyah.hotelbooking.api.services.FileService;
import com.hariansyah.hotelbooking.api.services.HotelService;
import com.hariansyah.hotelbooking.api.services.RoomService;
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

@RequestMapping("/room")
@RestController
public class RoomController {

    @Autowired
    private RoomService service;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private HotelService hotelService;

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
    public ResponseMessage<RoomResponse> findById(
            @PathVariable Integer id
    ) {
        Room entity = service.findById(id);
        if(entity != null) {
            RoomResponse data = modelMapper.map(entity, RoomResponse.class);
            return ResponseMessage.success(data);
        }
        throw new EntityNotFoundException();
    }

    @PostMapping
    public ResponseMessage<Boolean> add(
            @RequestBody @Valid RoomRequest model,
            HttpServletRequest request
    ) {
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) throw new InvalidPermissionsException();
        validateManager(request);

        Room entity = modelMapper.map(model, Room.class);

        Hotel hotel = hotelService.findById(model.getHotelId());
        entity.setHotel(hotel);

        return ResponseMessage.success(service.save(entity));
    }

    @PutMapping("/{id}")
    public ResponseMessage<Boolean> edit(
            @PathVariable Integer id,
            @RequestBody @Valid RoomRequest model,
            HttpServletRequest request
    ) {
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) throw new InvalidPermissionsException();
        validateManager(request);

        Room entity = service.findById(id);

        Hotel hotel = hotelService.findById(model.getHotelId());
        entity.setHotel(hotel);

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
    public ResponseMessage<List<RoomResponse>> findAll() {
        List<Room> entities = service.findAll();
        List<RoomResponse> data = entities.stream()
                .map(e -> modelMapper.map(e, RoomResponse.class))
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

        Room entity = service.findById(id);

        fileService.upload(entity, model.getFile().getInputStream());

        return ResponseMessage.success(true);
    }

    @GetMapping("/download/{id}.png")
    public void download(
            @PathVariable Integer id,
            HttpServletResponse response
    ) throws IOException {
        Room entity = service.findById(id);
        if (entity == null) {
            throw new EntityExistsException();
        }

        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "filename=\"" + entity.getId() + "\"" );
        fileService.download(entity, response.getOutputStream());
    }
}
