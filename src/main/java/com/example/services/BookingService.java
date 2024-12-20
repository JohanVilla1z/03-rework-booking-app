package com.example.services;

import com.example.data.DataBase;
import com.example.models.Accommodation;
import com.example.models.DayPass;
import com.example.models.Stay;
import com.example.services.interfaces.IBookingService;
import com.example.services.interfaces.IMenuService;
import com.example.services.interfaces.IValidatorService;

import java.util.Scanner;

public class BookingService implements IBookingService {

    private final DataBase dataBase;
    private final IMenuService menuService;
    private final IValidatorService validatorService;

    public BookingService(DataBase dataBase, IMenuService menuService, IValidatorService validatorService) {
        this.dataBase = dataBase;
        this.menuService = menuService;
        this.validatorService = validatorService;
    }

    public void start() {
        Integer option = menuService.mainMenu();
        String selectedCity;

        do {
            switch (option) {
                case 1 -> {
                    System.out.println("Reservar alojamiento");
                    askForCity();
                    askForType();
                }
                case 2 -> System.out.println("Ver reservas");
                case 3 -> System.out.println("Modificar reserva");
            }
            option = menuService.mainMenu();
        } while (option != 4);
    }

    public void askForCity() {
        listCities();
        String selectedCity = validatorService.readString("Por favor, seleccione una de las ciudades:");
        if (!validateCity(selectedCity)) {
            System.out.println("La ciudad seleccionada no es válida. Intente de nuevo.");
            return;
        }
    }

    public void askForType() {
        String this.selectedCity;
        String selectedType = validatorService.readString("¿Qué tipo de reserva desea hacer? (1.Alojamiento/ 2.Día de sol)");
        listStaysByCity(selectedCity);
        String selectedAccommodation = validatorService.readString("Ingrese el nombre del alojamiento:");

        int numberOfAdults = validatorService.readInt("Ingrese cantidad de adultos que reservarán:");
        int numberOfChildren = validatorService.readInt("Ingrese cantidad de niños que reservarán:");
        String confirmation = validatorService.readString("¿Desea confirmar la reserva? (Sí/No)");

        if (confirmation.equalsIgnoreCase("Si")) {
            System.out.println("Reserva realizada con éxito para " + selectedAccommodation + " en " + selectedCity + ".");
        } else {
            System.out.println("Reserva cancelada.");
        }
    }

    public void listCities() {
        for (String city : dataBase.getCities()) {
            System.out.print(city + " | ");
        }
        System.out.println();
    }

    public void listStaysByCity(String city) {
        System.out.println("Alojamientos disponibles en " + city + ":");
        for (Stay stay : dataBase.getStays()) {
            if (stay.getCity().equalsIgnoreCase(city)) {
                System.out.println("  * " + stay.getName() + " - desde $" + stay.getBasePrice() + " USD por noche");
            }
        }
    }

    public void listStayByTye(String type, String city) {
        System.out.println("Alojamientos disponibles en " + city + ":");
        for (Stay stay : dataBase.getStays()) {
            stay.describe(); //TODO:Metodo descriptivo
        }
    }

    private boolean validateCity(String city) {
        String cityUpper = city.toUpperCase();
        return dataBase.getCities().contains(cityUpper);
    }

    private boolean validateAccommodation(String accommodationName, String city) {
        return dataBase.getAccommodations().stream()
                .anyMatch(a -> a.getName().equalsIgnoreCase(accommodationName) && a.getCity().equalsIgnoreCase(city));
    }
}
