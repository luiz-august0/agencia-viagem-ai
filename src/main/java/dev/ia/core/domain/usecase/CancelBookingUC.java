package dev.ia.core.domain.usecase;

import dev.ia.core.domain.entity.Booking;
import dev.ia.core.domain.enums.BookingStatus;
import dev.ia.core.gateway.BookingRepository;
import dev.ia.infra.context.SecurityContext;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;

@ApplicationScoped
public class CancelBookingUC {

    @Inject
    BookingRepository bookingRepository;

    public Optional<Booking> execute(long bookingId) {
        String currentUser = SecurityContext.getCurrentUser();
        if (this.bookingRepository.hasBooking(bookingId)) {
            Booking booking = this.bookingRepository.findById(bookingId);
            // Validando o usuário "logado", e não apenas o informado
            if (booking.customerName().equals(currentUser)) {
                Booking cancelledBooking = new Booking(
                        booking.id(),
                        booking.customerName(),
                        booking.destination(),
                        booking.startDate(),
                        booking.endDate(),
                        BookingStatus.CANCELLED,
                        booking.category()
                );
                this.bookingRepository.update(bookingId, cancelledBooking);
                return Optional.of(cancelledBooking);
            }
        }
        return Optional.empty();
    }

}
