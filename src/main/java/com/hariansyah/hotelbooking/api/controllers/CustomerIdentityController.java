package com.hariansyah.hotelbooking.api.controllers;

import com.hariansyah.hotelbooking.api.configs.jwt.JwtToken;
import com.hariansyah.hotelbooking.api.entities.Account;
import com.hariansyah.hotelbooking.api.entities.CustomerIdentity;
import com.hariansyah.hotelbooking.api.exceptions.InvalidPermissionsException;
import com.hariansyah.hotelbooking.api.models.ResponseMessage;
import com.hariansyah.hotelbooking.api.models.entitymodels.requests.CustomerIdentityRequest;
import com.hariansyah.hotelbooking.api.models.entitymodels.responses.CustomerIdentityResponse;
import com.hariansyah.hotelbooking.api.repositories.AccountRepository;
import com.hariansyah.hotelbooking.api.services.CustomerIdentityService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/identity")
@RestController
public class CustomerIdentityController {

    @Autowired
    private CustomerIdentityService service;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private JwtToken jwtTokenUtil;

    @GetMapping("/{id}")
    public ResponseMessage<CustomerIdentityResponse> findById(
            @PathVariable Integer id, HttpServletRequest request
    ) {
        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            String username = jwtTokenUtil.getUsernameFromToken(token);
            Account account = accountRepository.findByUsername(username);
            CustomerIdentity entity = service.findById(id);
            if(entity != null) {
                if (entity.getAccount().getId().equals(account.getId())) {
                    CustomerIdentityResponse data = modelMapper.map(entity, CustomerIdentityResponse.class);
                    return ResponseMessage.success(data);
                }
            }
        }
        throw new InvalidPermissionsException();
    }

    @PostMapping("/add")
    public ResponseMessage<CustomerIdentityResponse> add(
            @RequestBody @Valid CustomerIdentityRequest model,
            HttpServletRequest request
    ) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            String username = jwtTokenUtil.getUsernameFromToken(token);
            Account account = accountRepository.findByUsername(username);

            CustomerIdentity entity = modelMapper.map(model, CustomerIdentity.class);
            entity.setAccount(account);
            entity.setDeleted(false);
            entity = service.save(entity);

            CustomerIdentityResponse data = modelMapper.map(entity, CustomerIdentityResponse.class);
            return ResponseMessage.success(data);
        }
        throw new InvalidPermissionsException();
    }

    @PutMapping("{id}")
    public ResponseMessage<CustomerIdentityResponse> edit(
            @PathVariable Integer id,
            @RequestBody @Valid CustomerIdentityRequest model,
            HttpServletRequest request
    ) {
        CustomerIdentity entity = service.findById(id);

        String token = request.getHeader("Authorization");
        if (entity != null && token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            String username = jwtTokenUtil.getUsernameFromToken(token);
            Account account = accountRepository.findByUsername(username);
            if (entity.getAccount().getId().equals(account.getId())) {
                modelMapper.map(model, entity);
                entity = service.save(entity);

                CustomerIdentityResponse data = modelMapper.map(entity, CustomerIdentityResponse.class);
                return ResponseMessage.success(data);
            }
        }
        throw new InvalidPermissionsException();
    }

    @GetMapping
    public ResponseMessage<List<CustomerIdentityResponse>> findAll(
            HttpServletRequest request
    ) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            String username = jwtTokenUtil.getUsernameFromToken(token);
            Account account = accountRepository.findByUsername(username);

            List<CustomerIdentity> entities = service.findAll().stream().filter(value -> value.getAccount().getId().equals(account.getId()))
                    .collect(Collectors.toList());

            List<CustomerIdentityResponse> data = entities.stream().map(e -> modelMapper.map(e, CustomerIdentityResponse.class))
                    .collect(Collectors.toList());

            return ResponseMessage.success(data);
        }
        throw new InvalidPermissionsException();
    }
}
