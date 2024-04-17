package roomescape.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.Reservation;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;

@RestController
public class ReservationController {
    private final List<Reservation> reservations;
    private final AtomicLong atomicLong = new AtomicLong(0);

    public ReservationController() {
        this(new ArrayList<>());
    }

    public ReservationController(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    @PostMapping("/reservations")
    public ReservationResponse saveReservation(@RequestBody ReservationRequest reservationRequest) {
        Reservation reservation = fromRequest(reservationRequest);
        reservations.add(reservation);
        return toResponse(reservation);
    }

    private Reservation fromRequest(ReservationRequest reservationRequest) {
        long id = atomicLong.incrementAndGet();

        String name = reservationRequest.name();
        LocalDate date = reservationRequest.date();
        LocalTime time = reservationRequest.time();
        LocalDateTime dateTime = LocalDateTime.of(date, time);
        return new Reservation(id, name, dateTime);
    }

    private ReservationResponse toResponse(Reservation reservation) {
        return new ReservationResponse(reservation.getId(),
                reservation.getName(), reservation.getDate(), reservation.getTime());
    }

    @GetMapping("/reservations")
    public List<ReservationResponse> findAllReservations() {
        return reservations.stream()
                .map(this::toResponse)
                .toList();
    }

    @DeleteMapping("/reservations/{reservationId}")
    public void delete(@PathVariable Long reservationId) {
        reservations.stream().filter(reservation -> reservation.getId() == reservationId)
                .findAny()
                .ifPresent(reservations::remove);
    }
}
