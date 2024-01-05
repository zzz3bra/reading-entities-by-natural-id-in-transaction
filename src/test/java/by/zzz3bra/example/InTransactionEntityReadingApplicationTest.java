package by.zzz3bra.example;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * There is no @Test annotated method within here, because the AbstractBenchmark
 * defines one, which spawns the JMH runner. This class only contains JMH/Benchmark
 * related code.
 */
@SpringBootTest
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class InTransactionEntityReadingApplicationTest extends AbstractBenchmark {
    static private AuthorRepository authorRepository;
    static private AuthorService authorService;
    static private EntityManager entityManager;

    List<String> authorUuids;

    @Setup(Level.Trial)
    public void setupBenchmark() {
        generateAuthors();
        entityManager.getEntityManagerFactory().getCache().evictAll();
    }

    @TearDown(Level.Trial)
    public void tearDownBenchmark() {
        authorRepository.deleteAllInBatch();
    }

    @Benchmark
    public void testReadingAndFlushingInsideTransaction() {
        authorService.prolongAuthorsContractsOneByOne(authorUuids, Instant.now());
    }

    @Benchmark
    public void testReadingAndNoFlushingInsideTransaction() {
        authorService.prolongAuthorsContractsOneByOneIgnoringDirtyChecking(authorUuids, Instant.now());
    }

    @Benchmark
    public void testBatchReadingAndFlushingInsideTransaction() {
        authorService.prolongAuthorsContractsUsingFlushAtTransactionEndImplicitBatch(authorUuids, Instant.now());
    }

    private void generateAuthors() {
        authorUuids = new ArrayList<>();
        for (int i = 0; i < 10_000; i++) {
            Author author;
            if (i % 2 == 0) {
                author = new Author("Anonymous#" + i, "John Doe Real#" + i);
            } else {
                author = new Author(null, "John Doe Real#" + i);
            }
            author = authorRepository.save(author);
            authorUuids.add(author.getUuid());
        }
    }

    @Autowired
    void setAuthorRepository(AuthorRepository authorRepository) {
        InTransactionEntityReadingApplicationTest.authorRepository = authorRepository;
    }

    @Autowired
    void setAuthorService(AuthorService authorService) {
        InTransactionEntityReadingApplicationTest.authorService = authorService;
    }

    @Autowired
    void setEntityManager(EntityManager entityManager) {
        InTransactionEntityReadingApplicationTest.entityManager = entityManager;
    }
}
