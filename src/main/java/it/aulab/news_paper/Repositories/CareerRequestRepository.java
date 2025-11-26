package it.aulab.news_paper.Repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.aulab.news_paper.Models.CareerRequest;

@Repository
public interface CareerRequestRepository extends CrudRepository<CareerRequest, Long> {
}
