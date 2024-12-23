package com.example.services;

import com.example.repositories.AccommodationRepository;
import com.example.models.Stay;
import com.example.repositories.BookingRepository;
import com.example.services.booking.MakeBooking;
import com.example.services.interfaces.IBookingService;
import com.example.services.interfaces.IMenuService;
import com.example.utils.ConsoleUtils;

public class BookingService implements IBookingService {

    private final AccommodationRepository accommodationRepository;
    private final BookingRepository bookingRepository;
    private final IMenuService menuService;
    private final ConsoleUtils validatorService;

    public BookingService(AccommodationRepository accommodationRepository, BookingRepository bookingRepository, IMenuService menuService, ConsoleUtils validatorService) {
        this.accommodationRepository = accommodationRepository;
        this.bookingRepository = bookingRepository;
        this.menuService = menuService;
        this.validatorService = validatorService;
    }

    @Override
    public void start() {
        Integer option = menuService.mainMenu();
        do {
            switch (option) {
                case 1 -> {
                    System.out.println("Reservar alojamiento");
                    MakeBooking makeBooking = new MakeBooking(accommodationRepository, bookingRepository, validatorService);
                    makeBooking.execute();
                }
                case 2 -> System.out.println("Ver reservas");
                case 3 -> System.out.println("Modificar reserva");
            }
            option = menuService.mainMenu();
        } while (option != 4);
    }

    private boolean validateCity(String city) {
        String cityUpper = city.toUpperCase();
        return accommodationRepository.getCities().contains(cityUpper);
    }

    private boolean validateAccommodation(String accommodationName, String city) {
        return accommodationRepository.getAccommodations().stream()
                .anyMatch(a -> a.getName().equalsIgnoreCase(accommodationName) && a.getCity().equalsIgnoreCase(city));
    }
}
