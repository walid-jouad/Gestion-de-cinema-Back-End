package com.cinema.entities;

import com.cinema.entities.Film;
import com.cinema.entities.Salle;
import com.cinema.entities.Seance;
import com.cinema.entities.Ticket;
import org.springframework.data.rest.core.config.Projection;

import java.util.Collection;
import java.util.Date;

@Projection(types = {com.cinema.entities.Projection.class}, name = "p1")
public interface ProjectionProj {
    public Long getId();
    public Date getDateProjection();
    public double getPrix();
    public Salle getSalle();
    public Film getFilm();
    public Collection<Ticket> getTickets();
    public Seance getSeance();
}
