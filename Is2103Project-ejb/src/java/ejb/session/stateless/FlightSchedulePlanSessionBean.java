package ejb.session.stateless;

import entity.AircraftConfiguration;
import entity.CabinConfiguration;
import entity.CabinFee;
import entity.Flight;
import entity.FlightSchedule;
import entity.FlightSchedulePlan;
import entity.ReservedSeat;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class FlightSchedulePlanSessionBean implements FlightSchedulePlanSessionBeanRemote {

    @PersistenceContext(unitName = "Is2103Project-ejbPU")
    private EntityManager em;
    
    @Override
    public Long createWeeklyFlightSchedulePlan(Long flightId, 
            Long returnFlightId, 
            DayOfWeek dayOfWeek, 
            String timeInput, 
            String startDateInput, 
            String endDateInput, 
            String durationInput,
            String layoverInput,
            List<Long> cabinFees) {
        
        Flight flight = em.find(Flight.class, flightId);
        Flight returnFlight = (returnFlightId == 0) ? null : em.find(Flight.class, returnFlightId);
        
        int totalSeats = 0;
        List<CabinFee> list = new ArrayList<>();
        for (Long cfId : cabinFees) {
            CabinFee cf = em.find(CabinFee.class, cfId);
            totalSeats += cf.getCabinConfiguration().getCabinCapacity();
            list.add(cf);
        }
        
        FlightSchedulePlan fsp = new FlightSchedulePlan(flight.getFlightNum(), list, "Weekly Type");
        em.persist(fsp);
        
        FlightSchedulePlan rfsp = null; 
        if (!layoverInput.equals("-1") && !layoverInput.equals("-1")) {
           rfsp = new FlightSchedulePlan(returnFlight.getFlightNum(), list, "Weekly Type"); 
           em.persist(rfsp);
           rfsp.setIsReturnFlight(true);
           fsp.setReturnFlightSchedulePlan(rfsp);
           rfsp.setReturnFlightSchedulePlan(fsp);
        }
        em.flush();

        //time
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");
        LocalTime time = LocalTime.parse(timeInput, timeFormatter);
        
        //start date
        String[] start = startDateInput.split("/");
        int day = Integer.parseInt(start[0]);
        int month = Integer.parseInt(start[1]);
        int year = Integer.parseInt(start[2]) + 2000;
        LocalDateTime startTime = LocalDateTime.of(year, Month.of(month), day, 0, 0);
        
        //end date
        String[] end = endDateInput.split("/");
        day = Integer.parseInt(end[0]);
        month = Integer.parseInt(end[1]);
        year = Integer.parseInt(end[2]) + 2000;
        LocalDateTime endTime = LocalDateTime.of(year, Month.of(month), day, 0, 0);
        
        //Duration
        String[] dur = durationInput.split(" ");
        int hours = Integer.parseInt(dur[0]);
        int minutes = Integer.parseInt(dur[1]);
        Duration duration = Duration.ofHours(hours).plusMinutes(minutes);
        
        LocalDateTime currentDateTime = startTime;
        
        System.out.println("done2");
        while (!currentDateTime.isAfter(endTime)) {
            System.out.println(currentDateTime);
            if (currentDateTime.getDayOfWeek() == dayOfWeek) {  
                LocalDateTime dateTime = currentDateTime.withHour(time.getHour()).withMinute(time.getMinute());
                FlightSchedule fs = new FlightSchedule(dateTime, duration, fsp, flight);
                em.persist(fs);
                fsp.getFlightSchedules().add(fs);
                
            if (!layoverInput.equals("-1") && !layoverInput.equals("-1")) {
                dateTime = dateTime.plusHours(Long.parseLong(layoverInput));
                fs = new FlightSchedule(dateTime, duration, rfsp, returnFlight);
                em.persist(fs);
                fs.setSeats(totalSeats);
                rfsp.getFlightSchedules().add(fs);
            }
            }
            currentDateTime = currentDateTime.plusDays(1);
            System.out.println(currentDateTime);
        }
        
        System.out.println("all done");
        return fsp.getFlightSchedulePlanId();
    }
    
    @Override
    public Long createCabinFee(Long id, String name, int fee) {
        CabinConfiguration cc = em.find(CabinConfiguration.class, id);
        CabinFee cf = new CabinFee(name, fee);
        em.persist(cf);
        cf.setCabinConfiguration(cc);
        em.flush();
        return cf.getCabinFeeId();
    }

    @Override
    public Long createMultipleFlightSchedulePlan(
            Long flightId, 
            Long returnFlightId, 
            List<LocalDateTime> dateTimes, 
            List<Duration> durations,
            String layoverInput,
            List<Long> cabinFees) {
        
        Flight flight = em.find(Flight.class, flightId);
        Flight returFlight = (returnFlightId == 0) ? null : em.find(Flight.class, returnFlightId);
        
        int totalSeats = 0;
        List<CabinFee> list = new ArrayList<>();
        for (Long cfId : cabinFees) {
            CabinFee cf = em.find(CabinFee.class, cfId);
            totalSeats += cf.getCabinConfiguration().getCabinCapacity();
            list.add(cf);
        }
        
        FlightSchedulePlan fsp = new FlightSchedulePlan(flight.getFlightNum(), list, "Multiple Type");
        em.persist(fsp);
        
        FlightSchedulePlan rfsp = null;
        if (!layoverInput.equals("-1") && !layoverInput.equals("-1")) {
            rfsp = new FlightSchedulePlan(returFlight.getFlightNum(), list, "Multiple Type");
            rfsp.setIsReturnFlight(true);
            em.persist(rfsp);
            fsp.setReturnFlightSchedulePlan(rfsp);
            rfsp.setReturnFlightSchedulePlan(fsp);
        }
        em.flush();
        
        for (int i = 0; i < dateTimes.size(); i++) {
            LocalDateTime dateTime = dateTimes.get(i);
            Duration duration = durations.get(i);
            
            FlightSchedule fs = new FlightSchedule(dateTime, duration, fsp, flight);
            em.persist(fs);
            fs.setSeats(totalSeats);
            fsp.getFlightSchedules().add(fs);

            if (!layoverInput.equals("-1") && !layoverInput.equals("-1")) {
                dateTime = dateTime.plusHours(Long.parseLong(layoverInput));
                fs = new FlightSchedule(dateTime, duration, rfsp, returFlight);
                em.persist(fs);
                fs.setSeats(totalSeats);
                rfsp.getFlightSchedules().add(fs);
            }
        }

        return fsp.getFlightSchedulePlanId();
    }
    
    @Override
    public Long createNFlightSchedulePlan(
            Long flightId, 
            Long returnFlightId, 
            Long n,
            LocalDateTime startDate, 
            LocalDateTime endDate, 
            Duration duration,
            String layoverInput,
            List<Long> cabinFees) {
        
        Flight flight = em.find(Flight.class, flightId);
        Flight returFlight = (returnFlightId == 0) ? null : em.find(Flight.class, returnFlightId);
        
        int totalSeats = 0;
        List<CabinFee> list = new ArrayList<>();
        for (Long cfId : cabinFees) {
            CabinFee cf = em.find(CabinFee.class, cfId);
            totalSeats += cf.getCabinConfiguration().getCabinCapacity();
            list.add(cf);
        }
        
        String planType = "Recurrent Type(" + n + ")";
        FlightSchedulePlan fsp = new FlightSchedulePlan(flight.getFlightNum(), list, planType);
        em.persist(fsp);
        
        
        FlightSchedulePlan rfsp = null;
        if (!layoverInput.equals("-1") && !layoverInput.equals("-1")) {
            rfsp = new FlightSchedulePlan(returFlight.getFlightNum(), list, planType);
            rfsp.setIsReturnFlight(true);
            em.persist(rfsp);
            fsp.setReturnFlightSchedulePlan(rfsp);
            rfsp.setReturnFlightSchedulePlan(fsp);
        }
        
        em.flush();
        
        LocalDateTime currentDateTime = startDate;
        while (!currentDateTime.isAfter(endDate)) {
            System.out.println(currentDateTime);
            FlightSchedule fs = new FlightSchedule(currentDateTime, duration, fsp, flight);
            em.persist(fs);
            fs.setSeats(totalSeats);
            fsp.getFlightSchedules().add(fs);

            if (!layoverInput.equals("-1") && !layoverInput.equals("-1")) {
                LocalDateTime dateTime = currentDateTime.plusHours(Long.parseLong(layoverInput));
                fs = new FlightSchedule(dateTime, duration, rfsp, returFlight);
                em.persist(fs);
                fs.setSeats(totalSeats);
                rfsp.getFlightSchedules().add(fs);
            }
            currentDateTime = currentDateTime.plusDays(n);
        }
        
        return fsp.getFlightSchedulePlanId();
    }
    
    @Override
    public Long createSingleFlightSchedulePlan(
            Long flightId,
            Long returnFlightId,
            LocalDateTime dateTime,
            Duration duration,
            String layoverInput,
            List<Long> cabinFees) {
        
        Flight flight = em.find(Flight.class, flightId);
        Flight returFlight = (returnFlightId == 0) ? null : em.find(Flight.class, returnFlightId);
        
        int totalSeats = 0;
        List<CabinFee> list = new ArrayList<>();
        for (Long cfId : cabinFees) {
            CabinFee cf = em.find(CabinFee.class, cfId);
            totalSeats += cf.getCabinConfiguration().getCabinCapacity();
            list.add(cf);
        }
        
        FlightSchedulePlan fsp = new FlightSchedulePlan(flight.getFlightNum(), list, "Single Type");
        em.persist(fsp);
        em.flush();
        
        FlightSchedule fs = new FlightSchedule(dateTime, duration, fsp, flight);
        em.persist(fs);
        fs.setSeats(totalSeats);
        fsp.getFlightSchedules().add(fs);

        if (!layoverInput.equals("-1") && !layoverInput.equals("-1")) {
            List<CabinFee> list2 = new ArrayList<>();
            for ( CabinConfiguration cc : returFlight.getAircraftConfiguration().getCabinConfigurations()) {
                Query query = em.createQuery("SELECT c FROM CabinFee c LEFT JOIN c.cabinConfiguration cc WHERE cc.cabinConfigurationId = :id").setParameter("id", cc.getCabinConfigurationId());
                CabinFee temp = (CabinFee)query.getSingleResult();
                list2.add(temp);
            }
            
            FlightSchedulePlan rfsp = new FlightSchedulePlan(returFlight.getFlightNum(), list2, "Single Type");
            rfsp.setIsReturnFlight(true);
            em.persist(rfsp);
            dateTime = dateTime.plusHours(Long.parseLong(layoverInput));
            fs = new FlightSchedule(dateTime, duration, rfsp, returFlight);
            em.persist(fs);
            fs.setSeats(totalSeats);
            rfsp.getFlightSchedules().add(fs);
            fsp.setReturnFlightSchedulePlan(rfsp);
            rfsp.setReturnFlightSchedulePlan(fsp);
        }
        return fsp.getFlightSchedulePlanId();
    }
    
    @Override
    public List<String> getAllFlightSchedulePlan() {
        Query query = em.createQuery("SELECT f FROM FlightSchedulePlan f ORDER BY f.FlightNumber ASC");
        List <FlightSchedulePlan> f = query.getResultList();
        List<FlightSchedulePlan> orderedList = new ArrayList<>();
        
        Collections.sort(f, new Comparator<FlightSchedulePlan>() {
            @Override
            public int compare(FlightSchedulePlan plan1, FlightSchedulePlan plan2) {
                if (plan1.getFlightNumber().compareTo(plan2.getFlightNumber()) != 0) {
                    return plan1.getFlightNumber().compareTo(plan2.getFlightNumber());
                } else {
                    return plan1.firstDateTime().compareTo(plan2.firstDateTime());
                }
            }
        });
        
        Iterator<FlightSchedulePlan> iterator = f.iterator();
        while (iterator.hasNext()) {
            FlightSchedulePlan route = iterator.next();

            if (route.getReturnFlightSchedulePlan()!= null && f.contains(route.getReturnFlightSchedulePlan())) {
                orderedList.add(route);
                orderedList.add(route.getReturnFlightSchedulePlan());
                iterator.remove();
               
            } 
            if(route.getReturnFlightSchedulePlan() == null) {
                orderedList.add(route);
                iterator.remove();
            }
        }
        
        List<String> out = new ArrayList<>();
        for (int i = 0; i < orderedList.size(); i++) {
            out.add(orderedList.get(i).toString());
        }

        return out;
    }
    
    @Override
    public String flightScheduleDetail(Long id) throws NoSuchFieldException {
        FlightSchedulePlan fsp = em.find(FlightSchedulePlan.class, id);
        if (fsp == null) {
            throw new NoSuchFieldException();
        }
        Query query = em.createQuery("SELECT f FROM Flight f WHERE f.flightNumber = :flightnumber");
        query.setParameter("flightnumber", fsp.getFlightNumber());
        Flight flight = (Flight)query.getSingleResult();
        
        String out = fsp.getPlanType() + " Flight Schedule Plan(id: " + fsp.getFlightSchedulePlanId() + ")\n";
        out += "flight number: " + fsp.getFlightNumber();
        out += "   orgin to destination: " + flight.getFlightRoute().getOrigin().getIataCode() + " " + flight.getFlightRoute().getDestination().getIataCode();
        for(CabinFee cf : fsp.getCabinFees()) {
            out += "\nCabin " + cf.getName() + ": $" + cf.getFee();
        }
        
        out += "\nfirst flight: " + fsp.firstDateTime();
        out += "\n\nflight schedules:\n";
        List<FlightSchedule> list = fsp.getFlightSchedules();
        
        Collections.sort(list, new Comparator<FlightSchedule>() {
            @Override
            public int compare(FlightSchedule plan1, FlightSchedule plan2) {
                return plan1.getDepartureDateTime().compareTo(plan2.getDepartureDateTime());
            }
        });
        
        for (FlightSchedule fs : list) {
            out += fs.toString() + "\n";
        }
        
        return out;
    }
    
    public void deleteFlightSchedule(Long id) throws NoSuchFieldException, NoSuchMethodException {
        FlightSchedulePlan f = em.find(FlightSchedulePlan.class, id);
        if (f == null) {
            throw new NoSuchFieldException();
        }
        
        em.remove(f);
    }
    
    @Override
    public FlightSchedulePlan getFlightSchedulePlan(String flightNum) throws NoSuchFieldException {
        Query query = em.createQuery("SELECT f FROM FlightSchedulePlan f WHERE f.FlightNumber = :flightnumber");
        query.setParameter("flightnumber", flightNum);
        try {
            FlightSchedulePlan f = (FlightSchedulePlan)query.getSingleResult();
            for (FlightSchedule fe : f.getFlightSchedules()) {
                System.out.println(fe);
            }
            return f;
        } catch (NoResultException e) {
            throw new NoSuchFieldException();
        }
    }
    
    @Override
    public FlightSchedule getFlightSchedule(Long id) throws NoSuchFieldException {
        FlightSchedule fs = em.find(FlightSchedule.class, id);
        if (fs == null) {
            throw new NoSuchFieldException();
        }
        return fs;
    }
    
    @Override
    public String flightScheduleprint(Long id) {
        FlightSchedule fs = em.find(FlightSchedule.class, id);
        Flight f = fs.getFlight();
        AircraftConfiguration ac = f.getAircraftConfiguration();
        List<CabinConfiguration> cc = ac.getCabinConfigurations();
        List<ReservedSeat> rs = fs.getReservedSeats();
        String out = String.format("Flight Schedule (id: %d)", id) + "\n";
        out += String.format("Flight number: %s  Orgin-destation: %s", f.getFlightNum(), f.getFlightRoute().toString()) + "\n";
        out += String.format("total seats: %d  booked seats: %d  remaining seats: %d", fs.getTotalSeats(), fs.getTotalSeats()- fs.getRemainingSeats(), fs.getRemainingSeats()) + "\n";
        out += String.format("aircraft model: %s  number of cabin: %d", ac.getAircraftType().getName(), cc.size()) + "\n\n";
        
        for (CabinConfiguration c : cc) {
            int temp = c.getCabinCapacity();
            for (ReservedSeat r : rs) {
                if (c.getCabinclass().equals(r.getSeat().getCabinConfiguration().getCabinclass())) {
                    temp--;
                }
            }
            out += String.format("Cabin: %s  total: %d  booked: %d  remaining: %d\n", c.getCabinclass(), c.getCabinCapacity(), 
                    c.getCabinCapacity() - temp, temp);
        }
            
        return out;
    }
    
    @Override
    public String flightScheduleReservation(Long id) {
        Query query = em.createQuery("SELECT r FROM ReservedSeat r LEFT JOIN r.flightSchedule f LEFT JOIN r.seat s WHERE f.flightScheduleId = :id ORDER BY s.seatNumber");
        query.setParameter("id", id);
        
        List<ReservedSeat> list = query.getResultList();
        
        StringBuilder sb = new StringBuilder(String.format("%-10s%-20s%-10s\n", "Seat", "passenger name", "fare code"));
        
        for (ReservedSeat s : list) {
            Long cabinId = s.getSeat().getCabinConfiguration().getCabinConfigurationId();
            List<CabinFee> fees = s.getFlightSchedule().getFlightSchedulePlan().getCabinFees();
            String code = "";
            for (CabinFee fee : fees) {
                if(fee.getCabinConfiguration().getCabinConfigurationId() == cabinId) {
                    code = fee.getName();
                    break;
                }
            }
            sb.append(String.format("%-10s%-20s%-10s\n", s.getSeat().getSeatNumber(),s.getCustomer().getFirstName() + " " + s.getCustomer().getLastName(), code));
        }
        
        return sb.toString();
    }
    
    @Override
    public boolean isBeingUsed(FlightSchedulePlan fsp) {
        Query query = em.createQuery("SELECT r FROM ReservedSeat r");
        List<ReservedSeat> list = query.getResultList();
        for (ReservedSeat reservedSeat : list) {
            if (reservedSeat.getFlightSchedule().getFlightSchedulePlan().getFlightSchedulePlanId() == fsp.getFlightSchedulePlanId()) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void update(FlightSchedulePlan f) {
        em.merge(f);
    }
    
    @Override
    public void createFlightSchedule(FlightSchedule f) {
        FlightSchedulePlan fsp = em.find(FlightSchedulePlan.class, f.getFlightSchedulePlan().getFlightSchedulePlanId());
        Flight fa = em.find(Flight.class, f.getFlight().getFlightId());
        FlightSchedule fs = new FlightSchedule(f.getDepartureDateTime(),f.getEstimatedDuration(),fsp,fa);
        em.persist(fs);
        fs.setSeats(f.getTotalSeats());
        fsp.getFlightSchedules().add(fs);
    }
    
    @Override
    public void deleteFlightScheduleInd(Long id) throws NoSuchFieldException {
        FlightSchedule f = em.find(FlightSchedule.class, id);
        if (f == null) {
            throw new NoSuchFieldException();
        }

        FlightSchedulePlan fsp = f.getFlightSchedulePlan();
        fsp.getFlightSchedules().remove(f);
    }
}
