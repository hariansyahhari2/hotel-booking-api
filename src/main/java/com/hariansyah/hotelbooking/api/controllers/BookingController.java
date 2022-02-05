package com.hariansyah.hotelbooking.api.controllers;

import com.hariansyah.hotelbooking.api.configs.jwt.JwtToken;
import com.hariansyah.hotelbooking.api.entities.Account;
import com.hariansyah.hotelbooking.api.entities.Booking;
import com.hariansyah.hotelbooking.api.entities.CustomerIdentity;
import com.hariansyah.hotelbooking.api.entities.Room;
import com.hariansyah.hotelbooking.api.exceptions.*;
import com.hariansyah.hotelbooking.api.models.ResponseMessage;
import com.hariansyah.hotelbooking.api.models.entitymodels.requests.BookingRequest;
import com.hariansyah.hotelbooking.api.models.entitymodels.requests.DateRequest;
import com.hariansyah.hotelbooking.api.models.entitymodels.responses.BookingResponse;
import com.hariansyah.hotelbooking.api.repositories.AccountRepository;
import com.hariansyah.hotelbooking.api.services.BookingService;
import com.hariansyah.hotelbooking.api.services.CustomerIdentityService;
import com.hariansyah.hotelbooking.api.services.RoomService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.hariansyah.hotelbooking.api.enums.RoleEnum.*;
import static com.hariansyah.hotelbooking.api.enums.StatusEnum.*;
import static com.hariansyah.hotelbooking.api.models.validations.RoleValidation.validateRoleManagerOREmployee;
import static java.time.temporal.ChronoUnit.DAYS;

@RequestMapping("/booking")
@RestController
public class BookingController {

    @Autowired
    private BookingService service;

    @Autowired
    private RoomService roomService;

    @Autowired
    private CustomerIdentityService customerIdentityService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private JwtToken jwtTokenUtil;

    @Autowired
    private ModelMapper modelMapper;

    private void validateManagerOrEmployee(HttpServletRequest request) {
        validateRoleManagerOREmployee(request, jwtTokenUtil, accountRepository);
    }

    @GetMapping("/{id}")
    public ResponseMessage<BookingResponse> findById(
            @PathVariable Integer id
    ) {
        Booking entity = service.findById(id);
        if(entity != null) {
            BookingResponse data = modelMapper.map(entity, BookingResponse.class);
            return ResponseMessage.success(data);
        }
        throw new EntityNotFoundException();
    }

