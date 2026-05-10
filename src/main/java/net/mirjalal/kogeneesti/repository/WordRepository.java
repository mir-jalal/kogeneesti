package net.mirjalal.kogeneesti.repository;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import net.mirjalal.kogeneesti.model.Word;

@Repository
@Transactional
public interface WordRepository extends JpaRepository<Word, BigInteger>{

}
