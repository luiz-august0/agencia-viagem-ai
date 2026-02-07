package dev.ia.infra.repository;

import dev.ia.core.domain.entity.Booking;
import dev.ia.core.domain.enums.BookingStatus;
import dev.ia.core.domain.enums.Category;
import dev.ia.core.gateway.BookingRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class BookingRepositoryMemory implements BookingRepository {
    private final Map<Long, Booking> bookings = new HashMap<>();

    public BookingRepositoryMemory() {
        bookings.put(12345L, new Booking(12345L, "John Doe", "Tesouros do Egito",
                LocalDate.now().plusMonths(2), LocalDate.now().plusMonths(2).plusDays(10), BookingStatus.CONFIRMED, Category.TREASURES));
        bookings.put(67890L, new Booking(67890L, "Jane Smith", "Aventura Amazônia",
                LocalDate.now().plusMonths(3), LocalDate.now().plusMonths(3).plusDays(7), BookingStatus.CONFIRMED, Category.ADVENTURE));
        bookings.put(98765L, new Booking(98765L, "Peter Jones", "Trilha Inca",
                LocalDate.now().plusMonths(4), LocalDate.now().plusMonths(4).plusDays(8), BookingStatus.CONFIRMED, Category.ADVENTURE));
    }

    @Override
    public Booking findById(Long bookingId) {
        return this.bookings.get(bookingId);
    }

    @Override
    public Booking update(Long bookingId, Booking booking) {
        return this.bookings.put(bookingId, booking);
    }

    @Override
    public boolean hasBooking(Long bookingId) {
        return this.bookings.containsKey(bookingId);
    }

    @Override
    public List<Booking> findAllByCategory(Category category) {
        return bookings.values().stream()
                .filter(booking -> category.equals(booking.category()))
                .toList();
    }

}
