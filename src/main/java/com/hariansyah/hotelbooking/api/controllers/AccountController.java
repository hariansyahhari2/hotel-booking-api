package com.hariansyah.hotelbooking.api.controllers;

import com.hariansyah.hotelbooking.api.configs.jwt.JwtToken;
import com.hariansyah.hotelbooking.api.entities.Account;
import com.hariansyah.hotelbooking.api.entities.CustomerIdentity;
import com.hariansyah.hotelbooking.api.exceptions.EntityNotFoundException;
import com.hariansyah.hotelbooking.api.exceptions.InvalidPermissionsException;
import com.hariansyah.hotelbooking.api.models.ResponseMessage;
import com.hariansyah.hotelbooking.api.models.entitymodels.requests.AccountRequest;
import com.hariansyah.hotelbooking.api.models.entitymodels.requests.CustomerIdentityWithAccountRequest;
import com.hariansyah.hotelbooking.api.models.entitymodels.responses.AccountResponse;
import com.hariansyah.hotelbooking.api.models.entitymodels.responses.CustomerIdentityResponse;
import com.hariansyah.hotelbooking.api.repositories.AccountRepository;
import com.hariansyah.hotelbooking.api.services.CustomerIdentityService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.ValidationException;
import java.security.NoSuchAlgorithmException;

import static com.hariansyah.hotelbooking.api.enums.RoleEnum.*;

@RequestMapping("/account")
@RestController
public class AccountController {

    @Autowired
    private AccountRepository repository;

    @Autowired
    private CustomerIdentityService service;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private JwtToken jwtTokenUtil;

    @PostMapping("/register")
    public ResponseMessage<CustomerIdentityResponse> addWithAccount(
            @RequestBody @Valid CustomerIdentityWithAccountRequest model
    ) {
        CustomerIdentity entity = modelMapper.map(model, CustomerIdentity.class);

        if (repository.existsByUsername(model.getAccount().getUsername())) {
            throw new ValidationException("Username already existed");
        }

        Account account = entity.getAccount();
        String hashPassword = new BCryptPasswordEncoder().encode(model.getAccount().getPassword());
        account.setPassword(hashPassword);
        account.setRole(GUEST);

        repository.save(account);
        entity.setAccount(account);
        entity = service.save(entity);

        CustomerIdentityResponse data = modelMapper.map(entity, CustomerIdentityResponse.class);
        return ResponseMessage.success(data);
    }

    @GetMapping("/{username}/make-guest")
    public ResponseMessage<AccountResponse> makeGuest(
            @PathVariable String username,
            HttpServletRequest request
    ) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            Account account = repository.findByUsername(username);
            token = token.substring(7);
            String loggedInUsername = jwtTokenUtil.getUsernameFromToken(token);
            Account loggedInAccount = repository.findByUsername(loggedInUsername);

            if (loggedInAccount.getUsername().equals(account.getUsername()) || loggedInAccount.getRole().equals(GUEST) || loggedInAccount.getRole().equals(HOTEL_EMPLOYEE))
                throw new InvalidPermissionsException();

            if (loggedInAccount.getRole().equals(account.getRole()) || (loggedInAccount.getRole().equals(HOTEL_MANAGER) && account.getRole().equals(ADMIN)))
                throw new InvalidPermissionsException();

            account.setRole(GUEST);
            repository.save(account);

            AccountResponse data = modelMapper.map(account, AccountResponse.class);
            return ResponseMessage.success(data);
        }
        throw new InvalidPermissionsException();
    }

    @GetMapping("/{username}/make-hotel-employee")
    public ResponseMessage<AccountResponse> makeHotelEmployee(
            @PathVariable String username,
            HttpServletRequest request
    ) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            Account account = repository.findByUsername(username);
            if (account == null) throw new EntityNotFoundException();

            token = token.substring(7);
            String loggedInUsername = jwtTokenUtil.getUsernameFromToken(token);
            Account loggedInAccount = repository.findByUsername(loggedInUsername);

            if (loggedInAccount.getRole().equals(HOTEL_EMPLOYEE) || loggedInAccount.getRole().equals(GUEST))
                throw new InvalidPermissionsException();
            account.setRole(HOTEL_EMPLOYEE);
            repository.save(account);

            AccountResponse data = modelMapper.map(account, AccountResponse.class);
            return ResponseMessage.success(data);
        }
        throw new InvalidPermissionsException();
    }

    @GetMapping("/{username}/make-hotel-manager")
    public ResponseMessage<AccountResponse> makeHotelManager(
            @PathVariable String username,
            HttpServletRequest request
    ) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            Account account = repository.findByUsername(username);
            token = token.substring(7);
            String loggedInUsername = jwtTokenUtil.getUsernameFromToken(token);
            Account loggedInAccount = repository.findByUsername(loggedInUsername);

            if (!loggedInAccount.getRole().equals(ADMIN)) throw new InvalidPermissionsException();
            account.setRole(HOTEL_MANAGER);
            repository.save(account);

            AccountResponse data = modelMapper.map(account, AccountResponse.class);
            return ResponseMessage.success(data);
        }
        throw new InvalidPermissionsException();
    }

    @GetMapping("/{username}/make-admin")
    public ResponseMessage<AccountResponse> makeAdmin(
            @PathVariable String username,
            HttpServletRequest request
    ) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            Account account = repository.findByUsername(username);
            token = token.substring(7);
            String loggedInUsername = jwtTokenUtil.getUsernameFromToken(token);
            Account loggedInAccount = repository.findByUsername(loggedInUsername);

            if (!loggedInAccount.getRole().equals(ADMIN)) {
                throw new InvalidPermissionsException();
            }
            account.setRole(ADMIN);
            repository.save(account);

            AccountResponse data = modelMapper.map(account, AccountResponse.class);
            return ResponseMessage.success(data);
        }
        throw new InvalidPermissionsException();
    }

    @PutMapping("/edit-account")
    public Boolean edit(@RequestBody AccountRequest model) throws NoSuchAlgorithmException {
        Account account = repository.findByUsername(model.getUsername());
        String password = model.getPassword();
        String encodedPassword = new BCryptPasswordEncoder().encode(password);

        account.setPassword(encodedPassword);
        repository.save(account);
        return true;
    }

    @GetMapping("/me")
    public ResponseMessage<AccountResponse> findById(
            HttpServletRequest request
    ) {
        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);

            String username = jwtTokenUtil.getUsernameFromToken(token);

            Account account = repository.findByUsername(username);

            if(account != null) {
                AccountResponse data = modelMapper.map(account, AccountResponse.class);
                return ResponseMessage.success(data);
            }
        }
        throw new EntityNotFoundException();
    }
}
