package com.hariansyah.hotelbooking.api.models.entitymodels.responses;

public class RoomResponse {

    private Integer id;

    private String roomType;

    private String about;

    private Double price;

    private Integer numberOfRoom;

    private HotelResponse hotel;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getNumberOfRoom() {
        return numberOfRoom;
    }

    public void setNumberOfRoom(Integer numberOfRoom) {
        this.numberOfRoom = numberOfRoom;
    }

    public HotelResponse getHotel() {
        return hotel;
    }

    public void setHotel(HotelResponse hotel) {
        this.hotel = hotel;
    }
}
