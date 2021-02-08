package com.cinema.services;

import com.cinema.dao.*;
import com.cinema.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.stream.Stream;

@Service
@Transactional
public class CinemaInitServiceImpl implements ICinemaInitService{

    @Autowired
    VilleRepository villeRepository;

    @Autowired
    CinemaRepository cinemaRepository;

    @Autowired
    SalleRepository salleRepository;

    @Autowired
    PlaceRepository placeRepository;

    @Autowired
    CategorieRepository categorieRepository;

    @Autowired
    ProjectionRepository projectionRepository;

    @Autowired
    FilmRepository filmRepository;

    @Autowired
    SeanceRepository seanceRepository;

    @Autowired
    TicketRepository ticketRepository;

    @Override
    public void initVilles() {
        Stream.of("Casablanca","Marrakech","Rabat","Tanger").forEach(nameVille -> {
            Ville ville = new Ville();
            ville.setName(nameVille);
            ville.setAltitude(Math.random()*100);
            ville.setLatitude(Math.random()*100);
            ville.setLongitude(Math.random()*100);
            villeRepository.save(ville);
        });
    }

    @Override
    public void initCinemas() {
        villeRepository.findAll().forEach(v -> {
            Stream.of("MegaRama","IMAX","FOUNOUN","CHAHRAZAD").forEach(nameCinema -> {
                Cinema cinema = new Cinema();
                cinema.setName(nameCinema);
                cinema.setLatitude(Math.random()*100);
                cinema.setLongitude(Math.random()*100);
                cinema.setAltitude(Math.random()*100);
                cinema.setVille(v);
                cinema.setNombreSalles(3+(int)(Math.random()*3));
                cinemaRepository.save(cinema);
            });
        });
    }

    @Override
    public void initSalles() {
        cinemaRepository.findAll().forEach(c -> {
            for(int i=0;i<c.getNombreSalles();i++){
                Salle salle = new Salle();
                salle.setName("Salle "+ (i+1));
                salle.setCinema(c);
                salle.setNombrePlace(15+(int) (Math.random()*2));
                salleRepository.save(salle);
            }
        });
    }

    @Override
    public void initPlaces() {
        salleRepository.findAll().forEach(salle -> {
            for(int i=0;i<salle.getNombrePlace();i++){
                Place place = new Place();
                place.setLatitude(Math.random()*100);
                place.setNumero((int)(Math.random()*20));
                place.setAltitude(Math.random()*100);
                place.setLongitude(Math.random()*100);
                place.setSalle(salle);
                placeRepository.save(place);
            }
        });
    }

    @Override
    public void initSeances() {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Stream.of("12:00","15:00","17.30","19:45","22:00").forEach(heureDebut -> {
            Seance seance = new Seance();
            try {
                seance.setHeureDebut(dateFormat.parse(heureDebut));
                seanceRepository.save(seance);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void initCategories() {
        Stream.of("Policier","Comédie","Action","Drame").forEach(nameCategory -> {
            Categorie categorie = new Categorie();
            categorie.setName(nameCategory);
            categorieRepository.save(categorie);
        });
    }

    @Override
    public void initFilms() {
        double[] durees = new double[] {1, 1.5, 2, 2.5};
        List<Categorie> categorieList = categorieRepository.findAll();
        Stream.of("GameofThrones","Seigneurdesanneaux","Spiderman","CatWomen").forEach(nameFilm -> {
            Film film = new Film();
            film.setDuree(durees[new Random().nextInt(durees.length)]);
            film.setTitre(nameFilm);
            film.setPhoto(nameFilm.replaceAll(" ", "") + ".jpg");
            film.setCategorie(categorieList.get(new Random().nextInt(categorieList.size())));
            filmRepository.save(film);
        });
    }

    @Override
    public void initProjections() {
        double[] prices = new double[] {60, 65, 80, 85, 100, 120};
        List<Film> films = filmRepository.findAll();
        villeRepository.findAll().forEach(ville -> {
            ville.getCinemas().forEach(cinema -> {
                cinema.getSalles().forEach(salle ->  {
                    int index = new Random().nextInt(films.size());
                    Film film = films.get(index);
                        seanceRepository.findAll().forEach(seance -> {
                            Projection projection = new Projection();
                            projection.setDateProjection(new Date());
                            projection.setFilm(film);
                            projection.setPrix(prices[new Random().nextInt(prices.length)]);
                            projection.setSalle(salle);
                            projection.setSeance(seance);
                            projectionRepository.save(projection);
                        });
                });
            });
        });
    }

    @Override
    public void initTickets() {
        projectionRepository.findAll().forEach(projection -> {
            projection.getSalle().getPlaces().forEach(place -> {
                Ticket ticket = new Ticket();
                ticket.setProjection(projection);
                ticket.setPlace(place);
                ticket.setPrix(projection.getPrix());
                ticket.setReservee(false);
                ticketRepository.save(ticket);
            });
        });
    }
}
