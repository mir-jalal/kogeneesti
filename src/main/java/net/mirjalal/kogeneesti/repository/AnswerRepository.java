package net.mirjalal.kogeneesti.repository;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;

import jakarta.transaction.Transactional;
import net.mirjalal.kogeneesti.model.Answer;

@Transactional
public interface AnswerRepository extends JpaRepository<Answer, BigInteger> {
    
}
