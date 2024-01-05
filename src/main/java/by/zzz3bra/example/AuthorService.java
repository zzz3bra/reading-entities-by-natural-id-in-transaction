package by.zzz3bra.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.List;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Transactional
    public void prolongAuthorsContractsOneByOne(List<String> authorUuids, Instant prolongationDate) {
        for (String authorUuid : authorUuids) {
            Author author = authorRepository.findOneByUuid(authorUuid).orElseThrow(() -> new AuthorNotBeFoundException("Could not find author by UUID " + authorUuid));
            prolongContractIfPossible(author, prolongationDate);
        }
    }

    @Transactional
    public void prolongAuthorsContractsOneByOneIgnoringDirtyChecking(List<String> authorUuids, Instant prolongationDate) {
        for (String authorUuid : authorUuids) {
            Author author = authorRepository.findOneIgnoringPersistenceContextChangesByUuid(authorUuid).orElseThrow(() -> new AuthorNotBeFoundException("Could not find author by UUID " + authorUuid));
            prolongContractIfPossible(author, prolongationDate);
        }
    }

    @Transactional
    public void prolongAuthorsContractsUsingFlushAtTransactionEndImplicitBatch(List<String> authorUuids, Instant prolongationDate) {
        List<Author> authors = authorRepository.findAllByUuidIn(authorUuids);
        for (Author author : authors) {
            prolongContractIfPossible(author, prolongationDate);
        }
    }

    private void prolongContractIfPossible(Author author, Instant prolongationDate) {
        // our policy only allows nicknames for authors, otherwise we do not prolong contracts with them
        if (StringUtils.hasLength(author.getPseudonym())) {
            author.setContractProlongedAt(prolongationDate);
        }
    }

    public List<Author> queryAllAuthorsWithSortingByPseudonymOrFullName() {
        return authorRepository.findAll(AuthorSpecifications.orderByPseudonymOrFullName());
    }

    public List<Author> queryAllAuthorsWithSortingByPseudonymOrFullName(int page, int size) {
        return authorRepository.findAll(AuthorSpecifications.orderByPseudonymOrFullName(),
                PageRequest.of(page, size, Sort.unsorted())).getContent();
    }

    public List<Author> queryAllAuthorsWithSortingByPseudonymOrFullNameSpringDataNeumann(int page, int size) {
        return authorRepository.findAll(Specification.where(null),
                PageRequest.of(page, size, JpaSort.unsafe("coalesce('pseudonym', 'fullName')"))).getContent();
    }
}
