package com.driver.services;


import com.driver.EntryDto.BookTicketEntryDto;
import com.driver.model.Passenger;
import com.driver.model.Ticket;
import com.driver.model.Train;
import com.driver.repository.PassengerRepository;
import com.driver.repository.TicketRepository;
import com.driver.repository.TrainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TicketService {

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    TrainRepository trainRepository;

    @Autowired
    PassengerRepository passengerRepository;


    public Integer bookTicket(BookTicketEntryDto bookTicketEntryDto)throws Exception{

        //Check for validity
        //Use bookedTickets List from the TrainRepository to get bookings done against that train
        // Incase the there are insufficient tickets
        // throw new Exception("Less tickets are available");
        //otherwise book the ticket, calculate the price and other details
        //Save the information in corresponding DB Tables
        //Fare System : Check problem statement
        //Incase the train doesn't pass through the requested stations
        //throw new Exception("Invalid stations");
        //Save the bookedTickets in the train Object
        // Also in the passenger Entity change the attribute bookedTickets by using the attribute bookingPersonId.
        //And the end return the ticketId that has come from db


        Train train = trainRepository.getOne(bookTicketEntryDto.getTrainId());

        int bookedTicket = 0;
        for(Ticket ticket : train.getBookedTickets()){
            bookedTicket += ticket.getPassengersList().size();
        }

        int leftTicket = train.getNoOfSeats() - bookedTicket;
        if(leftTicket < bookTicketEntryDto.getNoOfSeats()) throw new Exception("Less tickets are available");

        String[] route = train.getRoute().split(",");
        boolean isFromStation = false;
        boolean isToStation = false;
        for(int i=0; i<route.length; i++){
            if(route[i].equals(bookTicketEntryDto.getFromStation())) isFromStation = true;
            if(route[i].equals(bookTicketEntryDto.getToStation()))  isToStation = true;
        }

        if(!isFromStation || !isToStation) throw new Exception("Invalid stations");

        train = trainRepository.save(train);
        Ticket updatedTicket = train.getBookedTickets().get(train.getBookedTickets().size()-1);

        return updatedTicket.getTicketId();

    }
}
