package dev.ia.core.domain.usecase;

import dev.ia.core.domain.entity.Booking;
import dev.ia.core.domain.enums.Category;
import dev.ia.core.gateway.BookingRepository;
import dev.ia.infra.context.SecurityContext;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class GetBookingsByCategoryUC {

    @Inject
    BookingRepository bookingRepository;

    public List<Booking> execute(Category category) {
        List<Booking> bookings = this.bookingRepository.findAllByCategory(category);
        if (bookings.stream().anyMatch((booking -> booking.customerName().equals(SecurityContext.getCurrentUser())))) {
            return bookings;
        }
        return List.of();
    }

}
