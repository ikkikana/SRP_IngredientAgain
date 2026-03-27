package school.hei.spring_srp_project.entity;



import java.time.Instant;

public record Product(Integer id, String name, Double price, Instant creationDate) {}