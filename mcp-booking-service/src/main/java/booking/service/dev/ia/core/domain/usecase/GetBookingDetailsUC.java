package booking.service.dev.ia.core.domain.usecase;

import booking.service.dev.ia.core.domain.entity.Booking;
import booking.service.dev.ia.core.gateway.BookingRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;

@ApplicationScoped
public class GetBookingDetailsUC {

    @Inject
    BookingRepository bookingRepository;

    public Optional<Booking> execute(Long bookingId) {
        return Optional.ofNullable(this.bookingRepository.findById(bookingId));
    }

}
