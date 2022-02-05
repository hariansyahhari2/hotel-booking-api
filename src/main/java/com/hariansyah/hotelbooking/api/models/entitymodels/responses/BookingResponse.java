package com.hariansyah.hotelbooking.api.models.entitymodels.responses;

import com.hariansyah.hotelbooking.api.enums.StatusEnum;

import java.time.LocalDate;

public class BookingResponse {

    private Integer id;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;

    private Integer personCount;

    private AccountResponse bookedBy;

    private CustomerIdentityResponse guest;

    private RoomResponse room;

    private Long numberOfNight;

    private Long roomCount;

    private Integer subTotal;

    private StatusEnum status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public Integer getPersonCount() {
        return personCount;
    }

    public void setPersonCount(Integer personCount) {
        this.personCount = personCount;
    }

    public AccountResponse getBookedBy() {
        return bookedBy;
    }

    public void setBookedBy(AccountResponse bookedBy) {
        this.bookedBy = bookedBy;
    }

    public CustomerIdentityResponse getGuest() {
        return guest;
    }

    public void setGuest(CustomerIdentityResponse guest) {
        this.guest = guest;
    }

    public RoomResponse getRoom() {
        return room;
    }

    public void setRoom(RoomResponse room) {
        this.room = room;
    }

    public Long getNumberOfNight() {
        return numberOfNight;
    }

    public void setNumberOfNight(Long numberOfNight) {
        this.numberOfNight = numberOfNight;
    }

    public Long getRoomCount() {
        return roomCount;
    }

    public void setRoomCount(Long roomCount) {
        this.roomCount = roomCount;
    }

    public Integer getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Integer subTotal) {
        this.subTotal = subTotal;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }
}
