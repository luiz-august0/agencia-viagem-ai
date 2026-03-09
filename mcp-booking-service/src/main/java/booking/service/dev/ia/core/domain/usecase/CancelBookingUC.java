package booking.service.dev.ia.core.domain.usecase;

import booking.service.dev.ia.core.domain.entity.Booking;
import booking.service.dev.ia.core.domain.enums.BookingStatus;
import booking.service.dev.ia.core.gateway.BookingRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;

@ApplicationScoped
public class CancelBookingUC {

    @Inject
    BookingRepository bookingRepository;

    public Optional<Booking> execute(long bookingId, String name) {
        if (this.bookingRepository.hasBooking(bookingId)) {
            Booking booking = this.bookingRepository.findById(bookingId);
            // Validando o usuário "logado", e não apenas o informado
            if (booking.customerName().equals(name)) {
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