    @PostMapping("book")
    public ResponseMessage<Boolean> book(
            @RequestBody @Valid BookingRequest model,
            HttpServletRequest request
    ) {
        Long numberOfNight = DAYS.between(model.getCheckInDate(), model.getCheckOutDate());
        if (numberOfNight <= 0) {
            throw new DateInvalidException();
        }
        Room room = roomService.findById(model.getRoomId());

        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            String username = jwtTokenUtil.getUsernameFromToken(token);
            Account bookedBy = accountRepository.findByUsername(username);
            CustomerIdentity guest = customerIdentityService.findById(model.getGuestId());

            if (!guest.getAccount().getUsername().equals(username)) throw new InvalidPermissionsException();

            Booking entity = modelMapper.map(model, Booking.class);
            LocalDate checkIn = model.getCheckInDate();
            LocalDate checkOut = model.getCheckOutDate();
            entity.setCheckInDate(checkIn);
            entity.setCheckOutDate(checkOut);
            entity.setBookedBy(bookedBy);
            entity.setGuest(guest);
            entity.setNumberOfNight(numberOfNight);

            Long checkRoom = service.findNumberOfBooked(checkIn, checkOut, room.getId());

            if ((checkRoom + model.getRoomCount()) <= room.getNumberOfRoom()) {
                Double subTotal = entity.getRoomCount() * room.getPrice() * numberOfNight;

                entity.setRoom(room);
                entity.setSubTotal(subTotal);
                entity.setStatus(CONFIRMED);

                return ResponseMessage.success(service.save(entity));
            }
        }
        throw new RoomNotAvailableException();
    }

    @GetMapping("/{id}/cancel")
    public ResponseMessage<Boolean> cancel(
            @PathVariable Integer id,
            HttpServletRequest request
    ) {
        Booking entity = service.findById(id);

        if (entity.getStatus().equals(CANCELLED)) throw new StatusCancelledException();

        if (entity.getStatus().equals(CHECKED_IN)) throw new StatusCheckedInException();

        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            String username = jwtTokenUtil.getUsernameFromToken(token);
            Account account = accountRepository.findByUsername(username);
            if (!entity.getBookedBy().getUsername().equals(username) || account.getRole().equals(HOTEL_EMPLOYEE) || account.getRole().equals(HOTEL_MANAGER)) {
                throw new InvalidPermissionsException();
            }
            entity.setStatus(CANCELLED);
            return ResponseMessage.success(service.edit(entity));
        }
        throw new InvalidPermissionsException();
    }

    @GetMapping("/{id}/check-in")
    public ResponseMessage<Boolean> checkIn(
            @PathVariable Integer id,
            HttpServletRequest request
    ) {
        Booking entity = service.findById(id);

        if (entity.getStatus().equals(CANCELLED)) throw new StatusCancelledException();
        if (entity.getStatus().equals(CHECKED_IN)) throw new StatusCheckedInException();

        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            String username = jwtTokenUtil.getUsernameFromToken(token);
            Account account = accountRepository.findByUsername(username);
            if (!entity.getBookedBy().getUsername().equals(username) || account.getRole().equals(HOTEL_EMPLOYEE) || account.getRole().equals(HOTEL_MANAGER))
                throw new InvalidPermissionsException();

            entity.setStatus(CHECKED_IN);
            return ResponseMessage.success(service.edit(entity));
        }
        throw new InvalidPermissionsException();
    }

    @GetMapping
    public ResponseMessage<List<BookingResponse>> findAll(
            HttpServletRequest request
    ) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            String username = jwtTokenUtil.getUsernameFromToken(token);
            Account account = accountRepository.findByUsername(username);

            List<Booking> entities = service.findAll();
            if (account.getRole().equals(GUEST)) {
                entities = entities.stream().filter(entity -> entity.getBookedBy().getUsername().equals(username))
                        .collect(Collectors.toList());
            }

            List<BookingResponse> data = entities.stream()
                    .map(e -> modelMapper.map(e, BookingResponse.class))
                    .collect(Collectors.toList());
            return ResponseMessage.success(data);
        }
        throw new InvalidPermissionsException();
    }

    @GetMapping("/hotel/{hotelId}")
    public ResponseMessage<List<BookingResponse>> findAllBookingByHotelWithTimeRange(
            @PathVariable Integer hotelId,
            @RequestBody @Valid DateRequest model,
            HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) throw new InvalidPermissionsException();
        validateManagerOrEmployee(request);

        List<Booking> entities = service.findAllBookingByHotelWithTimeRange(model.getStartingDate(), model.getTargetDate(), hotelId);
        List<BookingResponse> data = entities.stream()
                .map(e -> modelMapper.map(e, BookingResponse.class))
                .collect(Collectors.toList());
        return ResponseMessage.success(data);
    }

    @GetMapping("/room/{roomId}")
    public ResponseMessage<List<BookingResponse>> findAllBookingByRoomWithTimeRange(
            @PathVariable Integer roomId,
            @RequestBody @Valid DateRequest model,
            HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) throw new InvalidPermissionsException();
        validateManagerOrEmployee(request);

        List<Booking> entities = service.findAllBookingByRoomWithTimeRange(model.getStartingDate(), model.getTargetDate(), roomId);
        List<BookingResponse> data = entities.stream()
                .map(e -> modelMapper.map(e, BookingResponse.class))
                .collect(Collectors.toList());
        return ResponseMessage.success(data);
    }

    @GetMapping("/hotel/{hotelId}/all-time")
    public ResponseMessage<List<BookingResponse>> findAllBookingByHotelAllTime(
            @PathVariable Integer hotelId,
            HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) throw new InvalidPermissionsException();
        validateManagerOrEmployee(request);

        List<Booking> entities = service.findAllBookingByHotelAllTime(hotelId);
        List<BookingResponse> data = entities.stream()
                .map(e -> modelMapper.map(e, BookingResponse.class))
                .collect(Collectors.toList());
        return ResponseMessage.success(data);
    }

    @GetMapping("/room/{roomId}/all-time")
    public ResponseMessage<List<BookingResponse>> findAllBookingByRoomAllTime(
            @PathVariable Integer roomId,
            HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) throw new InvalidPermissionsException();
        validateManagerOrEmployee(request);

        List<Booking> entities = service.findAllBookingByRoomAllTime(roomId);
        List<BookingResponse> data = entities.stream()
                .map(e -> modelMapper.map(e, BookingResponse.class))
                .collect(Collectors.toList());
        return ResponseMessage.success(data);
    }
}
