package by.zzz3bra.example;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;
import java.util.List;
import java.util.Optional;

import static org.hibernate.annotations.QueryHints.FLUSH_MODE;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long>, JpaSpecificationExecutor<Author> {

    Optional<Author> findOneByUuid(String uuid);

    @QueryHints(value = {@QueryHint(name = FLUSH_MODE, value = "COMMIT")}, forCounting = false)
    Optional<Author> findOneIgnoringPersistenceContextChangesByUuid(String uuid);

    List<Author> findAllByUuidIn(List<String> uuids);
}
