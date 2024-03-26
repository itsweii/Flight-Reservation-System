package ejb.session.singleton;

import ejb.session.stateless.AircraftConfigurationSessionBeanLocal;
import ejb.session.stateless.AircraftTypeSessionBeanLocal;
import ejb.session.stateless.AirportSessionBeanLocal;
import ejb.session.stateless.CabinConfigurationSessionBeanLocal;
import ejb.session.stateless.FlightRouteSessionBeanLocal;
import ejb.session.stateless.FlightSchedulePlanSessionBeanRemote;
import ejb.session.stateless.FlightSessionBeanLocal;
import entity.AircraftConfiguration;
import entity.AircraftType;
import entity.Airport;
import entity.CabinConfiguration;
import entity.Flight;
import entity.FlightRoute;
import entity.Seat;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Singleton
public class Testcase implements TestcaseRemote {

    @EJB
    private AircraftConfigurationSessionBeanLocal aircraftConfigurationSessionBean;

    @EJB
    private FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBean;

    @EJB
    private CabinConfigurationSessionBeanLocal cabinConfigurationSessionBean;

    @EJB
    private FlightSessionBeanLocal flightSessionBean;

    @EJB
    private FlightRouteSessionBeanLocal flightRouteSessionBean;

    @EJB
    private AirportSessionBeanLocal airportSessionBean;

    @EJB
    private AircraftTypeSessionBeanLocal aircraftTypeSessionBean;

    @PersistenceContext(unitName = "Is2103Project-ejbPU")
    private EntityManager em;

