package net.mirjalal.kogeneesti.repository;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;

import jakarta.transaction.Transactional;
import net.mirjalal.kogeneesti.model.Quiz;

@Transactional
public interface QuizRepository extends JpaRepository<Quiz, BigInteger> {
    
}
