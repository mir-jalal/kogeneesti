package net.mirjalal.kogeneesti.repository;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import jakarta.transaction.Transactional;
import net.mirjalal.kogeneesti.model.Dictionary;

@Repository
@Transactional
public interface DictionaryRepository extends JpaRepository<Dictionary, BigInteger> {
    
}