    @Override
    public void build() {
        try{
            System.out.println("test case session bean running");
            
            // aircraft configuration
            AircraftType aircraftType737 = aircraftTypeSessionBean.getAircraftType("Boeing 737");
            AircraftType aircraftType747 = aircraftTypeSessionBean.getAircraftType("Boeing 747");
            
            List<Integer> config11 = new ArrayList<>();
            config11.add(1);
            config11.add(1);
            List<Integer> config22 = new ArrayList<>();
            config22.add(2);
            config22.add(2);
            List<Integer> config222 = new ArrayList<>();
            config222.add(2);
            config222.add(2);
            config222.add(2);
            List<Integer> config33 = new ArrayList<>();
            config33.add(3);
            config33.add(3);
            List<Integer> config343 = new ArrayList<>();
            config343.add(3);
            config343.add(4);
            config343.add(3);
            
            //Boeing 737 All Economy, 1, 180
            List<CabinConfiguration> cabinList = new ArrayList<>();
            CabinConfiguration cc = new CabinConfiguration("Y", 1, 30, 6, config33);
            em.persist(cc);
            cabinList.add(cc);
            AircraftConfiguration aircraftConfiguration = new AircraftConfiguration("Boeing 737 All Economy", 1, 180,cabinList,aircraftType737);
            aircraftConfigurationSessionBean.createAircraftConfiguration(aircraftConfiguration);
            System.out.println("Aircraft Configuration Boeing 737 All Economy, 1, 180 successfully created\n");
            
            //Boeing 737 Three Classes, 3, 180
            cabinList = new ArrayList<>();
            CabinConfiguration ccf = new CabinConfiguration("F", 1, 5, 2, config11);
            em.persist(ccf);
            for (int i = 0 + 1; i < ccf.getNumberOfRow() + 1; i++) {
                for (int j = 0; j < ccf.getNumberOfSeatAbreast(); j++) {
                    String num = "" + String.valueOf(i) + (char)('A' + j % 26);
                    Seat s = new Seat(num);
                    em.persist(s);
                    s.setCabinConfiguration(ccf);
                    ccf.getSeats().add(s);
                }
            }
            cabinList.add(ccf);
            
            cc = new CabinConfiguration("J", 1, 4, 2, config22);
            em.persist(cc);
            for (int i = 0 + 1; i < cc.getNumberOfRow() + 1; i++) {
                for (int j = 0; j < cc.getNumberOfSeatAbreast(); j++) {
                    String num = "" + String.valueOf(i) + (char)('A' + j % 26);
                    Seat s = new Seat(num);
                    em.persist(s);
                    s.setCabinConfiguration(cc);
                    cc.getSeats().add(s);
                }
            }
            cabinList.add(cc);
            
            cc = new CabinConfiguration("Y", 1, 25, 6, config33);
            em.persist(cc);
            for (int i = 0 + 1; i < cc.getNumberOfRow() + 1; i++) {
                for (int j = 0; j < cc.getNumberOfSeatAbreast(); j++) {
                    String num = "" + String.valueOf(i) + (char)('A' + j % 26);
                    Seat s = new Seat(num);
                    em.persist(s);
                    s.setCabinConfiguration(cc);
                    cc.getSeats().add(s);
                }
            }
            cabinList.add(cc);
            aircraftConfiguration = new AircraftConfiguration("Boeing 737 Three Classes", 1, 180,cabinList,aircraftType737);
            aircraftConfigurationSessionBean.createAircraftConfiguration(aircraftConfiguration);
            System.out.println("Aircraft Configuration Boeing 737 Three Classes, 3, 180 successfully created\n");
            
            //Boeing 747 All Economy, 1, 380
            cabinList = new ArrayList<>();
            cc = new CabinConfiguration("Y", 2, 38, 10, config343);
            em.persist(cc);
            for (int i = 0 + 1; i < cc.getNumberOfRow() + 1; i++) {
                for (int j = 0; j < cc.getNumberOfSeatAbreast(); j++) {
                    String num = "" + String.valueOf(i) + (char)('A' + j % 26);
                    Seat s = new Seat(num);
                    em.persist(s);
                    s.setCabinConfiguration(cc);
                    cc.getSeats().add(s);
                }
            }
            cabinList.add(cc);
            aircraftConfiguration = new AircraftConfiguration("Boeing 747 All Economy", 1, 180,cabinList,aircraftType747);
            aircraftConfigurationSessionBean.createAircraftConfiguration(aircraftConfiguration);
            System.out.println("Aircraft Configuration Boeing 747 All Economy, 1, 380 successfully created\n");

            //Boeing 747 Three Classes, 3, 360
            cabinList = new ArrayList<>();
            cabinList.add(ccf);
            
            cc = new CabinConfiguration("J", 2, 5, 6, config222);
            em.persist(cc);
            for (int i = 0 + 1; i < cc.getNumberOfRow() + 1; i++) {
                for (int j = 0; j < cc.getNumberOfSeatAbreast(); j++) {
                    String num = "" + String.valueOf(i) + (char)('A' + j % 26);
                    Seat s = new Seat(num);
                    em.persist(s);
                    s.setCabinConfiguration(cc);
                    cc.getSeats().add(s);
                }
            }
            cabinList.add(cc);
            
            cc = new CabinConfiguration("Y", 2, 32, 10, config343);
            em.persist(cc);
            for (int i = 0 + 1; i < cc.getNumberOfRow() + 1; i++) {
                for (int j = 0; j < cc.getNumberOfSeatAbreast(); j++) {
                    String num = "" + String.valueOf(i) + (char)('A' + j % 26);
                    Seat s = new Seat(num);
                    em.persist(s);
                    s.setCabinConfiguration(cc);
                    cc.getSeats().add(s);
                }
            }
            cabinList.add(cc);
            aircraftConfiguration = new AircraftConfiguration("Boeing 747 Three Classes", 3, 360,cabinList,aircraftType747);
            aircraftConfigurationSessionBean.createAircraftConfiguration(aircraftConfiguration);
            System.out.println("Aircraft Configuration Boeing 747 Three Classes, 3, 360 successfully created\n");

            
        //flight route
            Airport originAirport = airportSessionBean.findAirportByIATA("SIN");
            
            Airport destinationAirport = airportSessionBean.findAirportByIATA("HKG");
            FlightRoute flightRoute = new FlightRoute(originAirport,destinationAirport);
            FlightRoute returnRoute = new FlightRoute(destinationAirport, originAirport);
            em.persist(flightRoute);
            em.persist(returnRoute);
            flightRoute.setReturnRoute(returnRoute);
            returnRoute.setReturnRoute(flightRoute);
            System.out.println(String.format("FlightRoute %s %s Created", originAirport.getIataCode(), destinationAirport.getIataCode()));
            
            destinationAirport = airportSessionBean.findAirportByIATA("TPE");
            flightRoute = new FlightRoute(originAirport,destinationAirport);
            returnRoute = new FlightRoute(destinationAirport, originAirport);
            em.persist(flightRoute);
            em.persist(returnRoute);
            flightRoute.setReturnRoute(returnRoute);
            returnRoute.setReturnRoute(flightRoute);
            System.out.println(String.format("FlightRoute %s %s Created", originAirport.getIataCode(), destinationAirport.getIataCode()));
            
            destinationAirport = airportSessionBean.findAirportByIATA("NRT");
            flightRoute = new FlightRoute(originAirport,destinationAirport);
            returnRoute = new FlightRoute(destinationAirport, originAirport);
            em.persist(flightRoute);
            em.persist(returnRoute);
            flightRoute.setReturnRoute(returnRoute);
            returnRoute.setReturnRoute(flightRoute);
            System.out.println(String.format("FlightRoute %s %s Created", originAirport.getIataCode(), destinationAirport.getIataCode()));
            
            
            originAirport = airportSessionBean.findAirportByIATA("HKG");
            destinationAirport = airportSessionBean.findAirportByIATA("NRT");
            flightRoute = new FlightRoute(originAirport,destinationAirport);
            returnRoute = new FlightRoute(destinationAirport, originAirport);
            em.persist(flightRoute);
            em.persist(returnRoute);
            flightRoute.setReturnRoute(returnRoute);
            returnRoute.setReturnRoute(flightRoute);
            System.out.println(String.format("FlightRoute %s %s Created", originAirport.getIataCode(), destinationAirport.getIataCode()));
            
            originAirport = airportSessionBean.findAirportByIATA("TPE");
            destinationAirport = airportSessionBean.findAirportByIATA("NRT");
            flightRoute = new FlightRoute(originAirport,destinationAirport);
            returnRoute = new FlightRoute(destinationAirport, originAirport);
            em.persist(flightRoute);
            em.persist(returnRoute);
            flightRoute.setReturnRoute(returnRoute);
            returnRoute.setReturnRoute(flightRoute);
            System.out.println(String.format("FlightRoute %s %s Created", originAirport.getIataCode(), destinationAirport.getIataCode()));
            
            originAirport = airportSessionBean.findAirportByIATA("SIN");
            destinationAirport = airportSessionBean.findAirportByIATA("SYD");
            flightRoute = new FlightRoute(originAirport,destinationAirport);
            returnRoute = new FlightRoute(destinationAirport, originAirport);
            em.persist(flightRoute);
            em.persist(returnRoute);
            flightRoute.setReturnRoute(returnRoute);
            returnRoute.setReturnRoute(flightRoute);
            System.out.println(String.format("FlightRoute %s %s Created", originAirport.getIataCode(), destinationAirport.getIataCode()));
            
            originAirport = airportSessionBean.findAirportByIATA("SYD");
            destinationAirport = airportSessionBean.findAirportByIATA("NRT");
            flightRoute = new FlightRoute(originAirport,destinationAirport);
            returnRoute = new FlightRoute(destinationAirport, originAirport);
            em.persist(flightRoute);
            em.persist(returnRoute);
            flightRoute.setReturnRoute(returnRoute);
            returnRoute.setReturnRoute(flightRoute);
            System.out.println(String.format("FlightRoute %s %s Created", originAirport.getIataCode(), destinationAirport.getIataCode()));
            
        //flight
            String temp = "ML111, SIN, HKG, Boeing 737 Three Classes";
            String name1 = "ML111";
            String name2 = "ML112";
            AircraftConfiguration config = aircraftConfigurationSessionBean.getAircraftConfiguration("Boeing 737 Three Classes");
            originAirport = airportSessionBean.findAirportByIATA("SIN");
            destinationAirport = airportSessionBean.findAirportByIATA("HKG");
            flightRoute = flightRouteSessionBean.findFlightRoute(originAirport, destinationAirport);
            Long flightID = flightSessionBean.craeteFlight(name1, flightRoute.getFlightRouteId(), config.getAircraftConfigurationId());
            returnRoute = flightRouteSessionBean.findFlightRoute(destinationAirport, originAirport);
            flightSessionBean.craeteReturnFlight(name2, returnRoute.getFlightRouteId(), config.getAircraftConfigurationId(), flightID);
            System.out.println(temp + " created");
            
            temp = "ML211, SIN, TPE, Boeing 737 Three Classes";
            name1 = "ML211";
            name2 = "ML212";
            config = aircraftConfigurationSessionBean.getAircraftConfiguration("Boeing 737 Three Classes");
            originAirport = airportSessionBean.findAirportByIATA("SIN");
            destinationAirport = airportSessionBean.findAirportByIATA("TPE");
            flightRoute = flightRouteSessionBean.findFlightRoute(originAirport, destinationAirport);
            flightID = flightSessionBean.craeteFlight(name1, flightRoute.getFlightRouteId(), config.getAircraftConfigurationId());
            returnRoute = flightRouteSessionBean.findFlightRoute(destinationAirport, originAirport);
            flightSessionBean.craeteReturnFlight(name2, returnRoute.getFlightRouteId(), config.getAircraftConfigurationId(), flightID);
            System.out.println(temp + " created");
            
            temp = "ML311, SIN, NRT, Boeing 747 Three Classes";
            name1 = "ML311";
            name2 = "ML312";
            config = aircraftConfigurationSessionBean.getAircraftConfiguration("Boeing 747 Three Classes");
            originAirport = airportSessionBean.findAirportByIATA("SIN");
            destinationAirport = airportSessionBean.findAirportByIATA("NRT");
            flightRoute = flightRouteSessionBean.findFlightRoute(originAirport, destinationAirport);
            flightID = flightSessionBean.craeteFlight(name1, flightRoute.getFlightRouteId(), config.getAircraftConfigurationId());
            returnRoute = flightRouteSessionBean.findFlightRoute(destinationAirport, originAirport);
            flightSessionBean.craeteReturnFlight(name2, returnRoute.getFlightRouteId(), config.getAircraftConfigurationId(), flightID);
            System.out.println(temp + " created");
            
            temp = "ML411, HKG, NRT, Boeing 737 Three Classes";
            name1 = "ML411";
            name2 = "ML412";
            config = aircraftConfigurationSessionBean.getAircraftConfiguration("Boeing 737 Three Classes");
            originAirport = airportSessionBean.findAirportByIATA("HKG");
            destinationAirport = airportSessionBean.findAirportByIATA("NRT");
            flightRoute = flightRouteSessionBean.findFlightRoute(originAirport, destinationAirport);
            flightID = flightSessionBean.craeteFlight(name1, flightRoute.getFlightRouteId(), config.getAircraftConfigurationId());
            returnRoute = flightRouteSessionBean.findFlightRoute(destinationAirport, originAirport);
            flightSessionBean.craeteReturnFlight(name2, returnRoute.getFlightRouteId(), config.getAircraftConfigurationId(), flightID);
            System.out.println(temp + " created");
            
            temp = "ML511, TPE, NRT, Boeing 737 Three Classes";
            name1 = "ML511";
            name2 = "ML512";
            config = aircraftConfigurationSessionBean.getAircraftConfiguration("Boeing 737 Three Classes");
            originAirport = airportSessionBean.findAirportByIATA("TPE");
            destinationAirport = airportSessionBean.findAirportByIATA("NRT");
            flightRoute = flightRouteSessionBean.findFlightRoute(originAirport, destinationAirport);
            flightID = flightSessionBean.craeteFlight(name1, flightRoute.getFlightRouteId(), config.getAircraftConfigurationId());
            returnRoute = flightRouteSessionBean.findFlightRoute(destinationAirport, originAirport);
            flightSessionBean.craeteReturnFlight(name2, returnRoute.getFlightRouteId(), config.getAircraftConfigurationId(), flightID);
            System.out.println(temp + " created");
            
            temp = "ML611, SIN, SYD, Boeing 737 Three Classes";
            name1 = "ML611";
            name2 = "ML612";
            config = aircraftConfigurationSessionBean.getAircraftConfiguration("Boeing 737 Three Classes");
            originAirport = airportSessionBean.findAirportByIATA("SIN");
            destinationAirport = airportSessionBean.findAirportByIATA("SYD");
            flightRoute = flightRouteSessionBean.findFlightRoute(originAirport, destinationAirport);
            flightID = flightSessionBean.craeteFlight(name1, flightRoute.getFlightRouteId(), config.getAircraftConfigurationId());
            returnRoute = flightRouteSessionBean.findFlightRoute(destinationAirport, originAirport);
            flightSessionBean.craeteReturnFlight(name2, returnRoute.getFlightRouteId(), config.getAircraftConfigurationId(), flightID);
            System.out.println(temp + " created");
            
            temp = "ML621, SIN, SYD, Boeing 737 All Economy";
            name1 = "ML621";
            name2 = "ML622";
            config = aircraftConfigurationSessionBean.getAircraftConfiguration("Boeing 737 All Economy");
            originAirport = airportSessionBean.findAirportByIATA("SIN");
            destinationAirport = airportSessionBean.findAirportByIATA("SYD");
            flightRoute = flightRouteSessionBean.findFlightRoute(originAirport, destinationAirport);
            flightID = flightSessionBean.craeteFlight(name1, flightRoute.getFlightRouteId(), config.getAircraftConfigurationId());
            returnRoute = flightRouteSessionBean.findFlightRoute(destinationAirport, originAirport);
            flightSessionBean.craeteReturnFlight(name2, returnRoute.getFlightRouteId(), config.getAircraftConfigurationId(), flightID);
            System.out.println(temp + " created");
            
            temp = "ML711, SYD, NRT, Boeing 747 Three Classes";
            name1 = "ML711";
            name2 = "ML712";
            config = aircraftConfigurationSessionBean.getAircraftConfiguration("Boeing 737 Three Classes");
            originAirport = airportSessionBean.findAirportByIATA("SYD");
            destinationAirport = airportSessionBean.findAirportByIATA("NRT");
            flightRoute = flightRouteSessionBean.findFlightRoute(originAirport, destinationAirport);
            flightID = flightSessionBean.craeteFlight(name1, flightRoute.getFlightRouteId(), config.getAircraftConfigurationId());
            returnRoute = flightRouteSessionBean.findFlightRoute(destinationAirport, originAirport);
            flightSessionBean.craeteReturnFlight(name2, returnRoute.getFlightRouteId(), config.getAircraftConfigurationId(), flightID);
            System.out.println(temp + " created");

        //Flight Schedule Plan
            //ML711, Recurrent Weekly
                Flight flight = flightSessionBean.retrieveFlight("ML711");
                Flight returnflight = flight.getReturnFlight();
                DayOfWeek dayOfWeek = DayOfWeek.MONDAY;
                String timeInput = "9:00 AM";
                String startDateInput = "1/12/23";
                String endDateInput = "31/12/23";
                String durationInput = "14 0";
                String layoverInput = "2";
                
                List<Long> cabinFees = new ArrayList<Long>();
                List<CabinConfiguration> list = cabinConfigurationSessionBean.getCabinConfigurations(flight.getFlightId());

                String code = "F";
                String name = "noCode";
                int fee = 6000;
                CabinConfiguration curr = null;
                for (CabinConfiguration cca : list) {
                    if (cca.getCabinclass().equals(code)) {
                        curr = cca;
                        break;
                    }
                }
                Long lg = 0l;
                lg = flightSchedulePlanSessionBean.createCabinFee(curr.getCabinConfigurationId(), name, fee);
                cabinFees.add(lg);
                
                code = "J";
                name = "noCode";
                fee = 3000;
                curr = null;
                for (CabinConfiguration cca : list) {
                    if (cca.getCabinclass().equals(code)) {
                        curr = cca;
                        break;
                    }
                }
                lg = flightSchedulePlanSessionBean.createCabinFee(curr.getCabinConfigurationId(), name, fee);
                cabinFees.add(lg);
                
                code = "Y";
                name = "noCode";
                fee = 1000;
                curr = null;
                for (CabinConfiguration cca : list) {
                    if (cca.getCabinclass().equals(code)) {
                        curr = cca;
                        break;
                    }
                }
                lg = flightSchedulePlanSessionBean.createCabinFee(curr.getCabinConfigurationId(), name, fee);
                cabinFees.add(lg);

                Long returnFlightId = returnflight.getFlightId();
                
                Long id = 0l;
                id = flightSchedulePlanSessionBean.createWeeklyFlightSchedulePlan(
                    flight.getFlightId(), 
                    returnFlightId, 
                    dayOfWeek, 
                    timeInput, 
                    startDateInput, 
                    endDateInput, 
                    durationInput,
                    layoverInput,
                    cabinFees);
                
                System.out.println("flight plan ML711, Recurrent Weekly creeated (id:" + id + ")");

            //ML611, Recurrent Weekly
            flight = flightSessionBean.retrieveFlight("ML611");
            returnflight = flight.getReturnFlight();
            dayOfWeek = DayOfWeek.SUNDAY;
            timeInput = "12:00 PM";
            startDateInput = "1/12/23";
            endDateInput = "31/12/23";
            durationInput = "8 0";
            layoverInput = "2";

            cabinFees = new ArrayList<Long>();
            list = cabinConfigurationSessionBean.getCabinConfigurations(flight.getFlightId());

            code = "F";
            name = "noCode";
            fee = 3000;
            curr = null;
            for (CabinConfiguration cca : list) {
                if (cca.getCabinclass().equals(code)) {
                    curr = cca;
                    break;
                }
            }
            lg = flightSchedulePlanSessionBean.createCabinFee(curr.getCabinConfigurationId(), name, fee);
            cabinFees.add(lg);

            code = "J";
            name = "noCode";
            fee = 1500;
            curr = null;
            for (CabinConfiguration cca : list) {
                if (cca.getCabinclass().equals(code)) {
                    curr = cca;
                    break;
                }
            }
            lg = flightSchedulePlanSessionBean.createCabinFee(curr.getCabinConfigurationId(), name, fee);
            cabinFees.add(lg);

            code = "Y";
            name = "noCode";
            fee = 500;
            curr = null;
            for (CabinConfiguration cca : list) {
                if (cca.getCabinclass().equals(code)) {
                    curr = cca;
                    break;
                }
            }
            lg = flightSchedulePlanSessionBean.createCabinFee(curr.getCabinConfigurationId(), name, fee);
            cabinFees.add(lg);

            returnFlightId = returnflight.getFlightId();

            id = flightSchedulePlanSessionBean.createWeeklyFlightSchedulePlan(
                flight.getFlightId(), 
                returnFlightId, 
                dayOfWeek, 
                timeInput, 
                startDateInput, 
                endDateInput, 
                durationInput,
                layoverInput,
                cabinFees);

            System.out.println("flight plan ML611, Recurrent Weekly creeated (id:" + id + ")");
            
            //ML311, Recurrent Weekly
            flight = flightSessionBean.retrieveFlight("ML311");
            returnflight = flight.getReturnFlight();
            dayOfWeek = DayOfWeek.MONDAY;
            timeInput = "10:00 PM";
            startDateInput = "1/12/23";
            endDateInput = "31/12/23";
            durationInput = "8 0";
            layoverInput = "3";

            cabinFees = new ArrayList<Long>();
            list = cabinConfigurationSessionBean.getCabinConfigurations(flight.getFlightId());

            code = "F";
            name = "noCode";
            fee = 3100;
            curr = null;
            for (CabinConfiguration cca : list) {
                if (cca.getCabinclass().equals(code)) {
                    curr = cca;
                    break;
                }
            }
            lg = flightSchedulePlanSessionBean.createCabinFee(curr.getCabinConfigurationId(), name, fee);
            cabinFees.add(lg);

            code = "J";
            name = "noCode";
            fee = 1600;
            curr = null;
            for (CabinConfiguration cca : list) {
                if (cca.getCabinclass().equals(code)) {
                    curr = cca;
                    break;
                }
            }
            lg = flightSchedulePlanSessionBean.createCabinFee(curr.getCabinConfigurationId(), name, fee);
            cabinFees.add(lg);

            code = "Y";
            name = "noCode";
            fee = 600;
            curr = null;
            for (CabinConfiguration cca : list) {
                if (cca.getCabinclass().equals(code)) {
                    curr = cca;
                    break;
                }
            }
            lg = flightSchedulePlanSessionBean.createCabinFee(curr.getCabinConfigurationId(), name, fee);
            cabinFees.add(lg);

            returnFlightId = returnflight.getFlightId();

            id = flightSchedulePlanSessionBean.createWeeklyFlightSchedulePlan(
                flight.getFlightId(), 
                returnFlightId, 
                dayOfWeek, 
                timeInput, 
                startDateInput, 
                endDateInput, 
                durationInput,
                layoverInput,
                cabinFees);

            System.out.println("flight plan ML311, Recurrent Weekly creeated (id:" + id + ")");
            
            //ML621, Recurrent Weekly
            flight = flightSessionBean.retrieveFlight("ML621");
            returnflight = flight.getReturnFlight();
            dayOfWeek = DayOfWeek.THURSDAY;
            timeInput = "10:00 AM";
            startDateInput = "1/12/23";
            endDateInput = "31/12/23";
            durationInput = "8 0";
            layoverInput = "2";

            cabinFees = new ArrayList<Long>();
            list = cabinConfigurationSessionBean.getCabinConfigurations(flight.getFlightId());

            code = "Y";
            name = "noCode";
            fee = 700;
            curr = null;
            for (CabinConfiguration cca : list) {
                if (cca.getCabinclass().equals(code)) {
                    curr = cca;
                    break;
                }
            }
            lg = flightSchedulePlanSessionBean.createCabinFee(curr.getCabinConfigurationId(), name, fee);
            cabinFees.add(lg);

            returnFlightId = returnflight.getFlightId();

            id = flightSchedulePlanSessionBean.createWeeklyFlightSchedulePlan(
                flight.getFlightId(), 
                returnFlightId, 
                dayOfWeek, 
                timeInput, 
                startDateInput, 
                endDateInput, 
                durationInput,
                layoverInput,
                cabinFees);

            System.out.println("flight plan ML621, Recurrent Weekly creeated (id:" + id + ")");
            
            //ML411, Recurrent NDay
            flight = flightSessionBean.retrieveFlight("ML411");
            returnflight = flight.getReturnFlight();
   
            Long n = 2l;
            int day = 1;
            int month = 12;
            int year = 2023;
            LocalDateTime startdate = LocalDateTime.of(year, Month.of(month), day, 0, 0);
            day = 31;
            month = 12;
            year = 2023;
            LocalDateTime enddate = LocalDateTime.of(year, Month.of(month), day, 0, 0);
            LocalDateTime startTime = startdate.withHour(13).withMinute(0);
            int hours = 4;
            int minutes = 0;
            Duration duration = Duration.ofHours(hours).plusMinutes(minutes);
            layoverInput = "4";

            cabinFees = new ArrayList<Long>();
            list = cabinConfigurationSessionBean.getCabinConfigurations(flight.getFlightId());

            code = "F";
            name = "noCode";
            fee = 2900;
            curr = null;
            for (CabinConfiguration cca : list) {
                if (cca.getCabinclass().equals(code)) {
                    curr = cca;
                    break;
                }
            }
            lg = flightSchedulePlanSessionBean.createCabinFee(curr.getCabinConfigurationId(), name, fee);
            cabinFees.add(lg);

            code = "J";
            name = "noCode";
            fee = 1400;
            curr = null;
            for (CabinConfiguration cca : list) {
                if (cca.getCabinclass().equals(code)) {
                    curr = cca;
                    break;
                }
            }
            lg = flightSchedulePlanSessionBean.createCabinFee(curr.getCabinConfigurationId(), name, fee);
            cabinFees.add(lg);

            code = "Y";
            name = "noCode";
            fee = 400;
            curr = null;
            for (CabinConfiguration cca : list) {
                if (cca.getCabinclass().equals(code)) {
                    curr = cca;
                    break;
                }
            }
            lg = flightSchedulePlanSessionBean.createCabinFee(curr.getCabinConfigurationId(), name, fee);
            cabinFees.add(lg);

            returnFlightId = returnflight.getFlightId();

            id = flightSchedulePlanSessionBean.createNFlightSchedulePlan(
                flight.getFlightId(), 
                returnFlightId, 
                n,
                startTime,
                enddate,
                duration,
                layoverInput,
                cabinFees);
            
            System.out.println("flight plan ML411, Recurrent NDay creeated (id:" + id + ")");
            
            //ML511, Manual Multiple
            flight = flightSessionBean.retrieveFlight("ML511");
            returnflight = flight.getReturnFlight();
            List<LocalDateTime> times = new ArrayList<>();
            List<Duration> durations = new ArrayList<>();

            LocalDateTime date = LocalDateTime.of(2023, Month.of(12), 7, 0, 0);
            LocalDateTime dateTime = date.withHour(17).withMinute(0);
            hours = 3;
            minutes = 0;
            duration = Duration.ofHours(hours).plusMinutes(minutes);
            times.add(dateTime);
            durations.add(duration);

            date = LocalDateTime.of(2023, Month.of(12), 8, 0, 0);
            times.add(dateTime);
            durations.add(duration);

            date = LocalDateTime.of(2023, Month.of(12), 9, 0, 0);
            times.add(dateTime);
            durations.add(duration);


            layoverInput = "2";
            list = cabinConfigurationSessionBean.getCabinConfigurations(flight.getFlightId());
            cabinFees = new ArrayList<Long>();

            code = "F";
            name = "noCode";
            fee = 3100;
            curr = null;
            for (CabinConfiguration cca : list) {
                if (cca.getCabinclass().equals(code)) {
                    curr = cca;
                    break;
                }
            }
            lg = flightSchedulePlanSessionBean.createCabinFee(curr.getCabinConfigurationId(), name, fee);
            cabinFees.add(lg);

            code = "J";
            name = "noCode";
            fee = 1600;
            curr = null;
            for (CabinConfiguration cca : list) {
                if (cca.getCabinclass().equals(code)) {
                    curr = cca;
                    break;
                }
            }
            lg = flightSchedulePlanSessionBean.createCabinFee(curr.getCabinConfigurationId(), name, fee);
            cabinFees.add(lg);

            code = "Y";
            name = "noCode";
            fee = 600;
            curr = null;
            for (CabinConfiguration cca : list) {
                if (cca.getCabinclass().equals(code)) {
                    curr = cca;
                    break;
                }
            }

            returnFlightId = returnflight.getFlightId();

            id = flightSchedulePlanSessionBean.createMultipleFlightSchedulePlan(
            flight.getFlightId(), 
            returnFlightId, 
            times, 
            durations,
            layoverInput,
            cabinFees);
            
            System.out.println("flight plan ML511, Manual Multiple creeated (id:" + id + ")");
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
