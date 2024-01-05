package by.zzz3bra.example;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import java.time.Instant;
import java.util.UUID;

@Entity
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true, nullable = false)
    private String uuid;
    private Instant contractProlongedAt;
    private String pseudonym;
    private String fullName;

    public Author() {
    }

    public Author(String pseudonym, String fullName) {
        this.pseudonym = pseudonym;
        this.fullName = fullName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public Instant getContractProlongedAt() {
        return contractProlongedAt;
    }

    public void setContractProlongedAt(Instant contractProlongedAt) {
        this.contractProlongedAt = contractProlongedAt;
    }

    public String getPseudonym() {
        return pseudonym;
    }

    public void setPseudonym(String pseudonym) {
        this.pseudonym = pseudonym;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @PrePersist
    void setUuid() {
        uuid = UUID.randomUUID().toString();
    }

    @Override
    public String toString() {
        return "Author{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                ", pseudonym='" + pseudonym + '\'' +
                ", fullName='" + fullName + '\'' +
                '}';
    }
}
